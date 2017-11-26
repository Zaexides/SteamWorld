package zaexides.steamworld.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class SWContainer extends Container
{
	public void AddPlayerSlots(IInventory playerInv, int xStart, int yStart)
	{
		for(int y = 0; y < 3; y++)
		{
			for(int x = 0; x < 9; x++)
			{
				addSlotToContainer(new Slot(playerInv, 9+ x+y*9, xStart+x*18, yStart+y*18));
			}
		}
		
		for(int x = 0; x < 9; x++)
		{
			addSlotToContainer(new Slot(playerInv, x, xStart + x*18, yStart + 58));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return !playerIn.isSpectator();
	}
	
	
}
