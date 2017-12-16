package zaexides.steamworld.network.messages;

import org.apache.logging.log4j.Level;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.network.PacketHandler;
import zaexides.steamworld.savedata.world.TeleporterData;
import zaexides.steamworld.savedata.world.TeleporterSaveData;
import zaexides.steamworld.te.TileEntityTeleporter;

public class MessageTeleporterRegister implements IMessage
{
	private BlockPos position;
	private String name;
	private String pass;

	public MessageTeleporterRegister() 
	{
	}
	
	public MessageTeleporterRegister(BlockPos position, String name, String pass)
	{
		this.position = position;
		this.name = name;
		this.pass = pass;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		position = BlockPos.fromLong(buf.readLong());
		name = ByteBufUtils.readUTF8String(buf);
		pass = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		buf.writeLong(position.toLong());
		ByteBufUtils.writeUTF8String(buf, name);
		ByteBufUtils.writeUTF8String(buf, pass);
	}

	public static class Handler implements IMessageHandler<MessageTeleporterRegister, IMessage>
	{
		@Override
		public IMessage onMessage(MessageTeleporterRegister message, MessageContext ctx) 
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> Handle(message, ctx));;
			return null;
		}
		
		private void Handle(MessageTeleporterRegister message, MessageContext ctx)
		{
			World world = ctx.getServerHandler().player.world;
			
			if(!world.isBlockLoaded(message.position))
				return;
			
			TileEntity tileEntity = world.getTileEntity(message.position);
			if(tileEntity != null && tileEntity instanceof TileEntityTeleporter)
			{
				TileEntityTeleporter teleporter = (TileEntityTeleporter) tileEntity;
				TeleporterSaveData teleporterSaveData = TeleporterSaveData.get(world);
				
				TeleporterData data = teleporterSaveData.getTeleporterData(teleporter.ownId);
				
				if(data == null)
				{
					data = new TeleporterData(message.position, world.provider.getDimension());
					teleporter.ownId = teleporterSaveData.addTeleporterData(data);
					data.id = teleporter.ownId;
				}
				data.name = message.name;
				data.netId = message.pass;
				teleporterSaveData.markDirty();
				teleporter.markDirty();
								
				NBTTagCompound compound = new NBTTagCompound();
				PacketHandler.wrapper.sendToAll(new MessageGetTeleporterData(teleporterSaveData.writeToNBT(compound), teleporterSaveData.mapName));
			}
		}
	}
}
