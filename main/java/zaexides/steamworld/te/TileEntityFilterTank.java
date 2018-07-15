package zaexides.steamworld.te;

import org.apache.logging.log4j.Level;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.fluids.FluidSteam;
import zaexides.steamworld.utility.capability.SteamWorldFluidTank;

public class TileEntityFilterTank extends SyncedTileEntity implements ICapabilityProvider
{
	private Fluid allowedFluid;
	
	public SteamWorldFluidTank tank = new SteamWorldFluidTank(Fluid.BUCKET_VOLUME * 16, this)
	{
		@Override
		public boolean canFillFluidType(FluidStack fluid) 
		{
			if(allowedFluid == null)
				return super.canFillFluidType(fluid);
			else
				return super.canFillFluidType(fluid) && fluid.getFluid().equals(allowedFluid);
		}
	};
	
	public TileEntityFilterTank() 
	{
		tank.allowDirtyMarking = true;
	}
	
	public void setAllowedFluid(Fluid fluid)
	{
		allowedFluid = fluid;
		markDirty();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		tank.writeToNBT(compound);
		compound.setString("target", allowedFluid == null ? "null" : allowedFluid.getName());
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		tank.readFromNBT(compound);
		
		String fluidName = compound.getString("target");
		FluidStack sampleStack = FluidRegistry.getFluidStack(fluidName, 1);
		if(sampleStack != null && sampleStack.amount > 0)
			allowedFluid = sampleStack.getFluid();
		else
			allowedFluid = null;
		
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
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
		return super.getCapability(capability, facing);
	}
}
