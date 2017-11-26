package zaexides.steamworld.te.energy;

import net.minecraftforge.energy.IEnergyStorage;

public class SWEnergyStorage implements IEnergyStorage
{
	private int capacity = 1000;
	private int energy = 0;
	
	public SWEnergyStorage(int capacity) 
	{
		this.capacity = capacity;
	}
	
	public void setCapacity(int capacity) 
	{
		this.capacity = capacity;
	}
	
	public void setEnergy(int energy) 
	{
		this.energy = energy;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) 
	{
		if(!canReceive())
			return 0;
		
		return receiveEnergyInternal(maxReceive, simulate);
	}
	
	public int receiveEnergyInternal(int maxReceive, boolean simulate)
	{
		int receivedEnergy = Math.min(capacity - energy, maxReceive);
		if(!simulate)
			energy += receivedEnergy;
		return receivedEnergy;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) 
	{
		if(!canExtract())
			return 0;
		
		int extractedEnergy = Math.min(energy, maxExtract);
		if(!simulate)
			energy -= extractedEnergy;
		return extractedEnergy;
	}

	@Override
	public int getEnergyStored() 
	{
		return energy;
	}

	@Override
	public int getMaxEnergyStored() 
	{
		return capacity;
	}

	@Override
	public boolean canExtract() 
	{
		return true;
	}

	@Override
	public boolean canReceive()
	{
		return true;
	}

}
