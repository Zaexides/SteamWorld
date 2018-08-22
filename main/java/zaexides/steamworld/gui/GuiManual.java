package zaexides.steamworld.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.te.TileEntityObilisk;
import zaexides.steamworld.utility.AnciteScriptHandler;
import zaexides.steamworld.utility.SWGuiUtil;

public class GuiManual extends GuiScreen
{
	private final int pageXSize = 180;
	private final int xSize = 359;
	private final int ySize = 256;
	
	private int guiLeft;
	private int guiTop;
	
	private int page = 0;
	
	private Page[] pages = new Page[]
			{
					//Steaite
					new Page("title", new ResourceLocation(ModInfo.MODID, "textures/gui/logo.png"), 0, 0, 128, 128, 128, 128),
					new Page("intro"),
					new Page("fluid_control", new ResourceLocation(ModInfo.MODID, "textures/gui/manual_steaite.png"), 0, 256, 155, 73, 512, 512),
					new Page("fluid_control_2"),
					new Page("basic_machine", new ResourceLocation(ModInfo.MODID, "textures/gui/manual_steaite.png"), 155, 256, 109, 109, 512, 512),
					new Page("basic_machine_2"),
					new Page("basic_machine_3"),
					new Page("crypts", new ResourceLocation(ModInfo.MODID, "textures/gui/manual_steaite.png"), 264, 256, 57, 111, 512, 512),
					//Ancite
					new Page("upgrades", new ResourceLocation(ModInfo.MODID, "textures/gui/manual_ancite.png"), 0, 256, 150, 103, 512, 512),
					new Page("assembler", new ResourceLocation(ModInfo.MODID, "textures/gui/manual_ancite.png"), 150, 256, 78, 78, 512, 512),
					new Page("assembler_recipes", new ResourceLocation(ModInfo.MODID, "textures/gui/manual_ancite.png"), 228, 256, 160, 180, 512, 512),
					//Endritch
					new Page("accelerator", new ResourceLocation(ModInfo.MODID, "textures/gui/manual_endritch.png"), 0, 256, 142, 78, 512, 512),
					new Page("teleporter", new ResourceLocation(ModInfo.MODID, "textures/gui/manual_endritch.png"), 142, 256, 70, 70, 512, 512),
					new Page("teleporter_details", new ResourceLocation(ModInfo.MODID, "textures/gui/manual_endritch.png"), 212, 256, 135, 82, 512, 512),
					new Page("portal", new ResourceLocation(ModInfo.MODID, "textures/gui/manual_endritch.png"), 0, 334, 96, 119, 512, 512),
					new Page("portal_2"),
					//Essen
					new Page("plates", new ResourceLocation(ModInfo.MODID, "textures/gui/manual_essen.png"), 0, 256, 54, 38, 512, 512),
					new Page("upgrades_2", new ResourceLocation(ModInfo.MODID, "textures/gui/manual_essen.png"), 54, 256, 100, 75, 512, 512),
					new Page("miner", new ResourceLocation(ModInfo.MODID, "textures/gui/manual_essen.png"), 154, 256, 135, 77, 512, 512),
					new Page("eclipse", new ResourceLocation(ModInfo.MODID, "textures/gui/manual_essen.png"), 0, 294, 54, 47, 512, 512),
			};
	
	private final int maxPageOverride;
	private final ResourceLocation backgroundImage;
	
	@Override
	public void initGui() 
	{
		guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;
	}
	
	public GuiManual(ResourceLocation backgroundImage) 
	{
		maxPageOverride = pages.length;
		this.backgroundImage = backgroundImage;
	}
	
	public GuiManual(int maxPages, ResourceLocation backgroundImage)
	{
		maxPageOverride = maxPages;
		this.backgroundImage = backgroundImage;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
		drawDefaultBackground();
		
		mc.getTextureManager().bindTexture(backgroundImage);
		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, xSize, ySize, 512, 512);
		drawButtons(mouseX, mouseY);
		drawPageNumbers();
		drawText();
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	private void drawPageNumbers()
	{
		int x = (guiLeft + pageXSize / 2) - (mc.fontRenderer.getStringWidth("-" + (page + 1) + "-") / 2);
		mc.fontRenderer.drawString("-" + (page + 1) + "-", x, guiTop + 241, 0x888888);
		
		if((page + 1) < maxPageOverride)
		{
			x = ((guiLeft + pageXSize) + pageXSize / 2) - (mc.fontRenderer.getStringWidth("-" + (page + 2) + "-") / 2);
			mc.fontRenderer.drawString("-" + (page + 2) + "-", x, guiTop + 241, 0x888888);
		}
	}
	
