package zaexides.steamworld.te;

import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.Fluid;
import zaexides.steamworld.blocks.machines.BlockMachine;
import zaexides.steamworld.utility.capability.SteamWorldSteamTank;

public abstract class TileEntityMachine extends SyncedTileEntity implements ITickable
{
	public SteamWorldSteamTank steamTank = new SteamWorldSteamTank(Fluid.BUCKET_VOLUME*4, this);
	
	public int progression = 0;
	protected boolean active = false;
	
	@Override
	public void update() 
	{
		if(world.isRemote || isInvalid())
			return;
		
		boolean success = false;
		if(steamTank.getFluidAmount() > 0)
			success = Execute();
		if(!success)
		{
			SetActive(false);
			if(progression > 0)
				progression--;
		}
		
		markDirty();
	}
	
	public void SetActive(boolean active)
	{
		if(this.active != active)
		{
			this.active = active;
			((BlockMachine)world.getBlockState(pos).getBlock()).UpdateState(world, pos, active);
		}
	}
	
	public abstract boolean Execute();
}
