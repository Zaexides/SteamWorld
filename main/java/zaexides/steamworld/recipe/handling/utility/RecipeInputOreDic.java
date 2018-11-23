package zaexides.steamworld.recipe.handling.utility;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeInputOreDic implements IRecipeInput
{
	public String oreDicName;
	
	public RecipeInputOreDic(String oreDicName) 
	{
		this.oreDicName = oreDicName;
	}
	
	@Override
	public boolean matchesItemStack(ItemStack itemStack) 
	{
		return OreDictionary.containsMatch(true, OreDictionary.getOres(oreDicName), itemStack);
	}

	@Override
	public boolean isEmpty() 
	{
		if(!OreDictionary.doesOreNameExist(oreDicName))
			return true;
		
		NonNullList<ItemStack> ores = OreDictionary.getOres(oreDicName);
		return ores == null || ores.size() == 0;
	}

	@Override
	public IRecipeInput copy() 
	{
		return new RecipeInputOreDic(oreDicName);
	}

	@Override
	public boolean isSame(IRecipeInput other) 
	{
		if(other instanceof RecipeInputOreDic)
			return ((RecipeInputOreDic) other).oreDicName.equals(oreDicName);
		else
		{
			if(OreDictionary.doesOreNameExist(oreDicName))
				return other.matchesItemStack(OreDictionary.getOres(oreDicName).get(0));
		}
		return false;
	}

	@Override
	public String toString() 
	{
		return oreDicName;
	}
}
