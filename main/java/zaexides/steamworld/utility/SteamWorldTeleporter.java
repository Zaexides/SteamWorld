package zaexides.steamworld.utility;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class SteamWorldTeleporter extends Teleporter
{
	private BlockPos blockPos;
	
	public SteamWorldTeleporter(WorldServer worldIn, BlockPos targetBlockPos) 
	{
		super(worldIn);
		blockPos = targetBlockPos;
	}

	@Override
	public void placeInPortal(Entity entityIn, float rotationYaw)
	{
		entityIn.motionX = 0;
		entityIn.motionY = 0;
		entityIn.motionZ = 0;
		entityIn.setPosition(blockPos.getX() + 0.5d, blockPos.getY() + 1.5d, blockPos.getZ() + 0.5d);
	}
}
