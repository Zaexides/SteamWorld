package zaexides.steamworld.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.containers.ContainerSimple;
import zaexides.steamworld.network.PacketHandler;
import zaexides.steamworld.network.messages.MessageTeleporterConnect;
import zaexides.steamworld.network.messages.MessageTeleporterRegister;
import zaexides.steamworld.savedata.world.TeleporterData;
import zaexides.steamworld.savedata.world.TeleporterSaveData;
import zaexides.steamworld.te.generic_machine.TileEntityTeleporter;
import zaexides.steamworld.utility.SWGuiUtil;
import zaexides.steamworld.utility.interfaces.IGuiContainerUtil;

public class GuiTeleporter extends GuiContainer implements IGuiContainerUtil
{
	private TileEntityTeleporter tileEntity;
	private IInventory playerInv;
	
	private GuiTextField guiTextField;
	private GuiTextField guiNetField;
	private final int GUI_TEXT_ID = 0;
	private final int GUI_NET_ID = 1;
	
	private final int BUTTON_X = 146;
	private final int BUTTON_Y = 42;
	
	private final int BUTTON_PAGE_X = 131;
	private final int BUTTON_PREV_Y = 22;
	private final int BUTTON_NEXT_Y = 43;
	
	private final int BUTTON_SHOW_NOMATCH_X = 149;
	private final int BUTTON_SHOW_NOMATCH_Y = 23;
	
	private String originalName;
	private String originalPass;
	
	private int currentPage = 0;
	private int maxPages = 1;
	
	private int updateTimer = 0;
	private final int UPDATE_TIMER_MAX = 10;
	
	private final int LIST_Y_START = 24;
	private final int LIST_X = 31;
	private final int LIST_BUTTON_X = LIST_X + 89;
	
	private List<TeleporterData> teleporterData;
	
	private boolean showNonMatching = false;
	
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
		
		getTeleporterInfo();
		
		originalName = tileEntity.getName();
		originalPass = tileEntity.getNetID();
		
		guiTextField = new GuiTextField(GUI_TEXT_ID, fontRenderer, guiLeft + 54, guiTop + 68, 112, 14);
		guiTextField.setMaxStringLength(16);
		if(originalName != null)
			guiTextField.setText(originalName);
		
		guiNetField = new GuiTextField(GUI_NET_ID, fontRenderer, guiLeft + 54, guiTop + 86, 112, 14);
		guiNetField.setMaxStringLength(16);
		if(originalPass != null)
			guiNetField.setText(originalPass);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		mc.getTextureManager().bindTexture(new ResourceLocation(ModInfo.MODID, "textures/gui/machine/gui_teleporter.png"));
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		if(tileEntity.ownId != -1 && guiTextField.getText() == originalName && guiNetField.getText() == originalPass)
			drawTexturedModalRect(guiLeft + BUTTON_X, guiTop + BUTTON_Y, 176, 0, 23, 23);
		
		if(currentPage > 0)
			drawTexturedModalRect(guiLeft + BUTTON_PAGE_X, guiTop + BUTTON_PREV_Y, 176, 23, 12, 21);
		if(currentPage < maxPages)
			drawTexturedModalRect(guiLeft + BUTTON_PAGE_X, guiTop + BUTTON_NEXT_Y, 188, 23, 12, 21);
		
		drawConnectButtons();
		
		if(showNonMatching)
			drawTexturedModalRect(guiLeft + BUTTON_SHOW_NOMATCH_X, guiTop + BUTTON_SHOW_NOMATCH_Y, 199, 0, 17, 17);
		
		SWGuiUtil.DrawFluid(this, tileEntity.steamTank, mouseX, mouseY, 8, 23, 16, 41);
		
