package zaexides.steamworld.blocks;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraftforge.fluids.BlockFluidFinite;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.fluids.FluidSteam;
import zaexides.steamworld.init.BlockInitializer;
import zaexides.steamworld.utility.interfaces.IModeledObject;

public class BlockSteam extends BlockFluidFinite implements IModeledObject
{
	public BlockSteam() 
	{
		super(FluidSteam.fluidSteam, new MaterialLiquid(MapColor.WHITE_STAINED_HARDENED_CLAY));
		setRegistryName("steam");
		setUnlocalizedName(ModInfo.MODID + ".steam");
		setCreativeTab(SteamWorld.CREATIVETAB_BLOCKS);
		
		BlockInitializer.BLOCKS.add(this);
	}

	@Override
	public void RegisterModels() 
	{
		SteamWorld.proxy.RegisterCustomMeshFluid(this, "steam");
	}
}
