package zaexides.steamworld.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.init.ItemInitializer;
import zaexides.steamworld.utility.interfaces.IModeledObject;

public class SteamWorldItem extends Item implements IModeledObject
{
	protected int burnTime = -1;
	
	public SteamWorldItem(String name)
	{
		super();
		Construct(name);
	}
	
	public SteamWorldItem(String name, CreativeTabs creativeTabs) 
	{
		super();
		Construct(name, creativeTabs);
	}
	
	private void Construct(String name)
	{
		Construct(name, SteamWorld.CREATIVETAB_ITEMS);
	}
	
	private void Construct(String name, CreativeTabs creativeTabs)
	{
		setUnlocalizedName(ModInfo.MODID + "." + name);
		setRegistryName(name);
		setCreativeTab(creativeTabs);
		
		ItemInitializer.ITEMS.add(this);
	}
	
	@Override
	public void RegisterModels()
	{
		SteamWorld.proxy.RegisterItemRenderer(this, 0, "inventory");
	}
	
	public SteamWorldItem SetBurnTime(int burnTime)
	{
		this.burnTime = burnTime;
		return this;
	}
	
	@Override
	public int getItemBurnTime(ItemStack itemStack) 
	{
		return burnTime;
	}
}
