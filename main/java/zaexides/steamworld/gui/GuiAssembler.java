package zaexides.steamworld.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import scala.collection.generic.BitOperations.Int;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.containers.ContainerAssembler;
import zaexides.steamworld.te.TileEntityAssembler;
import zaexides.steamworld.utility.IGuiContainerUtil;
import zaexides.steamworld.utility.SWGuiUtil;

public class GuiAssembler extends GuiContainer implements IGuiContainerUtil
{
	private TileEntityAssembler tileEntity;
	private IInventory playerInv;
	
	public GuiAssembler(EntityPlayer player, IInventory playerInv, TileEntityAssembler tileEntity) 
	{
		super(new ContainerAssembler(player, playerInv, tileEntity));
		
		xSize = 176;
		ySize = 182;
		
		this.tileEntity = tileEntity;
		this.playerInv = playerInv;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		mc.getTextureManager().bindTexture(new ResourceLocation(ModInfo.MODID, "textures/gui/machine/gui_steam_assembler.png"));
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		float progress = ((float) tileEntity.progression / ((float) tileEntity.duration / 4));
		
		float xOff1 = 26 * progress;
		if(xOff1 > 26) xOff1 = 26;
		
		float xOff2 = (26 * progress)-26;
		if(xOff2 > 26) xOff2 = 26;
		else if(xOff2 < 0) xOff2 = 0;
		
		float xOff3 = (26 * progress)-26*2;
		if(xOff3 > 26) xOff3 = 26;
		else if(xOff3 < 0) xOff3 = 0;
		
		float xOff4 = (26 * progress)-26*3;
		if(xOff4 > 26) xOff4 = 26;
		else if(xOff4 < 0) xOff4 = 0;
		
		drawTexturedModalRect(guiLeft + 75, guiTop + 73, 176, 0, (int)xOff1, 5);
		drawTexturedModalRect(guiLeft + 75, guiTop + 73, 176, 5, (int)xOff2, 5);
		drawTexturedModalRect(guiLeft + 75, guiTop + 73, 176, 10, (int)xOff3, 5);
		drawTexturedModalRect(guiLeft + 75, guiTop + 73, 176, 15, (int)xOff4, 5);
		
		SWGuiUtil.DrawFluid(this, tileEntity.steamTank, mouseX, mouseY, 8, 39, 16, 41);
		
		tileEntity = (TileEntityAssembler) tileEntity.getWorld().getTileEntity(tileEntity.getPos());
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) 
	{
		String fgString = I18n.format("container.steamworld.assembler");
		
		int x = xSize / 2 - mc.fontRenderer.getStringWidth(fgString) / 2;
		mc.fontRenderer.drawString(fgString, x, 6, 4210752);
		mc.fontRenderer.drawString(playerInv.getDisplayName().getFormattedText(), 8, 88, 4210752);
		
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
		SWGuiUtil.DrawFluidHoverText(this, tileEntity.steamTank, mouseX, mouseY, 8, 39, 16, 41);
		
		if(((IGuiContainerUtil)this).isInRegion(75, 73, 26, 5, mouseX, mouseY))
		{
			float progress = ((float) tileEntity.progression / (float) tileEntity.duration);
			int percentage = (int)(progress * 100f);
			
			List<String> hoverText = new ArrayList<String>();
			hoverText.add(percentage + "%");
			GuiUtils.drawHoveringText(hoverText, mouseX, mouseY, mc.displayWidth, mc.displayHeight, -1, mc.fontRenderer);
		}
	}

	@Override
	public boolean isInRegion(int x, int y, int w, int h, int mX, int mY) 
	{
		return isPointInRegion(x, y, w, h, mX, mY);
	}
}
