package zaexides.steamworld.integration.jei;

import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.Nonnull;

import com.jcraft.jorbis.Block;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import scala.reflect.internal.Trees.New;
import zaexides.steamworld.blocks.BlockInitializer;
import zaexides.steamworld.containers.ContainerAssembler;
import zaexides.steamworld.containers.ContainerSteamFurnace;
import zaexides.steamworld.containers.ContainerSteamGrinder;
import zaexides.steamworld.gui.GuiAssembler;
import zaexides.steamworld.gui.GuiSteamFurnace;
import zaexides.steamworld.gui.GuiSteamGrinder;
import zaexides.steamworld.integration.jei.assembler.RecipeCategoryAssembler;
import zaexides.steamworld.integration.jei.assembler.RecipeWrapperAssembler;
import zaexides.steamworld.integration.jei.grinder.RecipeCategoryGrinder;
import zaexides.steamworld.integration.jei.grinder.RecipeWrapperGrinder;
import zaexides.steamworld.items.ItemDust;
import zaexides.steamworld.items.ItemInitializer;
import zaexides.steamworld.recipe.handling.AssemblyRecipe;
import zaexides.steamworld.recipe.handling.AssemblyRecipeHandler;
import zaexides.steamworld.recipe.handling.DustRecipeHandler;

