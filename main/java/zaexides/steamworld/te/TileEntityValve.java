package zaexides.steamworld.te;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.plaf.basic.BasicBorders.MarginBorder;

import org.apache.logging.log4j.Level;

import com.jcraft.jorbis.Block;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import scala.annotation.elidable;
import scala.collection.generic.BitOperations.Int;
import zaexides.steamworld.BlockInitializer;
import zaexides.steamworld.ConfigHandler;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.machines.BlockValve;
import zaexides.steamworld.utility.PosFacing;
import zaexides.steamworld.utility.capability.MultiFluidTank;
import zaexides.steamworld.utility.capability.SteamWorldFluidTank;

public class TileEntityValve extends SyncedTileEntity implements ICapabilityProvider, ITickable
{
	public boolean invertedRedstone = true;
	public boolean ignoreRedstone = false;
	public boolean enabled = true;
	
	public MultiFluidTank multiFluidTank = new MultiFluidTank(this);
	
	private int updateTimer = ConfigHandler.fluidControllerUpdateRate;
	private int transportSpeed = 10;
	
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
			GetInput();
			GetOutput();
		}
		
		markDirty(false);
	}
	
	private void GetInput() 
	{
		Queue<PosFacing> neighbors = new LinkedList<PosFacing>();
		List<FluidHandlerTileEntityCombination> combinations = new ArrayList<FluidHandlerTileEntityCombination>();
		List<BlockPos> completed = new ArrayList<BlockPos>();
		completed.add(pos);
		
		EnumFacing blockFacing = world.getBlockState(pos).getValue(BlockValve.FACING);
		Enqueue(neighbors, completed, pos.add(blockFacing.getOpposite().getDirectionVec()), blockFacing);
		
		while(neighbors.size() > 0)
		{
			PosFacing posFace = neighbors.poll();
			BlockPos pos = posFace.blockPos;
			
			TileEntity tEntity = world.getTileEntity(pos);
			if(tEntity != null && !tEntity.isInvalid() && tEntity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, posFace.facing))
			{
				IFluidHandler fluidHandler = tEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, posFace.facing);
				if(fluidHandler != null)
					combinations.add(new FluidHandlerTileEntityCombination(tEntity, fluidHandler));
			}
			
			if(world.getBlockState(pos).getBlock() == BlockInitializer.BLOCK_FLUID_PIPE)
				EnqueueNeighbors(neighbors, completed, pos);
		}
		
		Collections.shuffle(combinations);
		
		if(combinations.size() > 0)
		{
			int totalDrainAmount = ConfigHandler.fluidControllerUpdateRate * transportSpeed;
			double drainPartitionDouble = split(totalDrainAmount, combinations.size());
			for(int i = 0; i < combinations.size(); i++)
			{
				int drainAmount = (int)drainPartitionDouble;
				if(drainAmount <= 0)
					break;
				
				IFluidHandler fluidHandler = combinations.get(i).fluidHandler;
				FluidStack fluidStack = fluidHandler.drain(drainAmount, false);
				if(fluidStack != null && fluidStack.amount > 0)
				{
					int fillAmount = multiFluidTank.fill(fluidStack, true);
					if(fillAmount > 0)
					{
						fluidHandler.drain(fillAmount, true);
						combinations.get(i).tileEntity.markDirty();
						totalDrainAmount -= fillAmount;
						break;
					}
				}
			}
		}
	}
	
	private void GetOutput() 
	{
		Queue<PosFacing> neighbors = new LinkedList<PosFacing>();
		List<FluidHandlerTileEntityCombination> combinations = new ArrayList<FluidHandlerTileEntityCombination>();
		List<BlockPos> completed = new ArrayList<BlockPos>();
		completed.add(pos);
		
		EnumFacing blockFacing = world.getBlockState(pos).getValue(BlockValve.FACING);
		Enqueue(neighbors, completed, pos.add(blockFacing.getDirectionVec()), blockFacing.getOpposite());
		
		while(neighbors.size() > 0)
		{
			PosFacing posFace = neighbors.poll();
			BlockPos pos = posFace.blockPos;
			
			TileEntity tEntity = world.getTileEntity(pos);
			if(tEntity != null && !tEntity.isInvalid() && tEntity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, posFace.facing))
			{
				IFluidHandler fluidHandler = tEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, posFace.facing);
				if(fluidHandler != null)
					combinations.add(new FluidHandlerTileEntityCombination(tEntity, fluidHandler));
			}
			
			if(world.getBlockState(pos).getBlock() == BlockInitializer.BLOCK_FLUID_PIPE)
				EnqueueNeighbors(neighbors, completed, pos);
		}
		
		Collections.shuffle(combinations);
		
		if(combinations.size() > 0)
		{
			int totalDrainAmount = ConfigHandler.fluidControllerUpdateRate * transportSpeed;
			double drainPartitionDouble = split(totalDrainAmount, combinations.size());
			for(int i = 0; i < combinations.size(); i++)
			{
				int drainAmount = (int)drainPartitionDouble;
				if(drainAmount <= 0)
					break;
				
				IFluidHandler fluidHandler = combinations.get(i).fluidHandler;
				totalDrainAmount -= FillOutput(combinations.get(i).tileEntity, drainAmount, fluidHandler);
			}
		}
	}
	
	private int FillOutput(TileEntity tileEntity, int drainAmount, IFluidHandler fluidHandler)
	{
		for(int j = 0; j < multiFluidTank.size(); j++)
		{
			FluidStack fluidStack = multiFluidTank.drain(drainAmount, j, false);
			if(fluidStack != null && fluidStack.amount > 0)
			{
				int fillAmount = fluidHandler.fill(fluidStack, true);
				
				if(fillAmount > 0)
				{
					multiFluidTank.drain(fillAmount, j, true);
					tileEntity.markDirty();
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
	
	private void EnqueueNeighbors(Queue<PosFacing> neighbors, List<BlockPos> completed, BlockPos pos)
	{
		Enqueue(neighbors, completed, pos.north(), EnumFacing.SOUTH);
		Enqueue(neighbors, completed, pos.south(), EnumFacing.NORTH);
		Enqueue(neighbors, completed, pos.east(), EnumFacing.WEST);
		Enqueue(neighbors, completed, pos.west(), EnumFacing.EAST);
		Enqueue(neighbors, completed, pos.up(), EnumFacing.DOWN);
		Enqueue(neighbors, completed, pos.down(), EnumFacing.UP);
	}
	
	private void Enqueue(Queue<PosFacing> neighbors, List<BlockPos> completed, BlockPos pos, EnumFacing facing)
	{
		if(!completed.contains(pos))
		{
			if(world.getBlockState(pos).getBlock() != BlockInitializer.BLOCK_FLUID_PIPE)
			{
				TileEntity tEntity = world.getTileEntity(pos);
				if(tEntity == null || tEntity.isInvalid() || !tEntity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing))
					return;
			}
			
			neighbors.offer(new PosFacing(pos, facing));
			completed.add(pos);
		}
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
	
	protected static class FluidHandlerTileEntityCombination
	{
		public IFluidHandler fluidHandler;
		public TileEntity tileEntity;
		
		public FluidHandlerTileEntityCombination() {}
		public FluidHandlerTileEntityCombination(TileEntity tileEntity, IFluidHandler fluidHandler)
		{
			this.fluidHandler = fluidHandler;
			this.tileEntity = tileEntity;
		}
	}
}
