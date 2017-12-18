package zaexides.steamworld.blocks.machines;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zaexides.steamworld.blocks.SteamWorldBlock;
import zaexides.steamworld.items.ItemInitializer;
import zaexides.steamworld.te.TileEntityNetherAccelerator;

public class BlockNetherAccelerator extends SteamWorldBlock implements ITileEntityProvider, IWrenchable
{
	public BlockNetherAccelerator(String name, Material material, float hardness) 
	{
		super(name, material, hardness);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityNetherAccelerator();
	}

	@Override
	public EnumActionResult onWrenchUse(World world, EntityPlayer player, BlockPos pos, EnumFacing facing,
			IBlockState blockState) 
	{
		if(removedByPlayer(blockState, world, pos, player, true))
		{
			harvestBlock(world, player, pos, blockState, null, ItemStack.EMPTY);
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.FAIL;
	}
}
