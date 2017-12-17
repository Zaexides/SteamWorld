package zaexides.steamworld.blocks.machines;

import org.apache.logging.log4j.Level;

import com.google.common.util.concurrent.Service.State;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import scala.tools.nsc.transform.patmat.ScalaLogic.TreesAndTypesDomain.Var;
import zaexides.steamworld.BlockInitializer;
import zaexides.steamworld.ItemInitializer;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.item.ItemBlockVariant;
import zaexides.steamworld.gui.GuiHandler;
import zaexides.steamworld.network.PacketHandler;
import zaexides.steamworld.network.messages.MessageGetTeleporterData;
import zaexides.steamworld.savedata.world.TeleporterData;
import zaexides.steamworld.savedata.world.TeleporterSaveData;
import zaexides.steamworld.te.TileEntityFisher;
import zaexides.steamworld.te.TileEntityTeleporter;
import zaexides.steamworld.utility.SWMaterials;
import zaexides.steamworld.utility.interfaces.IMetaName;
import zaexides.steamworld.utility.interfaces.IModeledObject;
import zaexides.steamworld.utility.interfaces.IOreDictionaryRegisterable;

public class BlockMachineVariant extends Block implements IMetaName, IModeledObject, IWrenchable, ITileEntityProvider
{
	public static final PropertyEnum<BlockMachineVariant.EnumType> VARIANT = PropertyEnum.<BlockMachineVariant.EnumType>create("variant", BlockMachineVariant.EnumType.class);
	
	public BlockMachineVariant(String name, float hardness)
	{
		super(SWMaterials.ENDRITCH);
		setUnlocalizedName(ModInfo.MODID + "." + name);
		setRegistryName(name);
		setHardness(hardness);
		setCreativeTab(SteamWorld.CREATIVETAB);
		
		setDefaultState(blockState.getBaseState().withProperty(VARIANT, BlockMachineVariant.EnumType.TELEPORTER));
		
		BlockInitializer.BLOCKS.add(this);
		ItemInitializer.ITEMS.add(new ItemBlockVariant(this).setRegistryName(this.getRegistryName()));
	}
	
	@Override
	public int damageDropped(IBlockState state) 
	{
		return ((BlockMachineVariant.EnumType)state.getValue(VARIANT)).getMeta();
	}
	
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
	{
		for(BlockMachineVariant.EnumType block$enumtype : BlockMachineVariant.EnumType.values())
		{
			items.add(new ItemStack(this, 1, block$enumtype.getMeta()));
		}
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) 
	{
		return this.getDefaultState().withProperty(VARIANT, BlockMachineVariant.EnumType.byMetadata(meta));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) 
	{
		return ((BlockMachineVariant.EnumType)state.getValue(VARIANT)).getMeta();
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
		if(id >= BlockMachineVariant.EnumType.values().length)
			return "error";
		return BlockMachineVariant.EnumType.values()[stack.getItemDamage()].getName();
	}
	
	@Override
	public void RegisterModels()
	{
		for(int i = 0; i < BlockMachineVariant.EnumType.values().length; i++)
		{
			SteamWorld.proxy.RegisterItemRenderers(Item.getItemFromBlock(this), i, "inventory", "generic_machine_" + BlockMachineVariant.EnumType.values()[i].getName());
		}
	}
	
	@Override
	public EnumActionResult onWrenchUse(World world, EntityPlayer player, BlockPos pos, EnumFacing facing,
			IBlockState blockState) 
	{
		switch(blockState.getValue(VARIANT))
		{
		default:
			harvestBlock(world, player, pos, blockState, null, ItemStack.EMPTY);
			return EnumActionResult.SUCCESS;
		}
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		switch(EnumType.byMetadata(meta))
		{
		case TELEPORTER:
			return new TileEntityTeleporter();
		}
		return null;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) 
	{
		if(!worldIn.isRemote)
		{
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			
			switch(state.getValue(VARIANT))
			{
			case TELEPORTER:
				NBTTagCompound compound = new NBTTagCompound();
				TeleporterSaveData teleporterSaveData = TeleporterSaveData.get(worldIn);
				PacketHandler.wrapper.sendTo(new MessageGetTeleporterData(teleporterSaveData.writeToNBT(compound), teleporterSaveData.mapName),(EntityPlayerMP)  playerIn);
				
				if(!(tileEntity instanceof TileEntityTeleporter))
					return false;
				else
					playerIn.openGui(SteamWorld.singleton, GuiHandler.TELEPORTER, worldIn, pos.getX(), pos.getY(), pos.getZ());
				break;
			}
		}
		return true;
	}
	
	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) 
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if(tileEntity != null)
		{
			switch(worldIn.getBlockState(pos).getValue(VARIANT))
			{
			case TELEPORTER:
				if(tileEntity instanceof TileEntityTeleporter)
					((TileEntityTeleporter) tileEntity).activate(entityIn);
				break;
			}
		}
		super.onEntityWalk(worldIn, pos, entityIn);
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) 
	{
		switch(state.getValue(VARIANT))
		{
		case TELEPORTER:
			TeleporterSaveData teleporterSaveData = TeleporterSaveData.get(worldIn);
			if(teleporterSaveData != null)
			{
				TileEntity tileEntity = worldIn.getTileEntity(pos);
				if(tileEntity instanceof TileEntityTeleporter)
					teleporterSaveData.removeTeleporterData(((TileEntityTeleporter)tileEntity).ownId);
			}
			break;
		}
		super.breakBlock(worldIn, pos, state);
	}
	
	public static enum EnumType implements IStringSerializable
	{
		TELEPORTER(0, "teleporter");
		
		private static final BlockMachineVariant.EnumType[] META_LOOKUP = new BlockMachineVariant.EnumType[values().length];
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
		
		public static BlockMachineVariant.EnumType byMetadata(int meta)
		{
			return META_LOOKUP[meta % values().length];
		}
		
		static
		{
			for(BlockMachineVariant.EnumType ancite$enumtype : values())
			{
				META_LOOKUP[ancite$enumtype.getMeta()] = ancite$enumtype;
			}
		}
	}
}
