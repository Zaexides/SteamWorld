package zaexides.steamworld.entity.villangler;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import zaexides.steamworld.init.EntityInitializer;

public class VillanglerMerchantGenBreeder extends VillanglerMerchantRecipeListGenerator
{
	public ArrayList<MerchantRecipeWeightItem> weightList;
	private final static int BASE_ENCHANTED_BOOK_COST = 5;
	
	public VillanglerMerchantGenBreeder() 
	{
		RecipeWeightListBuilder builder = new RecipeWeightListBuilder();
		builder.addTrade(10, new ItemStack(CURRENCY_RICH.getItem(), 2, CURRENCY_RICH.getMetadata()), getEggOf(EntityInitializer.GLOWDUSTY));
		builder.addTrade(30, new ItemStack(CURRENCY_POOR.getItem(), 40, CURRENCY_POOR.getMetadata()), getEggOf(EntityInitializer.FLYING_FISH));
		builder.addTrade(40, new ItemStack(CURRENCY_POOR.getItem(), 30, CURRENCY_POOR.getMetadata()), getEggOf("cow"));
		builder.addTrade(40, new ItemStack(CURRENCY_POOR.getItem(), 30, CURRENCY_POOR.getMetadata()), getEggOf("chicken"));
		builder.addTrade(40, new ItemStack(CURRENCY_POOR.getItem(), 30, CURRENCY_POOR.getMetadata()), getEggOf("pig"));
		builder.addTrade(40, new ItemStack(CURRENCY_POOR.getItem(), 30, CURRENCY_POOR.getMetadata()), getEggOf("sheep"));
		builder.addTrade(50, new ItemStack(CURRENCY_POOR.getItem(), 20, CURRENCY_POOR.getMetadata()), getEggOf("rabbit"));
		builder.addTrade(50, new ItemStack(CURRENCY_POOR.getItem(), 20, CURRENCY_POOR.getMetadata()), getEggOf("parrot"));
		builder.addTrade(20, new ItemStack(CURRENCY_POOR.getItem(), 50, CURRENCY_POOR.getMetadata()), getEggOf("wolf"));
		builder.addTrade(20, new ItemStack(CURRENCY_POOR.getItem(), 50, CURRENCY_POOR.getMetadata()), getEggOf("ocelot"));
		builder.addTrade(15, new ItemStack(CURRENCY_RICH.getItem(), 1, CURRENCY_RICH.getMetadata()), getEggOf("horse"));
		builder.addTrade(10, new ItemStack(CURRENCY_RICH.getItem(), 2, CURRENCY_RICH.getMetadata()), getEggOf("mooshroom"));
		weightList = builder.build();
	}
	
	private ItemStack getEggOf(String entity)
	{
		return getEggOf(new ResourceLocation("minecraft", entity));
	}
	
	private ItemStack getEggOf(ResourceLocation entity)
	{
		ItemStack eggStack = new ItemStack(Items.SPAWN_EGG);
		ItemMonsterPlacer.applyEntityIdToItemStack(eggStack, entity);
		return eggStack;
	}
	
	@Override
	public MerchantRecipeList generateMerchantRecipeList(MerchantRecipeList merchantRecipeList, Random random) 
	{
		int recipeAmount = random.nextInt(4) + 2;
		for(int i = 0; i < recipeAmount;)
		{
			int attempts = 0;
			MerchantRecipe recipe = WeightedRandom.getRandomItem(random, weightList).merchantRecipe;
			if(!merchantRecipeList.contains(recipe))
			{
				merchantRecipeList.add(recipe);
				i++;
			}
			else
			{
				attempts++;
				if(attempts >= 20)
					break;
			}
		}
		return merchantRecipeList;
	}
}
