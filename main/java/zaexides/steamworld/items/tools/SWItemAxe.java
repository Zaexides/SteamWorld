package zaexides.steamworld.items.tools;

import net.minecraft.item.ItemAxe;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.init.ItemInitializer;
import zaexides.steamworld.utility.interfaces.IModeledObject;

public class SWItemAxe extends ItemAxe implements IModeledObject
{
	public SWItemAxe(String name, ToolMaterial material, float damage, float speed)
	{
		super(material, damage, speed);
		setUnlocalizedName(ModInfo.MODID + "." + name);
		setRegistryName(name);
		setCreativeTab(SteamWorld.CREATIVETAB);
		
		ItemInitializer.ITEMS.add(this);
	}
	
	public SWItemAxe(String name, ToolMaterial material) 
	{
		this(name, material, 8.0f, -3.0f);
	}

	@Override
	public void RegisterModels()
	{
		SteamWorld.proxy.RegisterItemRenderers(this, 0, "inventory", "tools/" + getRegistryName().getResourcePath());
	}
}
