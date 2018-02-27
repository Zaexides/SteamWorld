package zaexides.steamworld.blocks.machines;

import java.util.Random;

import net.minecraft.block.Block;
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
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import zaexides.steamworld.blocks.SteamWorldBlock;
import zaexides.steamworld.te.TileEntityValve;

public class BlockValve extends SteamWorldBlock implements ITileEntityProvider, IWrenchable
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	public static final PropertyBool ENABLED = PropertyBool.create("enabled");
	
	private int capacity, speed;

	public BlockValve(String name, Material material, float hardness, int capacity, int speed)
	{
		super(name, material, hardness);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		
		this.capacity = capacity;
		this.speed = speed;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) 
	{
		return ((TileEntityValve)worldIn.getTileEntity(pos)).ChangeState(playerIn);
	}
	
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) 
	{
		((TileEntityValve)world.getTileEntity(pos)).GetEnabledState();
		super.onNeighborChange(world, pos, neighbor);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) 
	{
		worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer)), 2);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) 
	{
		((TileEntityValve)worldIn.getTileEntity(pos)).GetEnabledState();
		return state
				.withProperty(FACING, state.getValue(FACING))
				.withProperty(ENABLED, ((TileEntityValve)worldIn.getTileEntity(pos)).enabled);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) 
	{
		return getDefaultState()
				.withProperty(FACING, EnumFacing.getFront(meta & 7))
				.withProperty(ENABLED, (meta & 8) == 8);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) 
	{
		return state.getValue(FACING).getIndex();
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, FACING, ENABLED);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		TileEntityValve tileEntityValve = new TileEntityValve();
		tileEntityValve.SetStats(speed, capacity);
		return tileEntityValve;
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
