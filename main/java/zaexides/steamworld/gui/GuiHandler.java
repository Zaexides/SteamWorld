package zaexides.steamworld.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.containers.ContainerAssembler;
import zaexides.steamworld.containers.ContainerFarmer;
import zaexides.steamworld.containers.ContainerFertilizer;
import zaexides.steamworld.containers.ContainerFisher;
import zaexides.steamworld.containers.ContainerFluidMiner;
import zaexides.steamworld.containers.ContainerMiner;
import zaexides.steamworld.containers.ContainerSimple;
import zaexides.steamworld.containers.ContainerSteamFurnace;
import zaexides.steamworld.containers.ContainerSteamGenerator;
import zaexides.steamworld.containers.ContainerSteamGrinder;
import zaexides.steamworld.gui.energy.GuiDynamo;
import zaexides.steamworld.gui.energy.GuiSteamGeneratorElectric;
import zaexides.steamworld.te.TileEntityAssembler;
import zaexides.steamworld.te.TileEntityExperienceMachine;
import zaexides.steamworld.te.TileEntityFarmer;
import zaexides.steamworld.te.TileEntityFertilizer;
import zaexides.steamworld.te.TileEntityFisher;
import zaexides.steamworld.te.TileEntityFluidMiner;
import zaexides.steamworld.te.TileEntityGrinder;
import zaexides.steamworld.te.TileEntityMachine;
import zaexides.steamworld.te.TileEntityMiner;
import zaexides.steamworld.te.TileEntityObilisk;
import zaexides.steamworld.te.TileEntitySWFurnace;
import zaexides.steamworld.te.TileEntitySteamGenerator;
import zaexides.steamworld.te.TileEntitySteamGeneratorNether;
import zaexides.steamworld.te.energy.TileEntityDynamo;
import zaexides.steamworld.te.energy.TileEntitySteamGeneratorElectric;
import zaexides.steamworld.te.generic_machine.TileEntityTeleporter;

public class GuiHandler implements IGuiHandler
{
	public static final int STEAM_GENERATOR = 0;
	public static final int STEAM_GENERATOR_NETHER = 1;
	public static final int STEAM_FURNACE = 2;
	public static final int STEAM_GRINDER = 3;
	public static final int FISHER = 4;
	public static final int FARMER_LUMBER = 5;
	public static final int FERTILIZER = 6;
	public static final int ASSEMBLER = 7;
	public static final int EXPERIENCE_MACHINE = 8;
	public static final int TELEPORTER = 9;
	public static final int MINER = 10;
	public static final int FLUID_MINER = 11;
	
	public static final int DYNAMO = 100;
	public static final int GENERATOR_ELECTRIC = 101;
	
	public static final int OBILISK = 200;
	
