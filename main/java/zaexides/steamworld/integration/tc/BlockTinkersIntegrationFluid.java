package zaexides.steamworld.integration.tc;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.BlockInitializer;
import zaexides.steamworld.utility.interfaces.IModeledObject;

public class BlockTinkersIntegrationFluid extends BlockFluidClassic implements IModeledObject
{
	
	public BlockTinkersIntegrationFluid(Fluid fluid) 
	{
		super(fluid, Material.LAVA);
		
		BlockInitializer.BLOCKS.add(this);
	}

	@Override
	public void RegisterModels() 
	{
		SteamWorld.proxy.RegisterCustomMeshFluid(this, getRegistryName().getResourcePath().toLowerCase());
	}
}
