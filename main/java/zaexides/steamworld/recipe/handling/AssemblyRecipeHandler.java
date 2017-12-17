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
import zaexides.steamworld.blocks.machines.BlockMachineVariant;
import zaexides.steamworld.items.ItemUpgrade;
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
				new ItemStack(BlockInitializer.BLOCK_DECORATIVE, 1, BlockDecorative.EnumType.ENDRITCH_BLOCK.getMeta()),
				new ItemStack(Items.ENDER_EYE),
				new ItemStack(ItemInitializer.ITEM_NUGGET, 1, SWItemNugget.EnumVarietyMaterial.ANCITE.getMeta()),
				new ItemStack(BlockInitializer.BLOCK_DECORATIVE, 1, BlockDecorative.EnumType.ENDRITCH_BLOCK.getMeta()),
				new ItemStack(Items.ENDER_EYE),
				new ItemStack(ItemInitializer.ITEM_NUGGET, 1, SWItemNugget.EnumVarietyMaterial.ANCITE.getMeta())
				);
		
		AssemblyRecipe recipeTeleporter = new AssemblyRecipe(new ItemStack(BlockInitializer.MACHINE_VARIANT, 1, BlockMachineVariant.EnumType.TELEPORTER.getMeta()), 800,
				new ItemStack(Items.ENDER_PEARL),
				new ItemStack(BlockInitializer.BLOCK_DECORATIVE, 1, BlockDecorative.EnumType.ENDRITCH_BLOCK.getMeta()),
				new ItemStack(ItemInitializer.ITEM_NUGGET, 1, SWItemNugget.EnumVarietyMaterial.EMERALD.getMeta()),
				ItemStack.EMPTY,
				ItemStack.EMPTY,
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