	private void drawButtons(int mouseX, int mouseY)
	{
		int xPos1 = guiLeft + 8, xPos2 = guiLeft + 336, yPos = guiTop + 240;
		
		if(page <= 1)
			drawModalRectWithCustomSizedTexture(xPos1, yPos, 375, 16, 16, 8, 512, 512);
		else if(mouseX >= xPos1 && mouseX <= xPos1+16 && mouseY >= yPos && mouseY <= yPos+8)
			drawModalRectWithCustomSizedTexture(xPos1, yPos, 375, 8, 16, 8, 512, 512);
		else
			drawModalRectWithCustomSizedTexture(xPos1, yPos, 375, 0, 16, 8, 512, 512);
		
		if(page+2 >= maxPageOverride)
			drawModalRectWithCustomSizedTexture(xPos2, yPos, 359, 16, 16, 8, 512, 512);
		else if(mouseX >= xPos2 && mouseX <= xPos2+16 && mouseY >= yPos && mouseY <= yPos+8)
			drawModalRectWithCustomSizedTexture(xPos2, yPos, 359, 8, 16, 8, 512, 512);
		else
			drawModalRectWithCustomSizedTexture(xPos2, yPos, 359, 0, 16, 8, 512, 512);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException 
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		int xPos1 = guiLeft + 8, xPos2 = guiLeft + 336, yPos = guiTop + 240;
		
		if(mouseY >= yPos && mouseY <= yPos+8)
		{
			if(page >= 2 && mouseX >= xPos1 && mouseX <= xPos1+16)
			{
				page -= 2;
				mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
			}
			
			if(page+2 < maxPageOverride && mouseX >= xPos2 && mouseX <= xPos2+16)
			{
				page += 2;
				mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
			}
		}
	}
	
	private void drawText()
	{
		final int xPos1 = 16, xPos2 = 190, yPos = 16, xMax = 156;
		
		drawPage(pages[page], guiLeft + xPos1, guiTop + yPos, xMax);
		if((page + 1) < maxPageOverride)
			drawPage(pages[page + 1], guiLeft + xPos2, guiTop + yPos, xMax);
	}
	
	private void drawPage(Page page, int xPos, int yPos, int wrapWidth)
	{
		int yPosOff = 0;
		int centerX = (xPos + (((xPos + wrapWidth) - xPos) / 2));
		
		String title = page.GetTranslatedTitle();
		if(title != "none")
		{
			yPosOff += 16;
			mc.fontRenderer.drawString(title, centerX - (mc.fontRenderer.getStringWidth(title) / 2), yPos, 0);
		}
		
		if(page.image != null)
		{
			GlStateManager.color(1.0f, 1.0f, 1.0f);
			mc.getTextureManager().bindTexture(page.image);
			
			drawModalRectWithCustomSizedTexture(centerX - (page.imageW / 2), yPos + yPosOff, page.imageU, page.imageV, page.imageW, page.imageH, page.texW, page.texH);
			yPosOff += page.imageH + 4;
		}
		
		String text = page.GetTranslatedString();
		mc.fontRenderer.drawSplitString(page.GetTranslatedString(), xPos, yPos + yPosOff, wrapWidth, 0);
	}
	
	private static class Page
	{
		private final String text;
		private final ResourceLocation image;
		private int imageU, imageV, imageW, imageH, texW, texH;
		
		public Page(String text)
		{
			this.text = text;
			this.image = null;
		}
		
		public Page(String text, ResourceLocation image, int u, int v, int w, int h, int tw, int th)
		{
			this.text = text;
			this.image = image;
			this.imageU = u;
			this.imageV = v;
			this.imageW = w;
			this.imageH = h;
			this.texW = tw;
			this.texH = th;
		}
		
		public String GetTranslatedString()
		{
			if(text != "")
				return I18n.format("text.steamworld.manual.page." + text, ModInfo.VERSION, MinecraftForge.MC_VERSION, ForgeVersion.getVersion(), TextFormatting.BLUE, TextFormatting.BLACK);
			else
				return "";
		}
		
		public String GetTranslatedTitle()
		{
			if(text != "")
				return TextFormatting.UNDERLINE + I18n.format("text.steamworld.manual.title." + text);
			else
				return "";
		}
	}
}
