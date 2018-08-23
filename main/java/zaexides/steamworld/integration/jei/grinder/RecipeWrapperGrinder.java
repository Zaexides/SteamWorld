package zaexides.steamworld.integration.jei.grinder;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import zaexides.steamworld.recipe.handling.DustRecipe;
import zaexides.steamworld.recipe.handling.utility.IRecipeInput;
import zaexides.steamworld.recipe.handling.utility.RecipeInputItemStack;
import zaexides.steamworld.recipe.handling.utility.RecipeInputOreDic;

public class RecipeWrapperGrinder implements IRecipeWrapper
{
	private final List<ItemStack> INPUT;
	private final ItemStack OUTPUT;
	private final boolean AFFECT_BY_LEVEL;
	
	public RecipeWrapperGrinder(DustRecipe dustRecipe) 
	{
		IRecipeInput input = dustRecipe.getInput();
		
		if(input instanceof RecipeInputItemStack)
		{
			INPUT = new ArrayList<ItemStack>();
			INPUT.add(((RecipeInputItemStack) input).itemStack);
		}
		else
		{
			INPUT = OreDictionary.getOres(((RecipeInputOreDic) input).oreDicName);
		}
		
		OUTPUT = dustRecipe.getOutput();
		AFFECT_BY_LEVEL = dustRecipe.affectedByLevel();
	}

	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ingredients.setInput(ItemStack.class, INPUT);
		ingredients.setOutput(ItemStack.class, OUTPUT);
	}
	
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) 
	{
		if(AFFECT_BY_LEVEL)
			minecraft.fontRenderer.drawString("*", 58, 4, 0x22FF22);
	}
	
	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) 
	{
		final int x = 57;
		final int y = 1;
		final int w = 12;
		final int h = 12;
	
		List<String> tooltips = new ArrayList<String>();
		
		if(AFFECT_BY_LEVEL
				&& mouseX >= x && mouseX < (x+w)
				&& mouseY >= y && mouseY < (y+h))
		{
			tooltips.add(I18n.format("jei.steamworld.grinder_info"));
		}
		
		return tooltips;
	}
}
