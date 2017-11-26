package zaexides.steamworld;

import java.util.LinkedList;
import java.util.Queue;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import scala.collection.generic.BitOperations.Int;
import scala.reflect.internal.Trees.New;
import scala.tools.nsc.doc.model.Public;
import zaexides.steamworld.proxy.CommonProxy;

@Mod(modid = ModInfo.MODID, name = ModInfo.MODNAME, version = ModInfo.VERSION, useMetadata = true)
public class SteamWorld
{
    public static Logger logger;
    public static final CreativeTabs CREATIVETAB = new CreativeTab("steamworld");
    
    @SidedProxy(clientSide = ModInfo.CLIENT_PROXY, serverSide = ModInfo.COMMON_PROXY)
    public static CommonProxy proxy;
    
    @Mod.Instance(ModInfo.MODID)
    public static SteamWorld singleton;
    
    static
    {
    	FluidRegistry.enableUniversalBucket();
    }
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	proxy.PreInit(event);
    	logger = event.getModLog();
    	RegistryHandler.RegisterFluids();
    	RegistryHandler.RegisterTileEntities();
    }
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
    	RegistryHandler.MiscRegister();
    	proxy.Init();
    }
    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	proxy.PostInit();
    }
}
