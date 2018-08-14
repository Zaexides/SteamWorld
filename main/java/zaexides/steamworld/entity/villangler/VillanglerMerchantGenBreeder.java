package zaexides.steamworld.entity.villangler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import zaexides.steamworld.init.EntityInitializer;
import zaexides.steamworld.init.ItemInitializer;
import zaexides.steamworld.items.SWItemIngot;

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
		int recipeAmount = random.nextInt(3) + 6;
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
