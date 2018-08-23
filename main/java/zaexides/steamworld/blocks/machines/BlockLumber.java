package zaexides.steamworld.blocks.machines;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.gui.GuiHandler;
import zaexides.steamworld.te.TileEntityLumber;

public class BlockLumber extends BlockMachine implements ITileEntityProvider
{
	public int amount = 1, amountHT = 4;
	
	public BlockLumber(String name, float hardness, int amount, int amountHT) 
	{
		super(name, Material.IRON, hardness);
		this.amount = amount;
		this.amountHT = amountHT;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		TileEntityLumber lumber = new TileEntityLumber();
		setMachineStats(lumber, IsHighTier(meta));
		return lumber;
	}
	
	@Override
	public void setMachineStats(TileEntity tileEntity, boolean highTier)
	{
		((TileEntityLumber)tileEntity).SetStats(highTier ? amountHT : amount);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) 
	{
		if(!worldIn.isRemote)
		{
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if(!(tileEntity instanceof TileEntityLumber))
				return false;
			playerIn.openGui(SteamWorld.singleton, GuiHandler.FARMER_LUMBER, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) 
	{
		if(!keepInventory)
		{
			IItemHandler itemHandler = ((TileEntityLumber)worldIn.getTileEntity(pos)).outputStack;
			for(int i = 0; i < itemHandler.getSlots(); i++)
			{
				ItemStack stack = itemHandler.getStackInSlot(i);
				InventoryHelper.spawnItemStack(worldIn, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, stack);
			}
		}
		super.breakBlock(worldIn, pos, state);
	}
}
