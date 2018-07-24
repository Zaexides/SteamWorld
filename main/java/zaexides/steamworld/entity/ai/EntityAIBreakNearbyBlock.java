package zaexides.steamworld.entity.ai;

import org.apache.logging.log4j.Level;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.entity.interfaces.IEntityBreakBlockCallback;

public class EntityAIBreakNearbyBlock extends EntityAIBase
{
	private final EntityLiving entity;
	private int timer = 0, duration;
	private final Block target;
	private BlockPos nearestPos;
	private final int maxDistance;
	
	private boolean isEating;
	
	public EntityAIBreakNearbyBlock(EntityLiving entity, int duration, Block target, int maxDistance)
	{
		this.entity = entity;
		this.duration = duration;
		this.target = target;
		this.maxDistance = maxDistance;
		
		setMutexBits(3);
	}
	
	@Override
	public boolean shouldExecute() 
	{
		boolean canEat = true;
		if(entity instanceof IEntityBreakBlockCallback)
			canEat = ((IEntityBreakBlockCallback)entity).CanBreakBlocks();
		CalculateNearestBlockOfType();
		return nearestPos != null && canEat;
	}
	
	private void CalculateNearestBlockOfType()
	{
		this.nearestPos = null;
		
		BlockPos centerPos = new BlockPos(this.entity.posX, this.entity.posY, this.entity.posZ);
		
		for(int x = -maxDistance; x <= maxDistance; x++)
		{
			for(int z = -maxDistance; z <= maxDistance; z++)
			{
				for(int y = -maxDistance; y <= maxDistance; y++)
				{
					BlockPos targetPos = centerPos.add(x, y, z);
					if(this.entity.getDistance(targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5) <= maxDistance)
					{
						Block blockAtTargetPos = this.entity.world.getBlockState(targetPos).getBlock();
						
						if(blockAtTargetPos.equals(target))
						{
							this.nearestPos = targetPos;
							return;
						}
					}
				}
			}
		}
	}
	
	@Override
	public void startExecuting() 
	{
		this.isEating = true;
		this.timer = duration;
	}
	
	@Override
	public boolean shouldContinueExecuting() 
	{
		return this.isEating
				&& nearestPos != null
				&& this.entity.world.getBlockState(nearestPos).getBlock().equals(target)
				&& this.entity.getDistance(nearestPos.getX() + 0.5, nearestPos.getY() + 0.5, nearestPos.getZ() + 0.5) <= maxDistance;
	}
	
	@Override
	public void updateTask() 
	{
		this.entity.getLookHelper().setLookPosition(
				nearestPos.getX() + 0.5,
				nearestPos.getY() + 0.5,
				nearestPos.getZ() + 0.5,
				(float)this.entity.getHorizontalFaceSpeed(),
				(float)this.entity.getVerticalFaceSpeed()
				);
		
		timer--;
		
		if(timer % 5 == 0)
		{
			SoundType soundType = target.getSoundType(this.entity.world.getBlockState(nearestPos), this.entity.world, this.nearestPos, this.entity);
			SoundEvent event = soundType.getHitSound();
			this.entity.playSound(event, soundType.volume, soundType.pitch);
		}
		
		if(timer <= 0)
		{
			this.entity.world.destroyBlock(nearestPos, false);
			this.isEating = false;
			if(this.entity instanceof IEntityBreakBlockCallback)
				((IEntityBreakBlockCallback)this.entity).OnBlockBroken(target, nearestPos);
		}
	}
}
