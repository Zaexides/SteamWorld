package zaexides.steamworld.init;

import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zaexides.steamworld.ModInfo;

@Mod.EventBusSubscriber(modid = ModInfo.MODID)
public class PotionTypeInitializer 
{
	private static final int DEFAULT_DURATION_BUFF = 3600;
	private static final int DEFAULT_DURATION_DEBUFF = 1800;
	
	public static final PotionType UNLUCK = addPotionType("unluck", new PotionEffect(MobEffects.UNLUCK, 3000, 0));
	
	@SubscribeEvent
	public static void registerPotionTypes(RegistryEvent.Register<PotionType> event)
	{
		event.getRegistry().registerAll(
				UNLUCK
				);
	}
	
	private static PotionType addPotionType(String name, PotionEffect effect)
	{
		ResourceLocation registryName = new ResourceLocation(ModInfo.MODID, name);
		return new PotionType(effect.getPotion().getRegistryName().toString().replace(':', '.'), effect).setRegistryName(registryName);
	}
	
	//Already exist
	public static final PotionType LUCK = getRegisteredPotionType("luck");
	private static PotionType getRegisteredPotionType(String name)
	{
		return PotionType.REGISTRY.getObject(new ResourceLocation(name));
	}
}
