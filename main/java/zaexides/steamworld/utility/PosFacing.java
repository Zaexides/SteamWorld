package zaexides.steamworld.utility;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class PosFacing 
{
	public BlockPos blockPos;
	public EnumFacing facing;
	
	public PosFacing(BlockPos blockPos, EnumFacing facing) 
	{
		this.blockPos = blockPos;
		this.facing = facing;
	}
}
