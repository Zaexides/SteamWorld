package zaexides.steamworld.utility.capability;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class ItemStackHandlerSteamWorld extends ItemStackHandler
{
	public ItemStackHandlerSteamWorld() 
	{
		super();
	}
	
	public ItemStackHandlerSteamWorld(int size)
	{
		super(size);
	}
	
	public ItemStack getStack(int slot)
	{
		return getStackInSlot(slot);
	}
	
	public ItemStack forceExtractItem(int slot, int amount, boolean simulate) 
	{
		return super.extractItem(slot, amount, simulate);
	}
	
	public ItemStack forceInsertItem(int slot, ItemStack stack, boolean simulate)
	{
		return super.insertItem(slot, stack, simulate);
	}
}
