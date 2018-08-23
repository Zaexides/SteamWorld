package zaexides.steamworld.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import zaexides.steamworld.te.TileEntityFarmer;
import zaexides.steamworld.te.TileEntityLumber;
import zaexides.steamworld.te.TileEntityMachine;
import zaexides.steamworld.utility.capability.ItemStackHandlerSteamWorld;

public class ContainerFarmer extends SWContainer
{
	private TileEntityMachine tileEntity;
	private ItemStackHandlerSteamWorld handlerOut;
	private EntityPlayer player;
	
	public ContainerFarmer(EntityPlayer player, IInventory playerInv, TileEntityMachine tileEntity) 
	{
		this.tileEntity = tileEntity;
		if(tileEntity instanceof TileEntityFarmer)
			this.handlerOut = ((TileEntityFarmer)tileEntity).outputStack;
		else
			this.handlerOut = ((TileEntityLumber)tileEntity).outputStack;
		this.player = player;
		
		AddOwnSlots();
		AddPlayerSlots(playerInv, 8, 84);
	}
	
	private void AddOwnSlots()
	{
		for(int y = 0, i = 0; y < 3; y++)
		{
			for(int x = 0; x < 3; x++, i++)
			{
				addSlotToContainer(new SlotItemHandlerSteamWork(handlerOut, i, 62 + x*18, 17 + y*18)
				{
					@Override
					public boolean isItemValid(ItemStack stack) {
						return false;
					}
				});
			}
		}
	}
	
	@Override
	public int GetOwnSlots() {
		return handlerOut.getSlots();
	}
}
