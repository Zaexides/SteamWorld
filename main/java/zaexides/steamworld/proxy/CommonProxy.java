package zaexides.steamworld.proxy;

import java.io.File;

import org.apache.logging.log4j.Level;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import zaexides.steamworld.ConfigHandler;
import zaexides.steamworld.MiscEventHandler;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.RegistryHandler;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.BlockInitializer;
import zaexides.steamworld.gui.GuiHandler;
import zaexides.steamworld.integration.tc.TCMaterials;
import zaexides.steamworld.integration.tc.TinkersMelting;
import zaexides.steamworld.items.ItemDust;
import zaexides.steamworld.items.ItemInitializer;
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
	
	public void PreInit(FMLPreInitializationEvent e)
	{
		File dir = e.getModConfigurationDirectory();
		configuration = new Configuration(new File(dir.getPath(), "steamworld.cfg"));
		ConfigHandler.ReadConfig();
		
		PacketHandler.RegisterMessages("SteamWorld_NetChan");
		
    	RegistryHandler.RegisterFluids();
    	RegistryHandler.RegisterTileEntities();
    	
    	if(Loader.isModLoaded("tconstruct"))
    	{
	    	try
	    	{
	    		TinkersMelting.integrate();
	    		TCMaterials.registerMaterials();
	    	}
	    	catch(Exception exception)
	    	{
	    		SteamWorld.logger.log(Level.ERROR, "Ah, uhm, yeah, mod compatibility with Tinkers' kinda failed here at preInit. Send me this together with your forge logs, will ya? " + exception.toString());
	    	}
    	}
	}
	
	public void Init()
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(SteamWorld.singleton, new GuiHandler());
		MinecraftForge.EVENT_BUS.register(SteamWorld.eventHandler);
		
		RegistryHandler.RegisterWorldGen();
		RegistryHandler.RegisterMiscRecipes();
	}
	
	public void PostInit()
	{
		if(configuration.hasChanged())
			configuration.save();
		
		OreDictionaryScanner.ScanOreDictionary();
	}
}
