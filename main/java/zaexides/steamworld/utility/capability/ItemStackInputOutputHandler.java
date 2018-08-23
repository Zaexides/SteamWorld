package zaexides.steamworld.utility.capability;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class ItemStackInputOutputHandler implements IItemHandler, IItemHandlerModifiable
{
	public ItemStackHandlerInput inputHandler;
	public ItemStackHandlerOutput outputHandler;
	
	public ItemStackInputOutputHandler(ItemStackHandlerInput input, ItemStackHandlerOutput output) 
	{
		this.inputHandler = input;
		this.outputHandler = output;
	}
	
	public ItemStackHandlerSteamWorld getHandlerFromSlot(int slot)
	{
		if(slot < inputHandler.getSlots())
			return inputHandler;
		else
			return outputHandler;
	}
	
	public int getCorrectSlot(int slot)
	{
		if(slot < inputHandler.getSlots())
			return slot;
		else
			return slot - inputHandler.getSlots();
	}

	@Override
	public int getSlots() 
	{
		return inputHandler.getSlots() + outputHandler.getSlots();
	}

	@Override
	public ItemStack getStackInSlot(int slot) 
	{
		return getHandlerFromSlot(slot).getStackInSlot(getCorrectSlot(slot));
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) 
	{
		return getHandlerFromSlot(slot).insertItem(getCorrectSlot(slot), stack, simulate);
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) 
	{
		return getHandlerFromSlot(slot).extractItem(getCorrectSlot(slot), amount, simulate);
	}

	@Override
	public int getSlotLimit(int slot) 
	{
		return getHandlerFromSlot(slot).getSlotLimit(getCorrectSlot(slot));
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) 
	{
		getHandlerFromSlot(slot).setStackInSlot(getCorrectSlot(slot), stack);
	}
}
