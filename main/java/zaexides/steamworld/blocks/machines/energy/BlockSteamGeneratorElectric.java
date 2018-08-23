package zaexides.steamworld.blocks.machines.energy;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidUtil;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.SteamWorldBlock;
import zaexides.steamworld.blocks.machines.IWrenchable;
import zaexides.steamworld.gui.GuiHandler;
import zaexides.steamworld.te.energy.TileEntitySteamGeneratorElectric;

public class BlockSteamGeneratorElectric extends SteamWorldBlock implements ITileEntityProvider, IWrenchable
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	public BlockSteamGeneratorElectric(String name, float hardness) 
	{
		super(name, Material.IRON, hardness, SteamWorld.CREATIVETAB_UTILITY);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntitySteamGeneratorElectric();
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) 
	{
		if(!worldIn.isRemote)
		{
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if(!(tileEntity instanceof TileEntitySteamGeneratorElectric))
				return false;
			
			ItemStack heldItem = playerIn.getHeldItem(hand);
			TileEntitySteamGeneratorElectric te = (TileEntitySteamGeneratorElectric)tileEntity;
			
			int remainingSpace = te.fluidIn.getCapacity() - te.fluidIn.getFluidAmount();
			
			if(heldItem.getItem() == Items.WATER_BUCKET && remainingSpace >= Fluid.BUCKET_VOLUME)
			{
				FluidUtil.interactWithFluidHandler(playerIn, hand, te.fluidIn);
				
				worldIn.playSound((EntityPlayer)null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
			}
			else
				playerIn.openGui(SteamWorld.singleton, GuiHandler.GENERATOR_ELECTRIC, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
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
				.withProperty(FACING, state.getValue(FACING));
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState()
				.withProperty(FACING, EnumFacing.getFront((meta & 3) + 2));
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(FACING).getIndex()-2;
	}
	
	@Override
	protected BlockStateContainer createBlockState() 
	{
		return new BlockStateContainer(this, FACING);
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
