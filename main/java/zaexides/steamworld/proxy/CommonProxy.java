package zaexides.steamworld.proxy;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import zaexides.steamworld.BlockInitializer;
import zaexides.steamworld.ConfigHandler;
import zaexides.steamworld.ItemInitializer;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.RegistryHandler;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.gui.GuiHandler;
import zaexides.steamworld.items.ItemDust;
import zaexides.steamworld.items.SWItemNugget;
import zaexides.steamworld.network.PacketHandler;
import zaexides.steamworld.recipe.handling.DustRecipeHandler;
import zaexides.steamworld.recipe.handling.OreDictionaryScanner;

public class CommonProxy 
{
	public static Configuration configuration;
	
	public void RegisterItemRenderer(Item item, int metadata, String id)
	{
	}
	
	public void RegisterItemRenderers(Item item, int metadata, String id, String filename)
	{
	}
	
	public void RegisterCustomMeshFluid(Block block, String modelLocation)
	{
	}
	
	public void Init()
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(SteamWorld.singleton, new GuiHandler());
		
		DustRecipeHandler.RegisterRecipe(new ItemStack(Blocks.REDSTONE_ORE), new ItemStack(Items.REDSTONE, 5));
		DustRecipeHandler.RegisterRecipe(new ItemStack(Blocks.DIAMOND_ORE), new ItemStack(ItemInitializer.ITEM_NUGGET, 10, SWItemNugget.EnumVarietyMaterial.DIAMOND.getMeta()));
		DustRecipeHandler.RegisterRecipe(new ItemStack(Blocks.EMERALD_ORE), new ItemStack(ItemInitializer.ITEM_NUGGET, 10, SWItemNugget.EnumVarietyMaterial.EMERALD.getMeta()));
		DustRecipeHandler.RegisterRecipe(new ItemStack(Blocks.COAL_ORE), new ItemStack(Items.COAL, 5));
	}
	
	public void PreInit(FMLPreInitializationEvent e)
	{
		File dir = e.getModConfigurationDirectory();
		configuration = new Configuration(new File(dir.getPath(), "steamworld.cfg"));
		ConfigHandler.ReadConfig();
		
		PacketHandler.RegisterMessages("SteamWorld_NetChan");
	}
	
	public void PostInit()
	{
		if(configuration.hasChanged())
			configuration.save();
		
		OreDictionaryScanner.ScanOreDictionary();
	}
}
