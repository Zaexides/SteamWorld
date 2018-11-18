package zaexides.steamworld;

public class ModInfo 
{
	public static final String MODID = "steamworld";
    public static final String MODNAME = "SteamWorld";
    public static final String VERSION = "0.4.0.1";
    public static final String MC_VERSION = "[1.12,1.13)"; //Format will be parsed as `isAcceptedVersion = "[A,B)" => (A >= currentVersion < B)` (Not literally like this, but still.)
    
    public static final String CLIENT_PROXY = "zaexides.steamworld.proxy.ClientProxy";
    public static final String COMMON_PROXY = "zaexides.steamworld.proxy.CommonProxy";
    
    
    public static final String[] CREDIT_NAMES = 
	{
    		"drakkillen",
    		"mAdc0ck83",
    		"Zaexides",
    };
}
