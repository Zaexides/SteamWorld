package zaexides.steamworld.te;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityAltar extends SyncedTileEntity
{
	public ItemStackHandler itemStackHandler = new ItemStackHandler(1)
			{
				protected void onContentsChanged(int slot) {markDirty(true);};
			};
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		compound.setTag("item", itemStackHandler.serializeNBT());
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		if(compound.hasKey("item"))
			itemStackHandler.deserializeNBT((NBTTagCompound)compound.getTag("item"));
		super.readFromNBT(compound);
	}
}
