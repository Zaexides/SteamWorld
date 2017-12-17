package zaexides.steamworld.blocks;

import java.lang.ref.Reference;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zaexides.steamworld.BlockInitializer;
import zaexides.steamworld.ItemInitializer;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.fluids.FluidSteam;
import zaexides.steamworld.utility.interfaces.IModeledObject;

public class BlockSteam extends BlockFluidFinite implements IModeledObject
{
	public BlockSteam() 
	{
		super(FluidSteam.fluidSteam, new MaterialLiquid(MapColor.WHITE_STAINED_HARDENED_CLAY));
		setRegistryName("steam");
		setUnlocalizedName(ModInfo.MODID + ".steam");
		setCreativeTab(SteamWorld.CREATIVETAB);
		
		BlockInitializer.BLOCKS.add(this);
	}

	@Override
	public void RegisterModels() 
	{
		SteamWorld.proxy.RegisterCustomMeshFluid(this, "steam");
	}
}
