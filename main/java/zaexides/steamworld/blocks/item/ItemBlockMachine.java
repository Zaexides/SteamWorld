package zaexides.steamworld.blocks.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import zaexides.steamworld.blocks.machines.BlockMachine;
import zaexides.steamworld.utility.interfaces.IMetaName;

public class ItemBlockMachine extends ItemBlock
{

	public ItemBlockMachine(Block block)
	{
		super(block);
		setHasSubtypes(true);
		setMaxDamage(0);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) 
	{
		String unlocalizedName = super.getUnlocalizedName();
		if((stack.getMetadata() & 4) == 4)
			unlocalizedName += "_ht";
		return unlocalizedName;
	}
	
	@Override
	public int getMetadata(int damage) 
	{
		return damage;
	}
}
