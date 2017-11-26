package zaexides.steamworld.worldgen.structures.dwarven;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import org.apache.logging.log4j.Level;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import scala.languageFeature.postfixOps;
import scala.reflect.internal.Trees.This;
import scala.sys.process.processInternal;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.utility.PosDir;

public class WorldGenDwarvenStructure extends WorldGenerator
{
	private DwarvenStructureRoom room_core;
	public List<DwarvenStructureRoom> rooms_north = new ArrayList<DwarvenStructureRoom>();
	public List<DwarvenStructureRoom> rooms_south = new ArrayList<DwarvenStructureRoom>();
	public List<DwarvenStructureRoom> rooms_west = new ArrayList<DwarvenStructureRoom>();
	public List<DwarvenStructureRoom> rooms_east = new ArrayList<DwarvenStructureRoom>();
	public List<DwarvenStructureRoom> rooms_up = new ArrayList<DwarvenStructureRoom>();
	public List<DwarvenStructureRoom> rooms_down = new ArrayList<DwarvenStructureRoom>();
	
	private Queue<PosDir> nextPositions;
	private List<BlockPos> claimed;
	
	private static final int ROOM_DISTANCE = 10;

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
	{
		Init(rand);
		
		int maxSize = 16 + rand.nextInt(48);
		int size = 0;
		nextPositions = new LinkedList<PosDir>();
		claimed = new ArrayList<BlockPos>();
		
		room_core.Generate(worldIn, position.getX(), position.getY(), position.getZ());
		claimed.add(position);
		nextPositions.offer(new PosDir(position.add(0, 0, -ROOM_DISTANCE), (byte)0));
		nextPositions.offer(new PosDir(position.add(-ROOM_DISTANCE, 0, 0), (byte)1));
		nextPositions.offer(new PosDir(position.add(0, 0, ROOM_DISTANCE), (byte)2));
		nextPositions.offer(new PosDir(position.add(ROOM_DISTANCE, 0, 0), (byte)3));
		
		while(nextPositions.size() > 0 && size < maxSize)
		{
			PosDir posDir = nextPositions.poll();
			if(GenerateAt(worldIn, rand, posDir))
				size++;
		}
		
		nextPositions.clear();
		claimed.clear();
		return true;
	}
	
	private boolean GenerateAt(World world, Random rand, PosDir posDir)
	{
		if(claimed.contains(posDir.position))
			return false;
		
		SteamWorld.logger.log(Level.INFO, "Generating at " + posDir.position);
		if(!world.getChunkFromBlockCoords(posDir.position).isLoaded())
			return false;
		
		DwarvenStructureRoom room;
		List<DwarvenStructureRoom> list;
		switch(posDir.direction)
		{
			case 1:
				list = rooms_west;
				break;
			case 2:
				list = rooms_south;
				break;
			case 3:
				list = rooms_east;
				break;
			case 4:
				list = rooms_up;
				break;
			case 5:
				list = rooms_down;
				break;
			default:
				list = rooms_north;
				break;
		}

		room = list.get(rand.nextInt(list.size()));
		room.Generate(world, posDir.position.getX(), posDir.position.getY(), posDir.position.getZ());
		claimed.add(posDir.position);
		if((room.directions & (1 << 0)) > 0)
			nextPositions.offer(new PosDir(posDir.position.add(0, 0, -ROOM_DISTANCE), (byte)0));
		if((room.directions & (1 << 1)) > 0)
			nextPositions.offer(new PosDir(posDir.position.add(-ROOM_DISTANCE, 0, 0), (byte)1));
		if((room.directions & (1 << 2)) > 0)
			nextPositions.offer(new PosDir(posDir.position.add(0, 0, ROOM_DISTANCE), (byte)2));
		if((room.directions & (1 << 3)) > 0)
			nextPositions.offer(new PosDir(posDir.position.add(ROOM_DISTANCE, 0, 0), (byte)3));
		if((room.directions & (1 << 4)) > 0)
			nextPositions.offer(new PosDir(posDir.position.add(0, ROOM_DISTANCE, 0), (byte)4));
		if((room.directions & (1 << 5)) > 0)
			nextPositions.offer(new PosDir(posDir.position.add(0, -ROOM_DISTANCE, 0), (byte)5));
		return true;
	}
	
	private void Init(Random rand)
	{
		room_core = new DwarvenStructureRoomCore(rand, this);
		new DwarvenStructureRoomHallwayNS(rand, this);
		new DwarvenStructureRoomHallwayEW(rand, this);
		new DwarvenStructureRoomRoom(rand, this);
		new DwarvenStructureRoomTowerBase(rand, this);
		new DwarvenStructureRoomTowerTop(rand, this);
		new DwarvenStructureRoomTowerCenter(rand, this);
		new DwarvenStructureRoomHallwayCross(rand, this);
	}
	
	public void SetBlock(World world, BlockPos pos, IBlockState blockState)
	{
		setBlockAndNotifyAdequately(world, pos, blockState);
	}

}
