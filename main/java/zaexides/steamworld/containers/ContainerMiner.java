package zaexides.steamworld.containers;

import org.apache.logging.log4j.Level;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.items.ItemDrillHead;
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
						
						if(item instanceof ItemDrillHead)
						{
							return ((ItemDrillHead)item).getTier() <= tileEntity.maxTier;
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
