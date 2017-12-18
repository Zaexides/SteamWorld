package zaexides.steamworld.worldgen.structures.dwarven;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import scala.annotation.elidable;
import scala.collection.parallel.ParIterableLike.Min;
import scala.sys.process.processInternal;
import zaexides.steamworld.blocks.BlockAncite;
import zaexides.steamworld.blocks.BlockInitializer;
import zaexides.steamworld.utility.PosDir;

public class DwarvenStructureRoom
{
	public static final IBlockState FLOOR = BlockInitializer.BLOCK_ANCITE.getDefaultState().withProperty(BlockAncite.VARIANT, BlockAncite.EnumType.TILES);
	public static final IBlockState WALL = BlockInitializer.BLOCK_ANCITE.getDefaultState().withProperty(BlockAncite.VARIANT, BlockAncite.EnumType.BRICKS);
	public static final IBlockState CEILING = BlockInitializer.BLOCK_ANCITE.getDefaultState().withProperty(BlockAncite.VARIANT, BlockAncite.EnumType.PLATES);
	public static final IBlockState GOLD_STEAITE = BlockInitializer.BLOCK_STEAITE_GOLD.getDefaultState();
	public static final IBlockState AIR = Blocks.AIR.getDefaultState();
	
	public Random rand;
	private WorldGenDwarvenStructure worldGen;
	
	private static final List<IBlockState> ALLOWED_OVERWRITES = new ArrayList<IBlockState>();
	
	public byte directions = 0;
	
