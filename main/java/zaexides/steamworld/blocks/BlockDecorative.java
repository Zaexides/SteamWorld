package zaexides.steamworld.blocks;

import java.util.Random;

import com.google.common.base.Predicate;
import com.google.common.util.concurrent.Service.State;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import scala.tools.nsc.transform.patmat.ScalaLogic.TreesAndTypesDomain.Var;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.BlockAncite.EnumType;
import zaexides.steamworld.blocks.item.ItemBlockVariant;
import zaexides.steamworld.items.ItemInitializer;
import zaexides.steamworld.utility.interfaces.IMetaName;
import zaexides.steamworld.utility.interfaces.IModeledObject;
import zaexides.steamworld.utility.interfaces.IOreDictionaryRegisterable;

public class BlockDecorative extends Block implements IMetaName, IModeledObject, IOreDictionaryRegisterable
{
	public static final PropertyEnum<BlockDecorative.EnumType> VARIANT = PropertyEnum.<BlockDecorative.EnumType>create("variant", BlockDecorative.EnumType.class);
	
	public BlockDecorative(String name)
	{
		super(Material.ROCK);
		setUnlocalizedName(ModInfo.MODID + "." + name);
		setRegistryName(name);
		setHarvestLevels();
		setCreativeTab(SteamWorld.CREATIVETAB);
		
		setDefaultState(blockState.getBaseState().withProperty(VARIANT, BlockDecorative.EnumType.ENDRITCH_BLOCK));
		
		BlockInitializer.BLOCKS.add(this);
		ItemInitializer.ITEMS.add(new ItemBlockVariant(this).setRegistryName(this.getRegistryName()));
	}
	
	private void setHarvestLevels()
	{
		for(EnumType enumType : EnumType.values())
			setHarvestLevel("pickaxe", enumType.harvestLevel, getStateFromMeta(enumType.getMeta()));
	}
	
	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) 
	{
		return EnumType.byMetadata(getMetaFromState(blockState)).hardness;
	}
	
	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) 
	{
		return getBlockHardness(world.getBlockState(pos), world, pos);
	}
	
	@Override
	public int damageDropped(IBlockState state) 
	{
		return ((BlockDecorative.EnumType)state.getValue(VARIANT)).getMeta();
	}
	
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
	{
		for(BlockDecorative.EnumType block$enumtype : BlockDecorative.EnumType.values())
		{
			items.add(new ItemStack(this, 1, block$enumtype.getMeta()));
		}
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) 
	{
		return this.getDefaultState().withProperty(VARIANT, BlockDecorative.EnumType.byMetadata(meta));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) 
	{
		return ((BlockDecorative.EnumType)state.getValue(VARIANT)).getMeta();
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
		if(id >= BlockDecorative.EnumType.values().length)
			return "error";
		return BlockDecorative.EnumType.values()[stack.getItemDamage()].getName();
	}
	
	@Override
	public void RegisterModels()
	{
		for(int i = 0; i < BlockDecorative.EnumType.values().length; i++)
		{
			SteamWorld.proxy.RegisterItemRenderers(Item.getItemFromBlock(this), i, "inventory", BlockDecorative.EnumType.values()[i].getName());
		}
	}
	
	@Override
	public void RegisterOreInDictionary() 
	{
		OreDictionary.registerOre("blockPreservation", new ItemStack(this, 1, getMetaFromState(getDefaultState().withProperty(VARIANT, EnumType.PRESERVATION_COBBLE))));
		OreDictionary.registerOre("stone", new ItemStack(this, 1, getMetaFromState(getDefaultState().withProperty(VARIANT, EnumType.SKY_STONE))));
		OreDictionary.registerOre("cobblestone", new ItemStack(this, 1, getMetaFromState(getDefaultState().withProperty(VARIANT, EnumType.SKY_COBBLE))));
	}
	
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state,
			int fortune) 
	{
		if(state.getValue(VARIANT) == EnumType.SKY_STONE)
			drops.add(new ItemStack(this, 1, EnumType.SKY_COBBLE.getMeta()));
		else
			super.getDrops(drops, world, pos, state, fortune);
	}
	
	public static enum EnumType implements IStringSerializable
	{
		ENDRITCH_BLOCK(0, "block_endritch", 3f, 3),
		PRESERVATION_COBBLE(1, "block_preservation_cobble", 2.5f, 1),
		SKY_STONE(2, "sky_stone", 1f, 1),
		SKY_COBBLE(3, "sky_cobble", 1f, 1);
		
		private static final BlockDecorative.EnumType[] META_LOOKUP = new BlockDecorative.EnumType[values().length];
		private final int meta;
		private final String name, unlocalizedName;
		private int harvestLevel = 0;
		private final float hardness;
		
		private EnumType(int meta, String name, float hardness)
		{
			this.name = name;
			this.unlocalizedName = name;
			this.meta = meta;
			this.hardness = hardness;
		}
		
		private EnumType(int meta, String name, float hardness, int harvestLevel)
		{
			this(meta, name, hardness);
			this.harvestLevel = harvestLevel;
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
		
		public static BlockDecorative.EnumType byMetadata(int meta)
		{
			return META_LOOKUP[meta % values().length];
		}
		
		static
		{
			for(BlockDecorative.EnumType ancite$enumtype : values())
			{
				META_LOOKUP[ancite$enumtype.getMeta()] = ancite$enumtype;
			}
		}
	}
}