	public static final int MANUAL_STEAITE = 300;
	public static final int MANUAL_ANCITE = 301;
	public static final int MANUAL_ENDRITCH = 302;
	public static final int MANUAL_ESSEN = 303;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		switch(ID)
		{
		case STEAM_GENERATOR:
			return new ContainerSteamGenerator(player.inventory, (TileEntitySteamGenerator) world.getTileEntity(new BlockPos(x,y,z)));
		case STEAM_GENERATOR_NETHER:
			return new ContainerSimple(player, player.inventory, world.getTileEntity(new BlockPos(x,y,z)));
		case STEAM_FURNACE:
			return new ContainerSteamFurnace(player, player.inventory, (TileEntitySWFurnace) world.getTileEntity(new BlockPos(x,y,z)));
		case STEAM_GRINDER:
			return new ContainerSteamGrinder(player, player.inventory, (TileEntityGrinder) world.getTileEntity(new BlockPos(x,y,z)));
		case FISHER:
			return new ContainerFisher(player, player.inventory, (TileEntityFisher) world.getTileEntity(new BlockPos(x,y,z)));
		case FARMER_LUMBER:
			return new ContainerFarmer(player, player.inventory, (TileEntityMachine) world.getTileEntity(new BlockPos(x,y,z)));
		case FERTILIZER:
			return new ContainerFertilizer(player, player.inventory, (TileEntityFertilizer) world.getTileEntity(new BlockPos(x,y,z)));
		case ASSEMBLER:
			return new ContainerAssembler(player, player.inventory, (TileEntityAssembler) world.getTileEntity(new BlockPos(x,y,z)));
		case TELEPORTER:
			return new ContainerSimple(player, player.inventory, world.getTileEntity(new BlockPos(x,y,z)), 116);
		case MINER:
			return new ContainerMiner(player, player.inventory, (TileEntityMiner) world.getTileEntity(new BlockPos(x, y, z)));
		case FLUID_MINER:
			return new ContainerFluidMiner(player, player.inventory, (TileEntityFluidMiner) world.getTileEntity(new BlockPos(x, y, z)));
		case EXPERIENCE_MACHINE:
		case DYNAMO:
		case GENERATOR_ELECTRIC:
			return new ContainerSimple(player, player.inventory, world.getTileEntity(new BlockPos(x,y,z)));
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		switch(ID)
		{
		case STEAM_GENERATOR:
			return new GuiSteamGenerator(player.inventory, (TileEntitySteamGenerator) world.getTileEntity(new BlockPos(x,y,z)));
		case STEAM_GENERATOR_NETHER:
			return new GuiSteamGeneratorNether(player, player.inventory, (TileEntitySteamGeneratorNether) world.getTileEntity(new BlockPos(x,y,z)));
		case STEAM_FURNACE:
			return new GuiSteamFurnace(player, player.inventory, (TileEntitySWFurnace) world.getTileEntity(new BlockPos(x,y,z)));
		case STEAM_GRINDER:
			return new GuiSteamGrinder(player, player.inventory, (TileEntityGrinder) world.getTileEntity(new BlockPos(x,y,z)));
		case FISHER:
			return new GuiFisher(player, player.inventory, (TileEntityFisher) world.getTileEntity(new BlockPos(x,y,z)));
		case FARMER_LUMBER:
			return new GuiFarmer(player, player.inventory, (TileEntityMachine) world.getTileEntity(new BlockPos(x,y,z)));
		case FERTILIZER:
			return new GuiFertilizer(player, player.inventory, (TileEntityFertilizer) world.getTileEntity(new BlockPos(x,y,z)));
		case ASSEMBLER:
			return new GuiAssembler(player, player.inventory, (TileEntityAssembler) world.getTileEntity(new BlockPos(x,y,z)));
		case EXPERIENCE_MACHINE:
			return new GuiExperienceMachine(player, player.inventory, (TileEntityExperienceMachine) world.getTileEntity(new BlockPos(x,y,z)));
		case TELEPORTER:
			return new GuiTeleporter(player, player.inventory, (TileEntityTeleporter) world.getTileEntity(new BlockPos(x,y,z)));
		case MINER:
			return new GuiMiner(player, player.inventory, (TileEntityMiner) world.getTileEntity(new BlockPos(x, y, z)));
		case FLUID_MINER:
			return new GuiFluidMiner(player, player.inventory, (TileEntityFluidMiner) world.getTileEntity(new BlockPos(x, y, z)));
		case DYNAMO:
			return new GuiDynamo(player, player.inventory, (TileEntityDynamo) world.getTileEntity(new BlockPos(x,y,z)));
		case GENERATOR_ELECTRIC:
			return new GuiSteamGeneratorElectric(player, player.inventory, (TileEntitySteamGeneratorElectric) world.getTileEntity(new BlockPos(x,y,z)));
		case OBILISK:
			return new GuiObilisk(new BlockPos(x,y,z), world);
		case MANUAL_STEAITE:
			return new GuiManual(8, new ResourceLocation(ModInfo.MODID, "textures/gui/manual_steaite.png"));
		case MANUAL_ANCITE:
			return new GuiManual(11, new ResourceLocation(ModInfo.MODID, "textures/gui/manual_ancite.png"));
		case MANUAL_ENDRITCH:
			return new GuiManual(16, new ResourceLocation(ModInfo.MODID, "textures/gui/manual_endritch.png"));
		case MANUAL_ESSEN:
			return new GuiManual(20, new ResourceLocation(ModInfo.MODID, "textures/gui/manual_essen.png"));
		}
		
		return null;
	}

}
