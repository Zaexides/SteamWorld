package zaexides.steamworld.te;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import com.typesafe.config.Config;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.Sound;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import zaexides.steamworld.ConfigHandler;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.machines.BlockFisher;
import zaexides.steamworld.blocks.machines.BlockGrinder;
import zaexides.steamworld.blocks.machines.BlockSWFurnace;
import zaexides.steamworld.fluids.FluidSteam;
import zaexides.steamworld.items.ItemDrillHead;
import zaexides.steamworld.recipe.handling.DustRecipeHandler;
import zaexides.steamworld.recipe.handling.MinerRecipeHandler;
import zaexides.steamworld.utility.capability.ItemStackHandlerInput;
import zaexides.steamworld.utility.capability.ItemStackHandlerOutput;
import zaexides.steamworld.utility.capability.SteamWorldFluidTank;

public class TileEntityMiner extends TileEntityMachine implements ITickable
{
	public ItemStackHandlerInput inputStack = new ItemStackHandlerInput(1)
	{
		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
		{
			if(stack.getItem() instanceof ItemDrillHead)
				return super.insertItem(slot, stack, simulate);
			else
				return stack;
		}
	};
	public ItemStackHandlerOutput outputStack = new ItemStackHandlerOutput(1);
	
	private int speed = 1;
	public byte maxTier = 4;
	
	private static final int BASE_COST_PER_TICK = 5;
	public static final int TIME_PER_ORE = 1200;
			
	public void SetStats(int efficiency, byte maxTier)
	{
		this.speed = efficiency;
		this.maxTier = maxTier;
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
		compound.setInteger("efficiency", speed);
		compound.setByte("max_tier", maxTier);
		
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
		speed = compound.getInteger("efficiency");
		maxTier = compound.getByte("max_tier");
	}
	
	@Override
	public boolean Execute()
	{
		if(steamTank.getFluidAmount() < (BASE_COST_PER_TICK * speed))
			return false;
		
		ItemStack drillStack = inputStack.getStackInSlot(0).copy();
		if(outputStack.getStackInSlot(0).getCount() == 0 && drillStack.getCount() != 0)
		{
			SetActive(true);
			progression += speed;
			steamTank.drain(BASE_COST_PER_TICK * speed, true);
			
			if(progression >= TIME_PER_ORE)
			{
				ItemDrillHead drillHead = ((ItemDrillHead)drillStack.getItem());
				byte drillTier = drillHead.getTier();
				
				outputStack.setStackInSlot(0, MinerRecipeHandler.GetRandomResult(world.rand, drillTier).copy());
				ItemDrillHead.Damage(drillStack, world.rand);
				
				if(drillStack.getItemDamage() >= drillStack.getMaxDamage() - 1)
					inputStack.setStackInSlot(0, ItemStack.EMPTY);
				else
					inputStack.setStackInSlot(0, drillStack);
				
				progression = 0;
			}
			return true;
		}
		
		return false;
	}
}
