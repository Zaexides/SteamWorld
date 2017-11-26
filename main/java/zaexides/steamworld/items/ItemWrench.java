package zaexides.steamworld.items;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zaexides.steamworld.utility.IWrenchable;

public class ItemWrench extends SteamWorldItem
{
	public ItemWrench(String name) 
	{
		super(name);
		setMaxStackSize(1);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) 
	{
		tooltip.add("Easily Remove/Rotate SteamWorld machines.");
	}
	
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX,
			float hitY, float hitZ, EnumHand hand) 
	{
		IBlockState blockState = world.getBlockState(pos);
		if(blockState.getBlock() instanceof IWrenchable)
			return ((IWrenchable)blockState.getBlock()).onWrenchUse(world, player, pos, side, blockState);
		else
			return EnumActionResult.FAIL;
	}
}
