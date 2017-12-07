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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import zaexides.steamworld.SteamWorld;

public class ContainerSimple extends SWContainer
{
	private TileEntity tileEntity;
	private EntityPlayer player;
	
	public ContainerSimple(EntityPlayer player, IInventory playerInv, TileEntity tileEntity) 
	{
		this(player, playerInv, tileEntity, 84);
	}
	
	public ContainerSimple(EntityPlayer player, IInventory playerInv, TileEntity tileEntity, int playerInvYPos)
	{
		this.tileEntity = tileEntity;
		this.player = player;
		
		AddPlayerSlots(playerInv, 8, playerInvYPos);
	}
}
