package zaexides.steamworld.entity;

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import zaexides.steamworld.init.SoundInitializer;
import zaexides.steamworld.utility.SWDamageSource;

public class EntityAnciteGolem extends EntityGolem implements IMob, IRangedAttackMob
{		
	protected static final DataParameter<Float> AWAKENING = EntityDataManager.<Float>createKey(EntityAnciteGolem.class, DataSerializers.FLOAT);
	
	private static final AxisAlignedBB PLAYER_CHECK_AREA = new AxisAlignedBB(-4, -4, -4, 4, 4, 4);
	private static final int FALL_MAX_HURT_DAMAGE = 40;
    private static final float FALL_HURT_BASE_DAMAGE = 2.0F;
    
    private static final int DEFAULT_AMMO = 30;
    private static final int RELOAD_TIME = 200;
    private static final int EMPTY_SHOT_TIME = 10;
    private static final int TICKS_PER_SHOT = 2;
    private byte ammo = DEFAULT_AMMO;
    private int reloadTimer = 0;
	
	public EntityAnciteGolem(World worldIn) 
	{
		super(worldIn);
		setSize(1.0f, 1.0f);
	}
	
	@Override
	public SoundCategory getSoundCategory() 
	{
		return SoundCategory.HOSTILE;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource p_184601_1_) 
	{
		return SoundInitializer.ANCITE_GOLEM_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound() 
	{
		return SoundInitializer.ANCITE_GOLEM_DEATH;
	}
	
	@Override
	protected SoundEvent getFallSound(int heightIn) 
	{
		return SoundInitializer.ANCITE_GOLEM_FALL;
	}
	
	@Override
	protected void playStepSound(BlockPos pos, Block blockIn) 
	{
		this.playSound(SoundInitializer.ANCITE_GOLEM_STEP, 1.0F, 1.0F);
	}
	
	@Override
	protected void entityInit() 
	{
		super.entityInit();
		this.dataManager.register(AWAKENING, 0.0f);
	}
	
	@Override
	protected void applyEntityAttributes() 
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(30.0);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(10.0);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.7);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.465);
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.5);
	}
	
	@Override
	protected void initEntityAI() 
	{
		this.tasks.addTask(0, new EntityAIAttackRanged(this, 0.4, TICKS_PER_SHOT, 64.0f)
		{
			@Override
			public boolean shouldExecute() 
			{
				return super.shouldExecute() && getAwakeningStep() > 0.9f && getAmmo() > -EMPTY_SHOT_TIME;
			}
			
			@Override
			public boolean shouldContinueExecuting() 
			{
				return super.shouldContinueExecuting() && getAwakeningStep() > 0.9f;
			}
		});
		this.tasks.addTask(1, new EntityAIAttackMelee(this, 0.4, true)
				{
					@Override
					public boolean shouldExecute() 
					{
						return super.shouldExecute() && getAwakeningStep() > 0.9f;
					}
					
					@Override
					public boolean shouldContinueExecuting() 
					{
						return super.shouldContinueExecuting() && getAwakeningStep() > 0.9f;
					}
				});
		this.tasks.addTask(2, new EntityAIWanderAvoidWater(this, 0.4, 0.0f)
		{
			@Override
			public boolean shouldExecute() 
			{
				return super.shouldExecute() && getAwakeningStep() > 0.9f;
			}
			
			@Override
			public boolean shouldContinueExecuting() 
			{
				return super.shouldContinueExecuting() && getAwakeningStep() > 0.9f;
			}
		});
		this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f)
		{
			@Override
			public boolean shouldExecute() 
			{
				return super.shouldExecute() && getAwakeningStep() > 0.9f;
			}
		});
		this.tasks.addTask(4, new EntityAILookIdle(this)
		{
			@Override
			public boolean shouldExecute() 
			{
				return super.shouldExecute() && getAwakeningStep() > 0.9f;
			}
		});
		
		this.targetTasks.addTask(0, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 10, false, true, new Predicate<EntityLivingBase>()
				{
					public boolean apply(EntityLivingBase input) 
					{
						return !(input instanceof EntityGolem);
					};
				}));
	}
	
	@Override
	public void onUpdate() 
	{
		super.onUpdate();
		
		if(!world.isRemote)
		{
			float awakeningStep = getAwakeningStep();
			if(getAttackTarget() == null || !getAttackTarget().isEntityAlive() || world.getDifficulty() == EnumDifficulty.PEACEFUL)
			{
				if(awakeningStep > 0.0f)
					setAwakeningStep(awakeningStep - 0.05f);
				if(awakeningStep < 0.0f)
					setAwakeningStep(0.0f);
			}
			else
			{
				if(awakeningStep < 1.0f)
					setAwakeningStep(awakeningStep + 0.05f);
				if(awakeningStep > 1.0f)
					setAwakeningStep(1.0f);;
			}
			
			if(ammo <= -EMPTY_SHOT_TIME)
			{
				reloadTimer--;
				if(reloadTimer <= 0)
					ammo = DEFAULT_AMMO;
			}
		}
	}
	
	@Override
	public void fall(float distance, float damageMultiplier) 
	{
        int i = MathHelper.ceil(distance - 1.0F);

        if (i > 0)
        {
            List<Entity> list = Lists.newArrayList(this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()));
            DamageSource damagesource = SWDamageSource.GOLEM_FALL;

            for (Entity entity : list)
            {
                entity.attackEntityFrom(damagesource, (float)Math.min(MathHelper.floor((float)i * FALL_HURT_BASE_DAMAGE), FALL_MAX_HURT_DAMAGE));
            }
            
            this.playSound(getFallSound((int)distance), Math.min(i * 0.1f, 1.0f), 0.75f);
            if(i > 3)
            {
            	world.createExplosion(this, posX, posY, posZ, Math.min((i - 3) * 0.5f, 8), true);
            }
        }
	}
	
	public int getAmmo()
	{
		return ammo;
	}
	
	public float getAwakeningStep()
	{
		return dataManager.get(AWAKENING);
	}
	
	public void setAwakeningStep(float value)
	{
		dataManager.set(AWAKENING, value);
	}
	
	@Override
	public boolean attackEntityAsMob(Entity entityIn) 
	{
		float f = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        int i = 0;

        if (entityIn instanceof EntityLivingBase)
        {
            f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase)entityIn).getCreatureAttribute());
            i += EnchantmentHelper.getKnockbackModifier(this);
        }

        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

        if (flag)
        {
            if (i > 0 && entityIn instanceof EntityLivingBase)
            {
                ((EntityLivingBase)entityIn).knockBack(this, (float)i * 0.5F, (double)MathHelper.sin(this.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int j = EnchantmentHelper.getFireAspectModifier(this);

            if (j > 0)
            {
                entityIn.setFire(j * 4);
            }

            if (entityIn instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)entityIn;
                ItemStack itemstack = this.getHeldItemMainhand();
                ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;

                if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem().canDisableShield(itemstack, itemstack1, entityplayer, this) && itemstack1.getItem().isShield(itemstack1, entityplayer))
                {
                    float f1 = 0.25F + (float)EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

                    if (this.rand.nextFloat() < f1)
                    {
                        entityplayer.getCooldownTracker().setCooldown(itemstack1.getItem(), 100);
                        this.world.setEntityState(entityplayer, (byte)30);
                    }
                }
            }

            this.applyEnchantments(this, entityIn);
        }

        return flag;
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) 
	{
		super.writeEntityToNBT(compound);
		compound.setFloat("awakening", getAwakeningStep());
		compound.setByte("ammo", ammo);
		compound.setInteger("reloading", reloadTimer);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) 
	{
		super.readEntityFromNBT(compound);
		if(compound.hasKey("awakening"))
			setAwakeningStep(compound.getFloat("awakening"));
		if(compound.hasKey("ammo"))
			ammo = compound.getByte("ammo");
		if(compound.hasKey("reloading"))
			reloadTimer = compound.getInteger("reloading");
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) 
	{
		if(ammo > 0)
		{
			EntityTippedArrow arrow = new EntityTippedArrow(world, this);
			arrow.setEnchantmentEffectsFromEntity(this, distanceFactor);
			double dx = target.posX - posX;
			double dy = target.posY - posY;
			double dz = target.posZ - posZ;
			arrow.setThrowableHeading(dx, dy, dz, 2.3f, (5 - world.getDifficulty().getDifficultyId()) * 3.0f);
			this.playSound(SoundInitializer.ANCITE_GOLEM_SHOOT, 1.0f, 0.9f + rand.nextFloat() * 0.2f);
			world.spawnEntity(arrow);
		}
		else
			this.playSound(SoundInitializer.ANCITE_GOLEM_OUT_OF_AMMO, 0.5f, 0.9f + rand.nextFloat() * 0.2f);
		
		ammo--;
		
		if(ammo <= -EMPTY_SHOT_TIME)
			reloadTimer = RELOAD_TIME;
	}

	@Override
	public void setSwingingArms(boolean swingingArms) 
	{
	}
}
