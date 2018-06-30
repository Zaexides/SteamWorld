package zaexides.steamworld.items;

import java.util.List;

import org.apache.logging.log4j.Level;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;

public class ItemGaliteJet extends SteamWorldItem
{
	public ItemGaliteJet(String name) 
	{
		super(name, SteamWorld.CREATIVETAB_UTILITY);
		maxStackSize = 1;
		
		addPropertyOverride(new ResourceLocation(ModInfo.MODID, "active"), new IItemPropertyGetter() 
		{
			@Override
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn)
			{
				return stack.getMetadata();
			}
		});
	}
	
	@Override
	public int getMetadata(int damage) 
	{
		return damage;
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) 
	{
		if(stack.getMetadata() == 1)
		{
			if(entityIn.motionY < 0.2)
				entityIn.motionY += 0.1;
			
			if(entityIn.motionY > 0)
				entityIn.fallDistance = 0;
			else
				entityIn.fallDistance = (float)entityIn.motionY * -5;
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) 
	{
		tooltip.add(I18n.format("item.steamworld.galite_jet.tooltip[0]"));
		tooltip.add(I18n.format("item.steamworld.galite_jet.tooltip[1]"));
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) 
	{
		ItemStack stack = playerIn.getHeldItem(handIn).copy();
		stack.setItemDamage( stack.getItemDamage() > 0 ? 0 : 1 );
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}
	
	@Override
	public void RegisterModels() 
	{
		SteamWorld.proxy.RegisterItemRenderer(this, 0, "inventory");
		SteamWorld.proxy.RegisterItemRenderer(this, 1, "inventory");
	}
}
