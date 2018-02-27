package zaexides.steamworld.gui;

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
import zaexides.steamworld.containers.ContainerFisher;
import zaexides.steamworld.te.TileEntityFisher;
import zaexides.steamworld.utility.SWGuiUtil;
import zaexides.steamworld.utility.interfaces.IGuiContainerUtil;

public class GuiFisher extends GuiContainer implements IGuiContainerUtil
{
	private TileEntityFisher tileEntity;
	private IInventory playerInv;
	
	public GuiFisher(EntityPlayer player, IInventory playerInv, TileEntityFisher tileEntity) 
	{
		super(new ContainerFisher(player, playerInv, tileEntity));
		
		xSize = 176;
		ySize = 165;
		
		this.tileEntity = tileEntity;
		this.playerInv = playerInv;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		mc.getTextureManager().bindTexture(new ResourceLocation(ModInfo.MODID, "textures/gui/machine/gui_fisher.png"));
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		float progression = (float) tileEntity.progression / (float) tileEntity.MAX_PROGRESSION;
		float xOff = 24 * progression;
		
		drawTexturedModalRect(guiLeft + 47, guiTop + 34, 176, 14, (int)xOff, 17);
		
		SWGuiUtil.DrawFluid(this, tileEntity.steamTank, mouseX, mouseY, 8, 23, 16, 41);
		
		tileEntity = (TileEntityFisher) tileEntity.getWorld().getTileEntity(tileEntity.getPos());
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) 
	{
		String fgString = I18n.format("container.steamworld.fisher");
		
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
	}

	@Override
	public boolean isInRegion(int x, int y, int w, int h, int mX, int mY) 
	{
		return isPointInRegion(x, y, w, h, mX, mY);
	}
}
