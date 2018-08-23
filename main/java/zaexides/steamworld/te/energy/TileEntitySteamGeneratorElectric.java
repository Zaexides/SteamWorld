package zaexides.steamworld.te.energy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import zaexides.steamworld.ConfigHandler;
import zaexides.steamworld.te.TileEntityBaseGenerator;

public class TileEntitySteamGeneratorElectric extends TileEntityBaseGenerator implements ITickable, ICapabilityProvider
{
	public SWEnergyStorage energy = new SWEnergyStorage(20000);
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		compound.setInteger("energy", energy.getEnergyStored());
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		super.readFromNBT(compound);
		energy.setEnergy(compound.getInteger("energy"));
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) 
	{
		if(capability == CapabilityEnergy.ENERGY)
			return true;
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) 
	{
		if(capability == CapabilityEnergy.ENERGY)
			return CapabilityEnergy.ENERGY.cast(energy);
		return super.getCapability(capability, facing);
	}
	
	@Override
	public void update()
	{
		if(world.isRemote)
			return;
		
		int ENERGY_TO_FLUID = ConfigHandler.energyToFluid;
		int FLUID_FROM_ENERGY = ConfigHandler.fluidFromEnergy;
		int MAX_CONVERSIONS = ConfigHandler.maxConversionsPerTick;
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
}
