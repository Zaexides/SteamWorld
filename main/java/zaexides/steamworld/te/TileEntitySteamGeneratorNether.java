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
import zaexides.steamworld.utility.capability.FluidInputOutput;
import zaexides.steamworld.utility.capability.SteamWorldFluidTank;

public class TileEntitySteamGeneratorNether extends TileEntityBaseGenerator implements ICapabilityProvider, ITickable
{
	public boolean active = false;
	private boolean remoteActive = false;
	
	private int updateTimer = 0;
	private final int UPDATE_TIMER_MAX = 10;

	@Override
	public void update() 
	{
		if(efficiency != 2)
			efficiency = 2;
		
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
}
