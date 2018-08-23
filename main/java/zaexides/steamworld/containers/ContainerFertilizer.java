package zaexides.steamworld.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
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
	public int GetOwnSlots() {
		return handlerIn.getSlots();
	}
}
