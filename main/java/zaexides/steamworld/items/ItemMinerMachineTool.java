package zaexides.steamworld.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.utility.interfaces.IOreDictionaryRegisterable;

public class ItemMinerMachineTool extends SteamWorldItem implements IOreDictionaryRegisterable
{
	private final byte tier;
	private final String oreDicName;
	private final float speedModifier;
	private final int enchantability;
	
	public static final float EFFICIENCY_BASE_DECREASE = 1.4f;
	
	public boolean isPump = false;
	
	public static List<ItemMinerMachineTool> drillHeads = new ArrayList<ItemMinerMachineTool>(); //Used for JEI stuffs
	
	public ItemMinerMachineTool(String name, ToolMaterial toolMaterial, String oreDicName)
	{
		this(name, toolMaterial.getMaxUses(), (byte)toolMaterial.getHarvestLevel(), oreDicName, toolMaterial.getEfficiencyOnProperMaterial() - EFFICIENCY_BASE_DECREASE, toolMaterial.getEnchantability());
	}
	
	public ItemMinerMachineTool(String name, int uses, byte tier, String oreDicName, float speedModifier, int enchantability) 
	{
		super(name);
		setMaxDamage(uses);
		setMaxStackSize(1);
		this.tier = tier;
		this.oreDicName = oreDicName;
		this.speedModifier = speedModifier;
		this.enchantability = enchantability;
		drillHeads.add(this);
	}
	
	public ItemMinerMachineTool SetIsPump(boolean isPump)
	{
		this.isPump = isPump;
		return this;
	}
	
	@Override
	public void RegisterModels() 
	{
		String subFolder = isPump ? "pump/" : "drill/";
		SteamWorld.proxy.RegisterItemRenderers(this, 0, "inventory", subFolder + getRegistryName().getResourcePath());
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
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) 
	{
		if(enchantment.equals(Enchantments.EFFICIENCY) || enchantment.equals(Enchantments.FORTUNE))
			return true;
		return super.canApplyAtEnchantingTable(stack, enchantment);
	}
	
	public float getEfficiencyModifier(ItemStack stack)
	{
		float efficiencyModifier = this.speedModifier;
		if(stack.isItemEnchanted())
		{
			int effLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);
			efficiencyModifier += 0.1 * (Math.pow(effLevel, 2) + 1);
		}
		
		return efficiencyModifier;
	}
	
	@Override
	public int getItemEnchantability() 
	{
		return enchantability;
	}
}
