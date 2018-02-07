package zaexides.steamworld.blocks;

import org.apache.logging.log4j.Level;

import akka.event.Logging.Debug;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.te.TileEntityValve;

public class BlockObilisk extends SteamWorldBlock
{
	public static final PropertyBool TOP_PART = PropertyBool.create("top_part");
	
	public BlockObilisk(String name, Material material, float hardness) 
	{
		super(name, material, hardness);
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
			return new AxisAlignedBB(.3, 0, .3, .7, 1, .7);
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
		return this.getDefaultState().withProperty(TOP_PART, meta > 0);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) 
	{
		return state.getValue(TOP_PART) ? 1 : 0;
	}
	
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, TOP_PART);
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) 
	{
		IBlockState blockState = worldIn.getBlockState(pos.up());
		return blockState == null || blockState.getBlock() instanceof BlockLiquid || blockState.getBlock() == Blocks.AIR;
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) 
	{
		if(getMetaFromState(state) == 0)
			worldIn.setBlockState(pos.up(), getStateFromMeta(1));
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) 
	{
		IBlockState ownState = worldIn.getBlockState(pos);
		BlockPos otherPos = pos;
		if(getMetaFromState(ownState) == 0)
			otherPos = pos.up();
		else
			otherPos = pos.down();
				
		if(!worldIn.getBlockState(otherPos).getBlock().equals(this))
			worldIn.destroyBlock(pos, false);
	}
	
	public boolean canGenerateOn(Block block)
	{
		return !(block instanceof BlockLeaves) &&
			   !(block instanceof BlockLiquid) &&
			   !(block instanceof BlockLog) &&
			   block != Blocks.AIR;
	}
}
