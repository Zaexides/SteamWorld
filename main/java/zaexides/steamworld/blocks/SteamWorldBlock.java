package zaexides.steamworld.blocks;

import org.apache.logging.log4j.Level;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.property.IUnlistedProperty;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.items.ItemInitializer;
import zaexides.steamworld.utility.interfaces.IModeledObject;

public class SteamWorldBlock extends Block implements IModeledObject
{
	public SteamWorldBlock(String name, Material material, float hardness)
	{
		super(material);
		Construct(name, hardness, hardness * 5, 64);
	}
	
	public SteamWorldBlock(String name, Material material, float hardness, float resistance)
	{
		super(material);
		Construct(name, hardness, resistance, 64);
	}
	
	public SteamWorldBlock(String name, Material material, float hardness, float resistance, int maxStackSize)
	{
		super(material);
		Construct(name, hardness, resistance, maxStackSize);
	}
	
	private void Construct(String name, float hardness, float resistance, int maxStackSize)
	{
		setUnlocalizedName(ModInfo.MODID + "." + name);
		setRegistryName(name);
		setCreativeTab(SteamWorld.CREATIVETAB);
		setHardness(hardness);
		setResistance(resistance);
		
		BlockInitializer.BLOCKS.add(this);
		AddItemBlock(maxStackSize);
	}
	
	public void AddItemBlock(int maxStackSize) 
	{
		ItemInitializer.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()).setMaxStackSize(maxStackSize));
	}
	
	@Override
	public void RegisterModels()
	{
		SteamWorld.proxy.RegisterItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}