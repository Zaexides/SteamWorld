package zaexides.steamworld.blocks;

import org.apache.logging.log4j.Level;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.utility.interfaces.IOreDictionaryRegisterable;

public class SteamWorldBlockOre extends SteamWorldBlock implements IOreDictionaryRegisterable
{
	private String oreName;
	
	public SteamWorldBlockOre(String name, String oreName, Material material, float hardness, float resistance, int harvestLevel)
	{
		super(name, material, hardness, resistance);
		setHarvestLevel("pickaxe", harvestLevel);
		this.oreName = oreName;
	}

	public SteamWorldBlockOre(String name, String oreName, Material material, float hardness, int harvestLevel)
	{
		super(name, material, hardness);
		setHarvestLevel("pickaxe", harvestLevel);
		this.oreName = oreName;
	}

	@Override
	public void RegisterOreInDictionary() 
	{
		OreDictionary.registerOre(oreName, new ItemStack(this, 1));
	}
}
