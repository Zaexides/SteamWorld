package zaexides.steamworld.utility.capability;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
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
	
	@Nonnull
    public ItemStack insertItemStacked(@Nonnull ItemStack stack, boolean simulate)
    {
        if (stack.isEmpty())
            return stack;
        
        if (!stack.isStackable())
        {
            for (int i = 0; i < getSlots(); i++)
            {
                stack = forceInsertItem(i, stack, simulate);
                if (stack.isEmpty())
                    return ItemStack.EMPTY;
            }

            return stack;
        }

        int inventorySize = getSlots();

        for (int i = 0; i < inventorySize; i++)
        {
            ItemStack slotStack = getStackInSlot(i);
            if (ItemHandlerHelper.canItemStacksStackRelaxed(slotStack, stack))
            {
                stack = forceInsertItem(i, stack, simulate);

                if (stack.isEmpty())
                    break;
            }
        }

        if (!stack.isEmpty())
        {
            for (int i = 0; i < inventorySize; i++)
            {
                if (getStackInSlot(i).isEmpty())
                {
                    stack = forceInsertItem(i, stack, simulate);
                    if (stack.isEmpty())
                        break;
                }
            }
        }

        return stack;
    }
}
