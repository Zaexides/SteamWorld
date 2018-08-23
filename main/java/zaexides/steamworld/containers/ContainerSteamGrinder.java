package zaexides.steamworld.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import zaexides.steamworld.recipe.handling.DustRecipeHandler;
import zaexides.steamworld.te.TileEntityGrinder;
import zaexides.steamworld.utility.capability.ItemStackHandlerSteamWorld;

public class ContainerSteamGrinder extends SWContainer
{
	private TileEntityGrinder tileEntity;
	private ItemStackHandlerSteamWorld handlerIn, handlerOut;
	private EntityPlayer player;
	
	public ContainerSteamGrinder(EntityPlayer player, IInventory playerInv, TileEntityGrinder tileEntity) 
	{
		this.tileEntity = tileEntity;
		this.handlerIn = tileEntity.inputStack;
		this.handlerOut = tileEntity.outputStack;
		this.player = player;
		
		AddOwnSlots();
		AddPlayerSlots(playerInv, 8, 84);
	}
	
	private void AddOwnSlots()
	{
		addSlotToContainer(new SlotItemHandlerSteamWork(handlerIn, 0, 56, 35)
				{
					@Override
					public boolean isItemValid(ItemStack stack) {
						return DustRecipeHandler.GetRecipe(stack) != null;
					}
				});
		addSlotToContainer(new SlotItemHandlerSteamWork(handlerOut, 0, 116, 35)
				{
					@Override
					public boolean isItemValid(ItemStack stack) {
						return false;
					}
				});
	}
	
	@Override
	public int GetOwnSlots() {
		return handlerIn.getSlots() + handlerOut.getSlots();
	}
}
