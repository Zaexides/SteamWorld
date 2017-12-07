package zaexides.steamworld.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiTextField;
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
import zaexides.steamworld.containers.ContainerSimple;
import zaexides.steamworld.network.PacketHandler;
import zaexides.steamworld.network.messages.MessageTeleporterRegister;
import zaexides.steamworld.savedata.world.TeleporterData;
import zaexides.steamworld.savedata.world.TeleporterSaveData;
import zaexides.steamworld.te.TileEntityTeleporter;
import zaexides.steamworld.utility.IGuiContainerUtil;
import zaexides.steamworld.utility.SWGuiUtil;

public class GuiTeleporter extends GuiContainer implements IGuiContainerUtil
{
	private TileEntityTeleporter tileEntity;
	private IInventory playerInv;
	
	private GuiTextField guiTextField;
	private GuiTextField guiPassField;
	private final int GUI_TEXT_ID = 0;
	private final int GUI_PASS_ID = 1;
	
	private final int BUTTON_X = 146;
	private final int BUTTON_Y = 42;
	
	public GuiTeleporter(EntityPlayer player, IInventory playerInv, TileEntityTeleporter tileEntity) 
	{
		super(new ContainerSimple(player, playerInv, tileEntity, 116));
		
		xSize = 176;
		ySize = 198;
		
		this.tileEntity = tileEntity;
		this.playerInv = playerInv;
	}
	
	@Override
	public void initGui() 
	{
		super.initGui();
		
		guiTextField = new GuiTextField(GUI_TEXT_ID, fontRenderer, guiLeft + 54, guiTop + 68, 112, 14);
		guiTextField.setMaxStringLength(16);
		guiTextField.setText(tileEntity.getName());
		
		guiPassField = new GuiTextField(GUI_PASS_ID, fontRenderer, guiLeft + 54, guiTop + 86, 112, 14);
		guiPassField.setMaxStringLength(16);
		guiPassField.setText(tileEntity.getPass());
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		mc.getTextureManager().bindTexture(new ResourceLocation(ModInfo.MODID, "textures/gui/machine/gui_teleporter.png"));
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		if(tileEntity.ownId != -1)
			drawTexturedModalRect(guiLeft + BUTTON_X, guiTop + BUTTON_Y, 176, 0, 23, 23);
		
		SWGuiUtil.DrawFluid(this, tileEntity.steamTank, mouseX, mouseY, 8, 23, 16, 41);
		
		tileEntity = (TileEntityTeleporter) tileEntity.getWorld().getTileEntity(tileEntity.getPos());
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) 
	{
		String fgString = I18n.format("container.steamworld.teleporter");
		String tpName = I18n.format("container.steamworld.teleporter.name");
		String tpPass = I18n.format("container.steamworld.teleporter.pass");
		
		int x = xSize / 2 - mc.fontRenderer.getStringWidth(fgString) / 2;
		mc.fontRenderer.drawString(fgString, x, 6, 4210752);
		mc.fontRenderer.drawString(tpName + ":", 8, 70, 4210752);
		mc.fontRenderer.drawString(tpPass + ":", 8, 88, 4210752);
		mc.fontRenderer.drawString(playerInv.getDisplayName().getFormattedText(), 8, 104, 4210752);
		
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		guiTextField.drawTextBox();
		guiPassField.drawTextBox();
		renderHoveredToolTip(mouseX, mouseY);
		SWGuiUtil.DrawFluidHoverText(this, tileEntity.steamTank, mouseX, mouseY, 8, 23, 16, 41);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException 
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		guiTextField.mouseClicked(mouseX, mouseY, mouseButton);
		guiPassField.mouseClicked(mouseX, mouseY, mouseButton);
		if(SWGuiUtil.HandleButton(tileEntity.getPos(), (byte)0, mouseX, mouseY, guiLeft + BUTTON_X, guiTop + BUTTON_Y, 23, 23, false))
			PacketHandler.wrapper.sendToServer(new MessageTeleporterRegister(tileEntity.getPos(), guiTextField.getText(), guiPassField.getText()));
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException 
	{
		guiTextField.textboxKeyTyped(typedChar, keyCode);
		guiPassField.textboxKeyTyped(typedChar, keyCode);
		
		if(keyCode == 1 || !guiTextField.isFocused())
			super.keyTyped(typedChar, keyCode);
	}

	@Override
	public boolean isInRegion(int x, int y, int w, int h, int mX, int mY) 
	{
		return isPointInRegion(x, y, w, h, mX, mY);
	}
}
