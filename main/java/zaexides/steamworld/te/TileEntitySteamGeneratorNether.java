package zaexides.steamworld.te;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerFluidMap;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import scala.languageFeature.postfixOps;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.machines.BlockSteamGeneratorNether;
import zaexides.steamworld.fluids.FluidSteam;
import zaexides.steamworld.utility.FluidInputOutput;
import zaexides.steamworld.utility.SteamWorksFluidTank;

public class TileEntitySteamGeneratorNether extends SyncedTileEntity implements ICapabilityProvider, ITickable
{
	public SteamWorksFluidTank fluidIn = new SteamWorksFluidTank(Fluid.BUCKET_VOLUME * 4, this)
			{
				@Override
				public boolean canFillFluidType(net.minecraftforge.fluids.FluidStack fluid)
				{
					return fluid.getFluid() == FluidRegistry.WATER;
				};
			};
	public SteamWorksFluidTank fluidOut = new SteamWorksFluidTank(Fluid.BUCKET_VOLUME * 4, this)
			{
				@Override
				public boolean canFillFluidType(FluidStack fluid) { return false; };
			};
			
	public FluidInputOutput fluidInOut = new FluidInputOutput(fluidIn, fluidOut);
			
	public boolean active = false;
	private boolean remoteActive = false;
	
	private int updateTimer = 0;
	private final int UPDATE_TIMER_MAX = 10;
			
	public TileEntitySteamGeneratorNether() 
	{
		fluidIn.setTileEntity(this);
		fluidIn.allowDirtyMarking = true;
		fluidOut.setTileEntity(this);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		super.readFromNBT(compound);
		
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
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		NBTTagCompound fluidInTag = new NBTTagCompound();
		fluidIn.writeToNBT(fluidInTag);
		compound.setTag("fluidIn", fluidInTag);
		
		NBTTagCompound fluidOutTag = new NBTTagCompound();
		fluidOut.writeToNBT(fluidOutTag);
		compound.setTag("fluidOut", fluidOutTag);
		
		return super.writeToNBT(compound);
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
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(fluidInOut);
		return super.getCapability(capability, facing);
	}

	@Override
	public void update() 
	{
		if(world.isRemote || isInvalid())
			return;
		
		if(fluidIn.FluidAmount(FluidRegistry.WATER) > 0 && fluidOut.getFluidAmount() < (fluidOut.getCapacity()))
		{
			MakeSteam();
			if(!active)
				active = true;
			markDirty(false);
		}
		else if(active)
		{
			markDirty(false);
			active = false;
		}
		
		updateTimer++;
		if(updateTimer >= UPDATE_TIMER_MAX)
		{
			if(remoteActive != active)
			{
				((BlockSteamGeneratorNether)world.getBlockState(pos).getBlock()).UpdateState(world, pos, active);
				remoteActive = active;
			}
			markDirty();
			updateTimer = 0;
		}
	}
	
	private void MakeSteam()
	{
		if(fluidIn.FluidAmount(FluidRegistry.WATER) > 0)
		{
			fluidIn.allowDirtyMarking = false;
			
			int drainAmount = fluidIn.drain(2, false).amount;
			
			FluidStack fluidStack = new FluidStack(FluidSteam.fluidSteam, drainAmount);
			int fillAmount = fluidOut.fillInternal(fluidStack, true);
			
			fluidIn.drain(fillAmount, true);
			
			fluidIn.allowDirtyMarking = true;
		}
	}
}
