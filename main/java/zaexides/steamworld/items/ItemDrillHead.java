package zaexides.steamworld.items;

import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.utility.interfaces.IOreDictionaryRegisterable;

public class ItemDrillHead extends SteamWorldItem implements IOreDictionaryRegisterable
{
	private final byte tier;
	private final String oreDicName;
	public final float speedModifier;
	
	public static final float EFFICIENCY_BASE_DECREASE = 1.4f;
	
	public ItemDrillHead(String name, ToolMaterial toolMaterial, String oreDicName)
	{
		this(name, toolMaterial.getMaxUses(), (byte)toolMaterial.getHarvestLevel(), oreDicName, toolMaterial.getEfficiencyOnProperMaterial() - EFFICIENCY_BASE_DECREASE);
	}
	
	public ItemDrillHead(String name, int uses, byte tier, String oreDicName, float speedModifier) 
	{
		super(name);
		setMaxDamage(uses);
		setMaxStackSize(1);
		this.tier = tier;
		this.oreDicName = oreDicName;
		this.speedModifier = speedModifier;
	}
	
	@Override
	public void RegisterModels() 
	{
		SteamWorld.proxy.RegisterItemRenderers(this, 0, "inventory", "drill/" + getRegistryName().getResourcePath());
	}
	
	public byte getTier()
	{
		return tier;
	}
	
	public static void Damage(ItemStack stack, Random random) //Custom calculation because minecraft what?
	{
		if(stack.isItemEnchanted())
		{
			int unbLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack);
			if(random.nextInt(5) > unbLevel)
				stack.setItemDamage(stack.getItemDamage() + 1);
		}
		else
			stack.setItemDamage(stack.getItemDamage() + 1);
	}

	@Override
	public void RegisterOreInDictionary() 
	{
		OreDictionary.registerOre(oreDicName, this);
	}
}
