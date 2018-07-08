package zaexides.steamworld.items;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.init.ItemInitializer;
import zaexides.steamworld.utility.interfaces.IOreDictionaryRegisterable;

public class SWItemIngotLegacy extends SteamWorldItem implements IOreDictionaryRegisterable
{
	private String oreName;
	private SWItemIngot.EnumVarietyMaterial targetIngot;
	
	public SWItemIngotLegacy(String name, String oreName, SWItemIngot.EnumVarietyMaterial targetIngot)
	{
		super(name);
		this.oreName = oreName;
		this.targetIngot = targetIngot;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) 
	{
		ItemStack itemStackNew = new ItemStack(ItemInitializer.INGOT, stack.getCount(), targetIngot.getMeta());
		entityIn.replaceItemInInventory(itemSlot, itemStackNew);
	}

	@Override
	public void RegisterOreInDictionary() 
	{
		OreDictionary.registerOre(oreName, this);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemStack) 
	{
		return super.getUnlocalizedName(itemStack) + ".legacy";
	}
}
