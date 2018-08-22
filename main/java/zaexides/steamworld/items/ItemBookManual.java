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
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.gui.GuiHandler;
import zaexides.steamworld.init.LootTableInitializer;
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
		setHasSubtypes(true);
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
	public String getUnlocalizedName(ItemStack stack) 
	{
		int metadata = stack.getMetadata();
		EnumBook material = EnumBook.byMetadata(metadata);
		return super.getUnlocalizedName() + "_" + material.getName();
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack itemStack = playerIn.getHeldItem(handIn);
		
		playerIn.openGui(SteamWorld.singleton, EnumBook.byMetadata(itemStack.getMetadata()).gui, worldIn, (int)playerIn.posX, (int)playerIn.posY, (int)playerIn.posZ);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);
	}
	
	public static enum EnumBook implements IStringSerializable
	{
		STEAITE_MANUAL(0, "steaite", GuiHandler.MANUAL_STEAITE),
		ANCITE_MANUAL(1, "ancite", GuiHandler.MANUAL_ANCITE),
		ENDRITCH_MANUAL(2, "endritch", GuiHandler.MANUAL_ENDRITCH),
		ESSEN_MANUAL(3, "essen", GuiHandler.MANUAL_ESSEN);
		
		private final int meta;
		private final String name;
		private final int gui;
		private static final EnumBook[] META_LOOKUP = new EnumBook[values().length];
		
		private EnumBook(int meta, String name, int gui)
		{
			this.meta = meta;
			this.name = name;
			this.gui = gui;
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
