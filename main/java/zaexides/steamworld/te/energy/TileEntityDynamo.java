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
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import zaexides.steamworld.ConfigHandler;
import zaexides.steamworld.te.SyncedTileEntity;
import zaexides.steamworld.utility.capability.SteamWorldFluidTank;
import zaexides.steamworld.utility.capability.SteamWorldSteamTank;

public class TileEntityDynamo extends SyncedTileEntity implements ITickable, ICapabilityProvider
{
	public SWEnergyStorage energy = new SWEnergyStorage(20000)
	{
		public boolean canReceive() {return false;};
	};
	public SteamWorldSteamTank steamTank = new SteamWorldSteamTank(Fluid.BUCKET_VOLUME * 4, this);
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		compound.setInteger("energy", energy.getEnergyStored());
		steamTank.writeToNBT(compound);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		super.readFromNBT(compound);
		energy.setEnergy(compound.getInteger("energy"));
		steamTank.readFromNBT(compound);
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
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(steamTank);
		if(capability == CapabilityEnergy.ENERGY)
			return CapabilityEnergy.ENERGY.cast(energy);
		return super.getCapability(capability, facing);
	}
	
	@Override
	public void update()
	{
		if(world.isRemote)
			return;
		
		int FLUID_TO_ENERGY = ConfigHandler.fluidToEnergy;
		int ENERGY_FROM_FLUID = ConfigHandler.energyFromFluid;
		int MAX_CONVERSIONS = ConfigHandler.maxConversionsPerTick;
		int currentConversions = 0;
		
		while(currentConversions < MAX_CONVERSIONS)
		{
			if(steamTank.getFluidAmount() >= FLUID_TO_ENERGY)
			{
				int amount = steamTank.drain(FLUID_TO_ENERGY, false).amount;
				if(amount == FLUID_TO_ENERGY)
				{
					int accepted = energy.receiveEnergyInternal(ENERGY_FROM_FLUID, true);
					if(accepted == ENERGY_FROM_FLUID)
					{
						energy.receiveEnergyInternal(ENERGY_FROM_FLUID, false);
						steamTank.drain(FLUID_TO_ENERGY, true);
					}
				}
			}
			currentConversions++;
		}
		
		if(energy.getEnergyStored() > 0)
		{
			pushEnergy(pos.north(), EnumFacing.SOUTH);
			pushEnergy(pos.south(), EnumFacing.NORTH);
			pushEnergy(pos.east(), EnumFacing.WEST);
			pushEnergy(pos.west(), EnumFacing.EAST);
			pushEnergy(pos.up(), EnumFacing.DOWN);
			pushEnergy(pos.down(), EnumFacing.UP);
		}
		
		markDirty();
	}
	
	private boolean pushEnergy(BlockPos pos, EnumFacing facing)
	{
		TileEntity tileEntity = world.getTileEntity(pos);
		if(tileEntity != null && tileEntity.hasCapability(CapabilityEnergy.ENERGY, facing))
		{
			IEnergyStorage energyStorage = (IEnergyStorage) tileEntity.getCapability(CapabilityEnergy.ENERGY, facing);
			if(energyStorage != null && energyStorage.canReceive() && (energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored()))
			{
				int amount = energyStorage.receiveEnergy(energy.getEnergyStored(), false);
				energy.extractEnergy(amount, false);
				return true;
			}
		}
		return false;
	}
}
