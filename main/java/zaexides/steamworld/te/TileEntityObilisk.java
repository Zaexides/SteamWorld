package zaexides.steamworld.te;

import java.util.Random;

import org.apache.logging.log4j.Level;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import scala.noinline;
import zaexides.steamworld.SteamWorld;

public class TileEntityObilisk extends SyncedTileEntity
{
	public static final String[] OBILISK_TEXTS =
		{
			"A^;.d@912!N",
			"A^;.d@^CE^yXT19"
		};
	
	public static final String[] OBILISK_NAMES = 
		{
			"wD", "wJ", "m48", "‡Z", "·%", "7&", "2-d", "OOB", "N3s", "~Z", "UKy", "T-l",
			"ÛX", "qs", "@Zc", "/+", "$8", "y0", "9KO", "XZÚ", "wO2", "##", "%oK", "n&*",
			"UH", "($", "ÈKy", "9y", "8U", "x-", "ymY", "P¸W", "amY", "/V", "BBC", "LC#",
		};
	
	public static Random random; //Because worldgen random isn't random.
	
	public int textId = -1, nameId = -1;
	
	public static void instantiateObiliskRandom(World world)
	{
		if(random == null)
			random = new Random(world.getSeed());
	}
	
	public void generateText(int mainTextId)
	{
		if(textId == -1)
			textId = mainTextId;
		nameId = random.nextInt(OBILISK_NAMES.length);
		SteamWorld.logger.log(Level.INFO, "Name ID: " + nameId);
		markDirty();
	}
	
	public String getText()
	{
		return OBILISK_TEXTS[textId].replace(";", OBILISK_NAMES[nameId]);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		compound.setInteger("text_id", textId);
		compound.setInteger("name_id", nameId);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		super.readFromNBT(compound);
		textId = compound.getInteger("text_id");
		nameId = compound.getInteger("name_id");
	}
}
