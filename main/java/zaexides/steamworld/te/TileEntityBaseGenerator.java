package zaexides.steamworld.te;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import zaexides.steamworld.fluids.FluidSteam;
import zaexides.steamworld.utility.capability.FluidInputOutput;
import zaexides.steamworld.utility.capability.SteamWorldFluidTank;

public class TileEntityBaseGenerator extends SyncedTileEntity implements ICapabilityProvider, ITickable
{
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
	public int efficiency = 4;
			
	public TileEntityBaseGenerator() 
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
		
		int capacity = 4000;
		if(compound.hasKey("capacity"))
			capacity = compound.getInteger("capacity");
		fluidIn.setCapacity(capacity);
		fluidOut.setCapacity(capacity);
		
		if(compound.hasKey("speed"))
			efficiency = compound.getInteger("speed");
		
		if(compound.hasKey("fluidIn"))
		{
			fluidIn.readFromNBT((NBTTagCompound) compound.getTag("fluidIn"));
		}
		
		if(compound.hasKey("fluidOut"))
		{
			fluidOut.readFromNBT((NBTTagCompound) compound.getTag("fluidOut"));
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		compound.setInteger("capacity", fluidIn.getCapacity());
		
		compound.setInteger("speed", efficiency);
		
		NBTTagCompound fluidInTag = new NBTTagCompound();
		fluidIn.writeToNBT(fluidInTag);
		compound.setTag("fluidIn", fluidInTag);
		
		NBTTagCompound fluidOutTag = new NBTTagCompound();
		fluidOut.writeToNBT(fluidOutTag);
		compound.setTag("fluidOut", fluidOutTag);
		
		return super.writeToNBT(compound);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) 
	{
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(fluidInOut);
		return super.getCapability(capability, facing);
	}
	
	protected boolean MakeSteam()
	{
		return MakeSteam(efficiency);
	}
	
	protected boolean MakeSteam(int amount)
	{
		if(fluidIn.FluidAmount(FluidRegistry.WATER) > 0 && fluidOut.getFluidAmount() < fluidOut.getCapacity())
		{
			fluidIn.allowDirtyMarking = false;
			
			int drainAmount = fluidIn.drain(amount, false).amount;
			
			FluidStack fluidStack = new FluidStack(FluidSteam.fluidSteam, drainAmount);
			int fillAmount = fluidOut.fillInternal(fluidStack, true);
			
			fluidIn.drain(fillAmount, true);
			
			fluidIn.allowDirtyMarking = true;
			return drainAmount > 0;
		}
		return false;
	}

	@Override
	public void update() {
	}
}
