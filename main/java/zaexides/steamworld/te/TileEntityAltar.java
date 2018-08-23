package zaexides.steamworld.te;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import zaexides.steamworld.blocks.BlockAltar;

public class TileEntityAltar extends SyncedTileEntity implements ICapabilityProvider
{
	public ItemStackHandler itemStackHandler = new ItemStackHandler(1)
			{
				protected void onContentsChanged(int slot) {markDirty(true);};
			};
			
	public static boolean isDecorative(IBlockState blockState)
	{
		return blockState.getBlock() instanceof BlockAltar && blockState.getValue(BlockAltar.DECORATIVE);
	}
	
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
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) 
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) 
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemStackHandler);
		return super.getCapability(capability, facing);
	}
}
