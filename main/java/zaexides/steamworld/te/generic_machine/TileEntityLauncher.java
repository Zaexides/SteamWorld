package zaexides.steamworld.te.generic_machine;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import zaexides.steamworld.init.SoundInitializer;
import zaexides.steamworld.te.SyncedTileEntity;
import zaexides.steamworld.te.generic_machine.interfaces.IGenericMachineWalkActivate;
import zaexides.steamworld.utility.capability.SteamWorldSteamTank;

public class TileEntityLauncher extends SyncedTileEntity implements ICapabilityProvider, IGenericMachineWalkActivate
{
	public SteamWorldSteamTank steamTank = new SteamWorldSteamTank(Fluid.BUCKET_VOLUME, this);
	
	public static final int STEAM_PER_FORCE = 100;
	public static final int MAX_FORCE = Fluid.BUCKET_VOLUME / STEAM_PER_FORCE;
	public int force = 1;
	
	public TileEntityLauncher() 
	{
		steamTank.allowDirtyMarking = true;
	}
	
	public void ChangeForce(float hitY, EntityPlayer player)
	{		
		if(hitY > 0.5f && force < MAX_FORCE)
		{
			force++;
			TextComponentTranslation textComponentTranslation = new TextComponentTranslation("message.steamworld.launcher.force_increase", force);
			player.sendMessage(textComponentTranslation);
		}
		else if(hitY <= 0.5f && force > 1)
		{
			force--;
			TextComponentTranslation textComponentTranslation = new TextComponentTranslation("message.steamworld.launcher.force_decrease", force);
			player.sendMessage(textComponentTranslation);
		}
	}

	@Override
	public void onWalkedOn(Entity entity) 
	{
		int steamRequired = force * STEAM_PER_FORCE;
		if(steamTank.getFluidAmount() >= steamRequired)
		{
			double fx_x = pos.getX() + 0.5d;
			double fx_y = pos.getY() + 1.25d;
			double fx_z = pos.getZ() + 0.5d;
			
			entity.addVelocity(0, force * 0.2d, 0);
			if(world.isRemote)
				world.spawnParticle(EnumParticleTypes.CLOUD, fx_x, fx_y, fx_z, 0, force * 0.1d, 0);
			world.playSound((EntityPlayer)null, fx_x, fx_y, fx_z, SoundInitializer.LAUNCHER_LAUNCH, SoundCategory.BLOCKS, 1, 1);
			steamTank.drain(steamRequired, true);
			markDirty();
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		compound.setInteger("force", force);
		steamTank.writeToNBT(compound);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		force = compound.getInteger("force");
		steamTank.readFromNBT(compound);
		super.readFromNBT(compound);
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
}
