package zaexides.steamworld.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

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
