package zaexides.steamworld.utility;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.client.config.GuiUtils;
import zaexides.steamworld.network.PacketHandler;
import zaexides.steamworld.network.messages.MessageGuiButton;
import zaexides.steamworld.utility.interfaces.IGuiContainerUtil;

public class SWGuiUtil
{
	public static void DrawFluid(GuiContainer guiContainer, FluidTank tank, int mouseX, int mouseY, int x, int y, int w, int h)
	{
		if(tank.getFluidAmount() > 0)
		{
			ResourceLocation texture = tank.getFluid().getFluid().getStill();
			texture = new ResourceLocation(texture.getResourceDomain(), "textures/" + texture.getResourcePath() + ".png");
			guiContainer.mc.renderEngine.bindTexture(texture);
			float percentage = ((float)tank.getFluidAmount()) / ((float) tank.getCapacity());
			int drawnHeight = (int)((float)h * percentage);
			int drawnY = (int) ((float)h * (1 - percentage));
			
			int col = tank.getFluid().getFluid().getColor();
			float a = ((col >> 24) & 255) / 255.0f;
			float r = ((col >> 16) & 255) / 255.0f;
			float g = ((col >> 8) & 255) / 255.0f;
			float b = (col & 255) / 255.0f;
			
			GlStateManager.color(r, g, b, a);
			guiContainer.drawModalRectWithCustomSizedTexture(
					x + guiContainer.getGuiLeft(),
					y + guiContainer.getGuiTop() + drawnY,
					0,0,
					w,drawnHeight + 1,
					16,16
					);
			GlStateManager.color(1, 1, 1, 1);
		}
	}
	
	public static void DrawFluidHoverText(GuiContainer guiContainer, FluidTank tank, int mouseX, int mouseY, int x, int y, int w, int h)
	{
		if(!(guiContainer instanceof IGuiContainerUtil) || tank == null)
			return;
		IGuiContainerUtil utilCast = (IGuiContainerUtil) guiContainer;
		
		if(utilCast.isInRegion(x, y, w, h, mouseX, mouseY))
		{
			List<String> hoverText = new ArrayList<String>();
			if(tank.getFluidAmount() > 0)
				hoverText.add(tank.getFluid().getLocalizedName());
			else
			{
				TextComponentTranslation comp = new TextComponentTranslation("fluid.empty");
				hoverText.add(comp.getFormattedText());
			}
			hoverText.add(tank.getFluidAmount() + "/" + tank.getCapacity() + " mb");
			GuiUtils.drawHoveringText(hoverText, mouseX, mouseY, guiContainer.mc.displayWidth, guiContainer.mc.displayHeight, -1, guiContainer.mc.fontRenderer);
		}
	}
	
	public static void DrawHoverText(GuiContainer guiContainer, List<String> text, int mouseX, int mouseY, int x, int y, int w, int h)
	{
		if(!(guiContainer instanceof IGuiContainerUtil))
			return;
		IGuiContainerUtil utilCast = (IGuiContainerUtil) guiContainer;
		
		if(utilCast.isInRegion(x, y, w, h, mouseX, mouseY))
		{
			GuiUtils.drawHoveringText(text, mouseX, mouseY, guiContainer.mc.displayWidth, guiContainer.mc.displayHeight, -1, guiContainer.mc.fontRenderer);
		}
	}
	
	public static boolean HandleButton(BlockPos position, byte id, int mouseX, int mouseY, int x, int y, int w, int h)
	{
		return HandleButton(position, id, mouseX, mouseY, x, y, w, h, true);
	}
	
	public static boolean HandleButton(BlockPos position, byte id, int mouseX, int mouseY, int x, int y, int w, int h, boolean sendMessage)
	{
		if(mouseX >= x && mouseX < x+w && mouseY >= y && mouseY < y+h)
		{
			if(sendMessage)
				PacketHandler.wrapper.sendToServer(new MessageGuiButton(position, id));
			Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
			return true;
		}
		return false;
	}
}
