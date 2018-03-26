package zaexides.steamworld.blocks;

import org.apache.logging.log4j.Level;

import com.google.common.util.concurrent.Service.State;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
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

public class BlockSWFlower extends BlockBush implements IMetaName, IModeledObject
{
	public static final PropertyEnum<BlockSWFlower.EnumType> VARIANT = PropertyEnum.<BlockSWFlower.EnumType>create("variant", BlockSWFlower.EnumType.class);
	
	public BlockSWFlower(String name)
	{
		setUnlocalizedName(ModInfo.MODID + "." + name);
		setRegistryName(name);
		setCreativeTab(SteamWorld.CREATIVETAB);
		setSoundType(SoundType.PLANT);
		
		setDefaultState(blockState.getBaseState().withProperty(VARIANT, BlockSWFlower.EnumType.WITHER));
		
		BlockInitializer.BLOCKS.add(this);
		ItemInitializer.ITEMS.add(new ItemBlockVariant(this).setRegistryName(this.getRegistryName()));
	}
	
	@Override
	public int damageDropped(IBlockState state) 
	{
		return ((BlockSWFlower.EnumType)state.getValue(VARIANT)).getMeta();
	}
	
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
	{
		for(BlockSWFlower.EnumType block$enumtype : BlockSWFlower.EnumType.values())
		{
			items.add(new ItemStack(this, 1, block$enumtype.getMeta()));
		}
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) 
	{
		return this.getDefaultState().withProperty(VARIANT, BlockSWFlower.EnumType.byMetadata(meta));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) 
	{
		return ((BlockSWFlower.EnumType)state.getValue(VARIANT)).getMeta();
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
		if(id >= BlockSWFlower.EnumType.values().length)
			return "error";
		return BlockSWFlower.EnumType.values()[stack.getItemDamage()].getName();
	}
	
	@Override
	public void RegisterModels()
	{
		for(int i = 0; i < BlockSWFlower.EnumType.values().length; i++)
		{
			SteamWorld.proxy.RegisterItemRenderers(Item.getItemFromBlock(this), i, "inventory", "flower_" + BlockSWFlower.EnumType.values()[i].getName());
		}
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) 
	{
		if(getMetaFromState(state) == EnumType.WITHER.getMeta())
		{
			if(entityIn instanceof EntityLivingBase)
			{
				PotionEffect witherEffect = new PotionEffect(MobEffects.WITHER, 120, 0);
				((EntityLivingBase)entityIn).addPotionEffect(witherEffect);
			}
		}
	}
	
	public static enum EnumType implements IStringSerializable
	{
		WITHER(0, "wither");
		
		private static final BlockSWFlower.EnumType[] META_LOOKUP = new BlockSWFlower.EnumType[values().length];
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
		
		public static BlockSWFlower.EnumType byMetadata(int meta)
		{
			return META_LOOKUP[meta % values().length];
		}
		
		static
		{
			for(BlockSWFlower.EnumType ancite$enumtype : values())
			{
				META_LOOKUP[ancite$enumtype.getMeta()] = ancite$enumtype;
			}
		}
	}
}
