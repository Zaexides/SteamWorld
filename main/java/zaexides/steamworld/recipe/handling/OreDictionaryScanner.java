package zaexides.steamworld.recipe.handling;


import java.util.List;

import org.apache.logging.log4j.Level;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import zaexides.steamworld.SteamWorld;

public class OreDictionaryScanner 
{
	public static void ScanOreDictionary()
	{
		String[] oreNames = OreDictionary.getOreNames();
		
		for(String oreName : oreNames)
		{
			RegisterDust(oreName);
		}
	}
	
	private static void RegisterDust(String oreName)
	{
		if(oreName.contains("dust"))
		{
			String materialName = oreName.substring(4);
			List<ItemStack> dusts = OreDictionary.getOres(oreName);
			List<ItemStack> ingots = OreDictionary.getOres("ingot" + materialName);
			List<ItemStack> ores = OreDictionary.getOres("ore" + materialName);
			
			for(ItemStack d : dusts)
			{
				for(ItemStack i : ingots)
					DustRecipeHandler.RegisterRecipe(i, d);
				for(ItemStack o : ores)
					DustRecipeHandler.RegisterRecipe(o, new ItemStack(d.getItem(), 2, d.getMetadata()));
			}
		}
	}
}
