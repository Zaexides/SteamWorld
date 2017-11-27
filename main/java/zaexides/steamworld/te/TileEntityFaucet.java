package zaexides.steamworld.te;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.logging.log4j.Level;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDynamicLiquid;
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
import zaexides.steamworld.utility.capability.SteamWorksFluidTank;

public class TileEntityFaucet extends SyncedTileEntity implements ICapabilityProvider, ITickable
{
	public SteamWorksFluidTank tank = new SteamWorksFluidTank(Fluid.BUCKET_VOLUME, this);
	
	private int updateTimer = UPDATE_TIMER_RATE;
	private static final int UPDATE_TIMER_RATE = 5;

	public boolean PutBlock(BlockPos lookPos)
	{
		FluidStack fluidStack = tank.drain(Fluid.BUCKET_VOLUME, false);
		
		if(fluidStack.getFluid().getBlock() instanceof BlockFluidBase)
		{
			BlockFluidBase blockFluidBase = (BlockFluidBase)fluidStack.getFluid().getBlock();
			int amount = blockFluidBase.place(world, lookPos, fluidStack, true);
			if(amount > 0)
			{
				tank.drain(amount, true);
				return true;
			}
		}
		else if(fluidStack.amount == Fluid.BUCKET_VOLUME)
		{
			if(world.getBlockState(lookPos) == Blocks.AIR.getDefaultState() || world.getBlockState(lookPos).getBlock() instanceof BlockLiquid)
			{
				if(world.getBlockState(lookPos).getBlock() instanceof BlockLiquid)
				{
					if(world.getBlockState(lookPos).getValue(BlockLiquid.LEVEL) == 0)
						return false;
				}
				
				IBlockState blockState = fluidStack.getFluid().getBlock().getDefaultState();
				world.setBlockState(lookPos, blockState, 3);
				tank.drain(Fluid.BUCKET_VOLUME, true);
				world.notifyNeighborsOfStateChange(lookPos.down(), this.blockType, true);
				return true;
			}
		}
		return false;
	}
	
	public void FindPlaceToPut(BlockPos startPos) 
	{
		if(world.isRemote)
			return;
		
		if(tank.getFluidAmount() < tank.getCapacity())
			return;
		
		if(PutBlock(startPos))
			return;
		
		Queue<BlockPos> neighbors = new LinkedList<BlockPos>();
		List<BlockPos> completed = new ArrayList<BlockPos>();
		EnqueueNeighbors(neighbors, completed, startPos);
		
		while(neighbors.size() > 0 && completed.size() < ConfigHandler.faucetMaxChecks)
		{
			BlockPos lookPos = neighbors.poll();
			if(PutBlock(lookPos))
				return;
			EnqueueNeighbors(neighbors, completed, lookPos);
		}
	}
	
	private void EnqueueNeighbors(Queue<BlockPos> neighbors, List<BlockPos> completed, BlockPos pos)
	{
		Block block = world.getBlockState(pos).getBlock();
		if(!(block instanceof BlockFluidBase || block instanceof BlockLiquid || block == Blocks.AIR))
			return;
		
		Enqueue(neighbors, completed, pos.down());
		Enqueue(neighbors, completed, pos.north());
		Enqueue(neighbors, completed, pos.south());
		Enqueue(neighbors, completed, pos.east());
		Enqueue(neighbors, completed, pos.west());
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
			if(blockState.getBlock() != BlockInitializer.BLOCK_FAUCET)
				return;
			BlockPos lookPos = pos.add(blockState.getValue(BlockDrain.FACING).getDirectionVec());
			FindPlaceToPut(lookPos);
			updateTimer = UPDATE_TIMER_RATE;
		}
	}
}
