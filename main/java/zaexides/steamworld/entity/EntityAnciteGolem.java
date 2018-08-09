package zaexides.steamworld.entity;

import java.util.List;

import org.apache.logging.log4j.Level;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIFindEntityNearest;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.init.LootTableInitializer;
import zaexides.steamworld.utility.SWDamageSource;

public class EntityAnciteGolem extends EntityGolem implements IMob
{		
	protected static final DataParameter<Float> AWAKENING = EntityDataManager.<Float>createKey(EntityAnciteGolem.class, DataSerializers.FLOAT);
	
	private static final AxisAlignedBB PLAYER_CHECK_AREA = new AxisAlignedBB(-4, -4, -4, 4, 4, 4);
	private static final int FALL_MAX_HURT_DAMAGE = 40;
    private static final float FALL_HURT_BASE_DAMAGE = 2.0F;
	
	public EntityAnciteGolem(World worldIn) 
	{
		super(worldIn);
		setSize(1.0f, 1.0f);
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
		this.tasks.addTask(0, new EntityAIAttackMelee(this, 0.4, true)
				{
					@Override
					public boolean shouldExecute() 
					{
						return super.shouldExecute() && getAwakeningStep() > 0.9f;
					}
				});
		this.tasks.addTask(1, new EntityAIWanderAvoidWater(this, 0.4, 0.0f)
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
		this.tasks.addTask(2, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f)
		{
			@Override
			public boolean shouldExecute() 
			{
				return super.shouldExecute() && getAwakeningStep() > 0.9f;
			}
		});
		this.tasks.addTask(3, new EntityAILookIdle(this)
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
        }
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
        SteamWorld.logger.log(Level.INFO, flag);

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
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) 
	{
		super.readEntityFromNBT(compound);
		if(compound.hasKey("awakening"))
			setAwakeningStep(compound.getFloat("awakening"));
	}
}
