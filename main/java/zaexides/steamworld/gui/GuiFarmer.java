package zaexides.steamworld.gui;

import java.io.IOException;

import org.apache.logging.log4j.Level;

import net.minecraft.client.gui.GuiButton;
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
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.containers.ContainerFarmer;
import zaexides.steamworld.te.TileEntityLumber;
import zaexides.steamworld.te.TileEntityMachine;
import zaexides.steamworld.utility.SWGuiUtil;
import zaexides.steamworld.utility.interfaces.IGuiContainerUtil;

public class GuiFarmer extends GuiContainer implements IGuiContainerUtil
{
	private TileEntityMachine tileEntity;
	private IInventory playerInv;
	
	public GuiFarmer(EntityPlayer player, IInventory playerInv, TileEntityMachine tileEntity) 
	{
		super(new ContainerFarmer(player, playerInv, tileEntity));
		
		xSize = 176;
		ySize = 165;
		
		this.tileEntity = tileEntity;
		this.playerInv = playerInv;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		mc.getTextureManager().bindTexture(new ResourceLocation(ModInfo.MODID, "textures/gui/machine/gui_farmer.png"));
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		SWGuiUtil.DrawFluid(this, tileEntity.steamTank, mouseX, mouseY, 8, 23, 16, 41);
		
		tileEntity = (TileEntityMachine) tileEntity.getWorld().getTileEntity(tileEntity.getPos());
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) 
	{
		String unlocalizedName = "container.steamworld.farmer";
		if(tileEntity instanceof TileEntityLumber)
			unlocalizedName = "container.steamworld.lumber";
		String fgString = I18n.format(unlocalizedName);
		
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
