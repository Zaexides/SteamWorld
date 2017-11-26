package zaexides.steamworld.worldgen.structures.dwarven;

import java.util.Random;

import org.apache.logging.log4j.Level;

import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zaexides.steamworld.LootTableInitializer;
import zaexides.steamworld.SteamWorld;

public class DwarvenStructureRoomRoom extends DwarvenStructureRoom
{	
	public DwarvenStructureRoomRoom(Random rand, WorldGenDwarvenStructure worldGen) 
	{
		super(rand, worldGen);
		//          north   west     south        east        up        down
		directions = 1 + (1 << 1) + (1 << 2) + (1 << 3) + (0 << 4) + (0 << 5);
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
		
		for(int xOff = start.getX()+1; xOff < end.getX()-1; xOff++)
		{
			for(int zOff = start.getZ()+1; zOff < end.getZ()-1; zOff++)
			{
				int yOff = start.getY()+1;
				if(rand.nextFloat() > 0.99f)
				{
					BlockPos pos = new BlockPos(xOff, yOff, zOff);
					world.setBlockState(pos, Blocks.CHEST.correctFacing(world, pos, Blocks.CHEST.getDefaultState()));
					TileEntity tileEntity = world.getTileEntity(pos);
					if(tileEntity instanceof TileEntityChest)
					{
						((TileEntityChest)tileEntity).setLootTable(LootTableInitializer.CHEST_DWARVEN_STRUCTURE, rand.nextLong());
					}
				}
			}
		}
		
		GenerateDecoration(world, start.add(1,1,1), end.add(-1,-1,-1));
	}
}
