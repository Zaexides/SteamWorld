package zaexides.steamworld.te;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.logging.log4j.Level;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import zaexides.steamworld.BlockInitializer;
import zaexides.steamworld.ConfigHandler;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.machines.BlockDrain;
import zaexides.steamworld.utility.PosFacing;
import zaexides.steamworld.utility.SteamWorksFluidTank;

public class TileEntityDrain extends SyncedTileEntity implements ICapabilityProvider, ITickable
{
	public SteamWorksFluidTank tank = new SteamWorksFluidTank(Fluid.BUCKET_VOLUME, this);
	
	private int updateTimer = UPDATE_TIMER_RATE;
	private static final int UPDATE_TIMER_RATE = 5;

	public boolean DrainBlock(BlockPos lookPos)
	{
		IBlockState blockState = world.getBlockState(lookPos);
		Block block = blockState.getBlock();
		
		if(block instanceof IFluidBlock)
		{
			IFluidBlock fluidBlock = (IFluidBlock)block;
			FluidStack fluidStack = fluidBlock.drain(world, lookPos, false);
			int amount = tank.fill(fluidStack, false);
			if(amount == Fluid.BUCKET_VOLUME)
			{
				fluidBlock.drain(world, lookPos, true);
				tank.fill(fluidStack, true);
				return true;
			}
		}
		else if(block instanceof BlockLiquid)
		{
			if(blockState.getValue(BlockLiquid.LEVEL) == 0)
			{
				FluidStack fluidStack = null;
				if(block == Blocks.LAVA)
					fluidStack = new FluidStack(FluidRegistry.LAVA, Fluid.BUCKET_VOLUME);
				if(block == Blocks.WATER)
					fluidStack = new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME);
				
				if(fluidStack != null)
				{
					int amount = tank.fill(fluidStack, false);
					if(amount == Fluid.BUCKET_VOLUME)
					{
						world.setBlockState(lookPos, Blocks.AIR.getDefaultState(), 3);
						world.notifyNeighborsOfStateChange(lookPos, block, true);
						tank.fill(fluidStack, true);
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public void FindBlockToDrain(BlockPos startPos) 
	{
		if(world.isRemote)
			return;
		
		if(tank.getFluidAmount() >= tank.getCapacity())
			return;
		
		if(DrainBlock(startPos))
			return;
		
		Queue<BlockPos> neighbors = new LinkedList<BlockPos>();
		List<BlockPos> completed = new ArrayList<BlockPos>();
		EnqueueNeighbors(neighbors, completed, startPos);
		
		while(neighbors.size() > 0 && completed.size() < ConfigHandler.drainMaxChecks)
		{
			BlockPos lookPos = neighbors.poll();
			if(DrainBlock(lookPos))
				return;
			EnqueueNeighbors(neighbors, completed, lookPos);
		}
	}
	
	private void EnqueueNeighbors(Queue<BlockPos> neighbors, List<BlockPos> completed, BlockPos pos)
	{
		Block block = world.getBlockState(pos).getBlock();
		if(!(block instanceof BlockFluidBase || block instanceof BlockLiquid || block == Blocks.AIR))
			return;
		
		Enqueue(neighbors, completed, pos.north());
		Enqueue(neighbors, completed, pos.south());
		Enqueue(neighbors, completed, pos.east());
		Enqueue(neighbors, completed, pos.west());
		
		if(block != Blocks.AIR)
			Enqueue(neighbors, completed, pos.up());
	}
	
	private void Enqueue(Queue<BlockPos> neighbors, List<BlockPos> completed, BlockPos pos)
	{
		if(!completed.contains(pos))
		{
			neighbors.offer(pos);
			completed.add(pos);
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		tank.writeToNBT(compound);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		super.readFromNBT(compound);
		tank.readFromNBT(compound);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) 
	{
		EnumFacing frontFace = world.getBlockState(pos).getValue(BlockDrain.FACING);
		if(facing != frontFace && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) 
	{
		EnumFacing frontFace = world.getBlockState(pos).getValue(BlockDrain.FACING);
		if(facing != frontFace && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
		return super.getCapability(capability, facing);
	}

	@Override
	public void update() 
	{
		if(isInvalid())
			return;
		updateTimer--;
		if(updateTimer <= 0)
		{
			IBlockState blockState = world.getBlockState(pos);
			if(blockState.getBlock() != BlockInitializer.BLOCK_DRAIN)
				return;
			BlockPos lookPos = pos.add(world.getBlockState(pos).getValue(BlockDrain.FACING).getDirectionVec());
			FindBlockToDrain(lookPos);
			updateTimer = UPDATE_TIMER_RATE;
		}
	}
}