@JEIPlugin
public class SteamWorldJEI implements IModPlugin
{
	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) 
	{
		registry.addRecipeCategories(
				new RecipeCategoryGrinder(registry.getJeiHelpers().getGuiHelper()),
				new RecipeCategoryAssembler(registry.getJeiHelpers().getGuiHelper())
				);
	}
	
	@Override
	public void register(@Nonnull IModRegistry registry) 
	{
		IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();
		
		//Grinder Registering
		registry.handleRecipes(ItemStack.class, RecipeWrapperGrinder::new, RecipeCategoryGrinder.UID);
		registry.addRecipes(DustRecipeHandler.inputs, RecipeCategoryGrinder.UID);
		registry.addRecipeClickArea(GuiSteamGrinder.class, 80, 31, 24, 24, RecipeCategoryGrinder.UID);
		registry.addRecipeCatalyst(new ItemStack(BlockInitializer.GRINDER_STEAITE), RecipeCategoryGrinder.UID);
		registry.addRecipeCatalyst(new ItemStack(BlockInitializer.GRINDER_ANCITE), RecipeCategoryGrinder.UID);
		registry.addRecipeCatalyst(new ItemStack(BlockInitializer.GRINDER_ENDRITCH), RecipeCategoryGrinder.UID);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerSteamGrinder.class, RecipeCategoryGrinder.UID, 0, 1, 2, 36);
		
		//Furnace Registering
		registry.addRecipeClickArea(GuiSteamFurnace.class, 80, 31, 24, 24, VanillaRecipeCategoryUid.SMELTING);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerSteamFurnace.class, VanillaRecipeCategoryUid.SMELTING, 0, 1, 2, 36);
		registry.addRecipeCatalyst(new ItemStack(BlockInitializer.FURNACE_STEAITE), VanillaRecipeCategoryUid.SMELTING);
		registry.addRecipeCatalyst(new ItemStack(BlockInitializer.FURNACE_ANCITE), VanillaRecipeCategoryUid.SMELTING);
		registry.addRecipeCatalyst(new ItemStack(BlockInitializer.FURNACE_ENDRITCH), VanillaRecipeCategoryUid.SMELTING);
		
		//Assembly Registering
		registry.handleRecipes(AssemblyRecipe.class, RecipeWrapperAssembler::new, RecipeCategoryAssembler.UID);
		registry.addRecipes(AssemblyRecipeHandler.recipes, RecipeCategoryAssembler.UID);
		registry.addRecipeClickArea(GuiAssembler.class, 3, 3, 170, 16, RecipeCategoryAssembler.UID);
		registry.addRecipeCatalyst(new ItemStack(BlockInitializer.ASSEMBLER_ANCITE), RecipeCategoryAssembler.UID);
		registry.addRecipeCatalyst(new ItemStack(BlockInitializer.ASSEMBLER_ENDRITCH), RecipeCategoryAssembler.UID);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerAssembler.class, RecipeCategoryAssembler.UID, 0, 7, 8, 36);
		
		//Descriptions
		registry.addIngredientInfo(Arrays.asList(
				new ItemStack(BlockInitializer.GENERATOR_STEAITE),
				new ItemStack(BlockInitializer.GENERATOR_ANCITE),
				new ItemStack(BlockInitializer.GENERATOR_ENDRITCH)
				), ItemStack.class, "jei.steamworld.generator.description");
		registry.addIngredientInfo(Arrays.asList(
				new ItemStack(BlockInitializer.GRINDER_STEAITE),
				new ItemStack(BlockInitializer.GRINDER_ANCITE),
				new ItemStack(BlockInitializer.GRINDER_ENDRITCH)
				), ItemStack.class, "jei.steamworld.grinder.description");
		registry.addIngredientInfo(Arrays.asList(
				new ItemStack(BlockInitializer.FURNACE_STEAITE),
				new ItemStack(BlockInitializer.FURNACE_ANCITE),
				new ItemStack(BlockInitializer.FURNACE_ENDRITCH)
				), ItemStack.class, "jei.steamworld.furnace.description");
		registry.addIngredientInfo(Arrays.asList(
				new ItemStack(BlockInitializer.FISHER_STEAITE),
				new ItemStack(BlockInitializer.FISHER_ANCITE),
				new ItemStack(BlockInitializer.FISHER_ENDRITCH)
				), ItemStack.class, "jei.steamworld.fisher.description");
		registry.addIngredientInfo(Arrays.asList(
				new ItemStack(BlockInitializer.FARMER_STEAITE),
				new ItemStack(BlockInitializer.FARMER_ANCITE),
				new ItemStack(BlockInitializer.FARMER_ENDRITCH)
				), ItemStack.class, "jei.steamworld.farmer.description");
		registry.addIngredientInfo(Arrays.asList(
				new ItemStack(BlockInitializer.LUMBER_STEAITE),
				new ItemStack(BlockInitializer.LUMBER_ANCITE),
				new ItemStack(BlockInitializer.LUMBER_ENDRITCH)
				), ItemStack.class, "jei.steamworld.lumber.description");
		registry.addIngredientInfo(Arrays.asList(
				new ItemStack(BlockInitializer.FERTILIZER_STEAITE),
				new ItemStack(BlockInitializer.FERTILIZER_ANCITE),
				new ItemStack(BlockInitializer.FERTILIZER_ENDRITCH)
				), ItemStack.class, "jei.steamworld.fertilizer.description");
		registry.addIngredientInfo(Arrays.asList(
				new ItemStack(BlockInitializer.BLOCK_VALVE),
				new ItemStack(BlockInitializer.BLOCK_VALVE_ENDRITCH)
				), ItemStack.class, "jei.steamworld.fluid_controller.description");
		registry.addIngredientInfo(Arrays.asList(
				new ItemStack(BlockInitializer.EXPERIENCE_MACHINE_ANCITE),
				new ItemStack(BlockInitializer.EXPERIENCE_MACHINE_ENDRITCH)
				), ItemStack.class, "jei.steamworld.experience_machine.description");
		registry.addIngredientInfo(Arrays.asList(
				new ItemStack(BlockInitializer.ASSEMBLER_ANCITE),
				new ItemStack(BlockInitializer.ASSEMBLER_ENDRITCH)
				), ItemStack.class, "jei.steamworld.assembler.description");
		registry.addIngredientInfo(new ItemStack(BlockInitializer.GENERATOR_NETHER), 
				ItemStack.class, "jei.steamworld.generator_nether.description");
		registry.addIngredientInfo(new ItemStack(BlockInitializer.BLOCK_DRAIN), 
				ItemStack.class, "jei.steamworld.drain.description");
		registry.addIngredientInfo(new ItemStack(BlockInitializer.BLOCK_FAUCET), 
				ItemStack.class, "jei.steamworld.faucet.description");
		registry.addIngredientInfo(new ItemStack(BlockInitializer.BLOCK_NETHER_ACCELERATOR), 
				ItemStack.class, "jei.steamworld.accelerator.description");
		registry.addIngredientInfo(Arrays.asList(
				new ItemStack(BlockInitializer.BLOCK_STEAM_GENERATOR_ELECTRIC),
				new ItemStack(BlockInitializer.BLOCK_DYNAMO)
				), ItemStack.class, "jei.steamworld.energy.description");
	}
}
