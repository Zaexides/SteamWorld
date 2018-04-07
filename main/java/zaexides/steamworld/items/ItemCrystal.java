package zaexides.steamworld.items;

import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;

public class ItemCrystal extends SteamWorldItem
{
	public ItemCrystal(String name, int uses) 
	{
		super(name);
		setMaxDamage(uses);
		setMaxStackSize(1);
	}
	
	public static void damageCrystal(ItemStack stack, Random random) //Custom calculation because minecraft what?
	{
		if(stack.isItemEnchanted())
		{
			int unbLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack);
			if(random.nextInt(5) > unbLevel)
				stack.setItemDamage(stack.getItemDamage() + 1);
		}
		else
			stack.setItemDamage(stack.getItemDamage() + 1);
	}
}
