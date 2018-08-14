package zaexides.steamworld.entity.villangler;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import zaexides.steamworld.init.EntityInitializer;
import zaexides.steamworld.init.ItemInitializer;
import zaexides.steamworld.items.SWItemIngot;

public class VillanglerMerchantGenDefault extends VillanglerMerchantRecipeListGenerator
{
	public ArrayList<MerchantRecipeWeightItem> weightList;
	
	public VillanglerMerchantGenDefault() 
	{
		RecipeWeightListBuilder builder = new RecipeWeightListBuilder();
		builder.addTrade(60, new ItemStack(CURRENCY_POOR.getItem(), 10, CURRENCY_POOR.getMetadata()), new ItemStack(ItemInitializer.INGOT, 1, SWItemIngot.EnumVarietyMaterial.STEAITE.getMeta()));
		builder.addTrade(50, new ItemStack(CURRENCY_POOR.getItem(), 20, CURRENCY_POOR.getMetadata()), new ItemStack(ItemInitializer.INGOT, 1, SWItemIngot.EnumVarietyMaterial.ANCITE.getMeta()));
		builder.addTrade(70, new ItemStack(CURRENCY_POOR.getItem(), 64, CURRENCY_POOR.getMetadata()), new ItemStack(CURRENCY_RICH.getItem(), 1, CURRENCY_RICH.getMetadata()));
		builder.addTrade(70, new ItemStack(CURRENCY_RICH.getItem(), 1, CURRENCY_RICH.getMetadata()), new ItemStack(CURRENCY_POOR.getItem(), 64, CURRENCY_POOR.getMetadata()));
		builder.addTrade(40, new ItemStack(CURRENCY_POOR.getItem(), 2, CURRENCY_POOR.getMetadata()), new ItemStack(Items.LEATHER, 5));
		builder.addTrade(60, new ItemStack(Items.LEATHER, 10), CURRENCY_POOR);
		builder.addTrade(70, new ItemStack(ItemInitializer.INGOT, 5, SWItemIngot.EnumVarietyMaterial.STEAITE.getMeta()), CURRENCY_POOR);
		builder.addTrade(30, new ItemStack(Items.DIAMOND, 3), new ItemStack(CURRENCY_POOR.getItem(), 20, CURRENCY_POOR.getMetadata()));
		builder.addTrade(30, new ItemStack(Items.GUNPOWDER, 10), new ItemStack(CURRENCY_POOR.getItem(), 4, CURRENCY_POOR.getMetadata()));
		builder.addTrade(30, new ItemStack(Items.ROTTEN_FLESH, 15), new ItemStack(CURRENCY_POOR.getItem(), 4, CURRENCY_POOR.getMetadata()));
		builder.addTrade(30, new ItemStack(Items.BONE, 7), new ItemStack(CURRENCY_POOR.getItem(), 2, CURRENCY_POOR.getMetadata()));
		
		ItemStack glowdustyEggStack = new ItemStack(Items.SPAWN_EGG);
		ItemMonsterPlacer.applyEntityIdToItemStack(glowdustyEggStack, EntityInitializer.GLOWDUSTY);
		builder.addTrade(10, new ItemStack(CURRENCY_RICH.getItem(), 2, CURRENCY_RICH.getMetadata()), glowdustyEggStack);
		
		weightList = builder.build();
	}
	
	@Override
	public MerchantRecipeList generateMerchantRecipeList(MerchantRecipeList merchantRecipeList, Random random) 
	{
		int recipeAmount = random.nextInt(5) + 3;
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
