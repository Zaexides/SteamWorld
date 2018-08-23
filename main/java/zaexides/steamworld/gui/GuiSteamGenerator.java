package zaexides.steamworld.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.containers.ContainerSteamGenerator;
import zaexides.steamworld.te.TileEntitySteamGenerator;
import zaexides.steamworld.utility.SWGuiUtil;
import zaexides.steamworld.utility.interfaces.IGuiContainerUtil;

public class GuiSteamGenerator extends GuiContainer implements IGuiContainerUtil
{
	private TileEntitySteamGenerator tileEntity;
	private IInventory playerInv;
	
	public GuiSteamGenerator(IInventory playerInv, TileEntitySteamGenerator tileEntity) 
	{
		super(new ContainerSteamGenerator(playerInv, tileEntity));
		
		xSize = 176;
		ySize = 165;
		
		this.tileEntity = tileEntity;
		this.playerInv = playerInv;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		//GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(new ResourceLocation(ModInfo.MODID, "textures/gui/machine/gui_steam_generator.png"));
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		float burntimeFraction = 0;
		
		if(tileEntity.totalBurntime > 0)
			burntimeFraction = (float)tileEntity.burnTime / (float)tileEntity.totalBurntime;
		
		int yOff = (int)((1 - burntimeFraction) * 14);
		drawTexturedModalRect(
				guiLeft + 62,
				guiTop + 32 + yOff,
				176,
				0 + yOff,
				14,
				14 - yOff
				);
		
		if(tileEntity.burnTime > 0 && tileEntity.fluidIn.getFluidAmount() > 0 && tileEntity.fluidOut.getFluidAmount() < tileEntity.fluidOut.getCapacity())
			drawTexturedModalRect(guiLeft + 87, guiTop + 48, 176, 14, 22, 17);
		
		SWGuiUtil.DrawFluid(this, tileEntity.fluidIn, mouseX, mouseY, 42, 23, 16, 41);
		SWGuiUtil.DrawFluid(this, tileEntity.fluidOut, mouseX, mouseY, 122, 23, 16, 41);
		
		tileEntity = (TileEntitySteamGenerator) tileEntity.getWorld().getTileEntity(tileEntity.getPos());
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) 
	{
		String fgString = I18n.format("container.steamworld.steam_generator");
		
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
		SWGuiUtil.DrawFluidHoverText(this, tileEntity.fluidIn, mouseX, mouseY, 42, 23, 16, 41);
		SWGuiUtil.DrawFluidHoverText(this, tileEntity.fluidOut, mouseX, mouseY, 122, 23, 16, 41);
	}

	@Override
	public boolean isInRegion(int x, int y, int w, int h, int mX, int mY) 
	{
		return isPointInRegion(x, y, w, h, mX, mY);
	}
}
