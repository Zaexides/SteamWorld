package zaexides.steamworld.utility.capability;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class ItemStackHandlerOutput extends ItemStackHandlerSteamWorld
{
	public ItemStackHandlerOutput() 
	{
		super();
	}
	
	public ItemStackHandlerOutput(int size)
	{
		super(size);
	}
	
	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		return stack;
	}
}
