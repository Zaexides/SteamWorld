package zaexides.steamworld.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.BlockAncite;
import zaexides.steamworld.utility.interfaces.IModeledObject;
import zaexides.steamworld.utility.interfaces.IOreDictionaryRegisterable;

public class ItemMaterial extends SteamWorldItem implements IOreDictionaryRegisterable, IModeledObject
{
	protected String name;
	
	public ItemMaterial(String name) 
	{
		super(name);
		this.name = name;
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public void RegisterOreInDictionary()
	{
		for(EnumVarietyMaterial item$material : EnumVarietyMaterial.values())
		{
			String[] oreNames = item$material.getOreNames();
			if(oreNames != null)
			{
				for(String oreName : oreNames)
					OreDictionary.registerOre(oreName, new ItemStack(this, 1, item$material.getMeta()));
			}
		}
	}
	
	@Override
	public int getMetadata(int damage) 
	{
		return damage;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) 
	{
		int metadata = stack.getMetadata();
		EnumVarietyMaterial material = EnumVarietyMaterial.byMetadata(metadata);
		return super.getUnlocalizedName() + "_" + material.getName();
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) 
	{
		for(EnumVarietyMaterial item$material : EnumVarietyMaterial.values())
		{
			if(isInCreativeTab(tab))
				items.add(new ItemStack(this, 1, item$material.getMeta()));
		}
	}
	
	@Override
	public void RegisterModels()
	{
		for(int i = 0; i < EnumVarietyMaterial.values().length; i++)
		{
			SteamWorld.proxy.RegisterItemRenderers(this, i, "inventory", "material/" + EnumVarietyMaterial.values()[i].getName());
		}
	}
	
	public static enum EnumVarietyMaterial implements IStringSerializable
	{
		ESSEN_PLATE(0, "plate_essen", "plateEssen", "compressedEssen"),
		ADVANCED_MACHINE_PARTS(1, "advanced_machine_parts"),
		DIAMOND_PLATE(2, "plate_diamond", "plateDiamond", "compressedDiamond"),
		GALITE_PLATE(3, "plate_galite", "plateGalite", "compressedGalite"),
		IRON_PLATE(4, "plate_iron", "plateIron", "compressedIron"),
		GOLD_PLATE(5, "plate_gold", "plateGold", "compressedGold");
		
		private final int meta;
		private final String name;
		private final String[] oreNames;
		private static final EnumVarietyMaterial[] META_LOOKUP = new EnumVarietyMaterial[values().length];
		
		private EnumVarietyMaterial(int meta, String name, String... oreNames)
		{
			this.meta = meta;
			this.name = name;
			this.oreNames = oreNames;
		}
		
		public int getMeta()
		{
			return this.meta;
		}
		
		@Override
		public String toString() 
		{
			return this.name;
		}
		
		public String[] getOreNames()
		{
			return oreNames;
		}
		
		public static EnumVarietyMaterial byMetadata(int meta)
		{
			return META_LOOKUP[meta % values().length];
		}
		
		static
		{
			for(ItemMaterial.EnumVarietyMaterial item$materialtype : values())
			{
				META_LOOKUP[item$materialtype.getMeta()] = item$materialtype;
			}
		}

		@Override
		public String getName() 
		{
			return this.name;
		}
	}
}
