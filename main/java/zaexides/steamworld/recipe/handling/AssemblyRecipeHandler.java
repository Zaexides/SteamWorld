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
import zaexides.steamworld.BlockInitializer;
import zaexides.steamworld.ItemInitializer;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.BlockDecorative;
import zaexides.steamworld.items.SWItemNugget;

public class AssemblyRecipeHandler 
{
	public static List<AssemblyRecipe> recipes = new ArrayList<AssemblyRecipe>();
	
	public static AssemblyRecipe getOutput(List<ItemStack> inputs)
	{
		for(AssemblyRecipe recipe : recipes)
		{
			if(recipe.isRecipe(inputs))
				return recipe.Copy();
		}
		return null;
	}
	
	public static void RegisterRecipes()
	{
		AssemblyRecipe recipeDiamond = new AssemblyRecipe(new ItemStack(Items.DIAMOND), 3000,
				new ItemStack(Blocks.COAL_BLOCK),new ItemStack(Blocks.COAL_BLOCK),
				new ItemStack(Blocks.COAL_BLOCK),new ItemStack(Blocks.COAL_BLOCK),
				new ItemStack(Blocks.COAL_BLOCK),new ItemStack(Blocks.COAL_BLOCK)
				);
		
		AssemblyRecipe recipeEndritch = new AssemblyRecipe(new ItemStack(ItemInitializer.SHARD_ENDRITCH), 500,
				new ItemStack(Items.ENDER_PEARL), new ItemStack(ItemInitializer.ITEM_NUGGET, 1, SWItemNugget.EnumVarietyMaterial.ANCITE.getMeta()),
				new ItemStack(Items.CHORUS_FRUIT), new ItemStack(Items.ENDER_PEARL),
				new ItemStack(ItemInitializer.ITEM_NUGGET, 1, SWItemNugget.EnumVarietyMaterial.ANCITE.getMeta()), new ItemStack(Items.CHORUS_FRUIT)
				);
		
		AssemblyRecipe recipeEndritchGenerator = new AssemblyRecipe(new ItemStack(BlockInitializer.GENERATOR_ENDRITCH), 600,
				new ItemStack(BlockInitializer.GENERATOR_ANCITE),
				new ItemStack(BlockInitializer.BLOCK_DECORATIVE, 1, BlockDecorative.EnumType.ENDRITCH_BLOCK.getMeta()),
				ItemStack.EMPTY,
				new ItemStack(Items.ENDER_EYE),
				new ItemStack(BlockInitializer.BLOCK_DECORATIVE, 1, BlockDecorative.EnumType.ENDRITCH_BLOCK.getMeta()),
				ItemStack.EMPTY
				);
		
		AssemblyRecipe recipeEndritchValve = new AssemblyRecipe(new ItemStack(BlockInitializer.BLOCK_VALVE_ENDRITCH), 600,
				new ItemStack(BlockInitializer.BLOCK_VALVE),
				new ItemStack(BlockInitializer.BLOCK_DECORATIVE, 1, BlockDecorative.EnumType.ENDRITCH_BLOCK.getMeta()),
				ItemStack.EMPTY,
				new ItemStack(Items.ENDER_EYE),
				new ItemStack(BlockInitializer.BLOCK_DECORATIVE, 1, BlockDecorative.EnumType.ENDRITCH_BLOCK.getMeta()),
				ItemStack.EMPTY
				);
		
		AssemblyRecipe recipeEndritchFertilizer = new AssemblyRecipe(new ItemStack(BlockInitializer.FERTILIZER_ENDRITCH), 600,
				new ItemStack(BlockInitializer.FERTILIZER_ANCITE),
				new ItemStack(BlockInitializer.BLOCK_DECORATIVE, 1, BlockDecorative.EnumType.ENDRITCH_BLOCK.getMeta()),
				ItemStack.EMPTY,
				new ItemStack(Items.ENDER_EYE),
				new ItemStack(BlockInitializer.BLOCK_DECORATIVE, 1, BlockDecorative.EnumType.ENDRITCH_BLOCK.getMeta()),
				ItemStack.EMPTY
				);
		
		AssemblyRecipe recipeEndritchFarmer = new AssemblyRecipe(new ItemStack(BlockInitializer.FARMER_ENDRITCH), 600,
				new ItemStack(BlockInitializer.FARMER_ANCITE),
				new ItemStack(BlockInitializer.BLOCK_DECORATIVE, 1, BlockDecorative.EnumType.ENDRITCH_BLOCK.getMeta()),
				ItemStack.EMPTY,
				new ItemStack(Items.ENDER_EYE),
				new ItemStack(BlockInitializer.BLOCK_DECORATIVE, 1, BlockDecorative.EnumType.ENDRITCH_BLOCK.getMeta()),
				ItemStack.EMPTY
				);
		
		AssemblyRecipe recipeEndritchLumber = new AssemblyRecipe(new ItemStack(BlockInitializer.LUMBER_ENDRITCH), 600,
				new ItemStack(BlockInitializer.LUMBER_ANCITE),
				new ItemStack(BlockInitializer.BLOCK_DECORATIVE, 1, BlockDecorative.EnumType.ENDRITCH_BLOCK.getMeta()),
				ItemStack.EMPTY,
				new ItemStack(Items.ENDER_EYE),
				new ItemStack(BlockInitializer.BLOCK_DECORATIVE, 1, BlockDecorative.EnumType.ENDRITCH_BLOCK.getMeta()),
				ItemStack.EMPTY
				);
		
		AssemblyRecipe recipeEndritchFisher = new AssemblyRecipe(new ItemStack(BlockInitializer.FISHER_ENDRITCH), 600,
				new ItemStack(BlockInitializer.FISHER_ANCITE),
				new ItemStack(BlockInitializer.BLOCK_DECORATIVE, 1, BlockDecorative.EnumType.ENDRITCH_BLOCK.getMeta()),
				ItemStack.EMPTY,
				new ItemStack(Items.ENDER_EYE),
				new ItemStack(BlockInitializer.BLOCK_DECORATIVE, 1, BlockDecorative.EnumType.ENDRITCH_BLOCK.getMeta()),
				ItemStack.EMPTY
				);
		
		AssemblyRecipe recipeEndritchGrinder = new AssemblyRecipe(new ItemStack(BlockInitializer.GRINDER_ENDRITCH), 600,
				new ItemStack(BlockInitializer.GRINDER_ANCITE),
				new ItemStack(BlockInitializer.BLOCK_DECORATIVE, 1, BlockDecorative.EnumType.ENDRITCH_BLOCK.getMeta()),
				ItemStack.EMPTY,
				new ItemStack(Items.ENDER_EYE),
				new ItemStack(BlockInitializer.BLOCK_DECORATIVE, 1, BlockDecorative.EnumType.ENDRITCH_BLOCK.getMeta()),
				ItemStack.EMPTY
				);
		
		AssemblyRecipe recipeEndritchFurnace = new AssemblyRecipe(new ItemStack(BlockInitializer.FURNACE_ENDRITCH), 600,
				new ItemStack(BlockInitializer.FURNACE_ANCITE),
				new ItemStack(BlockInitializer.BLOCK_DECORATIVE, 1, BlockDecorative.EnumType.ENDRITCH_BLOCK.getMeta()),
				ItemStack.EMPTY,
				new ItemStack(Items.ENDER_EYE),
				new ItemStack(BlockInitializer.BLOCK_DECORATIVE, 1, BlockDecorative.EnumType.ENDRITCH_BLOCK.getMeta()),
				ItemStack.EMPTY
				);
		
		AssemblyRecipe recipeEndritchAssembler = new AssemblyRecipe(new ItemStack(BlockInitializer.ASSEMBLER_ENDRITCH), 600,
				new ItemStack(BlockInitializer.ASSEMBLER_ANCITE),
				new ItemStack(BlockInitializer.BLOCK_DECORATIVE, 1, BlockDecorative.EnumType.ENDRITCH_BLOCK.getMeta()),
				ItemStack.EMPTY,
				new ItemStack(Items.ENDER_EYE),
				new ItemStack(BlockInitializer.BLOCK_DECORATIVE, 1, BlockDecorative.EnumType.ENDRITCH_BLOCK.getMeta()),
				ItemStack.EMPTY
				);
		
		AssemblyRecipe recipeEndritchExperienceMachine = new AssemblyRecipe(new ItemStack(BlockInitializer.EXPERIENCE_MACHINE_ENDRITCH), 600,
				new ItemStack(BlockInitializer.EXPERIENCE_MACHINE_ANCITE),
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
		
		AssemblyRecipe recipeAncite = new AssemblyRecipe(new ItemStack(ItemInitializer.INGOT_ANCITE), 1500,
				new ItemStack(Blocks.DIAMOND_BLOCK),
				new ItemStack(Blocks.LAPIS_BLOCK),
				ItemStack.EMPTY,
				new ItemStack(Items.REDSTONE),
				new ItemStack(Items.REDSTONE),
				ItemStack.EMPTY
				);
	}
}
