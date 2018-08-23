package zaexides.steamworld.te;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import zaexides.steamworld.blocks.machines.BlockExperienceMachine;

public class TileEntityExperienceMachine extends TileEntityMachine implements ITickable
{
	private int efficiency = 1;
	public final int MAX_PROGRESSION = 100;
	
	private static final int COST_PER_TICK = 30;
			
	public void SetStats(int efficiency)
	{
		this.efficiency = efficiency;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) 
	{
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return true;
		
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) 
	{
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(steamTank);
		return super.getCapability(capability, facing);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		steamTank.writeToNBT(compound);
		
		compound.setInteger("progress", progression);
		compound.setInteger("efficiency", efficiency);
		
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		super.readFromNBT(compound);
		steamTank.readFromNBT(compound);
		
		progression = compound.getInteger("progress");
		efficiency = compound.getInteger("efficiency");
	}
	
	@Override
	public boolean Execute()
	{
		if(steamTank.getFluidAmount() < (COST_PER_TICK * efficiency) || world.getEntitiesWithinAABB(EntityXPOrb.class, new AxisAlignedBB(pos.add(-2,-2,-2), pos.add(2,2,2))).size() >= 10)
			return false;
		
		progression += efficiency;
		steamTank.drain(COST_PER_TICK * efficiency, true);
		SetActive(true);
		
		if(progression >= MAX_PROGRESSION)
		{
			int amount = 1;
			
			EnumFacing facing = world.getBlockState(pos).getValue(BlockExperienceMachine.FACING);
			double spawn_x = pos.getX() + 0.5d + facing.getDirectionVec().getX() * 0.6d;
			double spawn_y = pos.getY() + 0.5d;
			double spawn_z = pos.getZ() + 0.5d + facing.getDirectionVec().getZ() * 0.6d;
			
			world.spawnEntity(new EntityXPOrb(world, spawn_x, spawn_y, spawn_z, amount));
			progression = 0;
		}
		
		return true;
	}
}
