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
import zaexides.steamworld.te.TileEntityAssembler;

public class BlockAssembler extends BlockMachine implements ITileEntityProvider
{
	public int efficiency = 1, efficiencyHT = 3;
	
	public BlockAssembler(String name, float hardness, int efficiency, int efficiencyHT) 
	{
		super(name, Material.IRON, hardness);
		this.efficiency = efficiency;
		this.efficiencyHT = efficiencyHT;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		TileEntityAssembler assembler = new TileEntityAssembler();
		setMachineStats(assembler, IsHighTier(meta));
		return assembler;
	}
	
	@Override
	public void setMachineStats(TileEntity tileEntity, boolean highTier) 
	{
		((TileEntityAssembler)tileEntity).SetStats(highTier ? efficiencyHT : efficiency);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) 
	{
		if(!worldIn.isRemote)
		{
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if(!(tileEntity instanceof TileEntityAssembler))
				return false;
			playerIn.openGui(SteamWorld.singleton, GuiHandler.ASSEMBLER, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) 
	{
		if(!keepInventory)
		{
			IItemHandler itemHandler = ((TileEntityAssembler)worldIn.getTileEntity(pos)).outputStack;
			for(int i = 0; i < itemHandler.getSlots(); i++)
			{
				ItemStack stack = itemHandler.getStackInSlot(i);
				InventoryHelper.spawnItemStack(worldIn, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, stack);
			}
			itemHandler = ((TileEntityAssembler)worldIn.getTileEntity(pos)).inputStack;
			for(int i = 0; i < itemHandler.getSlots(); i++)
			{
				ItemStack stack = itemHandler.getStackInSlot(i);
				InventoryHelper.spawnItemStack(worldIn, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, stack);
			}
		}
		super.breakBlock(worldIn, pos, state);
	}
}
