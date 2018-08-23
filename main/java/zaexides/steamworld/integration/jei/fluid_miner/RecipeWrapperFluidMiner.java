package zaexides.steamworld.integration.jei.fluid_miner;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import zaexides.steamworld.items.ItemMinerMachineTool;
import zaexides.steamworld.recipe.handling.FluidMinerRecipe;

public class RecipeWrapperFluidMiner implements IRecipeWrapper
{
	private final List<List<ItemStack>> inputs;
	private final FluidStack output;
	
	public RecipeWrapperFluidMiner(FluidMinerRecipe recipe) 
	{
		inputs = new ArrayList<List<ItemStack>>();
		
		byte tier = recipe.getTier();
		
		ItemMinerMachineTool[] drillHeads = ItemMinerMachineTool.drillHeads.stream().filter(dh -> tier <= dh.getTier()).toArray(ItemMinerMachineTool[]::new);
		List<ItemStack> drillStackList = new ArrayList<ItemStack>();
		for(ItemMinerMachineTool dh : drillHeads)
		{
			if(dh.isPump)
				drillStackList.add(new ItemStack(dh));
		}
		inputs.add(drillStackList);
		
		FluidStack recipeOutput = recipe.getOutput();
		output = recipeOutput;
	}

	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ingredients.setInputLists(ItemStack.class, inputs);
		ingredients.setOutput(FluidStack.class, output);
	}
}
