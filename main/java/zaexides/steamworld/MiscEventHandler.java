package zaexides.steamworld;

import org.apache.logging.log4j.Level;

import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import zaexides.steamworld.items.ItemInitializer;

public class MiscEventHandler 
{
	@SubscribeEvent
	public void onLootTablesLoaded(LootTableLoadEvent loadEvent)
	{
		//Modified here and not in LootTableInitializer since that'd throw a ConcurrentModificationException
		if(loadEvent.getName().equals(LootTableList.GAMEPLAY_FISHING_TREASURE))
		{
			LootEntry lootEntry = new LootEntryItem(ItemInitializer.TREASURE, 1, 0, new LootFunction[0], new LootCondition[0], "steamworld_treasure");
			loadEvent.getTable().getPool("main").addEntry(lootEntry);
		}
	}
}
