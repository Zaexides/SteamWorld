package zaexides.steamworld.integration.jei.assembler;

import java.util.ArrayList;
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
import zaexides.steamworld.recipe.handling.AssemblyRecipe;
import zaexides.steamworld.recipe.handling.AssemblyRecipeHandler;
import zaexides.steamworld.recipe.handling.DustRecipeHandler;
import zaexides.steamworld.recipe.handling.utility.IRecipeInput;
import zaexides.steamworld.recipe.handling.utility.RecipeInputItemStack;
import zaexides.steamworld.recipe.handling.utility.RecipeInputOreDic;

public class RecipeWrapperAssembler implements IRecipeWrapper
{
	private final float SECOND_MULTIPLIER = 1f/20f;
	private final float MINUTE_MULTIPLIER = 1f/20f/60f;
	
	private final List<List<ItemStack>> INPUTS;
	private final ItemStack OUTPUT;
	private final int DURATION_SECONDS;
	private final int DURATION_MINUTES;
	
	public RecipeWrapperAssembler(AssemblyRecipe recipe) 
	{
		INPUTS = new ArrayList<List<ItemStack>>();
		for(IRecipeInput recipeInput : recipe.inputs)
		{
			if(recipeInput instanceof RecipeInputItemStack)
			{
				List<ItemStack> soleItemStackList = new ArrayList<ItemStack>();
				soleItemStackList.add(((RecipeInputItemStack) recipeInput).itemStack);
				INPUTS.add(soleItemStackList);
			}
			else if(recipeInput instanceof RecipeInputOreDic)
			{
				String oreDicName = ((RecipeInputOreDic)recipeInput).oreDicName;
				List<ItemStack> itemStacks = OreDictionary.getOres(oreDicName);
				INPUTS.add(itemStacks);
			}
		}
		OUTPUT = recipe.output;
		int duration = recipe.duration;
		DURATION_SECONDS = (int)Math.ceil(duration * SECOND_MULTIPLIER) % 60;
		DURATION_MINUTES = (int)Math.floor(duration * MINUTE_MULTIPLIER);
	}

	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ingredients.setInputLists(ItemStack.class, INPUTS);
		ingredients.setOutput(ItemStack.class, OUTPUT);
	}
	
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) 
	{
		String text = DURATION_MINUTES + ":" + String.format("%02d", DURATION_SECONDS);
		
		int w = minecraft.fontRenderer.getStringWidth(text);
		int x = (int)(recipeWidth * 0.5f - w * 0.5f);
		final int y = 54;
		
		minecraft.fontRenderer.drawString(text, x, y, 0xCCCCFF);
	}
	
	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) 
	{
		final int x = 21;
		final int y = 53;
		final int w = 48;
		final int h = 9;
		
		List<String> tooltips = new ArrayList<String>();
		if((mouseX >= x) && (mouseX < (x+w))
			&& (mouseY >= y) && (mouseY < (y+h)))
		{
			tooltips.add(I18n.format("jei.steamworld.time", DURATION_MINUTES, DURATION_SECONDS));
			tooltips.add(I18n.format("jei.steamworld.time_tier_info"));
		}
		return tooltips;
	}
}
