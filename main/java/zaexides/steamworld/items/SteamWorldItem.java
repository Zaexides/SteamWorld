package zaexides.steamworld.items;

import akka.Main;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.utility.interfaces.IModeledObject;

public class SteamWorldItem extends Item implements IModeledObject
{
	public SteamWorldItem(String name) 
	{
		super();
		Construct(name);
	}
	
	private void Construct(String name)
	{
		setUnlocalizedName(ModInfo.MODID + "." + name);
		setRegistryName(name);
		setCreativeTab(SteamWorld.CREATIVETAB);
		
		ItemInitializer.ITEMS.add(this);
	}
	
	@Override
	public void RegisterModels()
	{
		SteamWorld.proxy.RegisterItemRenderer(this, 0, "inventory");
	}
	
}
