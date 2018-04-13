package zaexides.steamworld.utility;

import org.apache.logging.log4j.Level;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import zaexides.steamworld.SteamWorld;

public class SteamWorldTeleporter extends Teleporter
{
	private BlockPos blockPos;
	private boolean safeMode = false;
	
	public SteamWorldTeleporter(WorldServer worldIn, BlockPos targetBlockPos) 
	{
		super(worldIn);
		blockPos = targetBlockPos;
	}
	
	public SteamWorldTeleporter(WorldServer worldIn, BlockPos targetBlockPos, boolean safeMode) 
	{
		super(worldIn);
		blockPos = targetBlockPos;
		this.safeMode = safeMode;
	}

	@Override
	public void placeInPortal(Entity entityIn, float rotationYaw)
	{
		entityIn.motionX = 0;
		entityIn.motionY = 0;
		entityIn.motionZ = 0;
		BlockPos pos = blockPos;
		SteamWorld.logger.log(Level.INFO, "Checking at " + pos);
		
		if(safeMode)
		{
			int attempts = 0;
			while(world.getBlockState(pos).getBlock() != Blocks.AIR || world.getBlockState(pos.down()).getBlock() != Blocks.AIR || world.getBlockState(pos.down(2)).getBlock() == Blocks.AIR)
			{
				SteamWorld.logger.log(Level.INFO, "Checking at " + pos);
				pos = pos.add(0, 1, 0);
				if(pos.getY() >= world.getActualHeight())
					pos = new BlockPos(pos.getX(), 3, pos.getZ());
				attempts++;
				if(attempts >= world.getActualHeight())
					break;
			}
		}
		
		entityIn.setPosition(pos.getX(), pos.getY(), pos.getZ());
	}
}
