package zaexides.steamworld.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import zaexides.steamworld.te.TileEntityFisher;
import zaexides.steamworld.utility.capability.ItemStackHandlerSteamWorld;

public class ContainerFisher extends SWContainer
{
	private TileEntityFisher tileEntity;
	private ItemStackHandlerSteamWorld handlerOut;
	private EntityPlayer player;
	
	public ContainerFisher(EntityPlayer player, IInventory playerInv, TileEntityFisher tileEntity) 
	{
		this.tileEntity = tileEntity;
		this.handlerOut = tileEntity.outputStack;
		this.player = player;
		
		AddOwnSlots();
		AddPlayerSlots(playerInv, 8, 84);
	}
	
	private void AddOwnSlots()
	{
		addSlotToContainer(new SlotItemHandlerSteamWork(handlerOut, 0, 80, 35)
				{
					@Override
					public boolean isItemValid(ItemStack stack) {
						return false;
					}
				});
	}
	
	@Override
	public int GetOwnSlots() {
		return handlerOut.getSlots();
	}
}
