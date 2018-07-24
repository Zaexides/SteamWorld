package zaexides.steamworld.entity.interfaces;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public interface IEntityBreakBlockCallback 
{
	void OnBlockBroken(Block block, BlockPos blockPos);
	boolean CanBreakBlocks();
}
