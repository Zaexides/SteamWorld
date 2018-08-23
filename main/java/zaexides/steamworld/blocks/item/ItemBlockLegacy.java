package zaexides.steamworld.blocks.item;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import zaexides.steamworld.blocks.BlockLegacy;

public class ItemBlockLegacy extends ItemBlock
{
	public ItemBlockLegacy(Block block)
	{
		super(block);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) 
	{
		if(block instanceof BlockLegacy)
		{
			BlockLegacy blockLegacy = (BlockLegacy)block;
			ItemStack itemStackNew = new ItemStack(blockLegacy.targetBlock, stack.getCount(), blockLegacy.targetMeta);
			entityIn.replaceItemInInventory(itemSlot, itemStackNew);
		}
	}
}
