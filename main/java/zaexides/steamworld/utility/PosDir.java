package zaexides.steamworld.utility;

import net.minecraft.util.math.BlockPos;

public class PosDir 
{
	public BlockPos position;
	public byte direction;
	
	public PosDir(BlockPos position, byte direction)
	{
		this.position = position;
		this.direction = direction;
	}
}
