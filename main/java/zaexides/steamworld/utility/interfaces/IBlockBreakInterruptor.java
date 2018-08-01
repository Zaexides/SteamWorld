package zaexides.steamworld.utility.interfaces;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;

public interface IBlockBreakInterruptor {
	public boolean CanBreakBlock(IBlockState blockState, EntityPlayer player, boolean isHarvestable);
}
