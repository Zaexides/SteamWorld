package zaexides.steamworld.te.generic_machine;

import org.apache.logging.log4j.Level;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
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
import zaexides.steamworld.init.BlockInitializer;
import zaexides.steamworld.savedata.world.TeleporterData;
import zaexides.steamworld.savedata.world.TeleporterSaveData;
import zaexides.steamworld.te.SyncedTileEntity;
import zaexides.steamworld.te.generic_machine.interfaces.IGenericMachineWalkActivate;
import zaexides.steamworld.utility.SteamWorldTeleporter;
import zaexides.steamworld.utility.capability.SteamWorldFluidTank;
import zaexides.steamworld.utility.capability.SteamWorldSteamTank;

public class TileEntityTeleporter extends SyncedTileEntity implements ICapabilityProvider, ITickable, IGenericMachineWalkActivate
{
	public int ownId = -1;
	public int targetId = -1;
	
	public int cooldown = 0;
	private final int MAX_COOLDOWN = 100;
	
	private final int PORTAL_UPDATE_FREQUENCY = 5;
	private int portalUpdateTimer = 0;
	private final IBlockState QUARTZ_BLOCK_STATE = Blocks.QUARTZ_BLOCK.getDefaultState();
	private final IBlockState AIR_BLOCK_STATE = Blocks.AIR.getDefaultState();
	private final IBlockState PORTAL_BLOCK_STATE = BlockInitializer.BLOCK_SW_PORTAL.getDefaultState();
	
	public SteamWorldSteamTank steamTank = new SteamWorldSteamTank(Fluid.BUCKET_VOLUME * 4, this);
	
	public TileEntityTeleporter() 
	{
		steamTank.allowDirtyMarking = true;
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
	
	@Override
	public void onWalkedOn(Entity entity)
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
							world.getMinecraftServer().getPlayerList().transferPlayerToDimension((EntityPlayerMP)entity, teleporterData.dimension, new SteamWorldTeleporter((WorldServer)world, teleporterData.position.add(0.5, 1.5, 0.5)));
						else
							world.getMinecraftServer().getPlayerList().transferEntityToWorld(entity, currentDimension, (WorldServer)world, (WorldServer)targetWorld, new SteamWorldTeleporter((WorldServer)world, teleporterData.position.add(0.5, 1.5, 0.5)));
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
		portalUpdateTimer++;
		if(portalUpdateTimer >= PORTAL_UPDATE_FREQUENCY)
		{
			updateSkyOfOldPortalStatus();
			portalUpdateTimer = 0;
		}
	}
	
	public void updateSkyOfOldPortalStatus()
	{
		if(world.provider.getDimensionType().getId() != 0)
			return;
		
		int state = getPortalState();
		if(state == 1 || state == 2)
			setPortal(state == 1);
	}
	
	private void setPortal(boolean active)
	{
		for(int dx = -1; dx <= 1; dx++)
		{
			for(int dz = -1; dz <= 1; dz++)
			{
				int xzDistance = (dx * dx) + (dz * dz);
				if(xzDistance != 0)
					world.setBlockState(pos.add(dx, 0, dz), active ? PORTAL_BLOCK_STATE : AIR_BLOCK_STATE);
			}
		}
	}
	
	/*
	 * Returns portal state.
	 * 0 = Valid, Active
	 * 1 = Valid, Inactive
	 * 2 = Invalid, Active
	 * 3 = Invalid, Inactive
	 */
	private int getPortalState()
	{
		int state = 0;
		for(int dx = -2; dx <= 2; dx++)
		{
			for(int dz = -2; dz <= 2; dz++)
			{
				for(int dy = -1; dy <= 3; dy++)
				{
					BlockPos checkPos = pos.add(dx, dy, dz);
					int xzDistance = (dx * dx) + (dz * dz);
					if((dy == 0 && xzDistance > 3) || dy == -1)
					{
						if(world.getBlockState(checkPos) != QUARTZ_BLOCK_STATE)
							state |= 2;
					}
					else if(dy == 0 && xzDistance > 0)
					{
						if(world.getBlockState(checkPos) != PORTAL_BLOCK_STATE)
						{
							if(world.getBlockState(checkPos) != AIR_BLOCK_STATE)
								state |= 2;
							else
								state |= 1;
						}
					}
					else if(dy == 0 && xzDistance == 0)
					{
						if(world.getBlockState(checkPos).getBlock() != BlockInitializer.MACHINE_VARIANT)
							state |= 2;
					}
					else if(dy > 0)
					{
						if(xzDistance == 0)
						{
							if(world.getBlockState(checkPos).getBlock() != BlockInitializer.OBILISK)
								state |= 2;
						}
						else
						{
							if(world.getBlockState(checkPos) != AIR_BLOCK_STATE)
								state |= 2;
						}
					}
				}
			}
		}
		
		return state;
	}
}
