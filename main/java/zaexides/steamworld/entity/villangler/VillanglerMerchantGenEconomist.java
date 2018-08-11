package zaexides.steamworld.entity.villangler;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import zaexides.steamworld.init.ItemInitializer;
import zaexides.steamworld.items.SWItemIngot;

public class VillanglerMerchantGenEconomist extends VillanglerMerchantRecipeListGenerator
{
	public ArrayList<MerchantRecipeWeightItem> weightList;
	
	public VillanglerMerchantGenEconomist() 
	{
		RecipeWeightListBuilder builder = new RecipeWeightListBuilder();
		builder.addTrade(90, new ItemStack(CURRENCY_POOR.getItem(), 64, CURRENCY_POOR.getMetadata()), new ItemStack(CURRENCY_RICH.getItem(), 1, CURRENCY_RICH.getMetadata()));
		builder.addTrade(90, new ItemStack(CURRENCY_RICH.getItem(), 1, CURRENCY_RICH.getMetadata()), new ItemStack(CURRENCY_POOR.getItem(), 64, CURRENCY_POOR.getMetadata()));
		builder.addTrade(50, new ItemStack(CURRENCY_RICH.getItem(), 1, CURRENCY_RICH.getMetadata()), new ItemStack(Items.EMERALD));
		builder.addTrade(50, new ItemStack(Items.EMERALD), new ItemStack(CURRENCY_POOR.getItem(), 48, CURRENCY_POOR.getMetadata()));
		builder.addTrade(20, new ItemStack(CURRENCY_POOR.getItem(), 50, CURRENCY_POOR.getMetadata()), new ItemStack(Items.DIAMOND, 3));
		builder.addTrade(20, new ItemStack(Items.DIAMOND, 3), new ItemStack(CURRENCY_POOR.getItem(), 20, CURRENCY_POOR.getMetadata()));
		builder.addTrade(10, new ItemStack(CURRENCY_POOR.getItem(), 10, CURRENCY_POOR.getMetadata()), new ItemStack(Items.GOLD_INGOT, 1));
		builder.addTrade(10, new ItemStack(Items.GOLD_INGOT, 1), new ItemStack(CURRENCY_POOR.getItem(), 5, CURRENCY_POOR.getMetadata()));
		builder.addTrade(2, new ItemStack(CURRENCY_RICH.getItem(), 50, CURRENCY_RICH.getMetadata()), new ItemStack(Items.NETHER_STAR, 1));
		builder.addTrade(5, new ItemStack(Items.NETHER_STAR, 1), new ItemStack(CURRENCY_RICH.getItem(), 32, CURRENCY_RICH.getMetadata()));
		weightList = builder.build();
	}
	
	@Override
	public MerchantRecipeList generateMerchantRecipeList(MerchantRecipeList merchantRecipeList, Random random) 
	{
		int recipeAmount = random.nextInt(3) + 4;
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
