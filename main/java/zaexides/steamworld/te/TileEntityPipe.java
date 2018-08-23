package zaexides.steamworld.te;

import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileEntityPipe extends TileEntity
{
	public void fetch(List<IFluidHandler> machineList, List<BlockPos> passedPositions, EnumFacing from, int distanceLeft)
	{
		if(distanceLeft <= 0 || passedPositions.contains(pos))
			return;
		passedPositions.add(pos);
		
		for(EnumFacing to : EnumFacing.VALUES)
		{
			if(!to.equals(from))
			{
				TileEntity nextEntity = world.getTileEntity(pos.add(to.getDirectionVec()));
				
				if(nextEntity != null)
				{
					if(nextEntity instanceof TileEntityPipe)
						((TileEntityPipe)nextEntity).fetch(machineList, passedPositions, to.getOpposite(), distanceLeft - 1);
					else if(nextEntity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, to.getOpposite()))
					{
						IFluidHandler fluidHandler = nextEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, to.getOpposite());
						if(!machineList.contains(fluidHandler))
							machineList.add(fluidHandler);
					}
				}
			}
		}
	}
}
