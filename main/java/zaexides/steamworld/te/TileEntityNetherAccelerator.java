package zaexides.steamworld.te;

import java.util.List;

import org.apache.logging.log4j.Level;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.init.SoundInitializer;

public class TileEntityNetherAccelerator extends TileEntity implements ITickable
{
	private static final float TIMER_MAX = 60;
	private float timer = TIMER_MAX;
	
	private static final float ENDERMAN_SPAWN_CHANCE = 0.001f;
	private static final float ENDERMITE_SPAWN_CHANCE = 0.003f;
	private static final float TELEPORT_CHANCE = 0.002f;
	
	private static final int SIDE_EFFECT_AREA_SIZE = 10;
	
	@Override
	public void update() 
	{
		if(isInvalid())
			return;
		
		timer--;
		if(timer <= 0)
			timer = TIMER_MAX;
		else
			return;
		
		int amount = 0;
		for(int x = -1; x <= 1; x++)
		{
			for(int y = -1; y <= 1; y++)
			{
				for(int z = -1; z <= 1; z++)
				{
					BlockPos pos = this.pos.add(x,y,z);
					TileEntity tileEntity = world.getTileEntity(pos);
					if(tileEntity != null && tileEntity instanceof TileEntitySteamGeneratorNether)
					{
						if(!world.isRemote)
							((ITickable)tileEntity).update();
						amount++;
					}
				}
			}
		}
		
		for(int a = 0; a < amount; a++)
		{
			TeleportMobs();
			
			if(!world.isRemote)
			{
				if(world.rand.nextFloat() <= (ENDERMAN_SPAWN_CHANCE * ((float)world.getDifficulty().getDifficultyId())))
					SpawnMob(new EntityEnderman(world));
				if(world.rand.nextFloat() <= (ENDERMITE_SPAWN_CHANCE * ((float)world.getDifficulty().getDifficultyId())))
					SpawnMob(new EntityEndermite(world));
			}
		}
	}

	private void SpawnMob(EntityLiving entity)
	{
		int mobAmount = world.getEntitiesWithinAABB(entity.getClass(),
				(new AxisAlignedBB(pos.add(-SIDE_EFFECT_AREA_SIZE,-SIDE_EFFECT_AREA_SIZE,-SIDE_EFFECT_AREA_SIZE), pos.add(SIDE_EFFECT_AREA_SIZE,SIDE_EFFECT_AREA_SIZE,SIDE_EFFECT_AREA_SIZE)))).size();
		
		if(mobAmount >= 6)
			return;
		
		boolean success = false;
		int attempts = 0;
		
		while(!success && attempts < 32)
		{
			double spawn_x = ((double)pos.getX()) + (world.rand.nextDouble() - world.rand.nextDouble()) * SIDE_EFFECT_AREA_SIZE + 0.5d;
			double spawn_y = ((double)pos.getY()) + (world.rand.nextDouble() - world.rand.nextDouble()) * SIDE_EFFECT_AREA_SIZE + 0.5d;
			double spawn_z = ((double)pos.getZ()) + (world.rand.nextDouble() - world.rand.nextDouble()) * SIDE_EFFECT_AREA_SIZE + 0.5d;
			
			entity.setLocationAndAngles(spawn_x, spawn_y, spawn_z, world.rand.nextFloat()*360, 0);
			if(entity.isNotColliding())
				success = true;
			else
				attempts++;
		}
		
		if(success)
		{
			world.spawnEntity(entity);
			entity.spawnExplosionParticle();
			entity.playSound(SoundInitializer.ACCELERATOR_TELEPORT, 1.0F, 1.0F);
		}
		else
			world.removeEntity(entity);
	}
	
	private void TeleportMobs()
	{
		List<EntityLivingBase> livingEntities = world.getEntitiesWithinAABB(
				EntityLivingBase.class,
				new AxisAlignedBB(pos.add(-SIDE_EFFECT_AREA_SIZE,-SIDE_EFFECT_AREA_SIZE,-SIDE_EFFECT_AREA_SIZE), pos.add(SIDE_EFFECT_AREA_SIZE,SIDE_EFFECT_AREA_SIZE,SIDE_EFFECT_AREA_SIZE))
				);
		
		for(EntityLivingBase entityLiving : livingEntities)
		{
			if(world.rand.nextDouble() <= TELEPORT_CHANCE)
			{
				boolean success = false;
				int attempts = 0;
				
				double original_x = entityLiving.posX;
				double original_y = entityLiving.posY;
				double original_z = entityLiving.posZ;
				
				while(!success && attempts < 128)
				{
					double target_x = ((double)pos.getX()) + (world.rand.nextDouble() - world.rand.nextDouble()) * SIDE_EFFECT_AREA_SIZE + 0.5d;
					double target_y = ((double)pos.getY()) + (world.rand.nextDouble() - world.rand.nextDouble()) * SIDE_EFFECT_AREA_SIZE + 0.5d;
					double target_z = ((double)pos.getZ()) + (world.rand.nextDouble() - world.rand.nextDouble()) * SIDE_EFFECT_AREA_SIZE + 0.5d;
					
					if(entityLiving.attemptTeleport(target_x, target_y, target_z))
					{
						success = true;
						entityLiving.playSound(SoundInitializer.ACCELERATOR_TELEPORT, 1.0F, 1.0F);
						world.playSound((EntityPlayer)null, original_x, original_y, original_z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, entityLiving.getSoundCategory(), 1.0F, 1.0F);
						
						double particle_xOff = (world.rand.nextDouble() - 0.5d) * ((double)entityLiving.width);
						double particle_yOff = world.rand.nextDouble() * ((double)entityLiving.height) - 0.25d;
						double particle_zOff = (world.rand.nextDouble() - 0.5d) * ((double)entityLiving.width);
						
						if(world.isRemote)
						{
							world.spawnParticle(EnumParticleTypes.PORTAL, target_x + particle_xOff, target_y + particle_yOff, target_z + particle_zOff, (world.rand.nextDouble() - 0.5D) * 2.0D, -world.rand.nextDouble(), (world.rand.nextDouble() - 0.5D) * 2.0D);
							world.spawnParticle(EnumParticleTypes.PORTAL, original_x + particle_xOff, original_y + particle_yOff, original_z + particle_zOff, (world.rand.nextDouble() - 0.5D) * 2.0D, -world.rand.nextDouble(), (world.rand.nextDouble() - 0.5D) * 2.0D);
						}
					}
					else
						attempts++;
				}
			}
		}
	}
}
