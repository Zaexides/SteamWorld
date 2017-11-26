package zaexides.steamworld.utility;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.message.LocalizedMessage;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.client.config.GuiUtils;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.network.PacketHandler;
import zaexides.steamworld.network.messages.MessageGuiButton;

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
			
			guiContainer.drawModalRectWithCustomSizedTexture(
					x + guiContainer.getGuiLeft(),
					y + guiContainer.getGuiTop() + drawnY,
					0,0,
					w,drawnHeight + 1,
					16,16
					);
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
	
	public static void HandleButton(BlockPos position, byte id, int mouseX, int mouseY, int x, int y, int w, int h)
	{
		if(mouseX >= x && mouseX < x+w && mouseY >= y && mouseY < y+h)
		{
			PacketHandler.wrapper.sendToServer(new MessageGuiButton(position, id));
			Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
		}
	}
}
