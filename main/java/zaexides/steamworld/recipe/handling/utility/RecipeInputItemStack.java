package zaexides.steamworld.recipe.handling.utility;

import net.minecraft.item.ItemStack;

public class RecipeInputItemStack implements IRecipeInput
{
	public ItemStack itemStack;
	
	public RecipeInputItemStack(ItemStack itemStack) 
	{
		this.itemStack = itemStack;
	}
	
	@Override
	public boolean matchesItemStack(ItemStack inputItemStack) 
	{
		return inputItemStack.getItem() == itemStack.getItem() && inputItemStack.getMetadata() == itemStack.getMetadata();
	}
	
	@Override
	public boolean isEmpty()
	{
		return itemStack.isEmpty();
	}

	@Override
	public IRecipeInput copy() 
	{
		return new RecipeInputItemStack(itemStack);
	}

	@Override
	public boolean isSame(IRecipeInput other) 
	{
		return other.matchesItemStack(itemStack);
	}
	
	@Override
	public String toString() 
	{
		return itemStack.toString();
	}
}
