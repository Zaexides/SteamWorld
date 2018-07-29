package zaexides.steamworld.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.utility.SWDamageSource;
import zaexides.steamworld.world.dimension.WorldProviderSkyOfOld;

@Mod.EventBusSubscriber(modid = ModInfo.MODID)
public class PlayerTickHandler 
{
	private static final int UPDATE_FREQUENCY = 30;
	private static final int COMBUSTION_HEIGHT = 20;
	private static final int FIRE_DEATH_HEIGHT = -10;
	private static final int COMBUSTION_DURATION = 5;
	
	@SubscribeEvent
	public static void OnPlayerTicked(PlayerTickEvent event)
	{
		EntityPlayer player = event.player;
		if((player.ticksExisted % UPDATE_FREQUENCY) == 0)
		{
			World world = player.world;
			
			SetAflameWhenCloseToCore(world, player);
		}
	}
	
	private static void SetAflameWhenCloseToCore(World world, EntityPlayer player)
	{
		if(!(world.provider instanceof WorldProviderSkyOfOld) || world.isRemote)
			return;
			
		if(player.posY <= COMBUSTION_HEIGHT)
		{
			player.setFire(COMBUSTION_DURATION);
			if(player.posY <= FIRE_DEATH_HEIGHT)
			{
				player.attackEntityFrom(SWDamageSource.CORE_HEAT, Float.MAX_VALUE);
			}
		}
	}
}
