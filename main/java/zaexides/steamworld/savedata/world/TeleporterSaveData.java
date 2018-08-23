package zaexides.steamworld.savedata.world;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import zaexides.steamworld.ModInfo;

public class TeleporterSaveData extends WorldSavedData
{
	private static final String DATA_ID = ModInfo.MODID + "_TeleporterData";
	
	private int pointer = 0;
	private List<TeleporterData> teleporterData = new ArrayList<TeleporterData>();
	
	public static TeleporterSaveData instance;
	
	public TeleporterSaveData() 
	{
		super(DATA_ID);
	}
	
	public TeleporterSaveData(String s)
	{
		super(s);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		int id = 0;
		boolean pointerSet = false;
		while(nbt.hasKey("teleporter_" + id))
		{
			TeleporterData teleporterData = new TeleporterData();
			teleporterData.readFromNBT((NBTTagCompound)nbt.getTag("teleporter_" + id));
			this.teleporterData.add(teleporterData);
			id++;
			if(teleporterData.free)
				pointerSet = true;
			
			if(!pointerSet)
				pointer++;
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		for(int i = 0; i < teleporterData.size(); i++)
		{
			NBTTagCompound subCompound = new NBTTagCompound();
			teleporterData.get(i).writeToNBT(subCompound);
			compound.setTag("teleporter_" + i, subCompound);
		}
		
		return compound;
	}
	
	public int addTeleporterData(TeleporterData teleporterData)
	{
		while(pointer < this.teleporterData.size() && !this.teleporterData.get(pointer).free)
		{
			pointer++;
		}
		
		if(pointer == this.teleporterData.size())
		{
			this.teleporterData.add(teleporterData);
		}
		else
		{
			this.teleporterData.set(pointer, teleporterData);
		}
		
		markDirty();
		return pointer;
	}
	
	public boolean removeTeleporterData(int id)
	{
		if(id >= teleporterData.size() || id < 0 || teleporterData.get(id).free)
			return false;
		
		teleporterData.get(id).free = true;
		if(pointer > id)
			pointer = id;
		markDirty();
		return true;
	}
	
	public TeleporterData getTeleporterData(int id)
	{
		if(id < teleporterData.size() && id >= 0)
			return teleporterData.get(id);
		return null;
	}
	
	public static TeleporterSaveData get(World world)
	{
		if(instance != null)
			return instance;
		
		MapStorage mapStorage = world.getMapStorage();
		instance = (TeleporterSaveData) mapStorage.getOrLoadData(TeleporterSaveData.class, DATA_ID);
		
		if(instance == null)
		{
			instance = new TeleporterSaveData();
			mapStorage.setData(DATA_ID, instance);
		}
		return instance;
	}
}
