package zaexides.steamworld.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zaexides.steamworld.te.IGuiButtonHandler;

public class MessageGuiButton implements IMessage
{
	private BlockPos position;
	private byte buttonId;
	
	public MessageGuiButton() 
	{
	}
	
	public MessageGuiButton(BlockPos pos, byte buttonId) 
	{
		this.position = pos;
		this.buttonId = buttonId;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		position = BlockPos.fromLong(buf.readLong());
		buttonId = buf.readByte();
	}
	
	@Override
	public void toBytes(ByteBuf buf) 
	{
		buf.writeLong(position.toLong());
		buf.writeByte(buttonId);
	}
	
	public static class Handler implements IMessageHandler<MessageGuiButton, IMessage>
	{
		@Override
		public IMessage onMessage(MessageGuiButton message, MessageContext ctx) 
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> Handle(message, ctx));;
			return null;
		}
		
		private void Handle(MessageGuiButton message, MessageContext ctx)
		{
			World world = ctx.getServerHandler().player.world;
			if(!world.isBlockLoaded(message.position))
				return;
			TileEntity tileEntity = world.getTileEntity(message.position);
			if(tileEntity != null && tileEntity instanceof IGuiButtonHandler)
				((IGuiButtonHandler)tileEntity).HandleGuiButtonEvent(message.buttonId);
		}
	}
}
