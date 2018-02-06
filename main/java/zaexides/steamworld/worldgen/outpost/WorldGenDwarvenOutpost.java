package zaexides.steamworld.worldgen.outpost;

import java.util.Random;

import org.apache.logging.log4j.Level;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import zaexides.steamworld.LootTableInitializer;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.BlockAncite;
import zaexides.steamworld.blocks.BlockInitializer;

public class WorldGenDwarvenOutpost extends WorldGenerator
{
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position) 
	{
		if(!worldIn.getChunkFromBlockCoords(position).isLoaded())
			return false;
		
		BlockPos start = position.add(-1,0,-1);
		BlockPos end = position.add(1,0,1);
		
		CreateFloor(start, end, worldIn, rand);
		CreatePillar(position.add(-1,1,-1), 3, worldIn);
		CreatePillar(position.add(1,1,-1), 3, worldIn);
		CreatePillar(position.add(-1,1,1), 3, worldIn);
		CreatePillar(position.add(1,1,1), 3, worldIn);
		
		CreateChest(position.add(0,1,0), worldIn);
		
		return true;
	}
	
	private void CreateFloor(BlockPos start, BlockPos end, World world, Random rand)
	{
		for(int x = start.getX(); x <= end.getX(); x++)
		{
			for(int z = start.getZ(); z <= end.getZ(); z++)
			{
				int y = start.getY();
				BlockPos pos = new BlockPos(x,y,z);
				int attempts = 0;
				while((world.getBlockState(pos).getBlock() == Blocks.GRASS || world.isAirBlock(pos)) && attempts < 80)
				{
					IBlockState blockState = BlockInitializer.BLOCK_ANCITE.getDefaultState().withProperty(BlockAncite.VARIANT, BlockAncite.EnumType.TILES);
					if(rand.nextFloat() > 0.35f)
						blockState = Blocks.COBBLESTONE.getDefaultState();
					
					world.setBlockState(pos, blockState);
					pos = pos.add(0,-1,0);
					attempts++;
				}
			}
		}
	}
	
	private void CreatePillar(BlockPos pos, int height, World world)
	{
		for(int y = 0; y < height; y++)
		{
			BlockPos blockPos = pos.add(0,y,0);
			IBlockState blockState = BlockInitializer.BLOCK_ANCITE.getDefaultState().withProperty(BlockAncite.VARIANT, BlockAncite.EnumType.BRICKS);
			world.setBlockState(blockPos, blockState);
		}
	}
	
	private void CreateChest(BlockPos pos, World world)
	{
		world.setBlockState(pos, Blocks.CHEST.correctFacing(world, pos, Blocks.CHEST.getDefaultState()));
		TileEntity tileEntity = world.getTileEntity(pos);
		if(tileEntity instanceof TileEntityChest)
		{
			//((TileEntityChest)tileEntity).setLootTable(LootTableInitializer.CHEST_DWARVEN_CORE, world.rand.nextLong());
		}
	}
}
