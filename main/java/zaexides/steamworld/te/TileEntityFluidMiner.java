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
import net.minecraftforge.fluids.FluidStack;
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
import zaexides.steamworld.items.ItemMinerMachineTool;
import zaexides.steamworld.recipe.handling.DustRecipeHandler;
import zaexides.steamworld.recipe.handling.FluidMinerRecipeHandler;
import zaexides.steamworld.recipe.handling.MinerRecipeHandler;
import zaexides.steamworld.utility.capability.FluidInputOutput;
import zaexides.steamworld.utility.capability.ItemStackHandlerInput;
import zaexides.steamworld.utility.capability.ItemStackHandlerOutput;
import zaexides.steamworld.utility.capability.SteamWorldFluidTank;

public class TileEntityFluidMiner extends TileEntityMachine implements ITickable
{
	public ItemStackHandlerInput inputStack = new ItemStackHandlerInput(1)
	{
		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
		{
			Item stackItem = stack.getItem();
			if(stackItem instanceof ItemMinerMachineTool && ((ItemMinerMachineTool)stackItem).isPump)
				return super.insertItem(slot, stack, simulate);
			else
				return stack;
		}
	};
	public SteamWorldFluidTank outputTank = new SteamWorldFluidTank(1000, this);
	
	private int speed = 1;
	public byte maxTier = 4;
	
	private static final int BASE_COST_PER_TICK = 5;
	public static final int TIME_PER_FLUID = 1200;
	
	public float progression;
			
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
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(new FluidInputOutput(steamTank, outputTank));
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inputStack);
		return super.getCapability(capability, facing);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		NBTTagCompound steamTankCompound = new NBTTagCompound();
		steamTank.writeToNBT(steamTankCompound);
		compound.setTag("steam", steamTankCompound);
		
		NBTTagCompound outputTankCompound = new NBTTagCompound();
		outputTank.writeToNBT(outputTankCompound);
		compound.setTag("output", outputTankCompound);
		
		compound.setTag("items_in", inputStack.serializeNBT());
		
		compound.setFloat("progress", progression);
		compound.setInteger("efficiency", speed);
		compound.setByte("max_tier", maxTier);
		
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		super.readFromNBT(compound);
		
		if(compound.hasKey("steam"))
			steamTank.readFromNBT(compound.getCompoundTag("steam"));
		if(compound.hasKey("output"))
			outputTank.readFromNBT(compound.getCompoundTag("output"));
		
		if(compound.hasKey("items_in"))
			inputStack.deserializeNBT((NBTTagCompound) compound.getTag("items_in"));
		
		progression = compound.getFloat("progress");
		speed = compound.getInteger("efficiency");
		maxTier = compound.getByte("max_tier");
	}
	
	@Override
	public void update() 
	{
		if(world.isRemote || isInvalid())
			return;
		
		boolean success = false;
		if(steamTank.getFluidAmount() > 0)
			success = Execute();
		if(!success)
		{
			SetActive(false);
			if(progression > 0)
				progression--;
		}
		
		markDirty();
	}
	
	@Override
	public boolean Execute()
	{
		if(steamTank.getFluidAmount() < (BASE_COST_PER_TICK * speed))
		{
			return false;
		}
		
		ItemStack drillStack = inputStack.getStackInSlot(0).copy();
		if((outputTank.getFluid() == null || outputTank.getFluidAmount() == 0) && drillStack.getCount() != 0)
		{
			ItemMinerMachineTool drillHead = ((ItemMinerMachineTool)drillStack.getItem());
			
			SetActive(true);
			progression += speed * drillHead.getEfficiencyModifier(drillStack);
			steamTank.drain(BASE_COST_PER_TICK * speed, true);
						
			if(progression >= TIME_PER_FLUID)
			{
				byte drillTier = drillHead.getTier();
				
				FluidStack resultFluid = FluidMinerRecipeHandler.GetRandomResult(world.rand, drillTier);
				outputTank.fill(resultFluid, true);
				
				ItemMinerMachineTool.Damage(drillStack, world.rand);
				
				if(drillStack.getItemDamage() >= drillStack.getMaxDamage() - 1)
					inputStack.setStackInSlot(0, ItemStack.EMPTY);
				else
					inputStack.setStackInSlot(0, drillStack);
				
				progression = 0;
			}
			return true;
		}
		
		progression--;
		if(progression < 0)
			progression = 0;
		return false;
	}
}
