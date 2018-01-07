package zaexides.steamworld.items;

import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.Level;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import zaexides.steamworld.LootTableInitializer;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.utility.interfaces.IModeledObject;

public class ItemTreasure extends SteamWorldItem implements IModeledObject
{
	protected String name;
	
	public ItemTreasure(String name) 
	{
		super(name);
		this.name = name;
		setMaxDamage(0);
		setMaxStackSize(1);
	}
	
	@Override
	public void RegisterModels()
	{
		for(int i = 0; i < EnumTreasure.values().length; i++)
		{
			SteamWorld.proxy.RegisterItemRenderers(this, i, "inventory", "treasure/" + name + "_" + EnumTreasure.values()[i].getName());
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
		for(EnumTreasure item$material : EnumTreasure.values())
		{
			if(isInCreativeTab(tab))
				items.add(new ItemStack(this, 1, item$material.getMeta()));
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) 
	{		
		if(!worldIn.isRemote)
		{
			ItemStack currentStack = playerIn.getHeldItem(handIn);
			ResourceLocation lootTableResourceLocation = EnumTreasure.byMetadata(currentStack.getMetadata()).getLootTable();
			LootTable lootTable = worldIn.getLootTableManager().getLootTableFromLocation(lootTableResourceLocation);
			LootContext.Builder contextBuilder = new LootContext.Builder((WorldServer) worldIn);
			
			contextBuilder.withLuck(playerIn.getLuck());

			List<ItemStack> stacks = lootTable.generateLootForPools(itemRand, contextBuilder.build());
			for(ItemStack stack : stacks)
			{
				if(!playerIn.inventory.addItemStackToInventory(stack))
				{
					InventoryHelper.spawnItemStack(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, stack);
				}
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, ItemStack.EMPTY);
	}
	
	public static enum EnumTreasure implements IStringSerializable
	{
		FISH(0, "fish", LootTableInitializer.TREASURE_BOX_FISHING),
		DUNGEON(1, "dungeon", LootTableInitializer.TREASURE_BOX_DUNGEON);
		
		private final int meta;
		private final String name;
		private final ResourceLocation lootTable;
		private static final EnumTreasure[] META_LOOKUP = new EnumTreasure[values().length];
		
		private EnumTreasure(int meta, String name, ResourceLocation lootTable)
		{
			this.meta = meta;
			this.name = name;
			this.lootTable = lootTable;
		}
		
		public int getMeta()
		{
			return this.meta;
		}
		
		public ResourceLocation getLootTable()
		{
			return this.lootTable;
		}
		
		@Override
		public String toString() 
		{
			return this.name;
		}
		
		public static EnumTreasure byMetadata(int meta)
		{
			return META_LOOKUP[meta % values().length];
		}
		
		static
		{
			for(EnumTreasure item$materialtype : values())
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
