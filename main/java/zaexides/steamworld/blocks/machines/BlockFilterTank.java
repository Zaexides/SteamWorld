package zaexides.steamworld.blocks.machines;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.SteamWorldBlock;
import zaexides.steamworld.blocks.item.ItemBlockMachine;
import zaexides.steamworld.client.rendering.tile.FilteredTankModel;
import zaexides.steamworld.init.ItemInitializer;
import zaexides.steamworld.te.TileEntityFilterTank;
import zaexides.steamworld.utility.interfaces.IModeledObject;

public class BlockFilterTank extends SteamWorldBlock implements ITileEntityProvider, IWrenchable
{
	public BlockFilterTank(String name, float hardness)
	{
		super(name, Material.IRON, hardness, SteamWorld.CREATIVETAB_UTILITY);
	}
	
	@Override
	protected void AddBlockItem(int maxStackSize) 
	{
		ItemInitializer.ITEMS.add(new ItemBlockMachine(this).AddToolTip("item.steamworld.filter_tank.tooltip[0]").AddToolTip("item.steamworld.filter_tank.tooltip[1]").setRegistryName(this.getRegistryName()).setMaxStackSize(maxStackSize));
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityFilterTank();
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public BlockRenderLayer getBlockLayer() 
	{
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) 
	{
		TileEntityFilterTank filterTank = (TileEntityFilterTank) worldIn.getTileEntity(pos);
		
		if(filterTank == null)
			return false;
		
		if(hitY <= .25f)
		{
			ItemStack handStack = playerIn.getHeldItem(hand);
			if(!handStack.isEmpty())
			{
				IFluidHandlerItem fluidHandlerItem = FluidUtil.getFluidHandler(handStack);
				if(fluidHandlerItem != null)
				{
					FluidStack fluidStack = fluidHandlerItem.drain(Integer.MAX_VALUE, false);
					if(fluidStack != null && fluidStack.amount > 0)
					{
						if(!worldIn.isRemote)
						{
							filterTank.setAllowedFluid(fluidStack.getFluid());
							TextComponentTranslation msg = new TextComponentTranslation("message.steamworld.filter_tank.set_fluid", fluidStack.getFluid().getLocalizedName(fluidStack));
							playerIn.sendMessage(msg);
						}
						return true;
					}
					else
					{
						if(!worldIn.isRemote)
						{
							filterTank.setAllowedFluid(null);
							TextComponentTranslation msg = new TextComponentTranslation("message.steamworld.filter_tank.any_fluid");
							playerIn.sendMessage(msg);
						}
						return true;
					}
				}
			}
		}
		else
		{
			IFluidHandler fluidHandler = filterTank.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
			return FluidUtil.interactWithFluidHandler(playerIn, hand, fluidHandler);
		}
		
		return false;
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
