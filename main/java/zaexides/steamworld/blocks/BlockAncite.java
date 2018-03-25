package zaexides.steamworld.blocks;

import com.google.common.util.concurrent.Service.State;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import scala.tools.nsc.transform.patmat.ScalaLogic.TreesAndTypesDomain.Var;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.item.ItemBlockVariant;
import zaexides.steamworld.init.BlockInitializer;
import zaexides.steamworld.init.ItemInitializer;
import zaexides.steamworld.utility.interfaces.IMetaName;
import zaexides.steamworld.utility.interfaces.IModeledObject;
import zaexides.steamworld.utility.interfaces.IOreDictionaryRegisterable;

public class BlockAncite extends Block implements IMetaName, IModeledObject, IOreDictionaryRegisterable
{
	public static final PropertyEnum<BlockAncite.EnumType> VARIANT = PropertyEnum.<BlockAncite.EnumType>create("variant", BlockAncite.EnumType.class);
	
	public BlockAncite(String name)
	{
		super(Material.IRON);
		setUnlocalizedName(ModInfo.MODID + "." + name);
		setRegistryName(name);
		setHarvestLevel("pickaxe", 3);
		setHardness(4.2f);
		setCreativeTab(SteamWorld.CREATIVETAB);
		
		setDefaultState(blockState.getBaseState().withProperty(VARIANT, BlockAncite.EnumType.BRICKS));
		
		BlockInitializer.BLOCKS.add(this);
		ItemInitializer.ITEMS.add(new ItemBlockVariant(this).setRegistryName(this.getRegistryName()));
	}
	
	@Override
	public void RegisterOreInDictionary() 
	{
		OreDictionary.registerOre("blockAncite", new ItemStack(this, 1, getMetaFromState(getDefaultState().withProperty(VARIANT, EnumType.BLOCK))));
		OreDictionary.registerOre("blockSteaite", new ItemStack(this, 1, EnumType.STEAITE.getMeta()));
	}
	
	@Override
	public int damageDropped(IBlockState state) 
	{
		return ((BlockAncite.EnumType)state.getValue(VARIANT)).getMeta();
	}
	
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
	{
		for(BlockAncite.EnumType block$enumtype : BlockAncite.EnumType.values())
		{
			items.add(new ItemStack(this, 1, block$enumtype.getMeta()));
		}
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) 
	{
		return this.getDefaultState().withProperty(VARIANT, BlockAncite.EnumType.byMetadata(meta));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) 
	{
		return ((BlockAncite.EnumType)state.getValue(VARIANT)).getMeta();
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
			EntityPlayer player) 
	{
		return new ItemStack(Item.getItemFromBlock(this), 1, getMetaFromState(world.getBlockState(pos)));
	}
	
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {VARIANT});
	}
	
	@Override
	public String getSpecialName(ItemStack stack) 
	{
		int id = stack.getItemDamage();
		if(id >= BlockAncite.EnumType.values().length)
			return "error";
		return BlockAncite.EnumType.values()[stack.getItemDamage()].getName();
	}
	
	@Override
	public void RegisterModels()
	{
		for(int i = 0; i < BlockAncite.EnumType.values().length; i++)
		{
			SteamWorld.proxy.RegisterItemRenderers(Item.getItemFromBlock(this), i, "inventory", "block_ancite_" + BlockAncite.EnumType.values()[i].getName());
		}
	}
	
	@Override
	public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) 
	{
		return worldObj.getBlockState(pos).equals(getStateFromMeta(EnumType.BLOCK.getMeta()));
	}
	
	public static enum EnumType implements IStringSerializable
	{
		BRICKS(0, "brick"),
		TILES(1, "floor"),
		PLATES(2, "plate"),
		BLOCK(3, "block"),
		BIG_BRICKS(4, "bigbricks"),
		STEAITE(5, "steaite_block");
		
		private static final BlockAncite.EnumType[] META_LOOKUP = new BlockAncite.EnumType[values().length];
		private final int meta;
		private final String name, unlocalizedName;
		
		private EnumType(int meta, String name)
		{
			this.name = name;
			this.unlocalizedName = name;
			this.meta = meta;
		}
		
		@Override
		public String getName()
		{
			return this.name;
		}
		
		public int getMeta()
		{
			return this.meta;
		}
		
		public String getUnlocalizedName()
		{
			return this.unlocalizedName;
		}
		
		@Override
		public String toString()
		{
			return this.name;
		}
		
		public static BlockAncite.EnumType byMetadata(int meta)
		{
			return META_LOOKUP[meta % values().length];
		}
		
		static
		{
			for(BlockAncite.EnumType ancite$enumtype : values())
			{
				META_LOOKUP[ancite$enumtype.getMeta()] = ancite$enumtype;
			}
		}
	}
}
