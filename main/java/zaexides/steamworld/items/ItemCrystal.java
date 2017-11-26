package zaexides.steamworld.items;

public class ItemCrystal extends SteamWorldItem
{
	public ItemCrystal(String name, int uses) 
	{
		super(name);
		setMaxDamage(uses);
		setMaxStackSize(1);
	}
}
