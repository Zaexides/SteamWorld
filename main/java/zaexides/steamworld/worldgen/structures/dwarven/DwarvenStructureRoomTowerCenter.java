package zaexides.steamworld.worldgen.structures.dwarven;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DwarvenStructureRoomTowerCenter extends DwarvenStructureRoom
{	
	public DwarvenStructureRoomTowerCenter(Random rand, WorldGenDwarvenStructure worldGen) 
	{
		super(rand, worldGen);
		//          north   west     south        east        up        down
		directions = 0 + (0 << 1) + (0 << 2) + (0 << 3) + (1 << 4) + (1 << 5);
		Register();
	}
	
	@Override
	public void Generate(World world, int x, int y, int z)
	{
		BlockPos start = new BlockPos(x, y, z);
		BlockPos end = start.add(10, 10, 10);
		CreateRoomBase(world, start, end);

		BlockPos middleStart = new BlockPos(start.getX() + 1, start.getY()-1, start.getZ() + 1);
		BlockPos middleEnd = new BlockPos(end.getX() - 1, end.getY()+1, end.getZ() - 1);
		FillArea(world, middleStart, middleEnd, AIR);
		
		FillArea(world, start.add(4,-1,4), end.add(-4,1,-4), GOLD_STEAITE);
		FillArea(world, start.add(4,-1,3), end.add(-4,1,-6), Blocks.LADDER.getDefaultState());
		
		GenerateDecoration(world, start.add(1,1,1), end.add(-1,-1,-1));
	}
}
