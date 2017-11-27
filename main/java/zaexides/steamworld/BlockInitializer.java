package zaexides.steamworld;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import scala.AnyValCompanion;
import scala.reflect.internal.Trees.New;
import zaexides.steamworld.blocks.BlockAncite;
import zaexides.steamworld.blocks.BlockDecorative;
import zaexides.steamworld.blocks.BlockFluidPipe;
import zaexides.steamworld.blocks.BlockSteam;
import zaexides.steamworld.blocks.SteamWorldBlock;
import zaexides.steamworld.blocks.SteamWorldBlockOre;
import zaexides.steamworld.blocks.machines.BlockAssembler;
import zaexides.steamworld.blocks.machines.BlockDrain;
import zaexides.steamworld.blocks.machines.BlockExperienceMachine;
import zaexides.steamworld.blocks.machines.BlockFarmer;
import zaexides.steamworld.blocks.machines.BlockFaucet;
import zaexides.steamworld.blocks.machines.BlockFertilizer;
import zaexides.steamworld.blocks.machines.BlockFisher;
import zaexides.steamworld.blocks.machines.BlockGrinder;
import zaexides.steamworld.blocks.machines.BlockLumber;
import zaexides.steamworld.blocks.machines.BlockNetherAccelerator;
import zaexides.steamworld.blocks.machines.BlockSWFurnace;
import zaexides.steamworld.blocks.machines.BlockSteamGenerator;
import zaexides.steamworld.blocks.machines.BlockSteamGeneratorNether;
import zaexides.steamworld.blocks.machines.BlockValve;
import zaexides.steamworld.blocks.machines.energy.BlockDynamo;
import zaexides.steamworld.blocks.machines.energy.BlockSteamGeneratorElectric;
import zaexides.steamworld.utility.SWMaterials;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockInitializer 
{
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	public static final SteamWorldBlockOre ORE_STEAITE = new SteamWorldBlockOre("ore_steaite", "oreSteaite", Material.ROCK, 2.5f, 3);
	public static final SteamWorldBlock BLOCK_STEAITE = new SteamWorldBlockOre("block_steaite", "blockSteaite", Material.IRON, 3f, 2);
	public static final SteamWorldBlock BLOCK_STEAITE_GOLD = new SteamWorldBlock("block_steaite_gold", Material.IRON, 3.2f);
	public static final Block LAMP_ENDRITCH = new SteamWorldBlock("lamp_endritch", SWMaterials.ENDRITCH, 3.5f).setLightLevel(1.0f);
	
	public static final BlockAncite BLOCK_ANCITE = new BlockAncite("block_ancite");
	public static final BlockDecorative BLOCK_DECORATIVE = new BlockDecorative("block_decorative");
	
	public static final BlockNetherAccelerator BLOCK_NETHER_ACCELERATOR = new BlockNetherAccelerator("nether_accelerator", SWMaterials.ENDRITCH, 5.0f);
	
	public static final BlockSteamGenerator GENERATOR_STEAITE = new BlockSteamGenerator("generator_steaite", Material.IRON, 3.5f, 4, 4);
	public static final BlockSteamGenerator GENERATOR_ANCITE = new BlockSteamGenerator("generator_ancite", Material.IRON, 4.5f, 8, 16);
	public static final BlockSteamGenerator GENERATOR_ENDRITCH = new BlockSteamGenerator("generator_endritch", SWMaterials.ENDRITCH, 5.5f, 16, 32);
	public static final BlockSteamGeneratorNether GENERATOR_NETHER = new BlockSteamGeneratorNether("generator_nether", Material.ROCK, 4.0f);
	
	public static final BlockExperienceMachine EXPERIENCE_MACHINE_ANCITE = new BlockExperienceMachine("experience_machine_ancite", 4.5f, 1);
	public static final BlockExperienceMachine EXPERIENCE_MACHINE_ENDRITCH = new BlockExperienceMachine("experience_machine_endritch", 5.5f, 2);
	
	public static final BlockFertilizer FERTILIZER_STEAITE = new BlockFertilizer("fertilizer_steaite", 3.5f, 1);
	public static final BlockFertilizer FERTILIZER_ANCITE = new BlockFertilizer("fertilizer_ancite", 4.5f, 2);
	public static final BlockFertilizer FERTILIZER_ENDRITCH = new BlockFertilizer("fertilizer_endritch", 5.5f, 3);
	
	public static final BlockSWFurnace FURNACE_STEAITE = new BlockSWFurnace("furnace_steaite", 3.5f, 1);
	public static final BlockSWFurnace FURNACE_ANCITE = new BlockSWFurnace("furnace_ancite", 4.5f, 2);
	public static final BlockSWFurnace FURNACE_ENDRITCH = new BlockSWFurnace("furnace_endritch", 5.5f, 3);
	
	public static final BlockAssembler ASSEMBLER_ANCITE = new BlockAssembler("assembler_ancite", 4.5f, 1);
	public static final BlockAssembler ASSEMBLER_ENDRITCH = new BlockAssembler("assembler_endritch", 5.5f, 2);
	
	public static final BlockGrinder GRINDER_STEAITE = new BlockGrinder("grinder_steaite", 3.5f, 1);
	public static final BlockGrinder GRINDER_ANCITE = new BlockGrinder("grinder_ancite", 4.5f, 2);
	public static final BlockGrinder GRINDER_ENDRITCH = new BlockGrinder("grinder_endritch", 5.5f, 3);
	
	public static final BlockFisher FISHER_STEAITE = new BlockFisher("fisher_steaite", 3.5f, 1);
	public static final BlockFisher FISHER_ANCITE = new BlockFisher("fisher_ancite", 4.5f, 2);
	public static final BlockFisher FISHER_ENDRITCH = new BlockFisher("fisher_endritch", 5.5f, 3);
	
	public static final BlockFarmer FARMER_STEAITE = new BlockFarmer("farmer_steaite", 3.5f, 1);
	public static final BlockFarmer FARMER_ANCITE = new BlockFarmer("farmer_ancite", 4.5f, 2);
	public static final BlockFarmer FARMER_ENDRITCH = new BlockFarmer("farmer_endritch", 5.5f, 3);
	
	public static final BlockLumber LUMBER_STEAITE = new BlockLumber("lumber_steaite", 3.5f, 1);
	public static final BlockLumber LUMBER_ANCITE = new BlockLumber("lumber_ancite", 4.5f, 2);
	public static final BlockLumber LUMBER_ENDRITCH = new BlockLumber("lumber_endritch", 5.5f, 3);
	
	public static final BlockSteam BLOCK_STEAM = new BlockSteam();
		
	public static final BlockFluidPipe BLOCK_FLUID_PIPE = new BlockFluidPipe();
	public static final BlockValve BLOCK_VALVE = new BlockValve("block_fluid_controller", Material.IRON, 2.5f, (int)(Fluid.BUCKET_VOLUME * 0.8), 800);
	public static final BlockValve BLOCK_VALVE_ENDRITCH = new BlockValve("block_fluid_controller_endritch", SWMaterials.ENDRITCH, 4.5f, (int)(Fluid.BUCKET_VOLUME * 8.5), 8000);
	public static final BlockDrain BLOCK_DRAIN = new BlockDrain();
	public static final BlockFaucet BLOCK_FAUCET = new BlockFaucet();
		
	public static final BlockDynamo BLOCK_DYNAMO = new BlockDynamo("block_dynamo", 3.5f);
	public static final BlockSteamGeneratorElectric BLOCK_STEAM_GENERATOR_ELECTRIC = new BlockSteamGeneratorElectric("generator_electric", 3.5f);
}
