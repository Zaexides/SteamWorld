package zaexides.steamworld.items.tools;

import net.minecraft.item.ItemHoe;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.init.ItemInitializer;
import zaexides.steamworld.utility.interfaces.IModeledObject;

public class SWItemHoe extends ItemHoe implements IModeledObject
{
	public SWItemHoe(String name, ToolMaterial material) 
	{
		super(material);
		setUnlocalizedName(ModInfo.MODID + "." + name);
		setRegistryName(name);
		setCreativeTab(SteamWorld.CREATIVETAB_UTILITY);
		
		ItemInitializer.ITEMS.add(this);
	}

	@Override
	public void RegisterModels()
	{
		SteamWorld.proxy.RegisterItemRenderers(this, 0, "inventory", "tools/" + getRegistryName().getResourcePath());
	}
}
