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

public class TileEntitySteamGenerator extends SyncedTileEntity implements ICapabilityProvider, ITickable
{
	public ItemStackHandlerInput itemStackHandler = new ItemStackHandlerInput(1);
	public SteamWorldFluidTank fluidIn = new SteamWorldFluidTank(Fluid.BUCKET_VOLUME * 4, this)
			{
				@Override
				public boolean canFillFluidType(net.minecraftforge.fluids.FluidStack fluid)
				{
					if(fluid == null)
						return false;
					return fluid.getFluid() == FluidRegistry.WATER;
				};
			};
	public SteamWorldFluidTank fluidOut = new SteamWorldFluidTank(Fluid.BUCKET_VOLUME * 4, this)
			{
				@Override
				public boolean canFillFluidType(FluidStack fluid) { return false; };
			};
			
	public FluidInputOutput fluidInOut = new FluidInputOutput(fluidIn, fluidOut);
			
	public int burnTime = 0;
	public int totalBurntime = 1;
	public int efficiency = 4;
	
	private static final int BASE_GENERATION_PER_TICK = 5;
			
	public TileEntitySteamGenerator() 
	{
		fluidIn.setTileEntity(this);
		fluidIn.allowDirtyMarking = true;
		fluidOut.setTileEntity(this);
	}
	
	public void SetStats(int capacity, int speed)
	{
		this.efficiency = speed;
		fluidIn.setCapacity(Fluid.BUCKET_VOLUME * capacity);
		fluidOut.setCapacity(Fluid.BUCKET_VOLUME * capacity);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		super.readFromNBT(compound);
		
		int capacity = compound.getInteger("capacity");
		fluidIn.setCapacity(capacity);
		fluidOut.setCapacity(capacity);
		
		efficiency = compound.getInteger("speed");
		
		if(compound.hasKey("items"))
		{
			itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("items"));
		}
		
		if(compound.hasKey("fluidIn"))
		{
			fluidIn.readFromNBT((NBTTagCompound) compound.getTag("fluidIn"));
		}
		
		if(compound.hasKey("fluidOut"))
		{
			fluidOut.readFromNBT((NBTTagCompound) compound.getTag("fluidOut"));
		}
		
		burnTime = compound.getInteger("burnTime");
		totalBurntime = compound.getInteger("totalBurnTime");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		compound.setTag("items", itemStackHandler.serializeNBT());
		
		compound.setInteger("capacity", fluidIn.getCapacity());
		
		compound.setInteger("speed", efficiency);
		
		NBTTagCompound fluidInTag = new NBTTagCompound();
		fluidIn.writeToNBT(fluidInTag);
		compound.setTag("fluidIn", fluidInTag);
		
		NBTTagCompound fluidOutTag = new NBTTagCompound();
		fluidOut.writeToNBT(fluidOutTag);
		compound.setTag("fluidOut", fluidOutTag);
		
		compound.setInteger("burnTime", burnTime);
		compound.setInteger("totalBurnTime", totalBurntime);
		
		return super.writeToNBT(compound);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return true;
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) 
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemStackHandler);
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(fluidInOut);
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
	
	private void MakeSteam()
	{
		if(fluidIn.FluidAmount(FluidRegistry.WATER) > 0)
		{
			fluidIn.allowDirtyMarking = false;
			
			int drainAmount = fluidIn.drain(efficiency, false).amount;
			
			FluidStack fluidStack = new FluidStack(FluidSteam.fluidSteam, drainAmount);
			int fillAmount = fluidOut.fillInternal(fluidStack, true);
			
			fluidIn.drain(fillAmount, true);
			
			fluidIn.allowDirtyMarking = true;
		}
	}
}
