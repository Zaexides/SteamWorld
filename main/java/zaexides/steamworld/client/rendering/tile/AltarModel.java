package zaexides.steamworld.client.rendering.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import zaexides.steamworld.te.TileEntityAltar;

public class AltarModel extends TileEntitySpecialRenderer<TileEntityAltar>
{
	@Override
	public void render(TileEntityAltar te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) 
	{
		ItemStack stack = te.itemStackHandler.getStackInSlot(0);
		
		if(stack.equals(ItemStack.EMPTY))
			return;
		
		GlStateManager.pushMatrix();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.translate(x + 0.5, y + 1.25, z + 0.5);
		GlStateManager.enableLighting();
		GlStateManager.scale(0.5, 0.5, 0.5);
		
		if(!(stack.getItem() instanceof ItemBlock))
		{
			float worldTime = te.getWorld().getTotalWorldTime() * 0.5f;
			GlStateManager.rotate(worldTime, 0.0f, 1.0f, 0.0f);
			GlStateManager.translate(0, Math.sin(worldTime * 0.05f) * 0.1f + 0.15f, 0);
		}
		
		Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
		
		GlStateManager.popMatrix();
	}
}
