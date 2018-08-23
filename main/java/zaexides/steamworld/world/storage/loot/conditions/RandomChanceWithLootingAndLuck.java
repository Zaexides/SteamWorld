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

public class RandomChanceWithLootingAndLuck implements LootCondition
{	
	private final float chance;
	private final float lootingMultiplier;
	private final float luckMultiplier;
	
	public static void Register()
	{
		LootConditionManager.registerCondition(new Serializer());
	}
	
	public RandomChanceWithLootingAndLuck(float chance, float lootingMultiplier, float luckMultiplier) 
	{
		this.chance = chance;
		this.lootingMultiplier = lootingMultiplier;
		this.luckMultiplier = luckMultiplier;
	}
	
	@Override
	public boolean testCondition(Random rand, LootContext context) 
	{
		int lootingLevel = context.getLootingModifier();
		float luck = context.getLuck();
		
		return rand.nextFloat() < this.chance + lootingLevel * lootingMultiplier + luck * luckMultiplier;
	}
	
	public static class Serializer extends LootCondition.Serializer<RandomChanceWithLootingAndLuck>
	{
		private final String CHANCE_JSON = "chance";
		private final String LOOTING_MULTIPLIER_JSON = "looting_multiplier";
		private final String LUCK_MULTIPLIER_JSON = "luck_multiplier";
		
		protected Serializer() 
		{
			super(new ResourceLocation(ModInfo.MODID, "random_chance_with_looting_and_luck"), RandomChanceWithLootingAndLuck.class);
		}
		
		@Override
		public void serialize(JsonObject json, RandomChanceWithLootingAndLuck value, JsonSerializationContext context)
		{
			json.addProperty(CHANCE_JSON, Float.valueOf(value.chance));
			json.addProperty(LOOTING_MULTIPLIER_JSON, Float.valueOf(value.lootingMultiplier));
			json.addProperty(LUCK_MULTIPLIER_JSON, Float.valueOf(value.luckMultiplier));
		}
		
		@Override
		public RandomChanceWithLootingAndLuck deserialize(JsonObject json, JsonDeserializationContext context) 
		{
			return new RandomChanceWithLootingAndLuck(
					JsonUtils.getFloat(json, CHANCE_JSON),
					JsonUtils.getFloat(json, LOOTING_MULTIPLIER_JSON),
					JsonUtils.getFloat(json, LUCK_MULTIPLIER_JSON)
					);
		}
	}
}
