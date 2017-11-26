package zaexides.steamworld.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;
import scala.collection.generic.BitOperations.Int;
import scala.xml.Text;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.containers.ContainerFertilizer;
import zaexides.steamworld.te.TileEntityFertilizer;
import zaexides.steamworld.utility.IGuiContainerUtil;
import zaexides.steamworld.utility.SWGuiUtil;

public class GuiFertilizer extends GuiContainer implements IGuiContainerUtil
{
	private TileEntityFertilizer tileEntity;
	private IInventory playerInv;
	
	public GuiFertilizer(EntityPlayer player, IInventory playerInv, TileEntityFertilizer tileEntity) 
	{
		super(new ContainerFertilizer(player, playerInv, tileEntity));
		
		xSize = 176;
		ySize = 165;
		
		this.tileEntity = tileEntity;
		this.playerInv = playerInv;
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException 
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		SWGuiUtil.HandleButton(tileEntity.getPos(), (byte)0, mouseX, mouseY, guiLeft + 133, guiTop + 34, 18, 18);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		mc.getTextureManager().bindTexture(new ResourceLocation(ModInfo.MODID, "textures/gui/machine/gui_fertilizer.png"));
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		int yOff = 0;
		if(tileEntity.cropOnlyMode)
			yOff += 8;
		
		drawTexturedModalRect(guiLeft + 134, guiTop + 35 + yOff, 176, 14, 16, 8);
		
		SWGuiUtil.DrawFluid(this, tileEntity.steamTank, mouseX, mouseY, 8, 23, 16, 41);
		
		tileEntity = (TileEntityFertilizer) tileEntity.getWorld().getTileEntity(tileEntity.getPos());
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) 
	{
		String fgString = I18n.format("container.steamworld.fertilizer");
		
		int x = xSize / 2 - mc.fontRenderer.getStringWidth(fgString) / 2;
		mc.fontRenderer.drawString(fgString, x, 6, 4210752);
		mc.fontRenderer.drawString(playerInv.getDisplayName().getFormattedText(), 8, 72, 4210752);
		
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
		SWGuiUtil.DrawFluidHoverText(this, tileEntity.steamTank, mouseX, mouseY, 8, 23, 16, 41);
		
		List<String> text = new ArrayList<String>();
		text.add(new TextComponentTranslation("text.steamworld.cropmode").getFormattedText());
		
		String translationKey = tileEntity.cropOnlyMode ? "text.steamworld.on" : "text.steamworld.off";
		TextComponentTranslation componentTranslation = new TextComponentTranslation(translationKey);
		componentTranslation.getStyle().setItalic(true);
		componentTranslation.getStyle().setColor(TextFormatting.GRAY);
		text.add(componentTranslation.getFormattedText());
		
		SWGuiUtil.DrawHoverText(this, text, mouseX, mouseY, 134, 35, 16, 16);
	}

	@Override
	public boolean isInRegion(int x, int y, int w, int h, int mX, int mY) 
	{
		return isPointInRegion(x, y, w, h, mX, mY);
	}
}