		tileEntity = (TileEntityTeleporter) tileEntity.getWorld().getTileEntity(tileEntity.getPos());
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) 
	{
		String fgString = I18n.format("container.steamworld.teleporter");
		String tpName = I18n.format("text.steamworld.teleporter.name");
		String tpPass = I18n.format("text.steamworld.teleporter.pass");
		
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
		
		DrawTextComponents();
		
		renderHoveredToolTip(mouseX, mouseY);
		SWGuiUtil.DrawFluidHoverText(this, tileEntity.steamTank, mouseX, mouseY, 8, 23, 16, 41);
		
		List<String> text = new ArrayList<String>();
		text.add(new TextComponentTranslation("text.steamworld.teleporter.upload").getFormattedText());
		SWGuiUtil.DrawHoverText(this, text, mouseX, mouseY, BUTTON_X, BUTTON_Y, 23, 23);
		
		List<String> text2 = new ArrayList<String>();
		text2.add(new TextComponentTranslation("text.steamworld.teleporter.showhide_nomatch").getFormattedText());
		SWGuiUtil.DrawHoverText(this, text2, mouseX, mouseY, BUTTON_SHOW_NOMATCH_X, BUTTON_SHOW_NOMATCH_Y, 17, 17);
	}
	
	private void DrawTextComponents()
	{
		GlStateManager.disableDepth();
		GlStateManager.disableLighting();
		GlStateManager.pushMatrix();
		
		guiTextField.drawTextBox();
		guiNetField.drawTextBox();
		
		drawTeleporterText();
		
		GlStateManager.popMatrix();
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
	}
	
	private void getTeleporterInfo()
	{
		TeleporterSaveData teleporterSaveData = TeleporterSaveData.get(tileEntity.getWorld());
		teleporterData = new ArrayList<TeleporterData>();
		
		int id = 0;
		TeleporterData emptyIncludedData;
		while((emptyIncludedData = teleporterSaveData.getTeleporterData(id)) != null)
		{
			if(!emptyIncludedData.free && emptyIncludedData.id != tileEntity.ownId)
			{
				if(showNonMatching || emptyIncludedData.netId.equals(tileEntity.getNetID()))
					teleporterData.add(emptyIncludedData);
			}
			id++;
		}
		
		maxPages = (int)Math.ceil(teleporterData.size() / 4.0) - 1;
		if(currentPage > maxPages)
			currentPage = maxPages;
		
		if(currentPage < 0)
			currentPage = 0;
	}
	
	@Override
	public void updateScreen() 
	{
		super.updateScreen();

		updateTimer++;
		if(updateTimer >= UPDATE_TIMER_MAX)
		{
			getTeleporterInfo();
			updateTimer = 0;
		}
	}
	
	private void drawConnectButtons()
	{
		for(int y = 0; y < 4; y++)
		{
			int id = y + (currentPage * 4);
			
			if(id >= this.teleporterData.size())
				break;
			
			TeleporterData teleporterData = this.teleporterData.get(id);
			
			int buttonTextOffset = 0;
			if(tileEntity.ownId == -1 || !teleporterData.netId.equals(tileEntity.getNetID()))
				buttonTextOffset = 20;
			else if(tileEntity.targetId == teleporterData.id)
				buttonTextOffset = 10;
			
			if(!teleporterData.netId.equals(tileEntity.getNetID()))
				drawTexturedModalRect(guiLeft + LIST_BUTTON_X - 10, guiTop + LIST_Y_START + 10 * y - 1, 176, 54, 10, 10); //Draw lock icon
			drawTexturedModalRect(guiLeft + LIST_BUTTON_X, guiTop + LIST_Y_START + 10 * y - 1, 176 + buttonTextOffset, 44, 10, 10);
		}
	}
	
	private void drawTeleporterText() 
	{
		for(int y = 0; y < 4; y++)
		{
			int id = y + (currentPage * 4);
			
			if(id >= this.teleporterData.size())
				break;
			
			TeleporterData teleporterData = this.teleporterData.get(id);
			
			int color = teleporterData.free ? 0x8A0808 : 0xFFFFFF;
			String text = teleporterData.id + " - ";
			
			if(teleporterData.free)
				text += "(Removed)";
			else
				text += teleporterData.name;
			
			drawString(fontRenderer, text, guiLeft + LIST_X, guiTop + LIST_Y_START + 10 * y, color);
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException 
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		guiTextField.mouseClicked(mouseX, mouseY, mouseButton);
		guiNetField.mouseClicked(mouseX, mouseY, mouseButton);
		if(SWGuiUtil.HandleButton(tileEntity.getPos(), (byte)0, mouseX, mouseY, guiLeft + BUTTON_X, guiTop + BUTTON_Y, 23, 23, false))
		{
			PacketHandler.wrapper.sendToServer(new MessageTeleporterRegister(tileEntity.getPos(), guiTextField.getText(), guiNetField.getText()));
			originalName = guiTextField.getText();
			originalPass = guiNetField.getText();
		}
		
		teleporterListButtons(mouseX, mouseY, mouseButton);
		
		if(currentPage > 0 && SWGuiUtil.HandleButton(null, (byte)1, mouseX, mouseY, guiLeft + BUTTON_PAGE_X, guiTop + BUTTON_PREV_Y, 12, 21, false))
			currentPage--;
		if(currentPage < maxPages && SWGuiUtil.HandleButton(null, (byte)2, mouseX, mouseY, guiLeft + BUTTON_PAGE_X, guiTop + BUTTON_NEXT_Y, 12, 21, false))
			currentPage++;
		
		if(SWGuiUtil.HandleButton(null, (byte) 99, mouseX, mouseY, guiLeft + BUTTON_SHOW_NOMATCH_X, guiTop + BUTTON_SHOW_NOMATCH_Y, 17, 17, false))
		{
			showNonMatching = !showNonMatching;
			getTeleporterInfo();
		}
	}
	
	private void teleporterListButtons(int mouseX, int mouseY, int mouseButton)
	{
		for(int y = 0; y < 4; y++)
		{
			int id = y + (currentPage * 4);
			
			if(id >= this.teleporterData.size())
				break;
			
			TeleporterData teleporterData = this.teleporterData.get(id);
			
			if(tileEntity.ownId != -1 && tileEntity.targetId != teleporterData.id && tileEntity.getNetID().equals(teleporterData.netId))
			{
				if(SWGuiUtil.HandleButton(null, (byte)(3 + y), mouseX, mouseY, guiLeft + LIST_BUTTON_X + 1, guiTop + LIST_Y_START + 10 * y + 1, 8, 8, false))
				{
					PacketHandler.wrapper.sendToServer(new MessageTeleporterConnect(tileEntity.getPos(), teleporterData.id));
				}
			}
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException 
	{
		guiTextField.textboxKeyTyped(typedChar, keyCode);
		guiNetField.textboxKeyTyped(typedChar, keyCode);
		
		if(keyCode == 1 || !(guiTextField.isFocused() || guiNetField.isFocused()))
			super.keyTyped(typedChar, keyCode);
	}

	@Override
	public boolean isInRegion(int x, int y, int w, int h, int mX, int mY) 
	{
		return isPointInRegion(x, y, w, h, mX, mY);
	}
}
