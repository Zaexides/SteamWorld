package zaexides.steamworld.entity.villangler;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zaexides.steamworld.advancements.SteamWorldCriteriaTriggers;
import zaexides.steamworld.entity.ai.EntityAILookAtTradePlayerBetter;
import zaexides.steamworld.entity.ai.EntityAITradePlayerBetter;
import zaexides.steamworld.entity.ai.EntityAIVillanglerMate;

public class EntityVillangler extends EntityAgeable implements INpc, IMerchant
{
	private static final DataParameter<Integer> VARIANT = EntityDataManager.<Integer>createKey(EntityVillangler.class, DataSerializers.VARINT);
	private EntityPlayer customer;
	@Nullable private MerchantRecipeList merchantList;
	
	public EntityVillangler(World worldIn) 
	{
		this(worldIn, VillanglerVariant.DEFAULT);
	}
	
	public EntityVillangler(World worldIn, VillanglerVariant variant) 
	{
		super(worldIn);
		setSize(0.8f, 2.0f);
		setVariant(variant);
	}
	
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
	{
		setRandomVariant(true);
		return super.onInitialSpawn(difficulty, livingdata);
	}
	
	private void setRandomVariant(boolean includeCultist)
	{
		int i = -1;
		while(i == -1 || (i == VillanglerVariant.CULTIST.id && !includeCultist))
			i = rand.nextInt(VillanglerVariant.values().length);
		setVariant(i);
	}
	
	@Override
	protected void entityInit() 
	{
		super.entityInit();
		this.dataManager.register(VARIANT, 0);
	}
	
	@Override
	public float getEyeHeight()
	{
		return 1.5f;
	}
	