	public DwarvenStructureRoom(Random rand, WorldGenDwarvenStructure worldGen)
	{
		this.rand = rand;
		this.worldGen = worldGen;
		
		if(ALLOWED_OVERWRITES.size() == 0)
		{
			ALLOWED_OVERWRITES.add(Blocks.STONE.getDefaultState());
			ALLOWED_OVERWRITES.add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE));
			ALLOWED_OVERWRITES.add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE));
			ALLOWED_OVERWRITES.add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE));
			ALLOWED_OVERWRITES.add(AIR);
			ALLOWED_OVERWRITES.add(Blocks.DIRT.getDefaultState());
			ALLOWED_OVERWRITES.add(Blocks.GRAVEL.getDefaultState());
			ALLOWED_OVERWRITES.add(Blocks.SAND.getDefaultState());
			ALLOWED_OVERWRITES.add(Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND));
			ALLOWED_OVERWRITES.add(Blocks.CLAY.getDefaultState());
			ALLOWED_OVERWRITES.add(Blocks.SANDSTONE.getDefaultState());
			
			ALLOWED_OVERWRITES.add(FLOOR);
			ALLOWED_OVERWRITES.add(CEILING);
			ALLOWED_OVERWRITES.add(WALL);
			ALLOWED_OVERWRITES.add(GOLD_STEAITE);
		}
	}
	
	public void Register() 
	{
		if((directions & (1 << 0)) > 0)
			worldGen.rooms_south.add(this);
		if((directions & (1 << 1)) > 0)
			worldGen.rooms_east.add(this);
		if((directions & (1 << 2)) > 0)
			worldGen.rooms_north.add(this);
		if((directions & (1 << 3)) > 0)
			worldGen.rooms_west.add(this);
		if((directions & (1 << 4)) > 0)
			worldGen.rooms_down.add(this);
		if((directions & (1 << 5)) > 0)
			worldGen.rooms_up.add(this);
	}
	
	public void Generate(World world, int x, int y, int z)
	{
		
	}
	
	public void SetBlock(World world, BlockPos pos, IBlockState blockState)
	{
		SetBlock(world, pos, blockState, false);
	}
	
	public void SetBlock(World world, BlockPos pos, IBlockState blockState, boolean notify)
	{
		if(pos.getY() > 0 && pos.getY() < 255)
		{
			if(ALLOWED_OVERWRITES.contains(world.getBlockState(pos)) || (world.getBlockState(pos) == Blocks.WATER.getDefaultState() && blockState != AIR) || world.getBlockState(pos).getBlock().getRegistryName().toString().toLowerCase().contains("ore"))
			{
				worldGen.SetBlock(world, pos, blockState);
				if(notify)
					world.notifyBlockUpdate(pos, AIR, AIR, 0);
			}
		}
	}
	
	public void FillArea(World world, BlockPos start, BlockPos end, IBlockState blockState)
	{
		for(int x = start.getX(); x < end.getX(); x++)
		{
			for(int y = start.getY(); y < end.getY(); y++)
			{
				for(int z = start.getZ(); z < end.getZ(); z++)
				{
					SetBlock(world, new BlockPos(x,y,z), blockState);
				}
			}
		}
	}
	
	public void GenerateDecoration(World world, BlockPos start, BlockPos end)
	{
		boolean cobwebs = rand.nextFloat() <= .15f;
		boolean slime = rand.nextFloat() <= .01f;
		boolean gravel = rand.nextFloat() <= .25f;
		boolean ice = world.getBiome(start).isSnowyBiome() && rand.nextFloat() <= .95f;
		boolean lava = start.getY() < 26 && rand.nextFloat() <= .05f;
		
		if(cobwebs)
		{
			MaybeFillArea(world, start, end, Blocks.WEB.getDefaultState(), .2f, false);
		}
		if(slime)
		{
			MaybeFillArea(world, start, end, Blocks.SLIME_BLOCK.getDefaultState(), .05f, false);
		}
		if(gravel)
		{
			MaybeFillArea(world, start, end, Blocks.GRAVEL.getDefaultState(), .1f, false);
		}
		if(ice)
		{
			MaybeFillArea(world, start, end, Blocks.ICE.getDefaultState(), .3f, false);
			MaybeFillArea(world, start, end, Blocks.PACKED_ICE.getDefaultState(), .1f, false);
			MaybeFillArea(world, start, end, Blocks.SNOW.getDefaultState(), .2f, false);
		}
		if(lava)
		{
			MaybeFillArea(world, start, end, Blocks.LAVA.getDefaultState(), .1f, true);
		}
	}
	
	public void MaybeFillArea(World world, BlockPos start, BlockPos end, IBlockState blockState, float chance, boolean notify)
	{
		for(int x = start.getX(); x < end.getX(); x++)
		{
			for(int y = start.getY(); y < end.getY(); y++)
			{
				for(int z = start.getZ(); z < end.getZ(); z++)
				{
					if(rand.nextFloat() <= chance && HasNeighbour(world, new BlockPos(x,y,z)))
						SetBlock(world, new BlockPos(x,y,z), blockState);
				}
			}
		}
	}
	
	public boolean HasNeighbour(World world, BlockPos pos)
	{
		for(int x = -1; x < 2; x += 2)
		{
			for(int y = -1; y < 2; y += 2)
			{
				for(int z = -1; z < 2; z += 2)
				{
					int magnitude = x+y+z;
					if(magnitude == 1 || magnitude == -1)
					{
						if(world.getBlockState(pos.add(x,y,z)) != AIR)
							return true;
					}
				}
			}
		}
		return false;
	}
	
	public void CreateRoomBase(World world, BlockPos start, BlockPos end)
	{
		for(int x = start.getX(); x < end.getX(); x++)
		{
			for(int y = start.getY(); y < end.getY(); y++)
			{
				for(int z = start.getZ(); z < end.getZ(); z++)
				{
					BlockPos pos = new BlockPos(x, y, z);
					switch(GetExtremeCount(pos, start, end))
					{
						case 0:
							SetBlock(world, pos, AIR);
						break;
						
						case 1:
							if(pos.getY() == start.getY())
								SetBlock(world, pos, FLOOR);
							else if(pos.getY() == end.getY()-1)
								SetBlock(world, pos, CEILING);
							else
							{
								if(pos.getY() == start.getY()+1)
									SetBlock(world, pos, GOLD_STEAITE);
								else
									SetBlock(world, pos, WALL);
							}
						break;
						
						case 2:
						case 3:
							SetBlock(world, pos, GOLD_STEAITE);
						break;
					}
				}
			}
		}
	}
	
	public int GetExtremeCount(BlockPos pos, BlockPos min, BlockPos max)
	{
		int count = 0;
		if(pos.getX() == min.getX() || pos.getX() == max.getX()-1)
			count++;
		if(pos.getY() == min.getY() || pos.getY() == max.getY()-1)
			count++;
		if(pos.getZ() == min.getZ() || pos.getZ() == max.getZ()-1)
			count++;
		return count;
	}
}
