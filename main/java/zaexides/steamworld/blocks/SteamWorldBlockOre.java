package zaexides.steamworld.blocks;

import net.minecraft.block.material.Material;
import net.minecraftforge.oredict.OreDictionary;
import zaexides.steamworld.BlockInitializer;
import zaexides.steamworld.utility.IOreDictionaryRegisterable;

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
		OreDictionary.registerOre(oreName, this);
	}
}
