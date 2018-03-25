package zaexides.steamworld.te;

import java.util.ArrayList;
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
import zaexides.steamworld.init.ItemInitializer;
import zaexides.steamworld.recipe.handling.AssemblyRecipe;
import zaexides.steamworld.recipe.handling.AssemblyRecipeHandler;
import zaexides.steamworld.recipe.handling.DustRecipeHandler;
import zaexides.steamworld.utility.capability.ItemStackHandlerInput;
import zaexides.steamworld.utility.capability.ItemStackHandlerOutput;
import zaexides.steamworld.utility.capability.ItemStackInputOutputHandler;
import zaexides.steamworld.utility.capability.SteamWorldFluidTank;

public class TileEntityAssembler extends TileEntityMachine implements ITickable
{
	private static final int CRYSTAL_SLOT = 6;
	public ItemStackHandlerInput inputStack = new ItemStackHandlerInput(7)
	{
		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) 
		{
			if(stack.getItem() == ItemInitializer.STEAITE_CRYSTAL)
				slot = CRYSTAL_SLOT;
			
			if(slot == CRYSTAL_SLOT)
			{
				if(stack.getItem() == ItemInitializer.STEAITE_CRYSTAL)
					return super.insertItem(slot, stack, simulate);
				else
					return stack;
			}
			else
				return super.insertItem(slot, stack, simulate);
		}
	};
	public ItemStackHandlerOutput outputStack = new ItemStackHandlerOutput(1);
	
	private int efficiency = 1;
	public int duration;
	
	private ItemStack lastOutput;
	private boolean firstTick = true;
	
	private static final int BASE_COST_PER_TICK = 20;
			
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
		{
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new ItemStackInputOutputHandler(inputStack, outputStack));
		}
		return super.getCapability(capability, facing);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		steamTank.writeToNBT(compound);
		compound.setTag("items_in", inputStack.serializeNBT());
		compound.setTag("items_out", outputStack.serializeNBT());
		
		compound.setInteger("progress", progression);
		compound.setInteger("efficiency", efficiency);
		compound.setInteger("duration", duration);
		
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		super.readFromNBT(compound);
		steamTank.readFromNBT(compound);
		if(compound.hasKey("items_out"))
			outputStack.deserializeNBT((NBTTagCompound) compound.getTag("items_out"));
		if(compound.hasKey("items_in"))
			inputStack.deserializeNBT((NBTTagCompound) compound.getTag("items_in"));
		
		progression = compound.getInteger("progress");
		efficiency = compound.getInteger("efficiency");
		duration = compound.getInteger("duration");
	}
	
	@Override
	public boolean Execute()
	{
		if(steamTank.getFluidAmount() < BASE_COST_PER_TICK * efficiency)
			return false;
		
		List<ItemStack> inputs = new ArrayList<ItemStack>();
		for(int i = 0; i < 6; i++)
			inputs.add(inputStack.getStackInSlot(i));
		AssemblyRecipe recipe = AssemblyRecipeHandler.getOutput(inputs);
		if(recipe == null)
		{
			progression = 0;
			return false;
		}
		ItemStack output = recipe.output;
		duration = recipe.duration;
		
		if(firstTick)
			firstTick = false;
		else
		{
			if(output.getItem() != lastOutput.getItem() || output.getItemDamage() != lastOutput.getItemDamage())
				progression = 0;
		}
		
		lastOutput = output;
		
		if(outputStack.getStackInSlot(0).getItem() != Items.AIR)
		{
			if(outputStack.getStackInSlot(0).getItem() != output.getItem() || outputStack.getStackInSlot(0).getItemDamage() != output.getItemDamage())
				return false;
			
			if((outputStack.getStackInSlot(0).getCount() + output.getCount()) > output.getMaxStackSize())
				return false;
		}
		else
			outputStack.setStackInSlot(0, ItemStack.EMPTY);
		
		if(output != null && output != ItemStack.EMPTY && inputStack.getStackInSlot(6).getItem() == ItemInitializer.STEAITE_CRYSTAL)
		{
			SetActive(true);
			
			steamTank.drain(efficiency * BASE_COST_PER_TICK, true);
			progression += efficiency;
			
			if(progression >= duration)
			{
				ItemStack outputSlot = outputStack.getStack(0);
				if(outputSlot.getItem() != Items.AIR)
				{
					outputSlot.grow(output.getCount());
					outputStack.setStackInSlot(0, outputSlot);
				}
				else
					outputStack.setStackInSlot(0, output);
				for(int i = 0; i < 6; i++)
				{
					ItemStack itemStack = inputStack.getStack(i);
					itemStack.shrink(1);
					inputStack.setStackInSlot(i, itemStack);
				}
				
				ItemStack crystalStack = inputStack.getStackInSlot(6).copy();
				int damage = crystalStack.getItemDamage();
				if(damage >= crystalStack.getMaxDamage()-1)
					inputStack.setStackInSlot(CRYSTAL_SLOT, ItemStack.EMPTY);
				else
				{
					crystalStack.setItemDamage(damage + 1);
					inputStack.setStackInSlot(CRYSTAL_SLOT, crystalStack);
				}
				
				progression = 0;
			}
			return true;
		}
		return false;
	}
}
