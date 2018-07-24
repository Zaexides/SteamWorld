package zaexides.steamworld.entity;

import java.util.List;

import org.apache.logging.log4j.Level;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import zaexides.steamworld.SteamWorld;

public class EntityAnemone extends EntityMob implements IRangedAttackMob
{		
	public EntityAnemone(World worldIn) 
	{
		super(worldIn);
		setSize(1.0f, 1.0f);
	}
	
	@Override
	public float getEyeHeight() 
	{
		return 1.0f;
	}
	
	@Override
	protected void initEntityAI() 
	{
		this.tasks.addTask(4, new EntityAIAttackRanged(this, 0.0, 15, 6.0f));
		this.targetTasks.addTask(0, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntitySkyFish.class, true));
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox() 
	{
		return this.isEntityAlive() ? this.getEntityBoundingBox() : null;
	}
	
	@Override
	protected boolean canTriggerWalking()
	{
		return false;
	}
	
	@Override
	public void applyEntityCollision(Entity entityIn) {
	}
	
	@Override
	protected void applyEntityAttributes() 
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0);
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) 
	{
		EntityAnemoneStinger stinger = new EntityAnemoneStinger(this.world, this);
		stinger.addEffect(new PotionEffect(MobEffects.POISON, 100, 0));
				
		double dx = target.posX - posX;
		double dy = target.posY - posY;
		double dz = target.posZ - posZ;
		
		if(dy < 8)
			dy = 8;
		
		stinger.setThrowableHeading(dx * .2, dy, dz * .2, 1.2f, (float)(14 - this.world.getDifficulty().getDifficultyId() * 4));
		this.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0f, 2.0f);
		this.world.spawnEntity(stinger);
	}

	@Override
	public void setSwingingArms(boolean swingingArms) {
	}
	
	@Override
	public boolean isPotionApplicable(PotionEffect potioneffectIn)
	{
		if(potioneffectIn.getPotion().equals(MobEffects.POISON))
			return false;
		return super.isPotionApplicable(potioneffectIn);
	}
}
