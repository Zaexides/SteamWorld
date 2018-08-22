package zaexides.steamworld.recipe.handling;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.BlockDecorative;
import zaexides.steamworld.blocks.machines.BlockMachineVariant;
import zaexides.steamworld.init.BlockInitializer;
import zaexides.steamworld.init.ItemInitializer;
import zaexides.steamworld.items.ItemMaterial;
import zaexides.steamworld.items.ItemUpgrade;
import zaexides.steamworld.items.SWItemIngot;
import zaexides.steamworld.items.SWItemNugget;
import zaexides.steamworld.recipe.handling.utility.IRecipeInput;
import zaexides.steamworld.recipe.handling.utility.RecipeInputItemStack;
import zaexides.steamworld.recipe.handling.utility.RecipeInputOreDic;

public class AssemblyRecipeHandler 
{
	public static List<AssemblyRecipe> recipes = new ArrayList<AssemblyRecipe>();
	public static List<IRecipeInput> blacklist = new ArrayList<IRecipeInput>();
	
	public static AssemblyRecipe getOutput(List<ItemStack> inputs)
	{
		for(AssemblyRecipe recipe : recipes)
		{
			if(recipe.isRecipe(inputs))
				return recipe.Copy();
		}
		return null;
	}
	
	public static void AddRecipeToList(AssemblyRecipe assemblyRecipe, boolean forced)
	{
		if(isBlackListed(assemblyRecipe.output) && !forced)
			SteamWorld.logger.log(Level.INFO, "Assembly recipe for " + assemblyRecipe.output + " was skipped as it's blacklisted.");
		else
			recipes.add(assemblyRecipe);
	}
	
	public static boolean isBlackListed(ItemStack input)
	{
		for(IRecipeInput recipeInput : AssemblyRecipeHandler.blacklist)
		{
			if(recipeInput.matchesItemStack(input))
				return true;
		}
		return false;
	}
	
