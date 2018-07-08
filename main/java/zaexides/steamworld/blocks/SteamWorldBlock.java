package zaexides.steamworld.blocks;

import org.apache.logging.log4j.Level;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.IUnlistedProperty;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.init.BlockInitializer;
import zaexides.steamworld.init.ItemInitializer;
import zaexides.steamworld.utility.interfaces.IModeledObject;

public class SteamWorldBlock extends Block implements IModeledObject
{	
	private boolean isBeaconBase = false;
	
	public SteamWorldBlock(String name, Material material, float hardness)
	{
		super(material);
		Construct(name, hardness, hardness * 5, 64);
	}
	
	public SteamWorldBlock(String name, Material material, float hardness, CreativeTabs creativeTabs)
	{
		super(material);
		Construct(name, hardness, hardness * 5, 64, creativeTabs);
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
	
	public SteamWorldBlock(String name, Material material, float hardness, float resistance, int maxStackSize, CreativeTabs creativeTabs)
	{
		super(material);
		Construct(name, hardness, resistance, maxStackSize, creativeTabs);
	}
	
	private void Construct(String name, float hardness, float resistance, int maxStackSize)
	{
		Construct(name, hardness, resistance, maxStackSize, SteamWorld.CREATIVETAB_BLOCKS);
	}
	
	private void Construct(String name, float hardness, float resistance, int maxStackSize, CreativeTabs creativeTabs)
	{
		setUnlocalizedName(ModInfo.MODID + "." + name);
		setRegistryName(name);
		setCreativeTab(creativeTabs);
		setHardness(hardness);
		setResistance(resistance);
		
		BlockInitializer.BLOCKS.add(this);
		AddBlockItem(maxStackSize);
	}
	
	protected void AddBlockItem(int maxStackSize) 
	{
		ItemInitializer.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()).setMaxStackSize(maxStackSize));
	}
	
	@Override
	public void RegisterModels()
	{
		SteamWorld.proxy.RegisterItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
	
	public SteamWorldBlock SetBeaconBase(boolean beaconBase) 
	{
		this.isBeaconBase = beaconBase;
		return this;
	}
	
	@Override
	public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) 
	{
		return isBeaconBase;
	}
}