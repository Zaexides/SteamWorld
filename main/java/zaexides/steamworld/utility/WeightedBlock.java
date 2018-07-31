package zaexides.steamworld.utility;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.WeightedRandom;

public class WeightedBlock extends WeightedRandom.Item
{
	public final IBlockState blockState;
	
	public WeightedBlock(IBlockState blockState, int itemWeightIn) 
	{
		super(itemWeightIn);
		this.blockState = blockState;
	}
	
	@Override
	public boolean equals(Object obj) 
	{
		return obj instanceof WeightedBlock && blockState.equals(((WeightedBlock)obj).blockState);
	}
}
