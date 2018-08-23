package zaexides.steamworld.entity.villangler;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.WeightedRandom;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import zaexides.steamworld.init.EntityInitializer;
import zaexides.steamworld.init.ItemInitializer;
import zaexides.steamworld.init.PotionTypeInitializer;
import zaexides.steamworld.items.ItemMaterial;
import zaexides.steamworld.items.SWItemIngot;

public class VillanglerMerchantGenCultist extends VillanglerMerchantRecipeListGenerator
{
	public ArrayList<MerchantRecipeWeightItem> weightList;
	
	public VillanglerMerchantGenCultist() 
	{
		RecipeWeightListBuilder builder = new RecipeWeightListBuilder();
		builder.addTrade(100000, new ItemStack(CURRENCY_RICH.getItem(), 10, CURRENCY_RICH.getMetadata()), new ItemStack(ItemInitializer.GENERIC_MATERIAL, 1, ItemMaterial.EnumVarietyMaterial.LEVIATHAN_FEATHER.getMeta()));
		builder.addTrade(60, new ItemStack(CURRENCY_POOR.getItem(), 1, CURRENCY_POOR.getMetadata()), new ItemStack(Items.ROTTEN_FLESH, 3));
		builder.addTrade(50, new ItemStack(Items.PORKCHOP, 16), new ItemStack(CURRENCY_POOR.getItem(), 1, CURRENCY_POOR.getMetadata()));
		builder.addTrade(30, new ItemStack(Blocks.OBSIDIAN, 32), new ItemStack(CURRENCY_POOR.getItem(), 16, CURRENCY_POOR.getMetadata()));
		builder.addTrade(30, new ItemStack(CURRENCY_POOR.getItem(), 12, CURRENCY_POOR.getMetadata()), new ItemStack(Items.ENDER_PEARL));
		builder.addTrade(10, new ItemStack(CURRENCY_POOR.getItem(), 40, CURRENCY_POOR.getMetadata()), new ItemStack(ItemInitializer.INGOT, 1, SWItemIngot.EnumVarietyMaterial.ENDRITCH.getMeta()));
		builder.addTrade(30, new ItemStack(CURRENCY_POOR.getItem(), 25, CURRENCY_POOR.getMetadata()), new ItemStack(Blocks.CHORUS_FLOWER));
		builder.addTrade(30, new ItemStack(CURRENCY_POOR.getItem(), 25, CURRENCY_POOR.getMetadata()), new ItemStack(Items.NETHER_WART, 5));
		builder.addTrade(5, new ItemStack(CURRENCY_POOR.getItem(), 30, CURRENCY_POOR.getMetadata()), GetPotionStack(PotionTypes.HARMING));
		builder.addTrade(5, new ItemStack(CURRENCY_POOR.getItem(), 40, CURRENCY_POOR.getMetadata()), GetPotionStack(PotionTypes.LONG_WEAKNESS));
		builder.addTrade(5, new ItemStack(CURRENCY_POOR.getItem(), 50, CURRENCY_POOR.getMetadata()), GetPotionStack(PotionTypes.STRONG_HARMING));
		builder.addTrade(5, new ItemStack(CURRENCY_POOR.getItem(), 50, CURRENCY_POOR.getMetadata()), GetPotionStack(PotionTypes.STRONG_POISON));
		builder.addTrade(5, new ItemStack(CURRENCY_POOR.getItem(), 30, CURRENCY_POOR.getMetadata()), GetPotionStack(PotionTypes.LONG_SLOWNESS));
		builder.addTrade(3, new ItemStack(CURRENCY_POOR.getItem(), 50, CURRENCY_POOR.getMetadata()), GetPotionStack(PotionTypeInitializer.UNLUCK));
		
		ItemStack glowdustyEggStack = new ItemStack(Items.SPAWN_EGG);
		ItemMonsterPlacer.applyEntityIdToItemStack(glowdustyEggStack, EntityInitializer.GLOWDUSTY);
		builder.addTrade(10, new ItemStack(CURRENCY_RICH.getItem(), 2, CURRENCY_RICH.getMetadata()), glowdustyEggStack);
		
		weightList = builder.build();
	}
	
	private ItemStack GetPotionStack(PotionType type)
	{
		ItemStack potion = new ItemStack(Items.POTIONITEM);
		return PotionUtils.addPotionToItemStack(potion, type);
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
