package zaexides.steamworld.proxy;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.BlockSteam;
import zaexides.steamworld.gui.GuiHandler;
import zaexides.steamworld.init.BlockInitializer;
import zaexides.steamworld.models.BakedModelLoader;
import zaexides.steamworld.utility.interfaces.IModeledObject;

public class ClientProxy extends CommonProxy
{
	@Override
	public void RegisterItemRenderer(Item item, int meta, String id)
	{
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
	}
	
	@Override
	public void RegisterItemRenderers(Item item, int metadata, String id, String filename) 
	{
		ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(new ResourceLocation(ModInfo.MODID, filename), id));
	}
	
	@Override
	public void RegisterCustomMeshFluid(Block block, String modelLocation)
	{
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(block), new ItemMeshDefinition() {
				
				@Override
				public ModelResourceLocation getModelLocation(ItemStack stack) 
				{
					return new ModelResourceLocation(ModInfo.MODID + ":" + modelLocation, "fluid");
				}
			});
		ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
				
				@Override
				protected ModelResourceLocation getModelResourceLocation(IBlockState state) 
				{
					return new ModelResourceLocation(ModInfo.MODID + ":" + modelLocation, "fluid");
				}
			});
	}
	
	@Override
	public void Init()
	{
		super.Init();
	}
	
	@Override
	public void PreInit(FMLPreInitializationEvent e) 
	{
		super.PreInit(e);
		ModelLoaderRegistry.registerLoader(new BakedModelLoader());
		OBJLoader.INSTANCE.addDomain(ModInfo.MODID);
	}
	
	@Override
	public void PostInit() 
	{
		super.PostInit();
	}
}
