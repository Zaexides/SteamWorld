package zaexides.steamworld.worldgen.biomes;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zaexides.steamworld.ModInfo;

@Mod.EventBusSubscriber(modid = ModInfo.MODID)
public class BiomeInitializer 
{
	@SubscribeEvent
	public static void registerBiomes(RegistryEvent.Register<Biome> event)
	{
		
	}
}
