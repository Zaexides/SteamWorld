package zaexides.steamworld;

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
import zaexides.steamworld.items.ItemInitializer;

public class LootTableInitializer 
{
	public static final ResourceLocation ANCITE_CRYPT = register("chests/ancite_crypt");
	public static final ResourceLocation TREASURE_BOX_FISHING = register("treasure_box/fishing");
	public static final ResourceLocation TREASURE_BOX_DUNGEON = register("treasure_box/dungeon");
	public static final ResourceLocation TREASURE_BOX_END = register("treasure_box/end");
	public static final ResourceLocation TREASURE_BOX_BIRTHDAY = register("treasure_box/bday");
	
	public static final ResourceLocation LEGACY_STRUCTURE = register("chests/dwarven_structure");
	public static final ResourceLocation LEGACY_STRUCTURE_CORE = register("chests/dwarven_structure_core");
		
	private static ResourceLocation register(String location)
	{
		return LootTableList.register(new ResourceLocation(ModInfo.MODID, location));
	}
}
