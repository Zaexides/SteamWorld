package zaexides.steamworld.blocks.machines;

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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import zaexides.steamworld.blocks.SteamWorldBlock;
import zaexides.steamworld.te.TileEntityDrain;
import zaexides.steamworld.te.TileEntityFaucet;
import zaexides.steamworld.utility.IWrenchable;

public class BlockFaucet extends SteamWorldBlock implements ITileEntityProvider, IWrenchable
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	
	public BlockFaucet() 
	{
		super("block_faucet", Material.IRON, 2.5f);
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
		return new TileEntityFaucet();
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
}
