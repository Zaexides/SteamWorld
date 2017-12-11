package zaexides.steamworld.savedata.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class TeleporterData
{
	public BlockPos position;
	public int dimension;
	public String name;
	public String password;
	public boolean free = false;
	public int id;
	
	public TeleporterData(BlockPos pos, int dimension)
	{
		this.position = pos;
		this.dimension = dimension;
	}
	
	public TeleporterData()
	{
	}
	
	public TeleporterData readFromNBT(NBTTagCompound nbt)
	{
		id = nbt.getInteger("id");
		free = nbt.getBoolean("unused");
		
		if(!free)
		{
			position = new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
			dimension = nbt.getInteger("dim");
			name = nbt.getString("name");
			password = nbt.getString(password);
		}
		
		return this;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setInteger("id", id);
		compound.setBoolean("unused", free);
		
		if(!free)
		{
			compound.setInteger("x", position.getX());
			compound.setInteger("y", position.getY());
			compound.setInteger("z", position.getZ());
			compound.setInteger("dim", dimension);
			compound.setString("name", name);
			compound.setString("password", password);
		}
		
		return compound;
	}
}
