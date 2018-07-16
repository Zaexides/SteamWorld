package zaexides.steamworld.integration.jei.fluid_miner;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import zaexides.steamworld.ModInfo;

public class RecipeCategoryFluidMiner implements IRecipeCategory
{
	public static final String UID = "steamworld.fluidMinerCat";
	public static String LOCALIZED_NAME;
	private IDrawable background, icon;
	
	public RecipeCategoryFluidMiner(IGuiHelper helper) 
	{
		final int xPos = 53;
		final int yPos = 22;
		final int width = 74;
		final int height = 43;
		
		LOCALIZED_NAME = I18n.format("jei.steamworld.fluid_miner");
		
		ResourceLocation resourceLocation = new ResourceLocation(ModInfo.MODID, "textures/gui/machine/gui_fluid_miner.png");
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
		IGuiFluidStackGroup fluidStackGroup = recipeLayout.getFluidStacks();
		group.init(0, true, 0, 17); //Position is generally -1. Padding must be taken into consideration, tho.
		fluidStackGroup.init(1, false, 57, 5, 16, 41, 1000, true, null);
		
		group.set(0, ingredients.getInputs(ItemStack.class).get(0));
		fluidStackGroup.set(1, ingredients.getOutputs(FluidStack.class).get(0));
	}
}