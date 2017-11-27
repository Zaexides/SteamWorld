package zaexides.steamworld.te;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import com.typesafe.config.Config;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.Sound;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
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
import zaexides.steamworld.recipe.handling.DustRecipeHandler;
import zaexides.steamworld.utility.capability.ItemStackHandlerInput;
import zaexides.steamworld.utility.capability.SteamWorksFluidTank;

public class TileEntityFertilizer extends TileEntityMachine implements ITickable, IGuiButtonHandler
{
	public ItemStackHandlerInput inputStack = new ItemStackHandlerInput(1)
			{
				@Override
				public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
				{
					if(stack.getItem() instanceof ItemDye)
					{
						EnumDyeColor dyeColor = EnumDyeColor.byDyeDamage(stack.getMetadata());
						if(dyeColor == EnumDyeColor.WHITE)
							return super.insertItem(slot, stack, simulate);
					}
					
					return stack;
				}
			};
	
	private static int radius = ConfigHandler.fertilizerArea;
	
	private static final int MAX_PROGRESSION = 600;
	private int efficiency = 1;
	private int fertilizedBlocks = 0;
	public boolean cropOnlyMode = true;
	
	private static final int COST_PER_GROWABLE = 20;
	private static final int CROP_PER_BONEMEAL = 16;
	
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
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inputStack);
		return super.getCapability(capability, facing);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		steamTank.writeToNBT(compound);
		compound.setTag("items_in", inputStack.serializeNBT());
		compound.setInteger("efficiency", efficiency);
		compound.setInteger("progress", progression);
		compound.setInteger("used", fertilizedBlocks);
		compound.setBoolean("cropMode", cropOnlyMode);
		
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		super.readFromNBT(compound);
		steamTank.readFromNBT(compound);
		if(compound.hasKey("items_in"))
			inputStack.deserializeNBT((NBTTagCompound) compound.getTag("items_in"));
		efficiency = compound.getInteger("efficiency");
		progression = compound.getInteger("progression");
		fertilizedBlocks = compound.getInteger("used");
		cropOnlyMode = compound.getBoolean("cropMode");
	}
	
	@Override
	public boolean Execute()
	{
		if(inputStack.getStackInSlot(0).getCount() > 0)
		{
			SetActive(true);
			progression += efficiency;
			
			if(progression >= MAX_PROGRESSION)
			{
				for(int xOff = -radius; xOff <= radius; xOff++)
				{
					for(int yOff = -radius; yOff <= radius; yOff++)
					{
						for(int zOff = -radius; zOff <= radius; zOff++)
						{
							if(steamTank.getFluidAmount() < COST_PER_GROWABLE || inputStack.getStackInSlot(0).getCount() <= 0)
								return false;
							if(cropOnlyMode)
								fertilize(pos.add(xOff, yOff, zOff), BlockCrops.class);
							else
								fertilize(pos.add(xOff, yOff, zOff), IGrowable.class);
						}
					}
				}
				
				progression = 0;
			}
			
			return true;
		}
		return false;
	}
	
	private void fertilize(BlockPos pos, Class<?> targetClass)
	{
		IBlockState blockState = world.getBlockState(pos);
		
		if(blockState.getBlock() == Blocks.GRASS && world.getBlockState(pos.up()).getBlock() != Blocks.AIR)
			return;
		
		if(targetClass.isInstance(blockState.getBlock()) && blockState.getBlock() instanceof IGrowable)
		{
			IGrowable iGrowable = (IGrowable) blockState.getBlock();
			if(iGrowable.canGrow(world, pos, blockState, world.isRemote)
					&& iGrowable.canUseBonemeal(world, world.rand, pos, blockState))
			{
				iGrowable.grow(world, world.rand, pos, blockState);
				fertilizedBlocks++;
				steamTank.drain(COST_PER_GROWABLE, true);
				world.playEvent(2005, pos, 0);
			}
		}
		
		while(fertilizedBlocks >= CROP_PER_BONEMEAL)
		{
			ItemStack itemStack = inputStack.getStack(0);
			itemStack.shrink(1);
			inputStack.setStackInSlot(0, itemStack);
			fertilizedBlocks -= CROP_PER_BONEMEAL;
		}
	}

	@Override
	public void HandleGuiButtonEvent(byte button) 
	{
		cropOnlyMode = !cropOnlyMode;
		markDirty();
	}
}
