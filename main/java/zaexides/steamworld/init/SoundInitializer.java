package zaexides.steamworld.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import zaexides.steamworld.ModInfo;

public class SoundInitializer 
{
	public final static List<SoundEvent> SOUND_EVENTS = new ArrayList<SoundEvent>();
	
	public static final SoundEvent ACCELERATOR_TELEPORT = addSound("tile.accelerator.teleport");
	
	public static final SoundEvent LAUNCHER_LAUNCH = addSound("tile.launcher.launch");
	
	public static final SoundEvent PRESERVATION_LIQUID_CHIME = addSound("tile.preservation_juice.chime");
	public static final SoundEvent WITHER_LIQUID_SCREAM = addSound("tile.wither_juice.scream");
	
	public static final SoundEvent ECLIPSE_STALKER_AMBIENT = addSound("mob.eclipse_stalker.say");
	public static final SoundEvent ECLIPSE_STALKER_HURT = addSound("mob.eclipse_stalker.hurt");
	public static final SoundEvent ECLIPSE_STALKER_DEATH = addSound("mob.eclipse_stalker.death");
	public static final SoundEvent ECLIPSE_STALKER_DESPAWN = addSound("mob.eclipse_stalker.disappear");
	
	public static final SoundEvent PROSHELLOR_DROP = addSound("mob.proshellor.drop");
	
	public static final SoundEvent SKYFISH_PLOP = addSound("mob.skyfish.plop");
	
	public static final SoundEvent ANCITE_GOLEM_HURT = addSound("mob.ancite_golem.hurt");
	public static final SoundEvent ANCITE_GOLEM_FALL = addSound("mob.ancite_golem.fall");
	public static final SoundEvent ANCITE_GOLEM_DEATH = addSound("mob.ancite_golem.death");
	public static final SoundEvent ANCITE_GOLEM_STEP = addSound("mob.ancite_golem.step");
	public static final SoundEvent ANCITE_GOLEM_SHOOT = addSound("mob.ancite_golem.shoot");
	public static final SoundEvent ANCITE_GOLEM_OUT_OF_AMMO = addSound("mob.ancite_golem.fail");
	
	public static final SoundEvent ANEMONE_SHOOT = addSound("mob.anemone.shoot");
	public static final SoundEvent ANEMONE_HURT = addSound("mob.anemone.hurt");
	public static final SoundEvent ANEMONE_DEATH = addSound("mob.anemone.death");
	
	private static SoundEvent addSound(String name)
	{
		ResourceLocation resourceLocation = new ResourceLocation(ModInfo.MODID, name);
		SoundEvent soundEvent = new SoundEvent(resourceLocation).setRegistryName(resourceLocation);
		SOUND_EVENTS.add(soundEvent);
		return soundEvent;
	}
}
