package zaexides.steamworld.containers;

import org.apache.logging.log4j.Level;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.recipe.handling.DustRecipeHandler;
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
