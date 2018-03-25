package zaexides.steamworld.blocks;

import java.util.Random;

import org.apache.logging.log4j.Level;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.item.ItemBlockVariant;
import zaexides.steamworld.init.BlockInitializer;
import zaexides.steamworld.init.ItemInitializer;
import zaexides.steamworld.utility.interfaces.IMetaName;
import zaexides.steamworld.utility.interfaces.IModeledObject;
import zaexides.steamworld.utility.interfaces.IOreDictionaryRegisterable;

public class SteamWorldBlockOre extends Block implements IMetaName, IModeledObject, IOreDictionaryRegisterable
{
	public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.<EnumType>create("variant", EnumType.class);
	
	public SteamWorldBlockOre(String name, Material material)
	{
		super(material);
		setUnlocalizedName(ModInfo.MODID + "." + name);
		setRegistryName(name);
		setHarvestLevels();
		setCreativeTab(SteamWorld.CREATIVETAB);
		
		setDefaultState(blockState.getBaseState().withProperty(VARIANT, EnumType.OVERWORLD_STEAITE));
		
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
		return getItemDropped(state, null, 0) instanceof ItemBlock ? ((EnumType)state.getValue(VARIANT)).getMeta() : 0;
	}
	
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
	{
		for(EnumType block$enumtype : EnumType.values())
		{
			items.add(new ItemStack(this, 1, block$enumtype.getMeta()));
		}
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) 
	{
		return this.getDefaultState().withProperty(VARIANT, EnumType.byMetadata(meta));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) 
	{
		return ((EnumType)state.getValue(VARIANT)).getMeta();
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
		if(id >= EnumType.values().length)
			return "error";
		return EnumType.values()[stack.getItemDamage()].getName();
	}

	@Override
	public void RegisterOreInDictionary() 
	{
		for(EnumType eType : EnumType.values())
			OreDictionary.registerOre(eType.oreName, new ItemStack(this, 1, eType.getMeta()));
	}
	
	@Override
	public void RegisterModels()
	{
		for(int i = 0; i < EnumType.values().length; i++)
		{
			SteamWorld.proxy.RegisterItemRenderers(Item.getItemFromBlock(this), i, "inventory", EnumType.values()[i].getName());
		}
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) 
	{
		if(getMetaFromState(state) == EnumType.SKY_COAL.getMeta())
			return Items.COAL;
		else if(getMetaFromState(state) == EnumType.SKY_DIAMOND.getMeta())
			return Items.DIAMOND;
		else
			return super.getItemDropped(state, rand, fortune);
	}
	
	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random)
	{
		if(fortune > 0 && EnumType.byMetadata(getMetaFromState(state)).multiDrop)
		{
			int i = random.nextInt(fortune + 2) - 1;

            if (i < 0)
            {
                i = 0;
            }

            return this.quantityDropped(random) * (i + 1);
		}
		else
			return super.quantityDropped(state, fortune, random);
	}
	
	public static enum EnumType implements IStringSerializable
	{
		OVERWORLD_STEAITE(0, "steaite_ow", "oreSteaite", 2.5f, 3),
		SKY_STEAITE(1, "steaite_sky", "oreSteaite", 3.0f, 3),
		SKY_COAL(2, "coal_sky", "oreCoal", 3.5f, 0, true),
		SKY_IRON(3, "iron_sky", "oreIron", 3.5f, 1),
		SKY_GOLD(4, "gold_sky", "oreGold", 3.5f, 2),
		SKY_DIAMOND(5, "diamond_sky", "oreDiamond", 3.5f, 2, true),
		SKY_ANCITE(6, "ancite_sky", "oreAncite", 5.0f, 3);
		
		private static final EnumType[] META_LOOKUP = new EnumType[values().length];
		private final int meta;
		private final String name, unlocalizedName, oreName;
		private int harvestLevel = 0;
		private final float hardness;
		private boolean multiDrop = false;
		
		private EnumType(int meta, String name, String oreName, float hardness)
		{
			this.name = name;
			this.unlocalizedName = name;
			this.meta = meta;
			this.hardness = hardness;
			this.oreName = oreName;
		}
		
		private EnumType(int meta, String name, String oreName, float hardness, int harvestLevel)
		{
			this(meta, name, oreName, hardness);
			this.harvestLevel = harvestLevel;
		}
		
		private EnumType(int meta, String name, String oreName, float hardness, int harvestLevel, boolean multiDrop)
		{
			this(meta, name, oreName, hardness, harvestLevel);
			this.multiDrop = multiDrop;
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
		
		public static EnumType byMetadata(int meta)
		{
			return META_LOOKUP[meta % values().length];
		}
		
		static
		{
			for(EnumType enumtype : values())
			{
				META_LOOKUP[enumtype.getMeta()] = enumtype;
			}
		}
	}
}
