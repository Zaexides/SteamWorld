package zaexides.steamworld.te;

import java.util.List;

import org.apache.logging.log4j.Level;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.machines.BlockFisher;
import zaexides.steamworld.blocks.machines.BlockGrinder;
import zaexides.steamworld.blocks.machines.BlockSWFurnace;
import zaexides.steamworld.fluids.FluidSteam;
import zaexides.steamworld.recipe.handling.DustRecipeHandler;
import zaexides.steamworld.utility.capability.ItemStackHandlerOutput;
import zaexides.steamworld.utility.capability.SteamWorldFluidTank;

public class TileEntityFisher extends TileEntityMachine implements ITickable
{
	public ItemStackHandlerOutput outputStack = new ItemStackHandlerOutput(1);
	
	private int efficiency = 1;
	public final int MAX_PROGRESSION = 400;
	
	private static final int BASE_COST_PER_TICK = 2;
			
	public void SetStats(int efficiency)
	{
		this.efficiency = efficiency;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) 
	{
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return true;
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return true;
		
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) 
	{
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(steamTank);
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(outputStack);
		return super.getCapability(capability, facing);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		steamTank.writeToNBT(compound);
		compound.setTag("items_out", outputStack.serializeNBT());
		
		compound.setInteger("progress", progression);
		compound.setInteger("efficiency", efficiency);
		
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		super.readFromNBT(compound);
		steamTank.readFromNBT(compound);
		if(compound.hasKey("items_out"))
			outputStack.deserializeNBT((NBTTagCompound) compound.getTag("items_out"));
		
		progression = compound.getInteger("progress");
		efficiency = compound.getInteger("efficiency");
	}
	
	@Override
	public boolean Execute()
	{
		if(!hasWaterBesidesIt())
			return false;
		
		if(steamTank.getFluidAmount() < (BASE_COST_PER_TICK * efficiency))
			return false;
		
		if(outputStack.getStackInSlot(0).getCount() == 0)
		{
			SetActive(true);
			progression += efficiency;
			steamTank.drain(BASE_COST_PER_TICK * efficiency, true);
			
			if(progression >= MAX_PROGRESSION)
			{
				LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer)this.world);
	            List<ItemStack> result = this.world.getLootTableManager().getLootTableFromLocation(LootTableList.GAMEPLAY_FISHING).generateLootForPools(world.rand, lootcontext$builder.build());
	            
	            if(result.size() > 0)
	            	outputStack.setStackInSlot(0, result.get(0));
	            
	            progression = 0;
			}
            
            return true;
		}
		return false;
	}
	
	private boolean hasWaterBesidesIt()
	{
		return isWater(pos.up())
				|| isWater(pos.down())
				|| isWater(pos.east())
				|| isWater(pos.west())
				|| isWater(pos.north())
				|| isWater(pos.south());
	}
	
	private boolean isWater(BlockPos pos)
	{
		Block block = world.getBlockState(pos).getBlock();
		return block == Blocks.WATER || block == Blocks.FLOWING_WATER;
	}
}
