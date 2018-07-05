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
import zaexides.steamworld.te.TileEntitySWFurnace;
import zaexides.steamworld.utility.capability.ItemStackHandlerSteamWorld;

public class ContainerSteamFurnace extends SWContainer
{
	private TileEntitySWFurnace tileEntity;
	private ItemStackHandlerSteamWorld handlerIn, handlerOut;
	private EntityPlayer player;
	
	public ContainerSteamFurnace(EntityPlayer player, IInventory playerInv, TileEntitySWFurnace tileEntity) 
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
					public boolean isItemValid(ItemStack stack) 
					{
						return FurnaceRecipes.instance().getSmeltingResult(stack) != ItemStack.EMPTY;
					}
				});
		addSlotToContainer(new SlotItemHandlerSteamWork(handlerOut, 0, 116, 35)
				{
					@Override
					public boolean isItemValid(ItemStack stack) {
						return false;
					}
					
					@Override
					public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) 
					{
						onSlotChange(ItemStack.EMPTY, stack);
						return super.onTake(thePlayer, stack);
					}
					
					@Override
					public void onSlotChange(ItemStack stackNew, ItemStack stackOld) 
					{
						int i = stackOld.getCount() - stackNew.getCount();

				        if (i > 0)
				        {
				            onCrafting(stackOld, i);
				        }
					}
					
					@Override
					protected void onCrafting(ItemStack stack, int amount) 
					{
						if(!ContainerSteamFurnace.this.player.world.isRemote)
						{
							float exp = FurnaceRecipes.instance().getSmeltingExperience(stack);
							int count = amount;
							
							if (exp == 0.0F)
				            {
				               count = 0;
				            }
				            else if (exp < 1.0F)
				            {
				                int j = MathHelper.floor((float)count * exp);

				                if (j < MathHelper.ceil((float)count * exp) && Math.random() < (double)((float)count * exp - (float)j))
				                {
				                    ++j;
				                }

				                count = j;
				            }
							
							while (count > 0)
				            {
				                int k = EntityXPOrb.getXPSplit(count);
				                count -= k;
				                ContainerSteamFurnace.this.player.world.spawnEntity(new EntityXPOrb(ContainerSteamFurnace.this.player.world, ContainerSteamFurnace.this.player.posX, ContainerSteamFurnace.this.player.posY + 0.5D, ContainerSteamFurnace.this.player.posZ + 0.5D, k));
				            }
						}
					}
				});
	}
	
	@Override
	public int GetOwnSlots() {
		return handlerIn.getSlots() + handlerOut.getSlots();
	}
}
