package zaexides.steamworld.blocks.machines;

import java.util.Random;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zaexides.steamworld.blocks.SteamWorldBlock;
import zaexides.steamworld.te.TileEntityDrain;

public class BlockDrain extends SteamWorldBlock implements ITileEntityProvider, IWrenchable
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	
	public BlockDrain() 
	{
		super("block_drain", Material.IRON, 2.5f);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) 
	{
		worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer)), 2);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) 
	{
		return getDefaultState()
				.withProperty(FACING, EnumFacing.getFront(meta & 7));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) 
	{
		return state.getValue(FACING).getIndex();
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, FACING);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityDrain();
	}

	@Override
	public EnumActionResult onWrenchUse(World world, EntityPlayer player, BlockPos pos, EnumFacing facing,
			IBlockState blockState) 
	{
		TileEntity tileEntity = world.getTileEntity(pos);
		
		EnumFacing enumFacing = blockState.getValue(FACING);
		EnumFacing targetFacing = EnumFacing.VALUES[((enumFacing.getIndex() + 1) % EnumFacing.VALUES.length)];
		world.setBlockState(pos, blockState.withProperty(FACING, targetFacing));
		
		if(tileEntity != null)
		{
			tileEntity.validate();
			world.setTileEntity(pos, tileEntity);
		}
		return EnumActionResult.SUCCESS;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		EnumFacing facing = stateIn.getValue(FACING);
		BlockPos frontPos = pos.add(facing.getDirectionVec());
		
		if(worldIn.getBlockState(frontPos).getMaterial() == Material.WATER)
		{
			for(int i = 0; i < rand.nextInt(5); i++)
			{
				Vec3i direction = facing.getDirectionVec();
				
				double xPos = frontPos.getX() + direction.getX() * rand.nextDouble() * 2.25 + rand.nextDouble() * 0.25 + 0.5;
				double yPos = frontPos.getY() + direction.getY() * rand.nextDouble() * 2.25 + rand.nextDouble() * 0.25 + 0.5;
				double zPos = frontPos.getZ() + direction.getZ() * rand.nextDouble() * 2.25 + rand.nextDouble() * 0.25 + 0.5;
				
				if(worldIn.getBlockState(new BlockPos(xPos, yPos, zPos)).getMaterial() == Material.WATER)
				{
					double xVel = facing.getOpposite().getDirectionVec().getX() * (rand.nextDouble() + 1);
					double yVel = facing.getOpposite().getDirectionVec().getY() * (rand.nextDouble() + 1);
					double zVel = facing.getOpposite().getDirectionVec().getZ() * (rand.nextDouble() + 1);
					
					worldIn.spawnParticle(EnumParticleTypes.WATER_BUBBLE, xPos, yPos, zPos, xVel, yVel, zVel);
				}
			}
		}
	}
}
