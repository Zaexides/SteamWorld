package zaexides.steamworld.items;

import net.minecraftforge.oredict.OreDictionary;
import zaexides.steamworld.utility.interfaces.IOreDictionaryRegisterable;

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
