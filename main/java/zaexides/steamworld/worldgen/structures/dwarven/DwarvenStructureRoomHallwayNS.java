package zaexides.steamworld.worldgen.structures.dwarven;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DwarvenStructureRoomHallwayNS extends DwarvenStructureRoom
{
	public DwarvenStructureRoomHallwayNS(Random rand, WorldGenDwarvenStructure worldGen) 
	{
		super(rand, worldGen);
		//      north   west     south        east        up        down
		directions = 1 + (0 << 1) + (1 << 2) + (0 << 3) + (0 << 4) + (0 << 5);
		Register();
		Register();
		Register();
	}

	@Override
	public void Generate(World world, int x, int y, int z)
	{
		BlockPos start = new BlockPos(x+3, y, z);
		BlockPos end = start.add(4, 5, 10);
		CreateRoomBase(world, start, end);
		
		BlockPos doorStart = new BlockPos(x+4,y+1,z);
		BlockPos doorEnd = new BlockPos(x+6,y+4,z+10);
		FillArea(world, doorStart, doorEnd, AIR);
	}
}
