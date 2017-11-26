package zaexides.steamworld.te;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class SyncedTileEntity extends TileEntity
{
	public void markDirty(boolean notifyUpdate)
	{
		super.markDirty();
		if(notifyUpdate)
			world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
	}
	
	@Override
	public void markDirty() 
	{
		markDirty(true);
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound tagCompound = new NBTTagCompound();
		writeToNBT(tagCompound);
		return tagCompound;
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag)
	{
		readFromNBT(tag);
	}
	
	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() 
	{
		NBTTagCompound updateCompound = getUpdateTag();
		this.writeToNBT(updateCompound);
		return new SPacketUpdateTileEntity(pos, 0, updateCompound);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		NBTTagCompound updateCompound = pkt.getNbtCompound();
		handleUpdateTag(updateCompound);
	}
}
