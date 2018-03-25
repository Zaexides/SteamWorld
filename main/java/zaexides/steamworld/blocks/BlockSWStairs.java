package zaexides.steamworld.blocks;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.init.BlockInitializer;
import zaexides.steamworld.init.ItemInitializer;
import zaexides.steamworld.utility.interfaces.IModeledObject;

public class BlockSWStairs extends BlockStairs implements IModeledObject
{
	public BlockSWStairs(String name, IBlockState modelState) 
	{
		super(modelState);
		setUnlocalizedName(ModInfo.MODID + "." + name);
		setRegistryName(name);
		setCreativeTab(SteamWorld.CREATIVETAB);
		
		BlockInitializer.BLOCKS.add(this);
		AddItemBlock(64);
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
