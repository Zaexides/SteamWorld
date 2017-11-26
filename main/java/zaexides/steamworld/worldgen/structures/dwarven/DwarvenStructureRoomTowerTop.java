package zaexides.steamworld.worldgen.structures.dwarven;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DwarvenStructureRoomTowerTop extends DwarvenStructureRoom
{	
	public DwarvenStructureRoomTowerTop(Random rand, WorldGenDwarvenStructure worldGen) 
	{
		super(rand, worldGen);
		//          north   west     south        east        up        down
		directions = 1 + (1 << 1) + (1 << 2) + (1 << 3) + (0 << 4) + (1 << 5);
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

		BlockPos floorStart = new BlockPos(start.getX() + 3, start.getY(), start.getZ() + 3);
		BlockPos floorEnd = new BlockPos(end.getX() - 3, start.getY()+1, end.getZ() - 3);
		FillArea(world, floorStart, floorEnd, AIR);
		
		FillArea(world, start.add(4,-1,4), end.add(-4,-6,-4), GOLD_STEAITE);
		FillArea(world, start.add(4,-1,3), end.add(-4,-6,-6), Blocks.LADDER.getDefaultState());
		FillArea(world, start.add(4,4,0), end.add(-4,-5, 0), GOLD_STEAITE);
		FillArea(world, start.add(0,4,4), end.add(0,-5, -4), GOLD_STEAITE);
		
		GenerateDecoration(world, start.add(1,1,1), end.add(-1,-1,-1));
	}
}
