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
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import zaexides.steamworld.blocks.BlockSWFlower;
import zaexides.steamworld.fluids.FluidPreservation;
import zaexides.steamworld.fluids.FluidWithering;
import zaexides.steamworld.init.BlockInitializer;
import zaexides.steamworld.init.ItemInitializer;
import zaexides.steamworld.init.PotionTypeInitializer;
import zaexides.steamworld.items.SWItemIngot;

public class VillanglerMerchantGenScientist extends VillanglerMerchantRecipeListGenerator
{
	public ArrayList<MerchantRecipeWeightItem> weightList;
	private final static int BASE_ENCHANTED_BOOK_COST = 5;
	
	public VillanglerMerchantGenScientist() 
	{
		RecipeWeightListBuilder builder = new RecipeWeightListBuilder();
		builder.addTrade(50, new ItemStack(CURRENCY_POOR.getItem(), 2, CURRENCY_POOR.getMetadata()), new ItemStack(Items.BOOK, 3));
		builder.addTrade(20, new ItemStack(CURRENCY_POOR.getItem(), 20, CURRENCY_POOR.getMetadata()), GetPotionStack(PotionTypes.SWIFTNESS));
		builder.addTrade(18, new ItemStack(CURRENCY_POOR.getItem(), 35, CURRENCY_POOR.getMetadata()), GetPotionStack(PotionTypes.LEAPING));
		builder.addTrade(22, new ItemStack(CURRENCY_POOR.getItem(), 25, CURRENCY_POOR.getMetadata()), GetPotionStack(PotionTypes.FIRE_RESISTANCE));
		builder.addTrade(20, new ItemStack(CURRENCY_POOR.getItem(), 20, CURRENCY_POOR.getMetadata()), GetPotionStack(PotionTypes.STRENGTH));
		builder.addTrade(22, new ItemStack(CURRENCY_POOR.getItem(), 25, CURRENCY_POOR.getMetadata()), GetPotionStack(PotionTypes.HEALING));
		builder.addTrade(19, new ItemStack(CURRENCY_POOR.getItem(), 30, CURRENCY_POOR.getMetadata()), GetPotionStack(PotionTypes.REGENERATION));
		builder.addTrade(16, new ItemStack(CURRENCY_POOR.getItem(), 45, CURRENCY_POOR.getMetadata()), GetPotionStack(PotionTypeInitializer.LUCK));
		builder.addTrade(15, new ItemStack(CURRENCY_POOR.getItem(), 12, CURRENCY_POOR.getMetadata()), new ItemStack(BlockInitializer.BLOCK_FLOWER, 1, BlockSWFlower.EnumType.WITHER.getMeta()));
		builder.addTrade(25, new ItemStack(BlockInitializer.BLOCK_FLOWER, 1, BlockSWFlower.EnumType.WITHER.getMeta()), new ItemStack(CURRENCY_POOR.getItem(), 10, CURRENCY_POOR.getMetadata()));
		
		builder.addDoubleTrade(5,
				new ItemStack(Items.BUCKET),
				new ItemStack(CURRENCY_RICH.getItem(), 2, CURRENCY_RICH.getMetadata()),
				FluidUtil.getFilledBucket(new FluidStack(FluidWithering.fluidWithering, Fluid.BUCKET_VOLUME)));
		builder.addDoubleTrade(5,
				new ItemStack(Items.BUCKET),
				new ItemStack(CURRENCY_RICH.getItem(), 2, CURRENCY_RICH.getMetadata()),
				FluidUtil.getFilledBucket(new FluidStack(FluidPreservation.fluidPreservation, Fluid.BUCKET_VOLUME)));
		
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
		int recipeAmount = random.nextInt(2) + 1;
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
