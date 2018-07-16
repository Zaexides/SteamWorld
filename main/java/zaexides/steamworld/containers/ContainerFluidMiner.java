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
import zaexides.steamworld.items.ItemMinerMachineTool;
import zaexides.steamworld.te.TileEntityFluidMiner;
import zaexides.steamworld.utility.capability.ItemStackHandlerSteamWorld;

public class ContainerFluidMiner extends SWContainer
{
	private TileEntityFluidMiner tileEntity;
	private ItemStackHandlerSteamWorld handlerIn;
	private EntityPlayer player;
	
	public ContainerFluidMiner(EntityPlayer player, IInventory playerInv, TileEntityFluidMiner tileEntity) 
	{
		this.tileEntity = tileEntity;
		this.handlerIn = tileEntity.inputStack;
		this.player = player;
		
		AddOwnSlots();
		AddPlayerSlots(playerInv, 8, 84);
	}
	
	private void AddOwnSlots()
	{
		addSlotToContainer(new SlotItemHandlerSteamWork(handlerIn, 0, 54, 36)
				{
					@Override
					public boolean isItemValid(ItemStack stack) {
						Item item = stack.getItem();
						
						if(item instanceof ItemMinerMachineTool && ((ItemMinerMachineTool) item).isPump)
						{
							return ((ItemMinerMachineTool)item).getTier() <= tileEntity.maxTier;
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
