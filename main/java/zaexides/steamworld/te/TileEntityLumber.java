package zaexides.steamworld.te;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.logging.log4j.Level;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.Sound;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemBlock;
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
import zaexides.steamworld.fluids.FluidSteam;
import zaexides.steamworld.recipe.handling.DustRecipeHandler;
import zaexides.steamworld.utility.capability.ItemStackHandlerOutput;
import zaexides.steamworld.utility.capability.SteamWorldFluidTank;

public class TileEntityLumber extends TileEntityMachine implements ITickable
{
	public ItemStackHandlerOutput outputStack = new ItemStackHandlerOutput(9);
	private Queue<BlockPos> replantQueue = new LinkedList<BlockPos>();
	
	private int production = 1;
	private static int radius = ConfigHandler.lumberArea;
	
	private static final int BASE_COST_PER_LOG = 15;
	private static final int BASE_COST_PER_LEAVES = 15;
			
	public void SetStats(int production)
	{
		this.production = production;
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
		
		compound.setInteger("production", production);
		
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		super.readFromNBT(compound);
		steamTank.readFromNBT(compound);
		if(compound.hasKey("items_out"))
			outputStack.deserializeNBT((NBTTagCompound) compound.getTag("items_out"));
		
		production = compound.getInteger("production");
	}
	
	@Override
	public boolean Execute()
	{
		if(hasEmptySlot())
		{
			SetActive(true);
			
			int zOff = (progression-radius) % radius;
			progression++;
			
			for(int xOff = -radius; xOff <= radius; xOff++)
			{
				for(int yOff = -radius; yOff <= radius; yOff++)
				{
					if(steamTank.getFluidAmount() < BASE_COST_PER_LOG)
						return false;
					if(!harvestBlock(pos.add(xOff, yOff, zOff), replantQueue))
						return true;
				}
			}
			
			if(progression >= (radius*2))
				progression = 0;
			
			return true;
		}
		return false;
	}
	
	private boolean harvestBlock(BlockPos pos, Queue<BlockPos> replantQueue)
	{
		IBlockState blockState = world.getBlockState(pos);
		boolean success = true;
		
		if(blockState.getBlock() instanceof BlockLog)
		{
			BlockLog log = (BlockLog) blockState.getBlock();
			NonNullList<ItemStack> logDrops = NonNullList.create();
			log.getDrops(logDrops, world, pos, blockState, production);
			
			for(int i = 0; i < logDrops.size(); i++)
			{
				ItemStack dropStack = logDrops.get(i);
				
				dropStack.grow(dropStack.getCount() * (production - 1));
				
				ItemStack stack = ItemHandlerHelper.insertItemStacked(outputStack, dropStack, false);
				
				if(!stack.isEmpty())
					success = false;
				InventoryHelper.spawnItemStack(world, pos.getX() + 0.5f, pos.getY() + 1.5f, pos.getZ() + 0.5f, stack);
			}
			
			Block bottomBlock = world.getBlockState(pos.down()).getBlock();
			if(bottomBlock != Blocks.AIR && !(bottomBlock instanceof BlockBush || bottomBlock instanceof BlockLog || bottomBlock instanceof BlockLeaves))
			{
				if(!replantQueue.contains(pos))
					replantQueue.offer(pos);
			}
			
			steamTank.drain(BASE_COST_PER_LOG, true);
			world.destroyBlock(pos, false);
		}
		
		if(blockState.getBlock() instanceof BlockLeaves)
		{
			BlockLeaves leaves = (BlockLeaves) blockState.getBlock();
			NonNullList<ItemStack> leavesDrops = NonNullList.create();
			leaves.getDrops(leavesDrops, world, pos, blockState, production);
			
			for(int i = 0; i < leavesDrops.size(); i++)
			{
				ItemStack dropStack = leavesDrops.get(i);
				
				dropStack.grow(dropStack.getCount() * (production - 1));
				
				if(replantQueue.size() > 0 && !dropStack.isEmpty() && dropStack.getItem() instanceof ItemBlock)
				{
					SteamWorld.logger.log(Level.INFO, "Stack is a block item");
					ItemBlock blockItem = (ItemBlock) dropStack.getItem();
					if(blockItem.getBlock() instanceof BlockSapling)
					{
						BlockPos setPos = replantQueue.poll();
						BlockSapling sapling = (BlockSapling) blockItem.getBlock();
						IBlockState blockState2 = sapling.getStateFromMeta(dropStack.getMetadata());
						
						SteamWorld.logger.log(Level.INFO, "pos? " + (setPos == null) + " block? " + (sapling == null) + " state? " + (blockState2 == null));
						
						world.setBlockState(setPos, 
								blockState2, 
								3);
						dropStack.shrink(1);
					}
				}
				
				ItemStack stack = ItemHandlerHelper.insertItemStacked(outputStack, dropStack, false);
				
				if(!stack.isEmpty())
					success = false;
				InventoryHelper.spawnItemStack(world, pos.getX() + 0.5f, pos.getY() + 1.5f, pos.getZ() + 0.5f, stack);
			}
			
			steamTank.drain(BASE_COST_PER_LEAVES, true);
			world.destroyBlock(pos, false);
		}
		return success;
	}
	
	private int emptySlots()
	{
		int empty = 0;
		for(int i = 0; i < outputStack.getSlots(); i++)
		{
			if(outputStack.getStackInSlot(i).getItem() == Items.AIR)
			{
				outputStack.setStackInSlot(i, ItemStack.EMPTY);
				empty++;
			}
		}
		return empty;
	}
	
	private boolean hasEmptySlot()
	{
		return emptySlots() > 0;
	}
}
