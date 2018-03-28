package zaexides.steamworld.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import scala.reflect.internal.Trees.New;
import zaexides.steamworld.items.ItemCrystal;
import zaexides.steamworld.items.ItemDust;
import zaexides.steamworld.items.ItemTreasure;
import zaexides.steamworld.items.ItemUpgrade;
import zaexides.steamworld.items.ItemWrench;
import zaexides.steamworld.items.SWItemIngot;
import zaexides.steamworld.items.SWItemIngotLegacy;
import zaexides.steamworld.items.SWItemNugget;
import zaexides.steamworld.items.SteamWorldItem;
import zaexides.steamworld.items.tools.SWItemAxe;
import zaexides.steamworld.items.tools.SWItemHoe;
import zaexides.steamworld.items.tools.SWItemPickaxe;
import zaexides.steamworld.items.tools.SWItemShovel;
import zaexides.steamworld.items.tools.SWItemSword;
import zaexides.steamworld.items.tools.SteamWorldToolMaterial;

public class ItemInitializer 
{
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	public static final SWItemIngotLegacy INGOT_STEAITE = new SWItemIngotLegacy("ingot_steaite", "ingotSteaite", SWItemIngot.EnumVarietyMaterial.STEAITE);
	public static final SWItemIngotLegacy INGOT_ANCITE = new SWItemIngotLegacy("ingot_ancite", "ingotAncite", SWItemIngot.EnumVarietyMaterial.ANCITE);
	public static final SWItemIngotLegacy SHARD_ENDRITCH = new SWItemIngotLegacy("shard_endritch", "ingotEndritch", SWItemIngot.EnumVarietyMaterial.ENDRITCH);
	public static final SteamWorldItem MACHINE_BASE = new SteamWorldItem("machine_base");
	public static final SteamWorldItem STEAITE_CRYSTAL = new ItemCrystal("steaite_crystal", 16);
	
	public static final SteamWorldItem TREASURE = new ItemTreasure("treasure");
	
	public static final ItemWrench WRENCH = new ItemWrench("wrench");
	
	public static final SWItemIngot INGOT = new SWItemIngot("ingot");
	public static final ItemDust METAL_DUST = new ItemDust("dust");
	public static final SWItemNugget ITEM_NUGGET = new SWItemNugget("nugget");
	public static final ItemUpgrade UPGRADE = new ItemUpgrade("upgrade");
	
	
	/* ==Tools== */
	public static final Item PICKAXE_GALITE = new SWItemPickaxe("pickaxe_galite", SteamWorldToolMaterial.GALITE);
	public static final Item AXE_GALITE = new SWItemAxe("axe_galite", SteamWorldToolMaterial.GALITE);
	public static final Item HOE_GALITE = new SWItemHoe("hoe_galite", SteamWorldToolMaterial.GALITE);
	public static final Item SHOVEL_GALITE = new SWItemShovel("shovel_galite", SteamWorldToolMaterial.GALITE);
	public static final Item SWORD_GALITE = new SWItemSword("sword_galite", SteamWorldToolMaterial.GALITE);
}