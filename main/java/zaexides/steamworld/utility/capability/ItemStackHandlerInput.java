package zaexides.steamworld.utility.capability;

import net.minecraft.item.ItemStack;

public class ItemStackHandlerInput extends ItemStackHandlerSteamWorld
{
	public ItemStackHandlerInput() 
	{
		super();
	}
	
	public ItemStackHandlerInput(int size)
	{
		super(size);
	}
	
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) 
	{
		return ItemStack.EMPTY;
	}
}
