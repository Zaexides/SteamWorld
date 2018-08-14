package zaexides.steamworld.entity;

import org.apache.logging.log4j.Level;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWaterFlying;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityFlyHelper;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityFlying;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.entity.ai.EntityAIBreakNearbyBlock;
import zaexides.steamworld.entity.interfaces.IEntityBreakBlockCallback;
import zaexides.steamworld.init.ItemInitializer;
import zaexides.steamworld.init.LootTableInitializer;
import zaexides.steamworld.init.SoundInitializer;
import zaexides.steamworld.items.ItemMaterial;

public class EntitySkyFish extends EntityFlyingAnimal implements IEntityBreakBlockCallback
{
	private byte woodEaten, leavesEaten;
	
	private static final byte LEAVES_POOP_THRESHOLD = 20;
	private static final byte WOOD_POOP_THRESHOLD = 32;
	
	private int hunger;
	
	private static final int LEAVES_SATURATION = 100;
	private static final int PLANKS_SATURATION = 500;
	private static final int LOG_SATURATION = 2000;
	
	private static final int LOG_EAT_MATE_CHANCE = 50;
	private static final int PLANKS_EAT_MATE_CHANCE = 200;
	
	private static final int HUNGER_PANIC = 6000;
	private static final int MAX_HUNGER = 12000; //Point at which they start to take damage.
	
	public EntitySkyFish(World worldIn) 
	{
		super(worldIn);
		setSize(1.8f, 0.7f);
		hunger = worldIn.rand.nextInt(7200) - 3600; //Initial hunger (-3600 to 3600 = -3 to 3 minutes)
	}
	
	@Override
	protected void initEntityAI() 
	{
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIMate(this, 1.0));
		this.tasks.addTask(2, new EntityAIPanic(this, 3.0));
		this.tasks.addTask(3, new EntityAIBreakNearbyBlock(this, 60, Blocks.LOG, 2));
		this.tasks.addTask(3, new EntityAIBreakNearbyBlock(this, 60, Blocks.LOG2, 2));
		this.tasks.addTask(3, new EntityAIBreakNearbyBlock(this, 40, Blocks.PLANKS, 2));
		this.tasks.addTask(3, new EntityAIBreakNearbyBlock(this, 20, Blocks.LEAVES, 2));
		this.tasks.addTask(3, new EntityAIBreakNearbyBlock(this, 20, Blocks.LEAVES2, 2));
		this.tasks.addTask(4, new EntityAIWanderAvoidWaterFlying(this, 1.0));
		this.tasks.addTask(5, new EntityAIFollowParent(this, 1.25));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
	}

	@Override
	public EntityAgeable createChild(EntityAgeable ageable) 
	{
		return new EntitySkyFish(this.world);
	}
	
	@Override
	protected void applyEntityAttributes() 
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6.0);
		this.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.6);
	}
	
	@Override
	protected SoundEvent getAmbientSound() 
	{
		return super.getAmbientSound();
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource source) 
	{
		return super.getHurtSound(source);
	}
	
	@Override
	protected SoundEvent getDeathSound() 
	{
		return super.getDeathSound();
	}
	
	@Override
	public float getEyeHeight() 
	{
		return this.height * .5f;
	}
	
	@Override
	public boolean isBreedingItem(ItemStack stack) 
	{
		return stack.getItem() == Items.STICK;
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) 
	{
		super.writeEntityToNBT(compound);
		compound.setByte("WoodEaten", woodEaten);
		compound.setByte("LeavesEaten", leavesEaten);
		compound.setInteger("Hunger", hunger);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) 
	{
		super.readEntityFromNBT(compound);
		if(compound.hasKey("WoodEaten"))
			woodEaten = compound.getByte("WoodEaten");
		if(compound.hasKey("LeavesEaten"))
			leavesEaten = compound.getByte("LeavesEaten");
		if(compound.hasKey("Hunger"))
			hunger = compound.getInteger("Hunger");
	}

	@Override
	public void OnBlockBroken(Block block, BlockPos blockPos) 
	{
		if(block instanceof BlockLog)
		{
			woodEaten += 4;
			hunger -= LOG_SATURATION;
			if(rand.nextInt(LOG_EAT_MATE_CHANCE) == 0)
				this.setInLove(null);
		}
		else if(block instanceof BlockPlanks)
		{
			woodEaten += 1;
			hunger -= PLANKS_SATURATION;
			if(rand.nextInt(PLANKS_EAT_MATE_CHANCE) == 0)
				this.setInLove(null);
		}
		
		if(woodEaten >= WOOD_POOP_THRESHOLD)
		{
			this.playSound(SoundInitializer.SKYFISH_PLOP, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			this.entityDropItem(new ItemStack(Items.COAL, 1, 1), 0);
			woodEaten -= WOOD_POOP_THRESHOLD;
		}
		
		if(block instanceof BlockLeaves)
		{
			leavesEaten++;
			hunger -= LEAVES_SATURATION;
			if(leavesEaten >= LEAVES_POOP_THRESHOLD)
			{
				this.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
				this.entityDropItem(new ItemStack(ItemInitializer.GENERIC_MATERIAL, 1, ItemMaterial.EnumVarietyMaterial.BIOMATTER.getMeta()), 0);
				leavesEaten -= LEAVES_POOP_THRESHOLD;
			}
		}
	}

	@Override
	public boolean CanBreakBlocks() 
	{
		return hunger > 0;
	}
	
	@Override
	public void onUpdate() 
	{
		super.onUpdate();
		
		PotionEffect saturationEffect = getActivePotionEffect(MobEffects.SATURATION);
		PotionEffect hungerEffect = getActivePotionEffect(MobEffects.HUNGER);
		
		int hungerTaken = 1;
		if(saturationEffect != null)
			hungerTaken -= saturationEffect.getAmplifier();
		if(hungerEffect != null)
			hungerTaken += hungerEffect.getAmplifier();
		
		if(hungerTaken > 0)
			hunger += hungerTaken;
		
		if(hunger >= HUNGER_PANIC)
			setRevengeTarget(this);
		
		if(hunger >= (MAX_HUNGER + 20))
		{
			hunger = MAX_HUNGER;
			this.damageEntity(DamageSource.STARVE, 1.0f);
		}
	}
	
	@Override
	protected ResourceLocation getLootTable()
	{
		return LootTableInitializer.DROPS_SKYFISH;
	}
}
