package zaexides.steamworld;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class LootTableInitializer 
{
	public static final ResourceLocation CHEST_DWARVEN_STRUCTURE = register("chests/dwarven_structure");
	public static final ResourceLocation CHEST_DWARVEN_CORE = register("chests/dwarven_structure_core");
	
	private static ResourceLocation register(String location)
	{
		return LootTableList.register(new ResourceLocation(ModInfo.MODID, location));
	}
}
