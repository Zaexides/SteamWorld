package zaexides.steamworld.recipe.handling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import zaexides.steamworld.ConfigHandler;
import zaexides.steamworld.SteamWorld;

public class DustRecipeHandler 
{
	private static Map<String, ItemStack> recipes = new HashMap<String, ItemStack>();
	public static List<ItemStack> inputs = new ArrayList<ItemStack>();
	
	public static void RegisterRecipe(ItemStack input, ItemStack output)
	{
		String registerName = input.getItem().getRegistryName() + "<" + input.getMetadata() + ">";
		String outputName = output.getItem().getRegistryName() + "<" + output.getMetadata() + ">";
		
		if(ConfigHandler.grinderBlacklist.contains(registerName) || ConfigHandler.grinderBlacklist.contains(outputName))
		{
			SteamWorld.logger.log(Level.INFO, "Skipped " + registerName + " > " + outputName + " as it is blacklisted.");
			return;
		}
		
		ItemStack itemStack = new ItemStack(input.getItem(), 1, input.getMetadata());
		
		if(recipes.containsKey(registerName))
			return;
		
		SteamWorld.logger.log(Level.INFO, "Registering dust recipe " + registerName + " > " + outputName);
		recipes.put(registerName, output);
		inputs.add(input);
	}
	
	public static ItemStack GetResult(ItemStack input)
	{
		String registerName = input.getItem().getRegistryName() + "<" + input.getMetadata() + ">";
		if(recipes.containsKey(registerName))
			return recipes.get(registerName).copy();
		return ItemStack.EMPTY;
	}
	
	public static List<ItemStack> GetInputs()
	{
		return inputs;
	}
}
