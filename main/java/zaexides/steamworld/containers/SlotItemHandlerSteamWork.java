package zaexides.steamworld.containers;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import zaexides.steamworld.utility.capability.ItemStackHandlerSteamWorld;

public class SlotItemHandlerSteamWork extends SlotItemHandler
{
	private final int index; //Imagine the unamused emoji here.
	private final ItemStackHandlerSteamWorld itemHandler; //Done because I need these, and DRY. Or DRF, in this case I guess.
	
	public SlotItemHandlerSteamWork(ItemStackHandlerSteamWorld itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
		this.index = index;
		this.itemHandler = itemHandler;
	}

	@Override
    public boolean isItemValid(@Nonnull ItemStack stack)
    {
        if (stack.isEmpty())
            return false;
        
        ItemStack remainder;
        ItemStack currentStack = itemHandler.getStackInSlot(index);
        itemHandler.setStackInSlot(index, ItemStack.EMPTY);
        remainder = itemHandler.forceInsertItem(index, stack, true);
        itemHandler.setStackInSlot(index, currentStack);
        return remainder.isEmpty() || remainder.getCount() < stack.getCount();
    }
	
	@Override
    public int getItemStackLimit(@Nonnull ItemStack stack)
    {
        ItemStack maxAdd = stack.copy();
        int maxInput = stack.getMaxStackSize();
        maxAdd.setCount(maxInput);

        ItemStack currentStack = itemHandler.getStackInSlot(index);

        itemHandler.setStackInSlot(index, ItemStack.EMPTY);

        ItemStack remainder = itemHandler.forceInsertItem(index, maxAdd, true);

        itemHandler.setStackInSlot(index, currentStack);

        return maxInput - remainder.getCount();
    }
	
	@Override
    public boolean canTakeStack(EntityPlayer playerIn)
    {
        return !this.itemHandler.forceExtractItem(index, 1, true).isEmpty();
    }
	
    @Override
    @Nonnull
    public ItemStack decrStackSize(int amount)
    {
        return this.itemHandler.forceExtractItem(index, amount, false);
    }
}
