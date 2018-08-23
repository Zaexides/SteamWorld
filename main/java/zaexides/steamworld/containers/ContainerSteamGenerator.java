package zaexides.steamworld.containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
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
		addSlotToContainer(new SlotItemHandlerSteamWork(handler, 0, xStart, yStart)
				{
					@Override
					public boolean isItemValid(ItemStack stack) 
					{
						return tileEntity.getBurnTime(stack) > 0;
					}
				});
	}
	
	@Override
	public int GetOwnSlots() {
		return handler.getSlots();
	}
}
