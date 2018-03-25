package zaexides.steamworld.integration.jei.assembler;

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
import zaexides.steamworld.init.ItemInitializer;

public class RecipeCategoryAssembler implements IRecipeCategory
{
	public static final String UID = "steamworld.assemblerCat";
	public static String LOCALIZED_NAME;
	private IDrawable background, icon;
	
	public RecipeCategoryAssembler(IGuiHelper helper) 
	{
		final int xPos = 0;
		final int yPos = 0;
		final int width = 90;
		final int height = 65;
		
		LOCALIZED_NAME = I18n.format("jei.steamworld.assembler");
		
		ResourceLocation resourceLocation = new ResourceLocation(ModInfo.MODID, "textures/gui/jei/jei_map.png");
		background = helper.createDrawable(resourceLocation, xPos, yPos, width, height, 0, 0, 0, 0);
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
		
		for(int y = 0; y < 3; y++)
			group.init(y + 0, true, 0, 11 + y*18);
		for(int y = 0; y < 3; y++)
			group.init(y + 3, true, 72, 11 + y*18);
		group.init(6, true, 36, 3);
		
		group.init(7, false, 36, 29);
		
		for(int i = 0; i < 6; i++)
			group.set(i, ingredients.getInputs(ItemStack.class).get(i));
		group.set(6, new ItemStack(ItemInitializer.STEAITE_CRYSTAL));
		group.set(7, ingredients.getOutputs(ItemStack.class).get(0));
	}
}