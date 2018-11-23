package zaexides.steamworld.te;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import zaexides.steamworld.items.ItemMinerMachineTool;
import zaexides.steamworld.recipe.handling.MinerRecipeHandler;
import zaexides.steamworld.utility.capability.ItemStackHandlerInput;
import zaexides.steamworld.utility.capability.ItemStackHandlerOutput;
import zaexides.steamworld.utility.capability.ItemStackInputOutputHandler;

public class TileEntityMiner extends TileEntityMachine implements ITickable
{
	public ItemStackHandlerInput inputStack = new ItemStackHandlerInput(1)
	{
		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
		{
			Item stackItem = stack.getItem();
			if(stackItem instanceof ItemMinerMachineTool && !((ItemMinerMachineTool)stackItem).isPump)
				return super.insertItem(slot, stack, simulate);
			else
				return stack;
		}
	};
	public ItemStackHandlerOutput outputStack = new ItemStackHandlerOutput(1);
	
	private int speed = 1;
	public byte maxTier = 4;
	
	private static final int BASE_COST_PER_TICK = 5;
	public static final int TIME_PER_ORE = 3600;
	
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
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(steamTank);
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new ItemStackInputOutputHandler(inputStack, outputStack));
		return super.getCapability(capability, facing);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		steamTank.writeToNBT(compound);
		compound.setTag("items_out", outputStack.serializeNBT());
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
		steamTank.readFromNBT(compound);
		if(compound.hasKey("items_out"))
			outputStack.deserializeNBT((NBTTagCompound) compound.getTag("items_out"));
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
		if(outputStack.getStackInSlot(0).getCount() == 0 && drillStack.getCount() != 0)
		{
			ItemMinerMachineTool drillHead = ((ItemMinerMachineTool)drillStack.getItem());
			
			SetActive(true);
			progression += speed * drillHead.getEfficiencyModifier(drillStack);
			steamTank.drain(BASE_COST_PER_TICK * speed, true);
						
			if(progression >= TIME_PER_ORE)
			{
				byte drillTier = drillHead.getTier();
				
				ItemStack output = ItemStack.EMPTY;
				while(output == null || output.isEmpty())
					output = MinerRecipeHandler.GetRandomResult(world.rand, drillTier).copy();

				int fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, drillStack);
				for(int i = 0; i < fortuneLevel; i++)
				{
					if(world.rand.nextInt(5) == 0)
						output.grow(1);
				}
				
				outputStack.setStackInSlot(0, output);
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
