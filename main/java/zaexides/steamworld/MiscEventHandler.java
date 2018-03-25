package zaexides.steamworld;

import org.apache.logging.log4j.Level;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import zaexides.steamworld.init.ItemInitializer;
import zaexides.steamworld.items.ItemTreasure;

public class MiscEventHandler 
{
	@SubscribeEvent
	public void onLootTablesLoaded(LootTableLoadEvent loadEvent)
	{
		ResourceLocation eventName = loadEvent.getName();
		final int FISH_TREASURE_METADATA = ItemTreasure.EnumTreasure.FISH.getMeta();
		final int END_TREASURE_METADATA = ItemTreasure.EnumTreasure.END.getMeta();
		
		//Modified here and not in LootTableInitializer since that'd throw a ConcurrentModificationException
		if(eventName.equals(LootTableList.GAMEPLAY_FISHING_TREASURE))
		{
			LootEntry lootEntry = new LootEntryItem(ItemInitializer.TREASURE, 1, 0, new LootFunction[] { new SetMetadata(new LootCondition[0], new RandomValueRange(FISH_TREASURE_METADATA)) }, new LootCondition[0], "steamworld_treasure");
			loadEvent.getTable().getPool("main").addEntry(lootEntry);
		}
		else if(eventName.equals(LootTableList.CHESTS_END_CITY_TREASURE))
		{
			LootEntry lootEntry = new LootEntryItem(ItemInitializer.TREASURE, 10, 0, new LootFunction[] { new SetMetadata(new LootCondition[0], new RandomValueRange(END_TREASURE_METADATA)) }, new LootCondition[0], "steamworld_treasure");
			loadEvent.getTable().getPool("main").addEntry(lootEntry);
		}
	}
}
