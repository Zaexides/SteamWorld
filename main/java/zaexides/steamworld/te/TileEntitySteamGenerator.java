package zaexides.steamworld.te;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerFluidMap;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import scala.languageFeature.postfixOps;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.machines.BlockSteamGenerator;
import zaexides.steamworld.fluids.FluidSteam;
import zaexides.steamworld.utility.capability.FluidInputOutput;
import zaexides.steamworld.utility.capability.ItemStackHandlerInput;
import zaexides.steamworld.utility.capability.SteamWorldFluidTank;

public class TileEntitySteamGenerator extends TileEntityBaseGenerator implements ICapabilityProvider, ITickable
{
	public ItemStackHandlerInput itemStackHandler = new ItemStackHandlerInput(1);
			
	public int burnTime = 0;
	public int totalBurntime = 1;
	
	private static final int BASE_GENERATION_PER_TICK = 5;
	
	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		super.readFromNBT(compound);
		
		if(compound.hasKey("items"))
		{
			itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("items"));
		}
		
		burnTime = compound.getInteger("burnTime");
		totalBurntime = compound.getInteger("totalBurnTime");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		compound.setTag("items", itemStackHandler.serializeNBT());
		
		compound.setInteger("burnTime", burnTime);
		compound.setInteger("totalBurnTime", totalBurntime);
		
		return super.writeToNBT(compound);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) 
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemStackHandler);
		return super.getCapability(capability, facing);
	}
	
	public int getBurnTime(ItemStack stack)
	{
		return TileEntityFurnace.getItemBurnTime(stack);
	}

	@Override
	public void update() 
	{
		if(world.isRemote || isInvalid())
			return;
		
		if(burnTime <= 0 && fluidIn.FluidAmount(FluidRegistry.WATER) > 0 && fluidOut.getFluidAmount() < (fluidOut.getCapacity()))
			SetBurnTime();
		
		if(burnTime > 0)
		{
			burnTime -= BASE_GENERATION_PER_TICK;
			
			for(int l = 0; l < BASE_GENERATION_PER_TICK; l++)
				MakeSteam();
			markDirty();
			if(burnTime == 0)
				((BlockSteamGenerator)world.getBlockState(pos).getBlock()).UpdateState(world, pos, false);
		}
	}
	
	private void SetBurnTime()
	{
		ItemStack stack = itemStackHandler.getStack(0);
		int localBurnTime = getBurnTime(stack);
		if(localBurnTime > 0)
		{
			burnTime = localBurnTime;
			totalBurntime = burnTime;
			stack.shrink(1);
			itemStackHandler.setStackInSlot(0, stack);
			((BlockSteamGenerator)world.getBlockState(pos).getBlock()).UpdateState(world, pos, burnTime > 0);
		}
	}
}
