package zaexides.steamworld.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import zaexides.steamworld.network.messages.MessageGetTeleporterData;
import zaexides.steamworld.network.messages.MessageGuiButton;
import zaexides.steamworld.network.messages.MessageTeleporterRegister;

public class PacketHandler 
{
	public static SimpleNetworkWrapper wrapper;
	
	public static void RegisterMessages(String channel)
	{
		wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(channel);
		
		//Always make sure that the discriminator (3rd parameter) is, well, unique.
		wrapper.registerMessage(MessageGuiButton.Handler.class, MessageGuiButton.class, 0, Side.SERVER);
		wrapper.registerMessage(MessageTeleporterRegister.Handler.class, MessageTeleporterRegister.class, 1, Side.SERVER);
		wrapper.registerMessage(MessageGetTeleporterData.Handler.class, MessageGetTeleporterData.class, 2, Side.CLIENT);
	}
}
