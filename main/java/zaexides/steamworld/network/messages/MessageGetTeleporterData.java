package zaexides.steamworld.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zaexides.steamworld.savedata.world.TeleporterSaveData;

public class MessageGetTeleporterData implements IMessage
{
	private NBTTagCompound teleporterSaveData;
	private String mapName;
	
	public MessageGetTeleporterData() 
	{
	}
	
	public MessageGetTeleporterData(NBTTagCompound teleporterSaveData, String mapName)
	{
		this.teleporterSaveData = teleporterSaveData;
		this.mapName = mapName;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		teleporterSaveData = ByteBufUtils.readTag(buf);
		mapName = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		ByteBufUtils.writeTag(buf, teleporterSaveData);
		ByteBufUtils.writeUTF8String(buf, mapName);
	}
	
	public static class Handler implements IMessageHandler<MessageGetTeleporterData, IMessage>
	{
		@Override
		public IMessage onMessage(MessageGetTeleporterData message, MessageContext ctx) 
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> Handle(message, ctx));;
			return null;
		}
		
		private void Handle(MessageGetTeleporterData message, MessageContext ctx)
		{
			TeleporterSaveData teleporterSaveData = new TeleporterSaveData(message.mapName);
			teleporterSaveData.readFromNBT(message.teleporterSaveData);
			teleporterSaveData.markDirty();
			TeleporterSaveData.instance = teleporterSaveData;
		}
	}
}
