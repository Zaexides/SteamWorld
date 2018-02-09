package zaexides.steamworld.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.utility.AnciteScriptHandler;

public class GuiObilisk extends GuiScreen
{
	private GuiButton closeButton;
	
	private final int xSize = 74;
	private final int ySize = 256;
	
	private int guiLeft;
	private int guiTop;
	
	private String text;
	
	public GuiObilisk(String text) 
	{
		this.text = text;
	}
	
	@Override
	public void initGui() 
	{
		guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;
        
        closeButton = new GuiButton(0, width / 2, height - 32, I18n.format("gui.back"));
        closeButton.x -= closeButton.width / 2;
        buttonList.add(closeButton);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
		drawDefaultBackground();
		
		mc.getTextureManager().bindTexture(new ResourceLocation(ModInfo.MODID, "textures/gui/obilisk.png"));
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		drawText();
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	private void drawText()
	{
		mc.getTextureManager().bindTexture(new ResourceLocation(ModInfo.MODID, "textures/gui/obilisk_text.png"));
		final int xPos = 20;
		final int yPos = 200;
		
		for(int i = 0; i < text.length(); i++)
		{
			int charId = AnciteScriptHandler.getIdFromChar(text.charAt(i));
			
			if(charId != -1)
			{
				int charXPos = guiLeft + xPos + ( (i % 2) * 18 );
				int charYPos = guiTop + yPos - (int)Math.floor(i / 2.0) * 22;
				int texPosX = (charId % 13) * 16;
				int texPosY = (int)Math.floor(charId / 13.0) * 32;
				
				drawTexturedModalRect(charXPos, charYPos, texPosX, texPosY, 16, 32);
			}
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		keyTyped('7', 1);
	}
}
