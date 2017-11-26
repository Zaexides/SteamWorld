package zaexides.steamworld.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class FluidInputOutput implements IFluidHandler
{
	protected SteamWorksFluidTank input, output;
	
	public FluidInputOutput(SteamWorksFluidTank input, SteamWorksFluidTank output) 
	{
		this.input = input;
		this.output = output;
	}

	@Override
	public IFluidTankProperties[] getTankProperties() 
	{
		List<IFluidTankProperties> properties = new ArrayList<IFluidTankProperties>();
		properties.addAll(Arrays.asList(input.getTankProperties()));
		properties.addAll(Arrays.asList(output.getTankProperties()));
		return (IFluidTankProperties[]) properties.toArray();
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) 
	{
		return input.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain)
	{
		return output.drain(resource, doDrain);
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) 
	{
		return output.drain(maxDrain, doDrain);
	}

}
