package zaexides.steamworld.items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import zaexides.steamworld.ItemInitializer;
import zaexides.steamworld.items.SWItemVariant.EnumVarietyMaterial;
import zaexides.steamworld.utility.IOreDictionaryRegisterable;

public class ItemDust extends SWItemVariant implements IOreDictionaryRegisterable
{
	public ItemDust(String name) 
	{
		super(name);
	}

	@Override
	public void RegisterOreInDictionary()
	{
		for(EnumVarietyMaterial item$material : EnumVarietyMaterial.values())
		{
			String oreName = name;
			oreName = oreName + item$material.getOreName();
			OreDictionary.registerOre(oreName, new ItemStack(this, 1, item$material.getMeta()));
		}
	}
}
