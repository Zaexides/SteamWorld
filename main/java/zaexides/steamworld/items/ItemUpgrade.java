package zaexides.steamworld.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.BlockAncite;
import zaexides.steamworld.blocks.machines.IUpgradeable;
import zaexides.steamworld.utility.interfaces.IModeledObject;

public class ItemUpgrade extends SteamWorldItem implements IModeledObject
{
	protected String name;
	
	public ItemUpgrade(String name) 
	{
		super(name);
		this.name = name;
		setMaxDamage(0);
		setHasSubtypes(true);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) 
	{
		tooltip.add("Shift Right-Click me on a machine");
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
		EnumUpgradeType material = EnumUpgradeType.byMetadata(metadata);
		return super.getUnlocalizedName() + "_" + material.getName();
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) 
	{
		for(EnumUpgradeType item$material : EnumUpgradeType.values())
		{
			if(isInCreativeTab(tab))
				items.add(new ItemStack(this, 1, item$material.getMeta()));
		}
	}
	
	@Override
	public void RegisterModels()
	{
		for(int i = 0; i < EnumUpgradeType.values().length; i++)
		{
			SteamWorld.proxy.RegisterItemRenderers(this, i, "inventory", name + "_" + EnumUpgradeType.values()[i].getName());
		}
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) 
	{
		IBlockState blockState = worldIn.getBlockState(pos);
		if(blockState != null)
		{
			Block block = blockState.getBlock();
			ItemStack handStack = player.getHeldItem(hand);
			if(block != null && block instanceof IUpgradeable)
				return ((IUpgradeable)block).OnUpgradeItemUse(EnumUpgradeType.byMetadata(handStack.getMetadata()), worldIn, pos, handStack, player);
		}
		return EnumActionResult.FAIL;
	}
	
	public static enum EnumUpgradeType implements IStringSerializable
	{
		ANCITE(0, "ancite"),
		ENDRITCH(1, "endritch");
		
		private final int meta;
		private final String name;
		private static final EnumUpgradeType[] META_LOOKUP = new EnumUpgradeType[values().length];
		
		private EnumUpgradeType(int meta, String name)
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
		
		public static EnumUpgradeType byMetadata(int meta)
		{
			return META_LOOKUP[meta % values().length];
		}
		
		static
		{
			for(ItemUpgrade.EnumUpgradeType item$materialtype : values())
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
