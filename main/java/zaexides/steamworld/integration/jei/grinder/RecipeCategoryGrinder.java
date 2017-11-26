package zaexides.steamworld.integration.jei.grinder;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import zaexides.steamworld.ModInfo;

public class RecipeCategoryGrinder implements IRecipeCategory
{
	public static final String UID = "steamworld.grinderCat";
	public static String LOCALIZED_NAME;
	private IDrawable background, icon;
	
	public RecipeCategoryGrinder(IGuiHelper helper) 
	{
		final int xPos = 55;
		final int yPos = 30;
		final int width = 82;
		final int height = 26;
		
		LOCALIZED_NAME = I18n.format("jei.steamworld.grinder");
		
		ResourceLocation resourceLocation = new ResourceLocation(ModInfo.MODID, "textures/gui/machine/gui_steam_grinder.png");
		background = helper.createDrawable(resourceLocation, xPos, yPos, width, height, 4, 4, 0, 0);
	}

	@Override
	public String getUid() {
		return UID;
	}

	@Override
	public String getTitle() {
		return LOCALIZED_NAME;
	}

	@Override
	public String getModName() {
		return ModInfo.MODNAME;
	}

	@Override
	public IDrawable getBackground() 
	{
		return background;
	}
	
	@Override
	public IDrawable getIcon() 
	{
		// TODO Auto-generated method stub
		return IRecipeCategory.super.getIcon();
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients)
	{
		IGuiItemStackGroup group = recipeLayout.getItemStacks();
		group.init(0, true, 0, 8); //Position is generally -1. Padding must be taken into consideration, tho.
		group.init(1, false, 60, 8);
		
		group.set(0, ingredients.getInputs(ItemStack.class).get(0));
		group.set(1, ingredients.getOutputs(ItemStack.class).get(0));
	}
}