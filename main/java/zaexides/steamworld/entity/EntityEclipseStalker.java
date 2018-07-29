package zaexides.steamworld.entity;

import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import zaexides.steamworld.entity.ai.FloatingMoveHelper;
import zaexides.steamworld.world.dimension.WorldProviderSkyOfOld;

public class EntityEclipseStalker extends EntityMob
{
	private static final Predicate<Entity> NOT_UNDEAD = new Predicate<Entity>()
    {
        public boolean apply(@Nullable Entity p_apply_1_)
        {
            return p_apply_1_ instanceof EntityLivingBase && ((EntityLivingBase)p_apply_1_).getCreatureAttribute() != EnumCreatureAttribute.UNDEAD && ((EntityLivingBase)p_apply_1_).attackable();
        }
    };
	
	public EntityEclipseStalker(World worldIn) 
	{
		super(worldIn);
		setSize(0.95f, 1.2f);
	}
	
	@Override
	public float getEyeHeight() 
	{
		return 0.8f;
	}
	
	@Override
	protected void initEntityAI()
	{
		this.tasks.addTask(0, new EntityAIAttackMelee(this, 1.0D, false));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityLiving.class, 10, true, false, NOT_UNDEAD));
	}
	
	@Override
	protected void applyEntityAttributes() 
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10.0);
	}
	
	@Override
	public EnumCreatureAttribute getCreatureAttribute() 
	{
		return EnumCreatureAttribute.UNDEAD;
	}
	
	@Override
	public void fall(float distance, float damageMultiplier)
    {
    }
	
	@Override
    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos)
    {
    }
	
    @Override
    public void travel(float p_191986_1_, float p_191986_2_, float p_191986_3_)
    {
    	super.travel(p_191986_1_, p_191986_2_, p_191986_3_);
    	if(this.motionY < 0)
    		this.motionY *= 0.5;
    }
    
    @Override
    public void onLivingUpdate() 
    {
    	if(this.world.isRemote)
    	{
    		int particleCount = 2;
    		if(isInvisible())
    			particleCount = 5;
    		
    		for(int i = 0; i < particleCount; i++)
    		{
    			this.world.spawnParticle(
    					EnumParticleTypes.SMOKE_LARGE,
    					this.posX + (rand.nextDouble() - 0.5) * (double)this.width,
    					this.posY + (rand.nextDouble() - 0.5) * (double)this.height,
    					this.posZ + (rand.nextDouble() - 0.5) * (double)this.width,
    					0.0,0.0,0.0
    					);
    		}
    	}
    	
    	super.onLivingUpdate();
    }
    
    @Override
    public void onUpdate() 
    {
    	super.onUpdate();
    	
    	boolean isEclipse = false;
    	if(world.provider instanceof WorldProviderSkyOfOld)
    		isEclipse = ((WorldProviderSkyOfOld)world.provider).getEclipseFactor() < 0.75f;
    	
    	if(!isEclipse)
    	{
    		if(!this.world.isRemote)
    			this.setDead();
    		else
    		{
    			for(int i = 0; i < 10; i++)
        		{
        			this.world.spawnParticle(
        					EnumParticleTypes.SMOKE_LARGE,
        					this.posX + (rand.nextDouble() - 0.5) * (double)this.width,
        					this.posY + (rand.nextDouble() - 0.5) * (double)this.height,
        					this.posZ + (rand.nextDouble() - 0.5) * (double)this.width,
        					rand.nextDouble() - 0.5,
        					rand.nextDouble() - 0.5,
        					rand.nextDouble() - 0.5
        					);
        		}
    			this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.BLOCK_REDSTONE_TORCH_BURNOUT, SoundCategory.HOSTILE, 1.0f, 0.8f, false);
    		}
    	}
    }
    
    @Override
    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_BLAZE_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource)
    {
        return SoundEvents.ENTITY_BLAZE_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_BLAZE_DEATH;
    }
    
    @Override
    public boolean getCanSpawnHere() 
    {
    	boolean isEclipse = false;
    	
    	if(world.provider instanceof WorldProviderSkyOfOld)
    		isEclipse = ((WorldProviderSkyOfOld)world.provider).getEclipseFactor() < 0.25f;
    	
    	return super.getCanSpawnHere() && isEclipse;
    }
}