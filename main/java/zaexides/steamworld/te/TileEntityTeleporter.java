package zaexides.steamworld.te;

import org.apache.logging.log4j.Level;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.SideOnly;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.savedata.world.TeleporterData;
import zaexides.steamworld.savedata.world.TeleporterSaveData;
import zaexides.steamworld.utility.SteamWorldTeleporter;
import zaexides.steamworld.utility.capability.SteamWorldFluidTank;

public class TileEntityTeleporter extends SyncedTileEntity implements ICapabilityProvider, ITickable
{
	public int ownId = -1;
	public int targetId = -1;
	
	public int cooldown = 0;
	private final int MAX_COOLDOWN = 100;
	
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
		cooldown = compound.getInteger("cooldown");
		steamTank.readFromNBT(compound);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		compound.setInteger("target_id", targetId);
		compound.setInteger("own_id", ownId);
		steamTank.writeToNBT(compound);
		compound.setInteger("cooldown", cooldown);
		return super.writeToNBT(compound);
	}
	
	public void activate(Entity entity)
	{
		if(steamTank.getFluidAmount() < 2000 && targetId != -1)
			return;
		
		if (cooldown <= 0 && entity instanceof EntityLivingBase && !entity.isRiding() && !entity.isBeingRidden() && entity.isNonBoss())
        {
			TeleporterSaveData teleporterSaveData = TeleporterSaveData.get(world);
			TeleporterData teleporterData = teleporterSaveData.getTeleporterData(targetId);
			if(teleporterData != null && !teleporterData.free && teleporterData.netId.equals(getNetID()))
			{
				int currentDimension = world.provider.getDimension();
				World targetWorld;
				if(teleporterData.dimension != currentDimension)
					targetWorld = world.getMinecraftServer().getWorld(teleporterData.dimension);
				else
					targetWorld = world;
				
				TileEntity otherTileEntity = targetWorld.getTileEntity(teleporterData.position);
				if(otherTileEntity != null && otherTileEntity instanceof TileEntityTeleporter)
				{
					((TileEntityTeleporter)otherTileEntity).cooldown = MAX_COOLDOWN;
					otherTileEntity.markDirty();
					
					if(teleporterData.dimension != currentDimension)
					{
						if(entity instanceof EntityPlayer)
							world.getMinecraftServer().getPlayerList().transferPlayerToDimension((EntityPlayerMP)entity, teleporterData.dimension, new SteamWorldTeleporter((WorldServer)world, teleporterData.position));
						else
							world.getMinecraftServer().getPlayerList().transferEntityToWorld(entity, currentDimension, (WorldServer)world, (WorldServer)targetWorld, new SteamWorldTeleporter((WorldServer)world, teleporterData.position));
					}
					((EntityLivingBase)entity).setPositionAndUpdate(otherTileEntity.getPos().getX() + 0.5d, otherTileEntity.getPos().getY() + 1.5d, otherTileEntity.getPos().getZ() + 0.5d);
					
					if(currentDimension == 1) //The End pulls off some shenanigans, I guess?
					{
						((EntityLivingBase)entity).setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);
						((WorldServer)targetWorld).spawnEntity(entity);
						((WorldServer)targetWorld).updateEntityWithOptionalForce(entity, true);
					}
					
					steamTank.drain(2000, true);
					markDirty();
				}
			}
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
	
	public String getNetID()
	{
		String pass = "";
		TeleporterData teleporterData = TeleporterSaveData.get(world).getTeleporterData(ownId);
		if(teleporterData != null)
			pass = teleporterData.netId;
		return pass;
	}

	@Override
	public void update() 
	{
		if(cooldown > 0)
			cooldown--;
	}
}
