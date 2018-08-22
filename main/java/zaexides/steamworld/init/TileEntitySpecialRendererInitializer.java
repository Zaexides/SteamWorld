package zaexides.steamworld.init;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.client.rendering.tile.AltarModel;
import zaexides.steamworld.client.rendering.tile.FilteredTankModel;
import zaexides.steamworld.client.rendering.tile.TeleporterModel;
import zaexides.steamworld.te.TileEntityAltar;
import zaexides.steamworld.te.TileEntityFilterTank;
import zaexides.steamworld.te.generic_machine.TileEntityTeleporter;

@EventBusSubscriber(modid = ModInfo.MODID)
public class TileEntitySpecialRendererInitializer 
{
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void OnModelRegister(ModelRegistryEvent event)
	{
		register(TileEntityAltar.class, new AltarModel());
		register(TileEntityFilterTank.class, new FilteredTankModel());
		register(TileEntityTeleporter.class, new TeleporterModel());
	}
	
	private static <T extends TileEntity> void register(Class<T> tileEntityClass, TileEntitySpecialRenderer<? super T> specialRenderer)
	{
		ClientRegistry.bindTileEntitySpecialRenderer(tileEntityClass, specialRenderer);
	}
}
