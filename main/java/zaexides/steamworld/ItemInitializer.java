package zaexides.steamworld;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import scala.reflect.internal.Trees.New;
import zaexides.steamworld.items.ItemCrystal;
import zaexides.steamworld.items.ItemDust;
import zaexides.steamworld.items.ItemUpgrade;
import zaexides.steamworld.items.ItemWrench;
import zaexides.steamworld.items.SWItemIngot;
import zaexides.steamworld.items.SWItemNugget;
import zaexides.steamworld.items.SteamWorldItem;

public class ItemInitializer 
{
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	public static final SWItemIngot INGOT_STEAITE = new SWItemIngot("ingot_steaite", "ingotSteaite");
	public static final SWItemIngot INGOT_ANCITE = new SWItemIngot("ingot_ancite", "ingotAncite");
	public static final SWItemIngot SHARD_ENDRITCH = new SWItemIngot("shard_endritch", "ingotEndritch");
	public static final SteamWorldItem MACHINE_BASE = new SteamWorldItem("machine_base");
	public static final SteamWorldItem STEAITE_CRYSTAL = new ItemCrystal("steaite_crystal", 16);
	
	public static final ItemWrench WRENCH = new ItemWrench("wrench");
	
	public static final ItemDust METAL_DUST = new ItemDust("dust");
	public static final SWItemNugget ITEM_NUGGET = new SWItemNugget("nugget");
	public static final ItemUpgrade UPGRADE = new ItemUpgrade("upgrade");
}