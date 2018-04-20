package zaexides.steamworld.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import zaexides.steamworld.LootTableInitializer;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.gui.GuiHandler;
import zaexides.steamworld.utility.interfaces.IModeledObject;

public class ItemBookManual extends SteamWorldItem implements IModeledObject
{
	protected String name;
	
	public ItemBookManual(String name) 
	{
		super(name);
		this.name = name;
		setMaxDamage(0);
		setMaxStackSize(1);
	}
	
	@Override
	public void RegisterModels()
	{
		for(int i = 0; i < EnumBook.values().length; i++)
		{
			SteamWorld.proxy.RegisterItemRenderers(this, i, "inventory", "book/" + name + "_" + EnumBook.values()[i].getName());
		}
	}
	
	@Override
	public int getMetadata(int damage) 
	{
		return damage;
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) 
	{
		for(EnumBook item$material : EnumBook.values())
		{
			if(isInCreativeTab(tab))
				items.add(new ItemStack(this, 1, item$material.getMeta()));
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		playerIn.openGui(SteamWorld.singleton, GuiHandler.MANUAL_STEAITE, worldIn, (int)playerIn.posX, (int)playerIn.posY, (int)playerIn.posZ);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}
	
	public static enum EnumBook implements IStringSerializable
	{
		STEAITE_MANUAL(0, "steaite");
		
		private final int meta;
		private final String name;
		private static final EnumBook[] META_LOOKUP = new EnumBook[values().length];
		
		private EnumBook(int meta, String name)
		{
			this.meta = meta;
			this.name = name;
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
		
		public static EnumBook byMetadata(int meta)
		{
			return META_LOOKUP[meta % values().length];
		}
		
		static
		{
			for(EnumBook item$materialtype : values())
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
