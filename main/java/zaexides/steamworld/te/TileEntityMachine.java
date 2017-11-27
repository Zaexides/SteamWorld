package zaexides.steamworld.te;

import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import zaexides.steamworld.blocks.machines.BlockFisher;
import zaexides.steamworld.blocks.machines.BlockMachine;
import zaexides.steamworld.utility.capability.SteamWorksFluidTank;

public abstract class TileEntityMachine extends SyncedTileEntity implements ITickable
{
	public SteamWorksFluidTank steamTank = new SteamWorksFluidTank(Fluid.BUCKET_VOLUME*4, this)
	{
		@Override
		public boolean canFillFluidType(net.minecraftforge.fluids.FluidStack fluid) 
		{
			return FluidRegistry.getFluidName(fluid).endsWith("steam");
		};
	};
	
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
