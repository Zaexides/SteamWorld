package zaexides.steamworld.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import scala.collection.generic.BitOperations.Int;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.containers.ContainerSteamGeneratorNether;
import zaexides.steamworld.te.TileEntitySteamGeneratorNether;
import zaexides.steamworld.utility.SWGuiUtil;
import zaexides.steamworld.utility.interfaces.IGuiContainerUtil;

public class GuiSteamGeneratorNether extends GuiContainer implements IGuiContainerUtil
{
	private TileEntitySteamGeneratorNether tileEntity;
	private IInventory playerInv;
	
	public GuiSteamGeneratorNether(IInventory playerInv, TileEntitySteamGeneratorNether tileEntity) 
	{
		super(new ContainerSteamGeneratorNether(playerInv, tileEntity));
		
		xSize = 176;
		ySize = 165;
		
		this.tileEntity = tileEntity;
		this.playerInv = playerInv;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		//GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(new ResourceLocation(ModInfo.MODID, "textures/gui/machine/gui_steam_generator_nether.png"));
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		if(tileEntity.fluidIn.getFluidAmount() > 0 && tileEntity.fluidOut.getFluidAmount() < tileEntity.fluidOut.getCapacity())
		{
			drawTexturedModalRect(
					guiLeft + 62,
					guiTop + 50,
					176,
					0,
					14,
					14
					);
			drawTexturedModalRect(guiLeft + 87, guiTop + 48, 176, 14, 22, 17);
		}
		
		SWGuiUtil.DrawFluid(this, tileEntity.fluidIn, mouseX, mouseY, 42, 23, 16, 41);
		SWGuiUtil.DrawFluid(this, tileEntity.fluidOut, mouseX, mouseY, 122, 23, 16, 41);
		
		tileEntity = (TileEntitySteamGeneratorNether) tileEntity.getWorld().getTileEntity(tileEntity.getPos());
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