	public static void RegisterRecipes()
	{
		final RecipeInputItemStack EMPTY_INPUT = new RecipeInputItemStack(ItemStack.EMPTY);
		final RecipeInputItemStack ENDER_PEARL_INPUT = new RecipeInputItemStack(new ItemStack(Items.ENDER_PEARL));
		final RecipeInputItemStack ENDER_EYE_INPUT = new RecipeInputItemStack(new ItemStack(Items.ENDER_EYE));
		final RecipeInputItemStack ENDRITCH_BLOCK_INPUT = new RecipeInputItemStack(new ItemStack(BlockInitializer.BLOCK_DECORATIVE, 1, BlockDecorative.EnumType.ENDRITCH_BLOCK.getMeta()));
		final RecipeInputItemStack CHORUS_FRUIT = new RecipeInputItemStack(new ItemStack(Items.CHORUS_FRUIT));
		final RecipeInputItemStack ADVANCED_MACHINE_PARTS = new RecipeInputItemStack(new ItemStack(ItemInitializer.GENERIC_MATERIAL, 1, ItemMaterial.EnumVarietyMaterial.ADVANCED_MACHINE_PARTS.getMeta()));
		final RecipeInputOreDic COAL_BLOCK = new RecipeInputOreDic("blockCoal");
		final RecipeInputOreDic NUGGET_ANCITE = new RecipeInputOreDic("nuggetAncite");
		final RecipeInputOreDic INGOT_GALITE = new RecipeInputOreDic("ingotGalite");
		final RecipeInputOreDic PLATE_GALITE = new RecipeInputOreDic("plateGalite");
		final RecipeInputOreDic PLATE_ESSEN = new RecipeInputOreDic("plateEssen");
		final RecipeInputOreDic INGOT_ESSEN = new RecipeInputOreDic("ingotEssen");
		final RecipeInputOreDic DIAMOND = new RecipeInputOreDic("gemDiamond");
		final RecipeInputOreDic PLATE_DIAMOND = new RecipeInputOreDic("plateDiamond");
		final RecipeInputOreDic INGOT_IRON = new RecipeInputOreDic("ingotIron");
		final RecipeInputOreDic INGOT_GOLD = new RecipeInputOreDic("ingotGold");
		final RecipeInputOreDic PLATE_IRON = new RecipeInputOreDic("plateIron");
		
		AssemblyRecipe recipeDiamond = new AssemblyRecipe(new ItemStack(Items.DIAMOND), 3000,
				COAL_BLOCK,COAL_BLOCK,
				COAL_BLOCK,COAL_BLOCK,
				COAL_BLOCK,COAL_BLOCK
				);
		
		AssemblyRecipe recipeEndritch = new AssemblyRecipe(new ItemStack(ItemInitializer.INGOT, 1, SWItemIngot.EnumVarietyMaterial.ENDRITCH.getMeta()), 500,
				ENDER_PEARL_INPUT,NUGGET_ANCITE,
				CHORUS_FRUIT, ENDER_PEARL_INPUT,
				NUGGET_ANCITE,CHORUS_FRUIT
				);
		
		AssemblyRecipe recipeEndritchValve = new AssemblyRecipe(new ItemStack(BlockInitializer.BLOCK_VALVE_ENDRITCH), 600,
				new ItemStack(BlockInitializer.BLOCK_VALVE),
				new ItemStack(BlockInitializer.BLOCK_DECORATIVE, 1, BlockDecorative.EnumType.ENDRITCH_BLOCK.getMeta()),
				ItemStack.EMPTY,
				new ItemStack(Items.ENDER_EYE),
				new ItemStack(BlockInitializer.BLOCK_DECORATIVE, 1, BlockDecorative.EnumType.ENDRITCH_BLOCK.getMeta()),
				ItemStack.EMPTY
				);
		
		AssemblyRecipe recipeNetherAccelerator = new AssemblyRecipe(new ItemStack(BlockInitializer.BLOCK_NETHER_ACCELERATOR), 1200,
				new ItemStack(BlockInitializer.BLOCK_DECORATIVE, 1, BlockDecorative.EnumType.ENDRITCH_BLOCK.getMeta()),
				new ItemStack(BlockInitializer.BLOCK_DECORATIVE, 1, BlockDecorative.EnumType.ENDRITCH_BLOCK.getMeta()),
				ItemStack.EMPTY,
				new ItemStack(Items.ENDER_PEARL),
				new ItemStack(Items.ENDER_PEARL),
				ItemStack.EMPTY
				);
		
		AssemblyRecipe recipeEndritchUpgrade = new AssemblyRecipe(new ItemStack(ItemInitializer.UPGRADE, 1, ItemUpgrade.EnumUpgradeType.ENDRITCH.getMeta()), 600,
				ENDRITCH_BLOCK_INPUT,
				ENDER_EYE_INPUT,
				NUGGET_ANCITE,
				ENDRITCH_BLOCK_INPUT,
				ENDER_EYE_INPUT,
				NUGGET_ANCITE
				);
		
		AssemblyRecipe recipeTeleporter = new AssemblyRecipe(new ItemStack(BlockInitializer.MACHINE_VARIANT, 1, BlockMachineVariant.EnumType.TELEPORTER.getMeta()), 800,
				ENDER_PEARL_INPUT,
				ENDRITCH_BLOCK_INPUT,
				new RecipeInputOreDic("nuggetEmerald"),
				EMPTY_INPUT,
				EMPTY_INPUT,
				EMPTY_INPUT
				);
		
		AssemblyRecipe recipeAncite = new AssemblyRecipe(new ItemStack(ItemInitializer.INGOT, 1, SWItemIngot.EnumVarietyMaterial.ANCITE.getMeta()), 1500,
				new RecipeInputOreDic("blockDiamond"),
				new RecipeInputOreDic("blockLapis"),
				EMPTY_INPUT,
				new RecipeInputOreDic("dustRedstone"),
				new RecipeInputOreDic("dustRedstone"),
				EMPTY_INPUT
				);
		
		AssemblyRecipe recipeIronPlate = new AssemblyRecipe(new ItemStack(ItemInitializer.GENERIC_MATERIAL, 1, ItemMaterial.EnumVarietyMaterial.IRON_PLATE.getMeta()), 50,
				INGOT_IRON,
				INGOT_IRON,
				INGOT_IRON,
				EMPTY_INPUT,
				EMPTY_INPUT,
				EMPTY_INPUT
				);
		
		AssemblyRecipe recipeIronPlateDouble = new AssemblyRecipe(new ItemStack(ItemInitializer.GENERIC_MATERIAL, 2, ItemMaterial.EnumVarietyMaterial.IRON_PLATE.getMeta()), 50,
				INGOT_IRON,
				INGOT_IRON,
				INGOT_IRON,
				INGOT_IRON,
				INGOT_IRON,
				INGOT_IRON
				);
		
		AssemblyRecipe recipeGoldPlate = new AssemblyRecipe(new ItemStack(ItemInitializer.GENERIC_MATERIAL, 1, ItemMaterial.EnumVarietyMaterial.GOLD_PLATE.getMeta()), 100,
				PLATE_IRON,
				INGOT_GOLD,
				EMPTY_INPUT,
				EMPTY_INPUT,
				EMPTY_INPUT,
				EMPTY_INPUT
				);
		
		AssemblyRecipe recipeGoldPlateDouble = new AssemblyRecipe(new ItemStack(ItemInitializer.GENERIC_MATERIAL, 2, ItemMaterial.EnumVarietyMaterial.GOLD_PLATE.getMeta()), 100,
				PLATE_IRON,
				INGOT_GOLD,
				PLATE_IRON,
				INGOT_GOLD,
				EMPTY_INPUT,
				EMPTY_INPUT
				);
		
		AssemblyRecipe recipeGoldPlateTriple = new AssemblyRecipe(new ItemStack(ItemInitializer.GENERIC_MATERIAL, 3, ItemMaterial.EnumVarietyMaterial.GOLD_PLATE.getMeta()), 100,
				PLATE_IRON,
				INGOT_GOLD,
				PLATE_IRON,
				INGOT_GOLD,
				PLATE_IRON,
				INGOT_GOLD
				);
		
		AssemblyRecipe recipeGalitePlate = new AssemblyRecipe(new ItemStack(ItemInitializer.GENERIC_MATERIAL, 1, ItemMaterial.EnumVarietyMaterial.GALITE_PLATE.getMeta()), 100,
				INGOT_GALITE,
				INGOT_GALITE,
				INGOT_GALITE,
				EMPTY_INPUT,
				EMPTY_INPUT,
				EMPTY_INPUT
				);
		
		AssemblyRecipe recipeGalitePlateDouble = new AssemblyRecipe(new ItemStack(ItemInitializer.GENERIC_MATERIAL, 2, ItemMaterial.EnumVarietyMaterial.GALITE_PLATE.getMeta()), 100,
				INGOT_GALITE,
				INGOT_GALITE,
				INGOT_GALITE,
				INGOT_GALITE,
				INGOT_GALITE,
				INGOT_GALITE
				);
		
		AssemblyRecipe recipeEssenPlate = new AssemblyRecipe(new ItemStack(ItemInitializer.GENERIC_MATERIAL, 1, ItemMaterial.EnumVarietyMaterial.ESSEN_PLATE.getMeta()), 150,
				PLATE_GALITE,
				INGOT_ESSEN,
				EMPTY_INPUT,
				EMPTY_INPUT,
				EMPTY_INPUT,
				EMPTY_INPUT
				);
		
		AssemblyRecipe recipeEssenPlateDouble = new AssemblyRecipe(new ItemStack(ItemInitializer.GENERIC_MATERIAL, 2, ItemMaterial.EnumVarietyMaterial.ESSEN_PLATE.getMeta()), 150,
				PLATE_GALITE,
				INGOT_ESSEN,
				PLATE_GALITE,
				INGOT_ESSEN,
				EMPTY_INPUT,
				EMPTY_INPUT
				);
		
		AssemblyRecipe recipeEssenPlateTriple = new AssemblyRecipe(new ItemStack(ItemInitializer.GENERIC_MATERIAL, 3, ItemMaterial.EnumVarietyMaterial.ESSEN_PLATE.getMeta()), 150,
				PLATE_GALITE,
				INGOT_ESSEN,
				PLATE_GALITE,
				INGOT_ESSEN,
				PLATE_GALITE,
				INGOT_ESSEN
				);
		
		AssemblyRecipe recipeDiamondPlate = new AssemblyRecipe(new ItemStack(ItemInitializer.GENERIC_MATERIAL, 1, ItemMaterial.EnumVarietyMaterial.DIAMOND_PLATE.getMeta()), 500,
				PLATE_ESSEN,
				DIAMOND,
				EMPTY_INPUT,
				EMPTY_INPUT,
				EMPTY_INPUT,
				EMPTY_INPUT
				);
		
		AssemblyRecipe recipeDiamondPlateDouble = new AssemblyRecipe(new ItemStack(ItemInitializer.GENERIC_MATERIAL, 2, ItemMaterial.EnumVarietyMaterial.DIAMOND_PLATE.getMeta()), 500,
				PLATE_ESSEN,
				DIAMOND,
				PLATE_ESSEN,
				DIAMOND,
				EMPTY_INPUT,
				EMPTY_INPUT
				);
		
		AssemblyRecipe recipeDiamondPlateTriple = new AssemblyRecipe(new ItemStack(ItemInitializer.GENERIC_MATERIAL, 3, ItemMaterial.EnumVarietyMaterial.DIAMOND_PLATE.getMeta()), 500,
				PLATE_ESSEN,
				DIAMOND,
				PLATE_ESSEN,
				DIAMOND,
				PLATE_ESSEN,
				DIAMOND
				);
		
		AssemblyRecipe recipeEssenUpgrade = new AssemblyRecipe(new ItemStack(ItemInitializer.UPGRADE, 1, ItemUpgrade.EnumUpgradeType.ESSEN.getMeta()), 1200,
				PLATE_ESSEN,
				ADVANCED_MACHINE_PARTS,
				PLATE_ESSEN,
				PLATE_DIAMOND,
				ADVANCED_MACHINE_PARTS,
				PLATE_DIAMOND
				);
	}
}
