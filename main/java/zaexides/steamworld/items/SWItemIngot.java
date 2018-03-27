package zaexides.steamworld.items;

import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.BlockAncite;
import zaexides.steamworld.utility.interfaces.IModeledObject;
import zaexides.steamworld.utility.interfaces.IOreDictionaryRegisterable;

public class SWItemIngot extends SteamWorldItem implements IModeledObject, IOreDictionaryRegisterable
{
	protected String name;
	
	public SWItemIngot(String name) 
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
			String oreName = name;
			oreName = oreName + item$material.getOreName();
			OreDictionary.registerOre(oreName, new ItemStack(this, 1, item$material.getMeta()));
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
			SteamWorld.proxy.RegisterItemRenderers(this, i, "inventory", "ingot/" + name + "_" + EnumVarietyMaterial.values()[i].getName());
		}
	}
	
	public static enum EnumVarietyMaterial implements IStringSerializable
	{
		STEAITE(0, "steaite", "Steaite"),
		ANCITE(1, "ancite", "Ancite"),
		ENDRITCH(2, "endritch", "Endritch"),
		GALITE(3, "galite", "Galite");
		
		private final int meta;
		private final String name, oreName;
		private static final EnumVarietyMaterial[] META_LOOKUP = new EnumVarietyMaterial[values().length];
		
		private EnumVarietyMaterial(int meta, String name, String oreName)
		{
			this.meta = meta;
			this.name = name;
			this.oreName = oreName;
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
		
		public String getOreName()
		{
			return oreName;
		}
		
		public static EnumVarietyMaterial byMetadata(int meta)
		{
			return META_LOOKUP[meta % values().length];
		}
		
		static
		{
			for(EnumVarietyMaterial item$materialtype : values())
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
