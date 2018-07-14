package zaexides.steamworld.integration.jei.miner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.Level;

import com.google.common.collect.ImmutableList;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.items.ItemDrillHead;
import zaexides.steamworld.recipe.handling.DustRecipeHandler;
import zaexides.steamworld.recipe.handling.MinerRecipe;
import zaexides.steamworld.recipe.handling.DustRecipe;
import zaexides.steamworld.recipe.handling.utility.IRecipeInput;
import zaexides.steamworld.recipe.handling.utility.RecipeInputItemStack;
import zaexides.steamworld.recipe.handling.utility.RecipeInputOreDic;

public class RecipeWrapperMiner implements IRecipeWrapper
{
	private final List<List<ItemStack>> inputs;
	private final ItemStack output;
	
	public RecipeWrapperMiner(MinerRecipe recipe) 
	{
		inputs = new ArrayList<List<ItemStack>>();
		
		byte tier = recipe.getTier();
		
		ItemDrillHead[] drillHeads = ItemDrillHead.drillHeads.stream().filter(dh -> tier <= dh.getTier()).toArray(ItemDrillHead[]::new);
		List<ItemStack> drillStackList = new ArrayList<ItemStack>();
		for(ItemDrillHead dh : drillHeads)
			drillStackList.add(new ItemStack(dh));
		inputs.add(drillStackList);
		
		IRecipeInput recipeOutput = recipe.getOutput();
		if(recipeOutput instanceof RecipeInputItemStack)
		{
			List<ItemStack> soleItemStackList = new ArrayList<ItemStack>();
			soleItemStackList.add(((RecipeInputItemStack) recipeOutput).itemStack);
			output = ((RecipeInputItemStack)recipeOutput).itemStack;
		}
		else if(recipeOutput instanceof RecipeInputOreDic)
		{
			String oreDicName = ((RecipeInputOreDic) recipeOutput).oreDicName;
			ItemStack itemStack = OreDictionary.getOres(oreDicName).get(0);
			output = itemStack;
		}
		else
			output = ItemStack.EMPTY;
	}

	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ingredients.setInputLists(ItemStack.class, inputs);
		ingredients.setOutput(ItemStack.class, output);
	}
}
