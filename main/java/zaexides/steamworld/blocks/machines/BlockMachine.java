package zaexides.steamworld.blocks.machines;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import zaexides.steamworld.blocks.SteamWorldBlock;
import zaexides.steamworld.te.TileEntitySteamGenerator;
import zaexides.steamworld.utility.IWrenchable;

public class BlockMachine extends SteamWorldBlock implements IWrenchable
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool ACTIVE = PropertyBool.create("active");
	protected boolean keepInventory;
	
	public BlockMachine(String name, Material material, float hardness)
	{
		super(name, material, hardness);
	}
	
	public BlockMachine(String name, Material material, float hardness, float resistance)
	{
		super(name, material, hardness, resistance);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) 
	{
		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) 
	{
		return state
				.withProperty(FACING, state.getValue(FACING))
				.withProperty(ACTIVE, state.getValue(ACTIVE));
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState()
				.withProperty(FACING, EnumFacing.getFront((meta & 3) + 2))
				.withProperty(ACTIVE, (meta & 8) == 8);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(FACING).getIndex()-2 + (state.getValue(ACTIVE) ? 8 : 0);
	}
	
	@Override
	protected BlockStateContainer createBlockState() 
	{
		return new BlockStateContainer(this, FACING, ACTIVE);
	}
	
	public void UpdateState(World world, BlockPos pos, boolean active) 
	{
		keepInventory = true;
		
		IBlockState blockState = world.getBlockState(pos);
		TileEntity tileEntity = world.getTileEntity(pos);
		
		world.setBlockState(pos, blockState.withProperty(FACING, blockState.getValue(FACING)).withProperty(ACTIVE, active));
		
		if (tileEntity != null)
        {
			tileEntity.validate();
            world.setTileEntity(pos, tileEntity);
        }
		
		keepInventory = false;
	}

	@Override
	public EnumActionResult onWrenchUse(World world, EntityPlayer player, BlockPos pos, EnumFacing facing,
			IBlockState blockState) 
	{
		if(removedByPlayer(blockState, world, pos, player, true))
		{
			harvestBlock(world, player, pos, blockState, null, ItemStack.EMPTY);
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.FAIL;
	}
}
