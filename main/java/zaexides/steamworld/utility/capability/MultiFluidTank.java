package zaexides.steamworld.utility.capability;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mezz.jei.plugins.vanilla.ingredients.FluidStackHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class MultiFluidTank implements IFluidHandler
{
	public List<SteamWorldFluidTank> tanks = new ArrayList<SteamWorldFluidTank>();
	public int capacity = Fluid.BUCKET_VOLUME * 1000;
	public TileEntity tileEntity;
	
	public MultiFluidTank(TileEntity tileEntity) 
	{
		this.tileEntity = tileEntity;
	}
	
	public MultiFluidTank(TileEntity tileEntity, int capacity)
	{
		this.capacity = capacity;
		this.tileEntity = tileEntity;
	}

	@Override
	public IFluidTankProperties[] getTankProperties() 
	{
		List<IFluidTankProperties> properties = new ArrayList<IFluidTankProperties>();
		for(SteamWorldFluidTank fluidTank : tanks)
		{
			properties.addAll(Arrays.asList(fluidTank.getTankProperties()));
		}
		return Arrays.copyOf(properties.toArray(), properties.size(), IFluidTankProperties[].class);
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) 
	{
		for(SteamWorldFluidTank fluidTank : tanks)
		{
			if(fluidTank.getFluid().getFluid() == resource.getFluid())
			{
				return fluidTank.fill(resource, doFill);
			}
		}
		
		SteamWorldFluidTank newTank = new SteamWorldFluidTank(capacity, tileEntity);
		if(doFill)
			tanks.add(newTank);
		return newTank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) 
	{
		FluidStack output = null;
		for(SteamWorldFluidTank fluidTank : tanks)
		{
			if(fluidTank.getFluid().getFluid() == resource.getFluid())
			{
				output = fluidTank.drain(resource, doDrain);
			}
		}
		CleanUp();
		return output;
	}
	
	private void CleanUp()
	{
		List<SteamWorldFluidTank> removeList = new ArrayList<SteamWorldFluidTank>();
		for(int i = 0; i < tanks.size(); i++)
		{
			if(tanks.get(i).getFluidAmount() == 0)
				removeList.add(tanks.get(i));
		}
		tanks.removeAll(removeList);
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) 
	{
		return drain(maxDrain, 0, doDrain);
	}
	
	public FluidStack drain(int maxDrain, int tank, boolean doDrain)
	{
		FluidStack output = null;
		if(tanks.size() > tank)
			output = tanks.get(tank).drain(maxDrain, doDrain);
		CleanUp();
		return output;
	}
	
	public int size()
	{
		return tanks.size();
	}
	
	public void ReadFromNBT(NBTTagCompound compound)
	{
		capacity = compound.getInteger("capacity");
		int i = 0;
		while(compound.hasKey("tank_" + i))
		{
			SteamWorldFluidTank fluidTank = new SteamWorldFluidTank(capacity, tileEntity);
			tanks.add(fluidTank);
			fluidTank.readFromNBT((NBTTagCompound)compound.getTag("tank_" + i));
			i++;
		}
	}
	
	public void WriteToNBT(NBTTagCompound compound)
	{
		for(int i = 0; i < tanks.size(); i++)
		{
			NBTTagCompound subCompound = new NBTTagCompound();
			tanks.get(i).writeToNBT(subCompound);
			compound.setTag("tank_" + i, subCompound);
		}
		compound.setInteger("capacity", capacity);
	}
}
