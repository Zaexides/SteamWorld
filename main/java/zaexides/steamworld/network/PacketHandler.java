package zaexides.steamworld.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import zaexides.steamworld.network.messages.MessageGetTeleporterData;
import zaexides.steamworld.network.messages.MessageGuiButton;
import zaexides.steamworld.network.messages.MessageTeleporterConnect;
import zaexides.steamworld.network.messages.MessageTeleporterRegister;

public class PacketHandler 
{
	public static SimpleNetworkWrapper wrapper;
	
	private static int currentId = 0;
	private static int getId()
	{
		int returnId = currentId;
		currentId++;
		return returnId;
	}
	
	public static void RegisterMessages(String channel)
	{
		wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(channel);
		
		wrapper.registerMessage(MessageGuiButton.Handler.class, MessageGuiButton.class, getId(), Side.SERVER);
		wrapper.registerMessage(MessageTeleporterRegister.Handler.class, MessageTeleporterRegister.class, getId(), Side.SERVER);
		wrapper.registerMessage(MessageGetTeleporterData.Handler.class, MessageGetTeleporterData.class, getId(), Side.CLIENT);
		wrapper.registerMessage(MessageTeleporterConnect.Handler.class, MessageTeleporterConnect.class, getId(), Side.SERVER);
	}
}
