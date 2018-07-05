package zaexides.steamworld.containers;

import org.apache.logging.log4j.Level;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.te.TileEntityFertilizer;
import zaexides.steamworld.utility.capability.ItemStackHandlerSteamWorld;

public class ContainerFertilizer extends SWContainer
{
	private TileEntityFertilizer tileEntity;
	private ItemStackHandlerSteamWorld handlerIn;
	private EntityPlayer player;
	
	public ContainerFertilizer(EntityPlayer player, IInventory playerInv, TileEntityFertilizer tileEntity) 
	{
		this.tileEntity = tileEntity;
		this.handlerIn = tileEntity.inputStack;
		this.player = player;
		
		AddOwnSlots();
		AddPlayerSlots(playerInv, 8, 84);
	}
	
	private void AddOwnSlots()
	{
		addSlotToContainer(new SlotItemHandlerSteamWork(handlerIn, 0, 80, 35)
				{
					@Override
					public boolean isItemValid(ItemStack stack) {
						if(stack.getItem() instanceof ItemDye)
						{
							EnumDyeColor dyeColor = EnumDyeColor.byDyeDamage(stack.getMetadata());
							if(dyeColor == EnumDyeColor.WHITE)
								return true;
						}
						return false;
					}
				});
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
		ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        
        int ownSlotCount = handlerIn.getSlots();
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
            	if (itemstack1.getItem() instanceof ItemDye)
                {
            		EnumDyeColor dyeColor = EnumDyeColor.byDyeDamage(itemstack1.getMetadata());
					if(dyeColor == EnumDyeColor.WHITE)
					{
	                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
	                    {
	                        return ItemStack.EMPTY;
	                    }
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
