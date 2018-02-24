package zaexides.steamworld.te;

import java.util.List;

import org.apache.logging.log4j.Level;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import zaexides.steamworld.ConfigHandler;
import zaexides.steamworld.SteamWorld;

public class TileEntityPipe extends TileEntity
{
	public void fetch(List<IFluidHandler> machineList, EnumFacing from, int distanceLeft)
	{
		if(distanceLeft <= 0)
			return;
		
		for(EnumFacing to : EnumFacing.VALUES)
		{
			if(!to.equals(from))
			{
				TileEntity nextEntity = world.getTileEntity(pos.add(to.getDirectionVec()));
				
				if(nextEntity != null)
				{
					if(nextEntity instanceof TileEntityPipe)
						((TileEntityPipe)nextEntity).fetch(machineList, to.getOpposite(), distanceLeft - 1);
					else if(nextEntity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, to))
						machineList.add(nextEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, to.getOpposite()));
				}
			}
		}
	}
}
