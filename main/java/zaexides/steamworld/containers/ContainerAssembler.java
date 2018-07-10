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
import zaexides.steamworld.init.ItemInitializer;
import zaexides.steamworld.items.ItemCrystal;
import zaexides.steamworld.recipe.handling.DustRecipeHandler;
import zaexides.steamworld.te.TileEntityAssembler;
import zaexides.steamworld.utility.capability.ItemStackHandlerSteamWorld;

public class ContainerAssembler extends SWContainer
{
	private TileEntityAssembler tileEntity;
	private ItemStackHandlerSteamWorld handlerOut;
	private ItemStackHandlerSteamWorld handlerIn;
	private EntityPlayer player;
	
	public ContainerAssembler(EntityPlayer player, IInventory playerInv, TileEntityAssembler tileEntity) 
	{
		this.tileEntity = tileEntity;
		this.handlerIn = tileEntity.inputStack;
		this.handlerOut = tileEntity.outputStack;
		this.player = player;
		
		AddOwnSlots();
		AddPlayerSlots(playerInv, 8, 100);
	}
	
	private void AddOwnSlots()
	{
		addSlotToContainer(new SlotItemHandlerSteamWork(handlerIn, 6, 80, 24)
		{
			@Override
			public boolean isItemValid(ItemStack stack) 
			{
				return stack.getItem() instanceof ItemCrystal;
			}
		});
		for(int y = 0; y < 3; y++)
		{
			addSlotToContainer(new SlotItemHandlerSteamWork(handlerIn, 0 + y,  54, 32 + y * 18));
			addSlotToContainer(new SlotItemHandlerSteamWork(handlerIn, 3 + y, 106, 32 + y * 18));
		}
		
		addSlotToContainer(new SlotItemHandlerSteamWork(handlerOut, 0, 80, 50)
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
