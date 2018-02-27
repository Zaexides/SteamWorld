package zaexides.steamworld.utility.capability;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidRegistry;

public class SteamWorldSteamTank extends SteamWorldFluidTank
{
	public SteamWorldSteamTank(int capacity, TileEntity tileEntity) 
	{
		super(capacity, tileEntity);
	}
	
	@Override
	public boolean canFillFluidType(net.minecraftforge.fluids.FluidStack fluid) 
	{
		return FluidRegistry.getFluidName(fluid).endsWith("steam");
	}
}
