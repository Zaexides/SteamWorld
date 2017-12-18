package zaexides.steamworld.items;

import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.Level;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import zaexides.steamworld.LootTableInitializer;
import zaexides.steamworld.SteamWorld;

public class ItemTreasure extends SteamWorldItem
{
	public ItemTreasure(String name) 
	{
		super(name);
		setMaxStackSize(1);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) 
	{		
		if(!worldIn.isRemote)
		{
			LootTable lootTable = worldIn.getLootTableManager().getLootTableFromLocation(LootTableInitializer.TREASURE_BOX);
			LootContext.Builder contextBuilder = new LootContext.Builder((WorldServer) worldIn);
			
			contextBuilder.withLuck(playerIn.getLuck());

			List<ItemStack> stacks = lootTable.generateLootForPools(itemRand, contextBuilder.build());
			for(ItemStack stack : stacks)
			{
				if(!playerIn.inventory.addItemStackToInventory(stack))
				{
					InventoryHelper.spawnItemStack(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, stack);
				}
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, ItemStack.EMPTY);
	}
}
