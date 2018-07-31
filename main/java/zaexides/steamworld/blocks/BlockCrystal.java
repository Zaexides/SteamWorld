package zaexides.steamworld.blocks;

import java.util.Random;

import com.google.common.base.Predicate;
import com.google.common.util.concurrent.Service.State;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
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
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import scala.tools.nsc.transform.patmat.ScalaLogic.TreesAndTypesDomain.Var;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.BlockAncite.EnumType;
import zaexides.steamworld.blocks.item.ItemBlockVariant;
import zaexides.steamworld.init.BlockInitializer;
import zaexides.steamworld.init.ItemInitializer;
import zaexides.steamworld.utility.interfaces.IMetaName;
import zaexides.steamworld.utility.interfaces.IModeledObject;
import zaexides.steamworld.utility.interfaces.IOreDictionaryRegisterable;

public class BlockCrystal extends Block implements IMetaName, IModeledObject
{
	public static final PropertyEnum<BlockCrystal.EnumType> VARIANT = PropertyEnum.<BlockCrystal.EnumType>create("variant", BlockCrystal.EnumType.class);
	
	public BlockCrystal(String name)
	{
		super(Material.GLASS);
		setUnlocalizedName(ModInfo.MODID + "." + name);
		setRegistryName(name);
		setHarvestLevels();
		setCreativeTab(SteamWorld.CREATIVETAB_BLOCKS);
		setSoundType(SoundType.GLASS);
		setLightLevel(0.3f);
		
		setTickRandomly(true);
		
		setDefaultState(blockState.getBaseState().withProperty(VARIANT, BlockCrystal.EnumType.GRAVITY_CRYSTAL));
		
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
		return ((BlockCrystal.EnumType)state.getValue(VARIANT)).getMeta();
	}
	
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
	{
		for(BlockCrystal.EnumType block$enumtype : BlockCrystal.EnumType.values())
		{
			items.add(new ItemStack(this, 1, block$enumtype.getMeta()));
		}
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) 
	{
		return this.getDefaultState().withProperty(VARIANT, BlockCrystal.EnumType.byMetadata(meta));
	}
	
	@Override
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) 
	{
		for(int i = 0; i < rand.nextInt(4) + 1; i++)
		{
			EnumFacing facing = EnumFacing.random(rand);
			double xPos = pos.getX() + 0.5 + rand.nextDouble() * 1.2 - 0.6;
			double yPos = pos.getY() + 0.5 + rand.nextDouble() * 1.2 - 0.6;
			double zPos = pos.getZ() + 0.5 + rand.nextDouble() * 1.2 - 0.6;
			
			int[] colors = EnumType.byMetadata(getMetaFromState(stateIn)).particleColor;
			worldIn.spawnParticle(EnumParticleTypes.REDSTONE, xPos, yPos, zPos, colors[0] / 255.0, colors[1] / 255.0, colors[2] / 255.0);
		}
	}
	
	@Override
	public BlockRenderLayer getBlockLayer() 
	{
		return BlockRenderLayer.TRANSLUCENT;
	}
	
	@Override
	public int getMetaFromState(IBlockState state) 
	{
		return ((BlockCrystal.EnumType)state.getValue(VARIANT)).getMeta();
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
		if(id >= BlockCrystal.EnumType.values().length)
			return "error";
		return BlockCrystal.EnumType.values()[stack.getItemDamage()].getName();
	}
	
	@Override
	public void RegisterModels()
	{
		for(int i = 0; i < BlockCrystal.EnumType.values().length; i++)
		{
			SteamWorld.proxy.RegisterItemRenderers(Item.getItemFromBlock(this), i, "inventory", "block_crystal_" + BlockCrystal.EnumType.values()[i].getName());
		}
	}
	
	public static enum EnumType implements IStringSerializable
	{
		GRAVITY_CRYSTAL(0, "gravity", 1.2f, new int[] {175, 204, 255}, 4),
		CORE_CRYSTAL(1, "core", 1.2f, new int[] {237, 143, 179}, 4),
		STAR_CRYSTAL(2, "star", 1.2f, new int[] {255, 242, 187}, 4);
		
		private static final BlockCrystal.EnumType[] META_LOOKUP = new BlockCrystal.EnumType[values().length];
		private final int meta;
		private final String name, unlocalizedName;
		private int harvestLevel = 0;
		private final float hardness;
		private final int[] particleColor;
		
		private EnumType(int meta, String name, float hardness, int[] particleColor)
		{
			this.name = name;
			this.unlocalizedName = name;
			this.meta = meta;
			this.hardness = hardness;
			this.particleColor = particleColor;
		}
		
		private EnumType(int meta, String name, float hardness, int[] particleColor, int harvestLevel)
		{
			this(meta, name, hardness, particleColor);
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
		
		public static BlockCrystal.EnumType byMetadata(int meta)
		{
			return META_LOOKUP[meta % values().length];
		}
		
		static
		{
			for(BlockCrystal.EnumType ancite$enumtype : values())
			{
				META_LOOKUP[ancite$enumtype.getMeta()] = ancite$enumtype;
			}
		}
	}
}
