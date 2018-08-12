package zaexides.steamworld.entity.villangler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.WeightedRandom;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import zaexides.steamworld.init.ItemInitializer;
import zaexides.steamworld.items.SWItemIngot;

public class VillanglerMerchantGenLibrarian extends VillanglerMerchantRecipeListGenerator
{
	public ArrayList<MerchantRecipeWeightItem> weightList;
	private final static int BASE_ENCHANTED_BOOK_COST = 5;
	
	public VillanglerMerchantGenLibrarian() 
	{
		RecipeWeightListBuilder builder = new RecipeWeightListBuilder();
		builder.addTrade(100, new ItemStack(CURRENCY_POOR.getItem(), 2, CURRENCY_POOR.getMetadata()), new ItemStack(Items.BOOK, 3));
		
		addEnchantmentTrades(builder);
		weightList = builder.build();
	}
	
	private void addEnchantmentTrades(RecipeWeightListBuilder builder)
	{
		for(Enchantment enchantment : Enchantment.REGISTRY)
		{
			for(int i = 1; i <= enchantment.getMaxLevel(); i++)
			{
				EnchantmentData enchantmentData = new EnchantmentData(enchantment, i);
				addEnchantmentTrade(enchantmentData, builder);
			}
		}
	}
	
	private void addEnchantmentTrade(EnchantmentData enchantmentData, RecipeWeightListBuilder builder)
	{
		ItemStack enchantedBook = ItemEnchantedBook.getEnchantedItemStack(enchantmentData);
		int cost = (int)((BASE_ENCHANTED_BOOK_COST + (enchantmentData.enchantmentLevel * 2.5f)) * (enchantmentData.enchantment.isTreasureEnchantment() ? 4 : 1) * ((11 - enchantmentData.enchantment.getRarity().getWeight()) * 1.2f));
		int richCost = 0;
		while(cost > 64)
		{
			richCost++;
			cost -= 64;
		}
		
		builder.addDoubleTrade(enchantmentData.enchantment.isTreasureEnchantment() ? 2 : 10 + enchantmentData.enchantment.getRarity().getWeight(),
				new ItemStack(CURRENCY_RICH.getItem(), richCost, CURRENCY_RICH.getMetadata()),
				new ItemStack(CURRENCY_POOR.getItem(), cost, CURRENCY_POOR.getMetadata()),
				enchantedBook);
	}
	
	@Override
	public MerchantRecipeList generateMerchantRecipeList(MerchantRecipeList merchantRecipeList, Random random) 
	{
		int recipeAmount = random.nextInt(10) + 5;
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
