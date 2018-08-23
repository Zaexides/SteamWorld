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
import zaexides.steamworld.te.TileEntityFluidMiner;

public class BlockFluidMiner extends BlockMachine implements ITileEntityProvider
{
	public int efficiency = 1, efficiencyHT = 3;
	public byte maxTier = 3, maxTierHT = 5;
	
	public BlockFluidMiner(String name, float hardness, int efficiency, int efficiencyHT, byte maxTier, byte maxTierHT) 
	{
		super(name, Material.IRON, hardness);
		this.efficiency = efficiency;
		this.efficiencyHT = efficiencyHT;
		this.maxTier = maxTier;
		this.maxTierHT = maxTierHT;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		TileEntityFluidMiner fluidMiner = new TileEntityFluidMiner();
		setMachineStats(fluidMiner, IsHighTier(meta));
		return fluidMiner;
	}
	
	@Override
	public void setMachineStats(TileEntity tileEntity, boolean highTier) 
	{
		((TileEntityFluidMiner)tileEntity).SetStats(
				highTier ? efficiencyHT : efficiency,
				highTier ? maxTierHT : maxTier
				);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) 
	{
		if(!worldIn.isRemote)
		{
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if(!(tileEntity instanceof TileEntityFluidMiner))
				return false;
			playerIn.openGui(SteamWorld.singleton, GuiHandler.FLUID_MINER, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) 
	{
		if(!keepInventory)
		{
			IItemHandler itemHandler = ((TileEntityFluidMiner)worldIn.getTileEntity(pos)).inputStack;
			for(int i = 0; i < itemHandler.getSlots(); i++)
			{
				ItemStack stack = itemHandler.getStackInSlot(i);
				InventoryHelper.spawnItemStack(worldIn, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, stack);
			}
		}
		super.breakBlock(worldIn, pos, state);
	}
}
