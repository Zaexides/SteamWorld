package zaexides.steamworld.worldgen.structures.dwarven;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DwarvenStructureRoomHallwayCross extends DwarvenStructureRoom
{
	public DwarvenStructureRoomHallwayCross(Random rand, WorldGenDwarvenStructure worldGen) 
	{
		super(rand, worldGen);
		//      north   west     south        east        up        down
		directions = 1 + (1 << 1) + (1 << 2) + (1 << 3) + (0 << 4) + (0 << 5);
		Register();
		Register();
		Register();
	}

	@Override
	public void Generate(World world, int x, int y, int z)
	{
		BlockPos start = new BlockPos(x, y, z+3);
		BlockPos end = start.add(10, 5, 4);
		CreateRoomBase(world, start, end);
		start = new BlockPos(x+3, y, z);
		end = start.add(4, 5, 10);
		CreateRoomBase(world, start, end);
		
		BlockPos doorStart = new BlockPos(x,y+1,z+4);
		BlockPos doorEnd = new BlockPos(x+10,y+4,z+6);
		FillArea(world, doorStart, doorEnd, AIR);
		doorStart = new BlockPos(x+4,y+1,z);
		doorEnd = new BlockPos(x+6,y+4,z+10);
		FillArea(world, doorStart, doorEnd, AIR);
	}
}
