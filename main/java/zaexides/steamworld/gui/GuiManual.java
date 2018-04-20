package zaexides.steamworld.gui;

import java.io.IOException;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.te.TileEntityObilisk;
import zaexides.steamworld.utility.AnciteScriptHandler;

public class GuiManual extends GuiScreen
{
	private GuiButton closeButton;
	
	private final int pageXSize = 180;
	private final int xSize = 359;
	private final int ySize = 256;
	
	private int guiLeft;
	private int guiTop;
	
	private int page = 0;
	
	public GuiManual() 
	{
	}
	
	@Override
	public void initGui() 
	{
		guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
		drawDefaultBackground();
		
		mc.getTextureManager().bindTexture(new ResourceLocation(ModInfo.MODID, "textures/gui/manual_steaite.png"));
		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, xSize, ySize, 512, 512);
		drawText();
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	private void drawText()
	{
		final int xPos1 = 16, xPos2 = 190, yPos = 16, xMax = 156;
		
		mc.fontRenderer.drawSplitString(I18n.format("text.manual.page[" + page + "].left"), guiLeft + xPos1, guiTop + yPos, xMax, 0x000000);
		mc.fontRenderer.drawSplitString(I18n.format("text.manual.page[" + page + "].right"), guiLeft + xPos2, guiTop + yPos, xMax, 0x000000);
	}
}
