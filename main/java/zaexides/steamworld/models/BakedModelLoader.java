package zaexides.steamworld.models;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import zaexides.steamworld.ModInfo;

public class BakedModelLoader implements ICustomModelLoader
{
	public static final PipeModel PIPE_MODEL = new PipeModel();

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		
	}

	@Override
	public boolean accepts(ResourceLocation modelLocation) {
		return modelLocation.getResourceDomain().equals(ModInfo.MODID) && (
				"block_pipe".equals(modelLocation.getResourcePath())
				);
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception {
		return PIPE_MODEL;
	}

}
