package zaexides.steamworld.init;

import org.apache.logging.log4j.Level;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zaexides.steamworld.ModInfo;

public class LootTableInitializer 
{
	public static final ResourceLocation ANCITE_CRYPT = register("chests/crypt/ancite_crypt");
	public static final ResourceLocation WITHER_LAB = register("chests/wither_lab/wither_lab");
	
	public static final ResourceLocation TOWER_FOOD = register("chests/tower/tower_food");
	public static final ResourceLocation TOWER_LIBRARY = register("chests/tower/tower_library");
	public static final ResourceLocation TOWER_RESIDENCE = register("chests/tower/tower_residence");
	
	public static final ResourceLocation TREASURE_BOX_FISHING = register("treasure_box/fishing");
	public static final ResourceLocation TREASURE_BOX_DUNGEON = register("treasure_box/dungeon");
	public static final ResourceLocation TREASURE_BOX_END = register("treasure_box/end");
	public static final ResourceLocation TREASURE_BOX_BIRTHDAY = register("treasure_box/bday");
	public static final ResourceLocation TREASURE_BOX_WITHER_LAB = register("treasure_box/wither_lab");
			
	public static final ResourceLocation DROPS_SKYFISH = register("entities/skyfish");
	public static final ResourceLocation DROPS_ANEMONE = register("entities/anemone");
	
	private static ResourceLocation register(String location)
	{
		return LootTableList.register(new ResourceLocation(ModInfo.MODID, location));
		//TODO: Add quality tags to treasure boxes.
	}
}
