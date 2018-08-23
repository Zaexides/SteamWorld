package zaexides.steamworld.advancements;


import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import zaexides.steamworld.ModInfo;

public class VillanglerTradeTrigger implements ICriterionTrigger<VillanglerTradeTrigger.Instance>
{
	private static final ResourceLocation ID = new ResourceLocation(ModInfo.MODID, "villangler_trade");
	private final Map<PlayerAdvancements, Listeners> listeners = Maps.<PlayerAdvancements, Listeners>newHashMap();
	
	@Override
	public ResourceLocation getId() 
	{
		return ID;
	}

	@Override
	public void addListener(PlayerAdvancements playerAdvancementsIn, Listener<VillanglerTradeTrigger.Instance> listener) 
	{
		Listeners villanglerTradeTrigger$listeners = this.listeners.get(playerAdvancementsIn);
		
		if(villanglerTradeTrigger$listeners == null)
		{
			villanglerTradeTrigger$listeners = new Listeners(playerAdvancementsIn);
			this.listeners.put(playerAdvancementsIn, villanglerTradeTrigger$listeners);
		}
		
		villanglerTradeTrigger$listeners.add(listener);
	}

	@Override
	public void removeListener(PlayerAdvancements playerAdvancementsIn, Listener<VillanglerTradeTrigger.Instance> listener) 
	{
		Listeners villanglerTradeTrigger$listeners = this.listeners.get(playerAdvancementsIn);
		
		if(villanglerTradeTrigger$listeners != null)
		{
			villanglerTradeTrigger$listeners.remove(listener);
			
			if(villanglerTradeTrigger$listeners.isEmpty())
				this.listeners.remove(playerAdvancementsIn);
		}
	}

	@Override
	public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
	{
		this.listeners.remove(playerAdvancementsIn);
	}

	@Override
	public VillanglerTradeTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) 
	{
		int count = JsonUtils.getInt(json, "amount", 1);
		return new Instance(count);
	}
	
	public void trigger(EntityPlayerMP entityPlayerMP)
	{
		Listeners villanglerTradeTrigger$listeners = this.listeners.get(entityPlayerMP.getAdvancements());
		
		if(villanglerTradeTrigger$listeners != null)
		{
			NBTTagCompound compound = entityPlayerMP.getEntityData();
			if(!compound.hasKey(EntityPlayer.PERSISTED_NBT_TAG))
				compound.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
			NBTTagCompound persistingData = compound.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
			
			if(persistingData.hasKey("sw_villangler_trades"))
			{
				villanglerTradeTrigger$listeners.trigger(persistingData.getInteger("sw_villangler_trades"));
			}
		}
	}
	
	public static class Instance extends AbstractCriterionInstance
	{
		private final int count;
		
		public Instance(int count) 
		{
			super(ID);
			this.count = count;
		}
		
		public boolean test(int amount)
		{
			return amount >= count;
		}
	}
	
	private static class Listeners
	{
		private final PlayerAdvancements playerAdvancements;
		private final Set<ICriterionTrigger.Listener<Instance>> listeners = Sets.<ICriterionTrigger.Listener<Instance>>newHashSet();
		
		public Listeners(PlayerAdvancements playerAdvancements) 
		{
			this.playerAdvancements = playerAdvancements;
		}
		
		public boolean isEmpty()
		{
			return this.listeners.isEmpty();
		}
		
		public void add(ICriterionTrigger.Listener<Instance> listener)
		{
			listeners.add(listener);
		}

		public void remove(ICriterionTrigger.Listener<Instance> listener)
		{
			listeners.remove(listener);
		}
		
		public void trigger(int amount)
		{
			List<ICriterionTrigger.Listener<Instance>> list = null;
			
			for(ICriterionTrigger.Listener<Instance> listener : this.listeners)
			{
				if(((Instance)listener.getCriterionInstance()).test(amount))
				{
					if(list == null)
						list = Lists.<ICriterionTrigger.Listener<Instance>>newArrayList();
					list.add(listener);
				}
			}
			
			if(list != null)
			{
				for(ICriterionTrigger.Listener<Instance> listener : list)
				{
					listener.grantCriterion(this.playerAdvancements);
				}
			}
		}
	}
}
