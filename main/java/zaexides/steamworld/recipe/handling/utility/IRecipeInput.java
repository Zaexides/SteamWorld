package zaexides.steamworld.recipe.handling.utility;

import net.minecraft.item.ItemStack;

public interface IRecipeInput 
{
	public boolean matchesItemStack(ItemStack itemStack);
	public boolean isEmpty();
	public IRecipeInput copy();
	
	public boolean isSame(IRecipeInput other);
}
