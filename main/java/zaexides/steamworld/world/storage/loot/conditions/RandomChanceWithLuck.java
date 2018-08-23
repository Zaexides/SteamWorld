package zaexides.steamworld.world.storage.loot.conditions;

import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import zaexides.steamworld.ModInfo;

public class RandomChanceWithLuck implements LootCondition
{	
	private final float chance;
	private final float luckMultiplier;
	
	public static void Register()
	{
		LootConditionManager.registerCondition(new Serializer());
	}
	
	public RandomChanceWithLuck(float chance, float luckMultiplier) 
	{
		this.chance = chance;
		this.luckMultiplier = luckMultiplier;
	}
	
	@Override
	public boolean testCondition(Random rand, LootContext context) 
	{
		float luck = context.getLuck();
		
		return rand.nextFloat() < this.chance + luck * luckMultiplier;
	}
	
	public static class Serializer extends LootCondition.Serializer<RandomChanceWithLuck>
	{
		private final String CHANCE_JSON = "chance";
		private final String LUCK_MULTIPLIER_JSON = "luck_multiplier";
		
		protected Serializer() 
		{
			super(new ResourceLocation(ModInfo.MODID, "random_chance_with_luck"), RandomChanceWithLuck.class);
		}
		
		@Override
		public void serialize(JsonObject json, RandomChanceWithLuck value, JsonSerializationContext context)
		{
			json.addProperty(CHANCE_JSON, Float.valueOf(value.chance));
			json.addProperty(LUCK_MULTIPLIER_JSON, Float.valueOf(value.luckMultiplier));
		}
		
		@Override
		public RandomChanceWithLuck deserialize(JsonObject json, JsonDeserializationContext context) 
		{
			return new RandomChanceWithLuck(
					JsonUtils.getFloat(json, CHANCE_JSON),
					JsonUtils.getFloat(json, LUCK_MULTIPLIER_JSON)
					);
		}
	}
}
