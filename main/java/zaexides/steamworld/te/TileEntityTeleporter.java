package zaexides.steamworld.te;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import zaexides.steamworld.savedata.world.TeleporterSaveData;
import zaexides.steamworld.utility.capability.SteamWorldFluidTank;

public class TileEntityTeleporter extends SyncedTileEntity implements ICapabilityProvider
{
	private int ownId = -1;
	private int targetId = -1;
	
	private SteamWorldFluidTank steamTank = new SteamWorldFluidTank(Fluid.BUCKET_VOLUME * 4, this)
	{
		@Override
		public boolean canFillFluidType(net.minecraftforge.fluids.FluidStack fluid) 
		{
			return FluidRegistry.getFluidName(fluid).endsWith("steam");
		};
	};
	
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
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(steamTank);
		return super.getCapability(capability, facing);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		super.readFromNBT(compound);
		targetId = compound.getInteger("target_id");
		ownId = compound.getInteger("own_id");
		steamTank.readFromNBT(compound);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		compound.setInteger("target_id", targetId);
		compound.setInteger("own_id", ownId);
		steamTank.writeToNBT(compound);
		return super.writeToNBT(compound);
	}
	
	public void activate(Entity entity)
	{
		if(steamTank.getFluidAmount() < 2000 && targetId != -1)
			return;
		
		if (!entity.isRiding() && !entity.isBeingRidden() && entity.isNonBoss())
        {
			TeleporterSaveData teleporterSaveData = TeleporterSaveData.get(world);
        }
	}
}
