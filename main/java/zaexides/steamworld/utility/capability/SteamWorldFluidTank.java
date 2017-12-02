package zaexides.steamworld.utility.capability;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import zaexides.steamworld.te.TileEntitySteamGenerator;

public class SteamWorldFluidTank extends FluidTank
{
	private TileEntity tileEntity;
	public boolean allowDirtyMarking = false;

	public SteamWorldFluidTank(int capacity, TileEntity tileEntity) 
	{
		super(capacity);
		this.tileEntity = tileEntity;
	}

	@Override
	public int fillInternal(FluidStack resource, boolean doFill) 
	{
		if(allowDirtyMarking)
			tileEntity.markDirty();
		return super.fillInternal(resource, doFill);
	};
	
	@Override
	public FluidStack drainInternal(int maxDrain, boolean doDrain) 
	{
		if(allowDirtyMarking)
			tileEntity.markDirty();
		return super.drainInternal(maxDrain, doDrain);
	};
	
	public int FluidAmount(Fluid fluid)
	{
		if(getFluidAmount() > 0 && this.fluid.getFluid() == fluid)
			return getFluidAmount();
		else
			return 0;
	}
}
