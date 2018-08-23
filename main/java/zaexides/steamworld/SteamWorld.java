package zaexides.steamworld;

import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import zaexides.steamworld.events.MiscEventHandler;
import zaexides.steamworld.proxy.CommonProxy;

@Mod(
		modid = ModInfo.MODID,
		name = ModInfo.MODNAME,
		version = ModInfo.VERSION,
		acceptedMinecraftVersions = ModInfo.MC_VERSION,
		useMetadata = true,
		updateJSON = "https://zaexides.net/steamworld/update.json",
		dependencies = "required-after:forge@[14.23.2.2611,);"
		)
public class SteamWorld
{
    public static Logger logger;
    public static final CreativeTabs CREATIVETAB_ITEMS = new CreativeTab("steamworld_items", 0);
    public static final CreativeTabs CREATIVETAB_BLOCKS = new CreativeTab("steamworld_blocks", 2);
    public static final CreativeTabs CREATIVETAB_UTILITY = new CreativeTab("steamworld_utility", 1);
    
    @SidedProxy(clientSide = ModInfo.CLIENT_PROXY, serverSide = ModInfo.COMMON_PROXY)
    public static CommonProxy proxy;
    
    @Mod.Instance(ModInfo.MODID)
    public static SteamWorld singleton;
    
    public static MiscEventHandler eventHandler = new MiscEventHandler();
    
    static
    {
    	FluidRegistry.enableUniversalBucket();
    }
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	logger = event.getModLog();
    	proxy.PreInit(event);
    }
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.Init();
    }
    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	proxy.PostInit();
    }
}
