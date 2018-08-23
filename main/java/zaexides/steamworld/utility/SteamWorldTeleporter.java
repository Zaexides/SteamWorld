package zaexides.steamworld.utility;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import zaexides.steamworld.ConfigHandler;
import zaexides.steamworld.init.BlockInitializer;

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
		
		if(this.world.provider.getDimensionType().getId() == ConfigHandler.dimensionId && safeMode)
		{
			for(int dx = -1; dx <= 1; dx++)
			{
				for(int dz = -1; dz <= 1; dz++)
				{
					for(int dy = -1; dy <= 2; dy++)
					{
						boolean isGround = dy < 0;
						BlockPos groundPos = pos.add(dx, dy, dz);
						world.setBlockState(groundPos, isGround ? BlockInitializer.BLOCK_SW_PORTAL_FRAME.getDefaultState() : Blocks.AIR.getDefaultState());
					}
				}
			}
		}
		else
		{
			BlockPos targetPos = blockPos;
			
			if(safeMode)
			{
				int attempts = 0;
				while(world.getBlockState(pos).getBlock() != Blocks.AIR || world.getBlockState(pos.down()).getBlock() != Blocks.AIR || world.getBlockState(pos.down(2)).getBlock() == Blocks.AIR)
				{
					if(attempts >= world.getActualHeight())
					{
						int y = pos.getY();
						pos = targetPos.add(random.nextInt(6) - 3, 0, random.nextInt(6) - 3);
						pos = new BlockPos(pos.getX(), y, pos.getZ());
					}
					
					pos = pos.add(0, 1, 0);
					if(pos.getY() >= world.getActualHeight())
						pos = new BlockPos(pos.getX(), 3, pos.getZ());
					attempts++;
					if(attempts >= world.getActualHeight() * 2)
					{
						pos = targetPos;
						break;
					}
				}
			}
		}
		entityIn.setPosition(pos.getX(), pos.getY(), pos.getZ());
	}
}
