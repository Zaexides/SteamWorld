package zaexides.steamworld.blocks.machines;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.SteamWorldBlock;
import zaexides.steamworld.items.ItemUpgrade.EnumUpgradeType;
import zaexides.steamworld.te.TileEntitySteamGenerator;

public class BlockMachine extends SteamWorldBlock implements IWrenchable, IUpgradeable
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool ACTIVE = PropertyBool.create("active");
	public static final PropertyBool HIGH_TIER = PropertyBool.create("high_tier");
	protected boolean keepInventory;
	
	protected BlockMachine upgradeBlock;
	protected byte currentTier = 0;
	
	public BlockMachine(String name, Material material, float hardness)
	{
		super(name, material, hardness, hardness * 5, 64, SteamWorld.CREATIVETAB_UTILITY);
	}
	
	public BlockMachine(String name, Material material, float hardness, float resistance)
	{
		super(name, material, hardness, resistance, 64, SteamWorld.CREATIVETAB_UTILITY);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) 
	{
		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) 
	{
		return state
				.withProperty(FACING, state.getValue(FACING))
				.withProperty(ACTIVE, state.getValue(ACTIVE))
				.withProperty(HIGH_TIER, state.getValue(HIGH_TIER));
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState()
				.withProperty(FACING, EnumFacing.getFront((meta & 3) + 2))
				.withProperty(ACTIVE, (meta & 8) == 8)
				.withProperty(HIGH_TIER, (meta & 4) == 4);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(FACING).getIndex()-2 + (state.getValue(ACTIVE) ? 8 : 0) + (state.getValue(HIGH_TIER) ? 4 : 0);
	}
	
	@Override
	protected BlockStateContainer createBlockState() 
	{
		return new BlockStateContainer(this, FACING, ACTIVE, HIGH_TIER);
	}
	
	public void UpdateState(World world, BlockPos pos, boolean active) 
	{
		keepInventory = true;
		
		IBlockState blockState = world.getBlockState(pos);
		TileEntity tileEntity = world.getTileEntity(pos);
		
		world.setBlockState(pos, blockState.withProperty(FACING, blockState.getValue(FACING)).withProperty(ACTIVE, active).withProperty(HIGH_TIER, blockState.getValue(HIGH_TIER)));
		
		if (tileEntity != null)
        {
			tileEntity.validate();
            world.setTileEntity(pos, tileEntity);
        }
		
		keepInventory = false;
	}

	@Override
	public EnumActionResult onWrenchUse(World world, EntityPlayer player, BlockPos pos, EnumFacing facing,
			IBlockState blockState) 
	{
		if(removedByPlayer(blockState, world, pos, player, true))
		{
			harvestBlock(world, player, pos, blockState, null, ItemStack.EMPTY);
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.FAIL;
	}
	
	public BlockMachine SetUpgradeData(BlockMachine blockMachine, byte currentTier)
	{
		this.upgradeBlock = blockMachine;
		this.currentTier = currentTier;
		return this;
	}
	
	public void setMachineStats(TileEntity tileEntity) {}

	@Override
	public EnumActionResult OnUpgradeItemUse(EnumUpgradeType upgradeType, World world, BlockPos pos, ItemStack itemStack, EntityPlayer player) 
	{
		if(upgradeBlock == null)
			return EnumActionResult.FAIL;
		
		EnumUpgradeType upgradeRequired = EnumUpgradeType.byMetadata(currentTier);
		
		if(upgradeType == upgradeRequired)
		{
			TileEntity originalTileEntity = world.getTileEntity(pos);
			keepInventory = true;
			world.setBlockState(pos, upgradeBlock.getStateFromMeta(this.getMetaFromState(world.getBlockState(pos))));
			if(originalTileEntity != null)
			{
				originalTileEntity.validate();
				world.setTileEntity(pos, originalTileEntity);
				upgradeBlock.setMachineStats(originalTileEntity);
			}
			keepInventory = false;
			
			itemStack.shrink(1);
			return EnumActionResult.SUCCESS;
		}
		else
		{
			if(!world.isRemote)
			{
				TextComponentTranslation componentTranslation = new TextComponentTranslation("message.steamworld.upgrade_fail");
				player.sendMessage(componentTranslation);
			}
			return EnumActionResult.FAIL;
		}
	}
}
