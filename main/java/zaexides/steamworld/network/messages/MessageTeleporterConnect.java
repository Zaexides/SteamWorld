package zaexides.steamworld.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zaexides.steamworld.network.PacketHandler;
import zaexides.steamworld.savedata.world.TeleporterData;
import zaexides.steamworld.savedata.world.TeleporterSaveData;
import zaexides.steamworld.te.generic_machine.TileEntityTeleporter;

public class MessageTeleporterConnect implements IMessage
{
	private BlockPos pos;
	private int targetId;
	
	public MessageTeleporterConnect() 
	{
	}
	
	public MessageTeleporterConnect(BlockPos pos, int targetId)
	{
		this.pos = pos;
		this.targetId = targetId;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		pos = BlockPos.fromLong(buf.readLong());
		targetId = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		buf.writeLong(pos.toLong());
		buf.writeInt(targetId);
	}

	public static class Handler implements IMessageHandler<MessageTeleporterConnect, IMessage>
	{
		@Override
		public IMessage onMessage(MessageTeleporterConnect message, MessageContext ctx) 
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> Handle(message, ctx));;
			return null;
		}
		
		private void Handle(MessageTeleporterConnect message, MessageContext ctx)
		{
			World world = ctx.getServerHandler().player.world;
			
			if(!world.isBlockLoaded(message.pos))
				return;

			TileEntity tileEntity = world.getTileEntity(message.pos);
			if(tileEntity != null && tileEntity instanceof TileEntityTeleporter)
			{
				TileEntityTeleporter teleporter = (TileEntityTeleporter) tileEntity;
				teleporter.targetId = message.targetId;
				teleporter.markDirty();
			}
		}
	}
}
