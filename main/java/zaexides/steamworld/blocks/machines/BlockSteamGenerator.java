package zaexides.steamworld.blocks.machines;

import java.util.Random;

import org.apache.logging.log4j.Level;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
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
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import scala.languageFeature.postfixOps;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.SteamWorldBlock;
import zaexides.steamworld.fluids.FluidSteam;
import zaexides.steamworld.gui.GuiHandler;
import zaexides.steamworld.te.TileEntitySteamGenerator;
import zaexides.steamworld.te.TileEntityValve;

public class BlockSteamGenerator extends BlockMachine implements ITileEntityProvider
{
	public int speed = 4, capacity = 4;
	
	public BlockSteamGenerator(String name, Material material, float hardness, int speed, int capacity)
	{
		super(name, material, hardness);
		this.speed = speed;
		this.capacity = capacity;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) 
	{
		if(!worldIn.isRemote)
		{
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if(!(tileEntity instanceof TileEntitySteamGenerator))
				return false;
			
			ItemStack heldItem = playerIn.getHeldItem(hand);
			TileEntitySteamGenerator te = (TileEntitySteamGenerator)tileEntity;
			
			int remainingSpace = te.fluidIn.getCapacity() - te.fluidIn.getFluidAmount();
			
			if(heldItem.getItem() == Items.WATER_BUCKET && remainingSpace >= Fluid.BUCKET_VOLUME)
			{
				FluidUtil.interactWithFluidHandler(playerIn, hand, te.fluidIn);
				
				worldIn.playSound((EntityPlayer)null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
			}
			else
				playerIn.openGui(SteamWorld.singleton, GuiHandler.STEAM_GENERATOR, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}
	
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
			double y = pos.getY() + 0.5d + ((rand.nextDouble() * 0.25d) - 0.125d) + 0.1d;
			double z = pos.getZ() + 0.5d + (offset.getZ() * 0.55d) + (sideOff.getZ() * randOff);
			
			worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0, 0, 0);
			worldIn.spawnParticle(EnumParticleTypes.FLAME, x, y, z, 0, 0, 0);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		TileEntity tEntity = new TileEntitySteamGenerator();
		((TileEntitySteamGenerator)tEntity).SetStats(capacity, speed);
		return tEntity;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) 
	{
		if(!keepInventory)
		{
			IItemHandler itemHandler = ((TileEntitySteamGenerator)worldIn.getTileEntity(pos)).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			for(int i = 0; i < itemHandler.getSlots(); i++)
			{
				ItemStack stack = itemHandler.getStackInSlot(i);
				InventoryHelper.spawnItemStack(worldIn, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, stack);
			}
		}
		super.breakBlock(worldIn, pos, state);
	}
}
