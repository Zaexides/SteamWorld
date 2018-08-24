package zaexides.steamworld.recipe.handling;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

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
			List<ItemStack> dustStacks = OreDictionary.getOres(oreName);
			if(dustStacks.size() == 0)
				return;
			
			String materialName = oreName.substring(4);
			ItemStack dust = dustStacks.get(0);
			
			if(OreDictionary.doesOreNameExist("ingot" + materialName))
				DustRecipeHandler.RegisterRecipe("ingot" + materialName, dust);
			if(OreDictionary.doesOreNameExist("ore" + materialName))
			{
				DustRecipe dustRecipe = DustRecipeHandler.RegisterRecipe("ore" + materialName, new ItemStack(dust.getItem(), 1, dust.getMetadata()));
				if(dustRecipe != null)
					dustRecipe.setAffectedByLevel(true);
			}
			if(OreDictionary.doesOreNameExist("block" + materialName))
				DustRecipeHandler.RegisterRecipe("block" + materialName, new ItemStack(dust.getItem(), 9, dust.getMetadata()));
		}
	}
}
