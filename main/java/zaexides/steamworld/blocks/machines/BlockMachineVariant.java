package zaexides.steamworld.blocks.machines;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
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
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.item.ItemBlockVariant;
import zaexides.steamworld.gui.GuiHandler;
import zaexides.steamworld.init.BlockInitializer;
import zaexides.steamworld.init.ItemInitializer;
import zaexides.steamworld.network.PacketHandler;
import zaexides.steamworld.network.messages.MessageGetTeleporterData;
import zaexides.steamworld.savedata.world.TeleporterSaveData;
import zaexides.steamworld.te.generic_machine.TileEntityLauncher;
import zaexides.steamworld.te.generic_machine.TileEntityTeleporter;
import zaexides.steamworld.te.generic_machine.interfaces.IGenericMachineWalkActivate;
import zaexides.steamworld.utility.SWMaterials;
import zaexides.steamworld.utility.interfaces.IMetaName;
import zaexides.steamworld.utility.interfaces.IModeledObject;

public class BlockMachineVariant extends Block implements IMetaName, IModeledObject, IWrenchable, ITileEntityProvider
{
	public static final PropertyEnum<BlockMachineVariant.EnumType> VARIANT = PropertyEnum.<BlockMachineVariant.EnumType>create("variant", BlockMachineVariant.EnumType.class);
	
	public BlockMachineVariant(String name, float hardness)
	{
		super(SWMaterials.ENDRITCH);
		setUnlocalizedName(ModInfo.MODID + "." + name);
		setRegistryName(name);
		setHardness(hardness);
		setCreativeTab(SteamWorld.CREATIVETAB_UTILITY);
		
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
		for(int i = 0; i < EnumType.values().length; i++)
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
		case LAUNCHER:
			return new TileEntityLauncher();
		default:
			return null;
		}
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
				
				if(tileEntity instanceof TileEntityTeleporter)
					playerIn.openGui(SteamWorld.singleton, GuiHandler.TELEPORTER, worldIn, pos.getX(), pos.getY(), pos.getZ());
				else
					return false;
				break;
			case LAUNCHER:
				if(tileEntity instanceof TileEntityLauncher)
					((TileEntityLauncher)tileEntity).ChangeForce(hitY, playerIn);
				else
					return false;
				break;
			default:
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) 
	{
		if(worldIn.isRemote)
			return;
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if(tileEntity != null && tileEntity instanceof IGenericMachineWalkActivate)
			((IGenericMachineWalkActivate)tileEntity).onWalkedOn(entityIn);
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
				{
					teleporterSaveData.removeTeleporterData(((TileEntityTeleporter)tileEntity).ownId);
					((TileEntityTeleporter)tileEntity).updateSkyOfOldPortalStatus();
				}
			}
			break;
		default:
			break;
		}
		super.breakBlock(worldIn, pos, state);
	}
	
	public static enum EnumType implements IStringSerializable
	{
		TELEPORTER(0, "teleporter"),
		LAUNCHER(1, "launcher");
		
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
