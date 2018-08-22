package zaexides.steamworld.advancements;

import net.minecraft.advancements.ICriterionTrigger;

public class SteamWorldCriteriaTriggers 
{
	public static final VillanglerTradeTrigger VILLANGLER_TRADE_TRIGGER = new VillanglerTradeTrigger();
	
	public static void RegisterTriggers()
	{
		RegisterTrigger(VILLANGLER_TRADE_TRIGGER);
	}
	
	private static void RegisterTrigger(ICriterionTrigger trigger)
	{
		net.minecraft.advancements.CriteriaTriggers.register(trigger);
	}
}
