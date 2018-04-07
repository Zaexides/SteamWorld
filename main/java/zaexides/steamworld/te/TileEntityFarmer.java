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
import zaexides.steamworld.recipe.handling.DustRecipeHandler;
import zaexides.steamworld.utility.capability.ItemStackHandlerOutput;
import zaexides.steamworld.utility.capability.SteamWorldFluidTank;

public class TileEntityFarmer extends TileEntityMachine implements ITickable
{
	public ItemStackHandlerOutput outputStack = new ItemStackHandlerOutput(9);
	
	private int production = 1;
	
	private static int radius = ConfigHandler.farmerArea;
	
	private static final int COST_PER_CROP = 25;
			
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
			
			for(int xOff = -radius; xOff <= radius; xOff++)
			{
				for(int yOff = -radius; yOff <= radius; yOff++)
				{
					for(int zOff = -radius; zOff <= radius; zOff++)
					{
						if(steamTank.getFluidAmount() < (COST_PER_CROP * production))
							return false;
						
						if(!harvestBlock(pos.add(xOff, yOff, zOff)))
							return true;
					}
				}
			}
			
			return true;
		}
		return false;
	}
	
	private boolean harvestBlock(BlockPos pos)
	{
		IBlockState blockState = world.getBlockState(pos);
		
		if(ConfigHandler.farmerModBlacklist.contains(blockState.getBlock().getRegistryName().getResourceDomain()))
			return true;
		
		if(blockState.getBlock() instanceof BlockCrops)
		{
			BlockCrops crop = (BlockCrops) blockState.getBlock();
			boolean success = true;
			
			if(crop.isMaxAge(blockState))
			{
				NonNullList<ItemStack> drops = NonNullList.create();
				crop.getDrops(drops, world, pos, blockState, production);
				
				for(int i = 0; i < drops.size(); i++)
				{
					ItemStack dropStack = drops.get(i);
					
					if(!ConfigHandler.farmerDropBlacklist.contains(dropStack.getItem().getRegistryName().toString()))
					{
						if(!(dropStack.getItem() instanceof IPlantable) || ConfigHandler.farmerAllowSeedDropUpgrade)
							dropStack.grow(dropStack.getCount() * (production - 1));
						
						if(dropStack.getItem() instanceof ItemSeeds)
							dropStack.shrink(1);
						
						ItemStack stack = outputStack.insertItemStacked(dropStack, false);
						
						if(!stack.isEmpty())
							success = false;
						InventoryHelper.spawnItemStack(world, pos.getX() + 0.5f, pos.getY() + 1.5f, pos.getZ() + 0.5f, stack);
					}
				}
				
				steamTank.drain(COST_PER_CROP * production, true);
				world.destroyBlock(pos, false);
				world.setBlockState(pos, crop.withAge(0), 3);
			}
			return success;
		}
		return true;
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
