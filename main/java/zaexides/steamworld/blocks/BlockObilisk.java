package zaexides.steamworld.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.gui.GuiHandler;
import zaexides.steamworld.te.TileEntityObilisk;

public class BlockObilisk extends SteamWorldBlock implements ITileEntityProvider
{
	public static final PropertyInteger PART = PropertyInteger.create("part", 0, 2);
	
	public BlockObilisk(String name, Material material, float hardness) 
	{
		super(name, material, hardness, hardness * 5, 1);
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos,
			EnumFacing side) 
	{
		return false;
	}
	
	@Override
	public boolean isBlockNormalCube(IBlockState state) 
	{
		return false;
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
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		if(getMetaFromState(state) > 0)
			return new AxisAlignedBB(.28, 0, .28, .72, 1, .72);
		return FULL_BLOCK_AABB;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) 
	{
		return getBoundingBox(blockState, worldIn, pos);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) 
	{
		return this.getDefaultState().withProperty(PART, meta);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) 
	{
		return state.getValue(PART);
	}
	
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, PART);
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) 
	{
		IBlockState blockState = worldIn.getBlockState(pos.up());
		IBlockState blockState2 = worldIn.getBlockState(pos.up().up());
		return 
				(blockState == null || blockState.getBlock() instanceof BlockLiquid || blockState.getBlock() == Blocks.AIR)
				&& (blockState2 == null || blockState2.getBlock() instanceof BlockLiquid || blockState2.getBlock() == Blocks.AIR);
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) 
	{
		if(getMetaFromState(state) == 0)
			worldIn.setBlockState(pos.up(), getStateFromMeta(1));
		else if(getMetaFromState(state) == 1)
			worldIn.setBlockState(pos.up(), getStateFromMeta(2));
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) 
	{
		IBlockState ownState = worldIn.getBlockState(pos);
		BlockPos otherPos = pos;
		
		int meta = getMetaFromState(ownState);
		
		if(meta == 0)
			otherPos = pos.up();
		else
			otherPos = pos.down();
				
		if(!worldIn.getBlockState(otherPos).getBlock().equals(this))
			worldIn.destroyBlock(pos, false);
		
		if(meta == 1)
			otherPos = pos.up();
		if(!worldIn.getBlockState(otherPos).getBlock().equals(this))
			worldIn.destroyBlock(pos, false);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) 
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos.down(getMetaFromState(state)));
		if(tileEntity != null && tileEntity instanceof TileEntityObilisk)
		{
			if(worldIn.isRemote && ((TileEntityObilisk)tileEntity).textId != -1)
				playerIn.openGui(SteamWorld.singleton, GuiHandler.OBILISK, worldIn, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		return false;
	}
	
	public boolean canGenerateOn(Block block, World world, BlockPos pos)
	{
		return !(block.isReplaceable(world, pos)) &&
			   block != Blocks.AIR;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		if(meta != 0)
			return null;
		TileEntityObilisk tileEntityObilisk = new TileEntityObilisk();
		return tileEntityObilisk;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) 
	{
		if(getMetaFromState(state) == 0)
		{
			TileEntity te = worldIn.getTileEntity(pos);
			
			if(te != null && te instanceof TileEntityObilisk)
			{
				TileEntityObilisk tileEntityObilisk = (TileEntityObilisk)te;
				NBTTagCompound nbtTagCompound = new NBTTagCompound();
				nbtTagCompound.setInteger("text_id", tileEntityObilisk.textId);
				nbtTagCompound.setInteger("name_id", tileEntityObilisk.nameId);
				ItemStack itemStack = new ItemStack(this, 1);
				itemStack.setTagCompound(nbtTagCompound);
				spawnAsEntity(worldIn, pos, itemStack);
			}
		}
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state,
			int fortune) 
	{
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) 
	{
		if(getMetaFromState(state) != 0)
			return;
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if(tileEntity != null && tileEntity instanceof TileEntityObilisk)
		{
			TileEntityObilisk tileEntityObilisk = (TileEntityObilisk)tileEntity;
			NBTTagCompound nbtTagCompound = stack.getTagCompound();
			if(nbtTagCompound != null && nbtTagCompound.hasKey("text_id") && nbtTagCompound.hasKey("name_id"))
			{
				tileEntityObilisk.textId = nbtTagCompound.getInteger("text_id");
				tileEntityObilisk.nameId = nbtTagCompound.getInteger("name_id");
			}
		}
	}
}
