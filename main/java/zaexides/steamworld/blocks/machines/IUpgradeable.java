package zaexides.steamworld.blocks.machines;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zaexides.steamworld.items.ItemUpgrade;

public interface IUpgradeable 
{
	public EnumActionResult OnUpgradeItemUse(ItemUpgrade.EnumUpgradeType upgradeType, World world, BlockPos pos, ItemStack itemStack, EntityPlayer player);
}
