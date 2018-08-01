package zaexides.steamworld.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.items.IItemHandler;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.item.ItemBlockVariant;
import zaexides.steamworld.client.rendering.tile.AltarModel;
import zaexides.steamworld.init.ItemInitializer;
import zaexides.steamworld.te.TileEntityAltar;
import zaexides.steamworld.utility.interfaces.IBlockBreakInterruptor;
import zaexides.steamworld.utility.interfaces.IMetaName;

public class BlockAltar extends SteamWorldBlock implements ITileEntityProvider, IMetaName, IBlockBreakInterruptor
{
	public static final PropertyBool DECORATIVE = PropertyBool.create("decorative");
	
	public BlockAltar(String name) 
	{
		super(name, Material.ROCK, 1000.0f);
		setCreativeTab(SteamWorld.CREATIVETAB_BLOCKS);
	}
	
	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) 
	{
		if(blockState.getValue(DECORATIVE))
			return 0.8f;
		else
			return 15.0f;
	}
	
	@Override
	protected void AddBlockItem(int maxStackSize) 
	{
		ItemInitializer.ITEMS.add(new ItemBlockVariant(this).setRegistryName(this.getRegistryName()));
	}
	
	@Override
	public int damageDropped(IBlockState state) 
	{
		return state.getValue(DECORATIVE) ? 1 : 0;
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
			EntityPlayer player) 
	{
		return new ItemStack(Item.getItemFromBlock(this), 1, getMetaFromState(world.getBlockState(pos)));
	}
	
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) 
	{
		items.add(new ItemStack(this, 1, 0));
		items.add(new ItemStack(this, 1, 1));
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) 
	{
		return state.withProperty(DECORATIVE, state.getValue(DECORATIVE));
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) 
	{
		return getDefaultState().withProperty(DECORATIVE, meta == 1);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) 
	{
		return state.getValue(DECORATIVE) ? 1 : 0;
	}
	
	@Override
	protected BlockStateContainer createBlockState() 
	{
		return new BlockStateContainer(this, DECORATIVE);
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) 
	{
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) 
	{
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityAltar();
	}
	
	@Override
	public void RegisterModels() 
	{
		SteamWorld.proxy.RegisterItemRenderers(Item.getItemFromBlock(this), 0, "inventory", "altar");
		SteamWorld.proxy.RegisterItemRenderers(Item.getItemFromBlock(this), 1, "inventory", "altar_decorative");
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAltar.class, new AltarModel());
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) 
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if(tileEntity instanceof TileEntityAltar)
		{
			ItemStack handStack = playerIn.getHeldItem(hand);
			ItemStack altarStack = ((TileEntityAltar)tileEntity).itemStackHandler.getStackInSlot(0);
			if(altarStack != ItemStack.EMPTY)
			{
				//Altar -> Player
				if(playerIn.inventory.addItemStackToInventory(altarStack.copy()))
				{
					worldIn.playSound(null, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.PLAYERS, 1.0f, 1.0f);
					((TileEntityAltar)tileEntity).itemStackHandler.setStackInSlot(0, ItemStack.EMPTY);
				}
			}
			else if(handStack != ItemStack.EMPTY && altarStack == ItemStack.EMPTY)
			{
				//Player -> Altar
				ItemStack newAlterStack = handStack.copy();
				newAlterStack.setCount(1);
				((TileEntityAltar)tileEntity).itemStackHandler.setStackInSlot(0, newAlterStack);
				ItemStack newHandStack = handStack.copy();
				newHandStack.shrink(1);
				if(newHandStack.getCount() <= 0)
					newHandStack = ItemStack.EMPTY;
				playerIn.setHeldItem(hand, newHandStack);
			}
			
			return true;
		}
		return false;
	}

	@Override
	public String getSpecialName(ItemStack stack) 
	{
		return (stack.getMetadata() == 1) ? "decorative" : "regular";
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) 
	{
		IItemHandler itemHandler = ((TileEntityAltar)worldIn.getTileEntity(pos)).itemStackHandler;
		for(int i = 0; i < itemHandler.getSlots(); i++)
		{
			ItemStack stack = itemHandler.getStackInSlot(i);
			InventoryHelper.spawnItemStack(worldIn, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, stack);
		}
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public boolean CanBreakBlock(IBlockState blockState, EntityPlayer player, boolean isHarvestable) 
	{
		if(blockState.getValue(DECORATIVE))
			return true;
		else
			return isHarvestable;
	}
}
