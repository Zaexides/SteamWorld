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
import zaexides.steamworld.te.TileEntitySteamGeneratorNether;

public class ContainerSteamGeneratorNether extends SWContainer
{
	private TileEntitySteamGeneratorNether tileEntity;
	
	public ContainerSteamGeneratorNether(IInventory playerInv, TileEntitySteamGeneratorNether tileEntity) 
	{
		this.tileEntity = tileEntity;
		
		AddPlayerSlots(playerInv, 8, 84);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
		ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        
        int ownSlotCount = 0;
        int playerSlotCountToolbar = ownSlotCount + playerIn.inventory.mainInventory.size();
        int playerSlotCount = playerSlotCountToolbar - 9;

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < ownSlotCount) //Own > Player
            {
                if (!this.mergeItemStack(itemstack1, ownSlotCount, playerSlotCountToolbar, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else //Player > Own
            {
            	if (index >= ownSlotCount && index < playerSlotCount)
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
