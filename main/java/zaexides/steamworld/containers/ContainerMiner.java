package zaexides.steamworld.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import zaexides.steamworld.items.ItemMinerMachineTool;
import zaexides.steamworld.te.TileEntityMiner;
import zaexides.steamworld.utility.capability.ItemStackHandlerSteamWorld;

public class ContainerMiner extends SWContainer
{
	private TileEntityMiner tileEntity;
	private ItemStackHandlerSteamWorld handlerOut;
	private ItemStackHandlerSteamWorld handlerIn;
	private EntityPlayer player;
	
	public ContainerMiner(EntityPlayer player, IInventory playerInv, TileEntityMiner tileEntity) 
	{
		this.tileEntity = tileEntity;
		this.handlerOut = tileEntity.outputStack;
		this.handlerIn = tileEntity.inputStack;
		this.player = player;
		
		AddOwnSlots();
		AddPlayerSlots(playerInv, 8, 84);
	}
	
	private void AddOwnSlots()
	{
		addSlotToContainer(new SlotItemHandlerSteamWork(handlerOut, 0, 110, 35)
				{
					@Override
					public boolean isItemValid(ItemStack stack) {
						return false;
					}
				});
		
		addSlotToContainer(new SlotItemHandlerSteamWork(handlerIn, 0, 54, 36)
				{
					@Override
					public boolean isItemValid(ItemStack stack) {
						Item item = stack.getItem();
						
						if(item instanceof ItemMinerMachineTool && !((ItemMinerMachineTool) item).isPump)
						{
							return ((ItemMinerMachineTool)item).getTier() <= tileEntity.maxTier;
						}
						
						return false;
					}
				});
	}
	
	@Override
	public int GetOwnSlots() {
		return handlerOut.getSlots() + handlerIn.getSlots();
	}
}
