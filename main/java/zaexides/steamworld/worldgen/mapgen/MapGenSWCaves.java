package zaexides.steamworld.worldgen.mapgen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.gen.MapGenCaves;
import zaexides.steamworld.blocks.BlockDecorative;
import zaexides.steamworld.init.BlockInitializer;

public class MapGenSWCaves extends MapGenCaves
{
	@Override
	protected boolean canReplaceBlock(IBlockState p_175793_1_, IBlockState p_175793_2_) 
	{
		if(p_175793_1_ == BlockInitializer.BLOCK_DECORATIVE.getStateFromMeta(BlockDecorative.EnumType.SKY_STONE.getMeta()))
			return true;
		else
			return super.canReplaceBlock(p_175793_1_, p_175793_2_);
	}
}
