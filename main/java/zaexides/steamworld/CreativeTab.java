package zaexides.steamworld;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zaexides.steamworld.init.BlockInitializer;
import zaexides.steamworld.init.ItemInitializer;

public class CreativeTab extends CreativeTabs
{
	private int tab;
	
	public CreativeTab(String label, int tab)
	{
		super(label);
		this.tab = tab;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public ItemStack getTabIconItem() 
	{
		switch(tab)
		{
		case 0: return new ItemStack(ItemInitializer.INGOT, 1, 0);
		case 1: return new ItemStack(BlockInitializer.GENERATOR_STEAITE, 1, 0);
		case 2: return new ItemStack(BlockInitializer.BLOCK_DECORATIVE, 1, 4);
		default: return new ItemStack(ItemInitializer.INGOT, 1, 1);
		}
	}
}