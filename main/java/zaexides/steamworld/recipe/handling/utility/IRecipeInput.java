package zaexides.steamworld.recipe.handling.utility;

import org.apache.logging.log4j.Level;

import net.minecraft.item.ItemStack;
import zaexides.steamworld.SteamWorld;

public interface IRecipeInput 
{
	public boolean matchesItemStack(ItemStack itemStack);
	public boolean isEmpty();
	public IRecipeInput copy();
	
	public boolean isSame(IRecipeInput other);
}
