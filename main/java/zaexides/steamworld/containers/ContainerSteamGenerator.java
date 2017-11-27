package zaexides.steamworld.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import zaexides.steamworld.te.TileEntitySteamGenerator;
import zaexides.steamworld.utility.capability.ItemStackHandlerSteamWorld;

public class ContainerSteamGenerator extends SWContainer
{
	private TileEntitySteamGenerator tileEntity;
	private ItemStackHandlerSteamWorld handler;
	
	public ContainerSteamGenerator(IInventory playerInv, TileEntitySteamGenerator tileEntity) 
	{
		this.tileEntity = tileEntity;
		this.handler = tileEntity.itemStackHandler;
		
		AddOwnSlots();
		AddPlayerSlots(playerInv, 8, 84);
	}
	
	private void AddOwnSlots()
	{
		int xStart = 61;
		int yStart = 48;
		addSlotToContainer(new SlotItemHandlerSteamWork(handler, 0, xStart, yStart));
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
		ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        
        int ownSlotCount = handler.getSlots();
        int playerSlotCountToolbar = ownSlotCount + playerIn.inventory.mainInventory.size();
        int playerSlotCount = playerSlotCountToolbar - 9;

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < ownSlotCount) //Own > Player
            {
                if (!this.mergeItemStack(itemstack1, playerSlotCount, playerSlotCountToolbar, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else //Player > Own
            {
            	if (tileEntity.getBurnTime(itemstack1) > 0)
                {
                    if (!this.mergeItemStack(itemstack1, 0, ownSlotCount, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            	else if (index >= ownSlotCount && index < playerSlotCount)
                {
                    if (!this.mergeItemStack(itemstack1, playerSlotCount, playerSlotCountToolbar, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= playerSlotCount && index < playerSlotCountToolbar && !this.mergeItemStack(itemstack1, ownSlotCount, playerSlotCount, false))
                {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }
}
