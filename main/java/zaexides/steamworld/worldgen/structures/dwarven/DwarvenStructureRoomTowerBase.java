package zaexides.steamworld.worldgen.structures.dwarven;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DwarvenStructureRoomTowerBase extends DwarvenStructureRoom
{	
	public DwarvenStructureRoomTowerBase(Random rand, WorldGenDwarvenStructure worldGen) 
	{
		super(rand, worldGen);
		//          north   west     south        east        up        down
		directions = 1 + (1 << 1) + (1 << 2) + (1 << 3) + (1 << 4) + (0 << 5);
		Register();
	}
	
	@Override
	public void Generate(World world, int x, int y, int z)
	{
		BlockPos start = new BlockPos(x, y, z);
		BlockPos end = start.add(10, 10, 10);
		CreateRoomBase(world, start, end);
		
		BlockPos doorStart = new BlockPos(x+4,y+1,z);
		BlockPos doorEnd = new BlockPos(x+6,y+4,z+10);
		FillArea(world, doorStart, doorEnd, AIR);
		
		doorStart = new BlockPos(x,y+1,z+4);
		doorEnd = new BlockPos(x+10,y+4,z+6);
		FillArea(world, doorStart, doorEnd, AIR);

		BlockPos ceilingStart = new BlockPos(start.getX() + 1, end.getY()-1, start.getZ() + 1);
		BlockPos ceilingEnd = new BlockPos(end.getX() - 1, end.getY(), end.getZ() - 1);
		FillArea(world, ceilingStart, ceilingEnd, AIR);
		
		FillArea(world, start.add(4,1,4), end.add(-4,1,-4), GOLD_STEAITE);
		FillArea(world, start.add(4,1,3), end.add(-4,1,-6), Blocks.LADDER.getDefaultState());
		
		GenerateDecoration(world, start.add(1,1,1), end.add(-1,-1,-1));
	}
}
