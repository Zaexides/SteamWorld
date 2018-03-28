package zaexides.steamworld.items.tools;

import net.minecraft.item.ItemSpade;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.init.ItemInitializer;
import zaexides.steamworld.utility.interfaces.IModeledObject;

public class SWItemShovel extends ItemSpade implements IModeledObject
{
	public SWItemShovel(String name, ToolMaterial material) 
	{
		super(material);
		setUnlocalizedName(ModInfo.MODID + "." + name);
		setRegistryName(name);
		setCreativeTab(SteamWorld.CREATIVETAB);
		
		ItemInitializer.ITEMS.add(this);
	}

	@Override
	public void RegisterModels()
	{
		SteamWorld.proxy.RegisterItemRenderers(this, 0, "inventory", "tools/" + getRegistryName().getResourcePath());
	}
}
