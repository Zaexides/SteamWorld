package zaexides.steamworld.items;

import net.minecraftforge.oredict.OreDictionary;
import zaexides.steamworld.ItemInitializer;
import zaexides.steamworld.utility.IOreDictionaryRegisterable;

public class SWItemIngot extends SteamWorldItem implements IOreDictionaryRegisterable
{
	private String oreName;
	
	public SWItemIngot(String name, String oreName)
	{
		super(name);
		this.oreName = oreName;
	}

	@Override
	public void RegisterOreInDictionary() 
	{
		OreDictionary.registerOre(oreName, this);
	}
}