	@Override
	protected void applyEntityAttributes() 
	{
		super.applyEntityAttributes();
		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3);
	}
	
	@Override
	public EntityAgeable createChild(EntityAgeable ageable) 
	{
		return new EntityVillangler(world);
	}
	
	@Override
	protected void onGrowingAdult() 
	{
		super.onGrowingAdult();
		setRandomVariant(false);
	}
	
	@Override
	protected void initEntityAI() 
	{
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIPanic(this, 1.0)
				{
					@Override
					public boolean shouldExecute() 
					{
						if(getRevengeTarget() instanceof EntityPlayer)
							return false;
						return super.shouldExecute();
					}
				});
		this.tasks.addTask(2, new EntityAITradePlayerBetter(this));
		this.tasks.addTask(2, new EntityAILookAtTradePlayerBetter(this));
		this.tasks.addTask(3, new EntityAIWanderAvoidWater(this, 0.6));
		this.tasks.addTask(4, new EntityAIVillanglerMate(this, 0.8));
		this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityLiving.class, 4.0f));
		this.tasks.addTask(6, new EntityAIOpenDoor(this, true));
	}
	
	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand)
	{
		ItemStack itemstack = player.getHeldItem(hand);
        boolean flag = itemstack.getItem() == Items.NAME_TAG;

        if (flag)
        {
            itemstack.interactWithEntity(player, this, hand);
            return true;
        }
        else if (!this.holdingSpawnEggOfClass(itemstack, this.getClass()) && this.isEntityAlive() && this.customer == null && !this.isChild() && !player.isSneaking())
        {
            if (this.merchantList == null)
            {
                this.populateMerchantRecipeList();
            }

            if (hand == EnumHand.MAIN_HAND)
            {
                player.addStat(StatList.TALKED_TO_VILLAGER);
            }

            if (!this.world.isRemote && !this.merchantList.isEmpty())
            {
                this.setCustomer(player);
                player.displayVillagerTradeGui(this);
            }
            else if (this.merchantList.isEmpty())
            {
                return super.processInteract(player, hand);
            }

            return true;
        }
        else
        {
            return super.processInteract(player, hand);
        }
	}
	
	private void populateMerchantRecipeList()
	{
		if(this.merchantList == null)
			this.merchantList = new MerchantRecipeList();
		
		getVariant().generateMerchantRecipeList(this.merchantList, this.rand);
	}

	@Override
	public void setCustomer(EntityPlayer player) 
	{
		this.customer = player;
	}

	@Override
	public EntityPlayer getCustomer() 
	{
		return this.customer;
	}

	@Override
	public MerchantRecipeList getRecipes(EntityPlayer player) 
	{
		if(this.merchantList == null)
			populateMerchantRecipeList();
		return this.merchantList;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setRecipes(MerchantRecipeList recipeList) {
	}

	@Override
	public void useRecipe(MerchantRecipe recipe) 
	{
		recipe.incrementToolUses();
        this.livingSoundTime = -this.getTalkInterval();
        int i = 3 + this.rand.nextInt(4);

        if (recipe.getRewardsExp())
        {
            this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY + 0.5D, this.posZ, i));
        }
        
        if(this.customer instanceof EntityPlayerMP)
        {
        	NBTTagCompound compound = this.customer.getEntityData();
			if(!compound.hasKey(EntityPlayer.PERSISTED_NBT_TAG))
				compound.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
			NBTTagCompound persistingData = compound.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
			
			int tradeCount = 0;
			if(persistingData.hasKey("sw_villangler_trades"))
				tradeCount = persistingData.getInteger("sw_villangler_trades");
			tradeCount++;
			persistingData.setInteger("sw_villangler_trades", tradeCount);
			
        	SteamWorldCriteriaTriggers.VILLANGLER_TRADE_TRIGGER.trigger((EntityPlayerMP)this.customer);
        }
	}

	@Override
	public void verifySellingItem(ItemStack stack) {
	}

	@Override
	public World getWorld() 
	{
		return this.world;
	}

	@Override
	public BlockPos getPos() 
	{
		return new BlockPos(this);
	}
	
	public void setVariant(int variant)
	{
		this.dataManager.set(VARIANT, variant);
	}
	
	public void setVariant(VillanglerVariant variant)
	{
		setVariant(variant.getId());
	}
	
	public VillanglerVariant getVariant()
	{
		return VillanglerVariant.values()[this.dataManager.get(VARIANT)];
	}
	
	@Override
	protected boolean canDespawn() 
	{
		return false;
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) 
	{
		super.readEntityFromNBT(compound);
		
		if(compound.hasKey("Variant"))
			setVariant(compound.getInteger("Variant"));
		
		if(compound.hasKey("Offers", 10))
		{
			NBTTagCompound offerNBT = compound.getCompoundTag("Offers");
			this.merchantList = new MerchantRecipeList(offerNBT);
		}
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) 
	{
		super.writeEntityToNBT(compound);
		
		compound.setInteger("Variant", getVariant().getId());
		
		if(this.merchantList != null)
		{
			compound.setTag("Offers", this.merchantList.getRecipiesAsTags());
		}
	}
	
	public static enum VillanglerVariant
	{
		DEFAULT(0, new VillanglerMerchantGenDefault()),
		ECONOMIST(1, new VillanglerMerchantGenEconomist()),
		LIBRARIAN(2, new VillanglerMerchantGenLibrarian()),
		SCIENTIST(3, new VillanglerMerchantGenScientist()),
		BREEDER(4, new VillanglerMerchantGenBreeder()),
		CULTIST(5, new VillanglerMerchantGenCultist());
		
		private final int id;
		private final VillanglerMerchantRecipeListGenerator recipeListGenerator;
		private VillanglerVariant(int id, VillanglerMerchantRecipeListGenerator recipeListGenerator)
		{
			this.id = id;
			this.recipeListGenerator = recipeListGenerator;
		}
		
		private int getId()
		{
			return id;
		}
		
		public MerchantRecipeList generateMerchantRecipeList(MerchantRecipeList merchantRecipeList, Random random)
		{
			return recipeListGenerator.generateMerchantRecipeList(merchantRecipeList, random);
		}
	}
}
