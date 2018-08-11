package zaexides.steamworld.entity.villangler;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import zaexides.steamworld.init.ItemInitializer;
import zaexides.steamworld.items.ItemMaterial;

public abstract class VillanglerMerchantRecipeListGenerator 
{
	public static final ItemStack CURRENCY_POOR = new ItemStack(ItemInitializer.GENERIC_MATERIAL, 1, ItemMaterial.EnumVarietyMaterial.CURRENCY_LOW.getMeta());
	public static final ItemStack CURRENCY_RICH = new ItemStack(ItemInitializer.GENERIC_MATERIAL, 1, ItemMaterial.EnumVarietyMaterial.CURRENCY_HIGH.getMeta());
	
	public abstract MerchantRecipeList generateMerchantRecipeList(MerchantRecipeList merchantRecipeList, Random random);
	
	public static class MerchantRecipeWeightItem extends WeightedRandom.Item
	{
		public MerchantRecipe merchantRecipe;
		
		public MerchantRecipeWeightItem(MerchantRecipe recipe, int itemWeightIn) 
		{
			super(itemWeightIn);
			this.merchantRecipe = recipe;
		}
	}
	
	public static class RecipeWeightListBuilder
	{
		public ArrayList<MerchantRecipeWeightItem> weightItems = new ArrayList<MerchantRecipeWeightItem>();
		
		public RecipeWeightListBuilder addTrade(int weight, Item buy, int buyAmount, Item sell, int sellAmount)
		{
			return addTrade(weight, buy, 0, buyAmount, sell, 0, sellAmount);
		}
		
		public RecipeWeightListBuilder addTrade(int weight, Item buy, int buyMeta, int buyAmount, Item sell, int sellAmount)
		{
			return addTrade(weight, buy, buyMeta, buyAmount, sell, 0, sellAmount);
		}
		
		public RecipeWeightListBuilder addTrade(int weight, Item buy, int buyAmount, Item sell, int sellMeta, int sellAmount)
		{
			return addTrade(weight, buy, 0, buyAmount, sell, sellMeta, sellAmount);
		}
		
		public RecipeWeightListBuilder addTrade(int weight, Item buy, int buyMeta, int buyAmount, Item sell, int sellMeta, int sellAmount)
		{
			ItemStack buyStack = new ItemStack(buy, buyAmount, buyMeta);
			ItemStack sellStack = new ItemStack(sell, sellAmount, sellMeta);
			return addTrade(weight, buyStack, sellStack);
		}
		
		public RecipeWeightListBuilder addTrade(int weight, ItemStack buyStack, ItemStack sellStack)
		{
			MerchantRecipe recipe = new MerchantRecipe(buyStack, sellStack);
			weightItems.add(new MerchantRecipeWeightItem(recipe, weight));
			return this;
		}
		
		public RecipeWeightListBuilder addDoubleTrade(int weight, Item buy1, int buy1Meta, int buy1Amount, Item buy2, int buy2Meta, int buy2Amount, Item sell, int sellMeta, int sellAmount)
		{
			ItemStack buy1Stack = new ItemStack(buy1, buy1Amount, buy1Meta);
			ItemStack buy2Stack = new ItemStack(buy2, buy2Amount, buy2Meta);
			ItemStack sellStack = new ItemStack(sell, sellAmount, sellMeta);
			return addDoubleTrade(weight, buy1Stack, buy2Stack, sellStack);
		}
		
		public RecipeWeightListBuilder addDoubleTrade(int weight, ItemStack buy1Stack, ItemStack buy2Stack, ItemStack sellStack)
		{
			MerchantRecipe recipe = new MerchantRecipe(buy1Stack, buy2Stack, sellStack);
			weightItems.add(new MerchantRecipeWeightItem(recipe, weight));
			return this;
		}
		
		public ArrayList<MerchantRecipeWeightItem> build()
		{
			return weightItems;
		}
	}
}
