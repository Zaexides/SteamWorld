package zaexides.steamworld.te.energy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import zaexides.steamworld.ConfigHandler;
import zaexides.steamworld.fluids.FluidSteam;
import zaexides.steamworld.te.SyncedTileEntity;
import zaexides.steamworld.utility.capability.FluidInputOutput;
import zaexides.steamworld.utility.capability.SteamWorksFluidTank;

public class TileEntitySteamGeneratorElectric extends SyncedTileEntity implements ITickable, ICapabilityProvider
{
	public SWEnergyStorage energy = new SWEnergyStorage(20000);
	public SteamWorksFluidTank fluidOut = new SteamWorksFluidTank(Fluid.BUCKET_VOLUME * 4, this)
	{
		@Override
		public boolean canFillFluidType(FluidStack fluid) {return false;};
	};
	public SteamWorksFluidTank fluidIn = new SteamWorksFluidTank(Fluid.BUCKET_VOLUME * 4, this)
	{
		@Override
		public boolean canFillFluidType(net.minecraftforge.fluids.FluidStack fluid)
		{
			return fluid.getFluid() == FluidRegistry.WATER;
		};
	};
	
	public TileEntitySteamGeneratorElectric() 
	{
		fluidIn.allowDirtyMarking = true;
	}
	
	public FluidInputOutput fluidInOut = new FluidInputOutput(fluidIn, fluidOut);
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		compound.setInteger("energy", energy.getEnergyStored());
		
		NBTTagCompound fluidInTag = new NBTTagCompound();
		fluidIn.writeToNBT(fluidInTag);
		compound.setTag("fluidIn", fluidInTag);
		
		NBTTagCompound fluidOutTag = new NBTTagCompound();
		fluidOut.writeToNBT(fluidOutTag);
		compound.setTag("fluidOut", fluidOutTag);
		
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		super.readFromNBT(compound);
		energy.setEnergy(compound.getInteger("energy"));
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
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) 
	{
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return true;
		if(capability == CapabilityEnergy.ENERGY)
			return true;
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) 
	{
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(fluidInOut);
		if(capability == CapabilityEnergy.ENERGY)
			return CapabilityEnergy.ENERGY.cast(energy);
		return super.getCapability(capability, facing);
	}
	
	@Override
	public void update()
	{
		if(world.isRemote)
			return;
		
		int ENERGY_TO_FLUID = ConfigHandler.energy_to_fluid;
		int FLUID_FROM_ENERGY = ConfigHandler.fluid_from_energy;
		int MAX_CONVERSIONS = ConfigHandler.max_conversions_per_tick;
		int currentConversions = 0;
		
		while(currentConversions < MAX_CONVERSIONS)
		{
			if(energy.getEnergyStored() >= ENERGY_TO_FLUID)
			{
				int amount = energy.extractEnergy(ENERGY_TO_FLUID, true);
				if(amount == ENERGY_TO_FLUID)
				{
					if(MakeSteam(FLUID_FROM_ENERGY))
						energy.extractEnergy(ENERGY_TO_FLUID, false);
				}
			}
			
			currentConversions++;
		}
		
		markDirty();
	}
	
	private boolean MakeSteam(int amount)
	{
		if(fluidIn.FluidAmount(FluidRegistry.WATER) >= amount)
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
}
