package zaexides.steamworld.te;

import org.apache.logging.log4j.Level;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.SideOnly;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.savedata.world.TeleporterData;
import zaexides.steamworld.savedata.world.TeleporterSaveData;
import zaexides.steamworld.utility.capability.SteamWorldFluidTank;

public class TileEntityTeleporter extends SyncedTileEntity implements ICapabilityProvider
{
	public int ownId = -1;
	private int targetId = -1;
	
	public SteamWorldFluidTank steamTank = new SteamWorldFluidTank(Fluid.BUCKET_VOLUME * 4, this)
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
	
	public String getName()
	{
		String name = "";
		TeleporterData teleporterData = TeleporterSaveData.get(world).getTeleporterData(ownId);
		if(teleporterData != null)
			name = teleporterData.name;
		return name;
	}
	
	public String getPass()
	{
		String pass = "";
		TeleporterData teleporterData = TeleporterSaveData.get(world).getTeleporterData(ownId);
		if(teleporterData != null)
			pass = teleporterData.password;
		return pass;
	}
}
