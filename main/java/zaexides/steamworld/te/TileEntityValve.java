package zaexides.steamworld.te;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import zaexides.steamworld.ConfigHandler;
import zaexides.steamworld.blocks.machines.BlockValve;
import zaexides.steamworld.utility.capability.MultiFluidTank;

public class TileEntityValve extends SyncedTileEntity implements ICapabilityProvider, ITickable
{
	public boolean invertedRedstone = true;
	public boolean ignoreRedstone = false;
	public boolean enabled = true;
	
	public MultiFluidTank multiFluidTank = new MultiFluidTank(this);
	
	private int updateTimer = ConfigHandler.fluidControllerUpdateRate;
	private int transportSpeed = 10;
	
	private List<IFluidHandler> inputMachines = new ArrayList<IFluidHandler>();
	
	private List<IFluidHandler> outputMachines = new ArrayList<IFluidHandler>();
	
	public void SetStats(int speed, int capacity)
	{
		multiFluidTank.capacity = capacity;
		transportSpeed = speed;
	}

	@Override
	public void update() 
	{
		if(world.isRemote || isInvalid())
			return;
		
		updateTimer--;
		if(updateTimer <= 0)
		{
			internalUpdate();
			updateTimer = ConfigHandler.fluidControllerUpdateRate;
		}
	}
	
	public boolean ChangeState(EntityPlayer player)
	{
		String msg;
		
		if(!ignoreRedstone)
		{
			if(invertedRedstone)
			{
				invertedRedstone = false;
				msg = "message.steamworld.rs_normal";
			}
			else
			{
				ignoreRedstone = true;
				msg = "message.steamworld.rs_ignore";
			}
		}
		else
		{
			ignoreRedstone = false;
			invertedRedstone = true;
			msg = "message.steamworld.rs_inverted";
		}
		
		if(!world.isRemote)
		{
			TextComponentTranslation textComponentTranslation = new TextComponentTranslation(msg, "");
			textComponentTranslation.getStyle().setItalic(true).setColor(TextFormatting.GRAY);
			player.sendMessage(textComponentTranslation);
		}
		markDirty();
		GetEnabledState();
		return true;
	}
	
	public void GetEnabledState() 
	{
		if(ignoreRedstone)
			enabled = true;
		else
		{
			boolean powered = world.isBlockIndirectlyGettingPowered(pos) > 0;
			
			if(invertedRedstone)
				enabled = !powered;
			else
				enabled = powered;
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setBoolean("enabled", enabled);
		
		int mode = (ignoreRedstone ? 1 : 0) + (invertedRedstone ? 2 : 0);
		compound.setInteger("rsmode", mode);
		multiFluidTank.WriteToNBT(compound);
		compound.setInteger("speed", transportSpeed);
		
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		enabled = compound.getBoolean("enabled");
		int mode = compound.getInteger("rsmode");
		
		invertedRedstone = (mode & 2) == 2;
		ignoreRedstone = (mode & 1) == 1;
		if(compound.hasKey("speed"))
			transportSpeed = compound.getInteger("speed");
		multiFluidTank.ReadFromNBT(compound);
		
		super.readFromNBT(compound);
	}

	private void internalUpdate()
	{
		GetEnabledState();
		
		if(enabled)
		{
			outputMachines.clear();
			inputMachines.clear();
			getInputOutput();
			drainInput();
			fillOutput();
		}
	}
	
	private void getInputOutput()
	{
		EnumFacing facing = world.getBlockState(pos).getValue(BlockValve.FACING);
		fetchListElements(outputMachines, facing);
		fetchListElements(inputMachines, facing.getOpposite());
	}
	
	private void fetchListElements(List<IFluidHandler> list, EnumFacing side)
	{
		list.clear();
		TileEntity sideEntity = world.getTileEntity(pos.add(side.getDirectionVec()));
		
		if(sideEntity != null)
		{
			if(sideEntity instanceof TileEntityPipe)
				((TileEntityPipe)sideEntity).fetch(list, new ArrayList<BlockPos>(), side.getOpposite(), ConfigHandler.pipeSystemMaxRange);
			else if(sideEntity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite()))
				list.add(sideEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite()));
		}
	}
	
	private void drainInput()
	{
		if(inputMachines.size() == 0)
			return;
		
		int totalDrainAmount = ConfigHandler.fluidControllerUpdateRate * transportSpeed;
		double drainPartition = split(totalDrainAmount, inputMachines.size());
		for(IFluidHandler fluidHandler : inputMachines)
		{
			int drainAmount = (int)drainPartition;
			if(drainAmount <= 0)
				break;
			
			FluidStack fluidStack = fluidHandler.drain(drainAmount, false);
			if(fluidStack != null && fluidStack.amount > 0)
			{
				int fillAmount = multiFluidTank.fill(fluidStack, true);
				if(fillAmount > 0)
				{
					fluidHandler.drain(fillAmount, true);
					totalDrainAmount -= fillAmount;
				}
			}
		}
	}
	
	private void fillOutput()
	{
		if(outputMachines.size() == 0)
			return;
		
		int totalFillAmount = ConfigHandler.fluidControllerUpdateRate * transportSpeed;
		double fillPartition = split(totalFillAmount, outputMachines.size());
		for(IFluidHandler fluidHandler : outputMachines)
		{
			int fillAmount = (int)fillPartition;
			if(fillAmount <= 0)
				break;
			
			totalFillAmount -= fillFluidHandler(fluidHandler, fillAmount);
		}
	}
	
	private int fillFluidHandler(IFluidHandler fluidHandler, int amount)
	{
		for(int i = 0; i < multiFluidTank.size(); i++)
		{
			FluidStack fluidStack = multiFluidTank.drain(amount, i, false);
			if(fluidStack != null && fluidStack.amount > 0)
			{
				int fillAmount = fluidHandler.fill(fluidStack, true);
				
				if(fillAmount > 0)
				{
					multiFluidTank.drain(fillAmount, i, true);
					return fillAmount;
				}
			}
		}
		return 0;
	}
	
	private int split(int numerator, int denominator)
	{
		int result = 0;
		while(numerator >= denominator)
		{
			numerator -= denominator;
			result++;
		}
		return result;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) 
	{
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
		{
			EnumFacing blockFacing = world.getBlockState(pos).getValue(BlockValve.FACING);
			if(facing == blockFacing || facing == blockFacing.getOpposite())
				return true;
		}
		
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) 
	{
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
		{
			EnumFacing blockFacing = world.getBlockState(pos).getValue(BlockValve.FACING);
			if(facing == blockFacing || facing == blockFacing.getOpposite())
			{
				return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(multiFluidTank);
			}
		}
		return super.getCapability(capability, facing);
	}
}
