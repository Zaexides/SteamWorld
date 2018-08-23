package zaexides.steamworld.te;

import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidRegistry;
import zaexides.steamworld.blocks.machines.BlockSteamGeneratorNether;

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
