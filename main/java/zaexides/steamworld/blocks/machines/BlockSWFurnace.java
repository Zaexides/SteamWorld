package zaexides.steamworld.blocks.machines;

import java.util.Random;

import org.apache.logging.log4j.Level;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.gui.GuiHandler;
import zaexides.steamworld.te.TileEntitySWFurnace;

public class BlockSWFurnace extends BlockMachine implements ITileEntityProvider
{
	public int speed = 1;
	
	public BlockSWFurnace(String name, float hardness, int speed) 
	{
		super(name, Material.IRON, hardness);
		this.speed = speed;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		TileEntitySWFurnace swFurnace = new TileEntitySWFurnace();
		setMachineStats(swFurnace);
		return swFurnace;
	}
	
	@Override
	public void setMachineStats(TileEntity tileEntity) 
	{
		((TileEntitySWFurnace)tileEntity).SetStats(speed);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) 
	{
		if(stateIn.getValue(ACTIVE))
		{
			EnumFacing facing = stateIn.getValue(FACING);
			Vec3i offset = facing.getDirectionVec();
			
			EnumFacing side = facing.rotateY();
			Vec3i sideOff = side.getDirectionVec();
			double randOff = (rand.nextDouble() * 0.5d) - 0.25d;
			
			double x = pos.getX() + 0.5d + (offset.getX() * 0.55d) + (sideOff.getX() * randOff);
			double y = pos.getY() + 0.5d + ((rand.nextDouble() * 0.25d) - 0.125d);
			double z = pos.getZ() + 0.5d + (offset.getZ() * 0.55d) + (sideOff.getZ() * randOff);
			
			worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0, 0, 0);
		}
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) 
	{
		if(!worldIn.isRemote)
		{
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if(!(tileEntity instanceof TileEntitySWFurnace))
				return false;
			playerIn.openGui(SteamWorld.singleton, GuiHandler.STEAM_FURNACE, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) 
	{
		if(!keepInventory)
		{
			IItemHandler itemHandler = ((TileEntitySWFurnace)worldIn.getTileEntity(pos)).inputStack;
			for(int i = 0; i < itemHandler.getSlots(); i++)
			{
				ItemStack stack = itemHandler.getStackInSlot(i);
				InventoryHelper.spawnItemStack(worldIn, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, stack);
			}
			itemHandler = ((TileEntitySWFurnace)worldIn.getTileEntity(pos)).outputStack;
			for(int i = 0; i < itemHandler.getSlots(); i++)
			{
				ItemStack stack = itemHandler.getStackInSlot(i);
				InventoryHelper.spawnItemStack(worldIn, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, stack);
			}
		}
		super.breakBlock(worldIn, pos, state);
	}
}
