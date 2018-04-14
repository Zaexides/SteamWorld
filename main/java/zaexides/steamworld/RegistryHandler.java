package zaexides.steamworld;

import org.apache.logging.log4j.Level;
import org.omg.PortableServer.ServantActivator;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidRegistry.FluidRegisterEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import zaexides.steamworld.blocks.BlockAncite;
import zaexides.steamworld.blocks.BlockDecorative;
import zaexides.steamworld.blocks.BlockSteam;
import zaexides.steamworld.blocks.SteamWorldBlockOre;
import zaexides.steamworld.fluids.FluidPreservation;
import zaexides.steamworld.fluids.FluidSteam;
import zaexides.steamworld.fluids.FluidWithering;
import zaexides.steamworld.init.BlockInitializer;
import zaexides.steamworld.init.ItemInitializer;
import zaexides.steamworld.integration.tc.TCMaterials;
import zaexides.steamworld.integration.tc.TinkersMelting;
import zaexides.steamworld.items.ItemDust;
import zaexides.steamworld.items.SWItemIngot;
import zaexides.steamworld.items.SWItemNugget;
import zaexides.steamworld.recipe.handling.AssemblyRecipeHandler;
import zaexides.steamworld.recipe.handling.DustRecipeHandler;
import zaexides.steamworld.te.TileEntityAssembler;
import zaexides.steamworld.te.TileEntityDrain;
import zaexides.steamworld.te.TileEntityExperienceMachine;
import zaexides.steamworld.te.TileEntityFarmer;
import zaexides.steamworld.te.TileEntityFaucet;
import zaexides.steamworld.te.TileEntityFertilizer;
import zaexides.steamworld.te.TileEntityFisher;
import zaexides.steamworld.te.TileEntityGrinder;
import zaexides.steamworld.te.TileEntityLumber;
import zaexides.steamworld.te.TileEntityNetherAccelerator;
import zaexides.steamworld.te.TileEntityObilisk;
import zaexides.steamworld.te.TileEntityPipe;
import zaexides.steamworld.te.TileEntitySWFurnace;
import zaexides.steamworld.te.TileEntitySteamGenerator;
import zaexides.steamworld.te.TileEntitySteamGeneratorNether;
import zaexides.steamworld.te.TileEntityValve;
import zaexides.steamworld.te.energy.TileEntityDynamo;
import zaexides.steamworld.te.energy.TileEntitySteamGeneratorElectric;
import zaexides.steamworld.te.generic_machine.TileEntityLauncher;
import zaexides.steamworld.te.generic_machine.TileEntityTeleporter;
import zaexides.steamworld.utility.interfaces.IModeledObject;
import zaexides.steamworld.utility.interfaces.IOreDictionaryRegisterable;
import zaexides.steamworld.worldgen.WorldGenerationOres;
import zaexides.steamworld.worldgen.dimension.DimensionTypeSteamWorld;
import zaexides.steamworld.worldgen.structure.WorldGenCrypt;
import zaexides.steamworld.worldgen.structure.WorldGenPortalBuilding;
import zaexides.steamworld.worldgen.structure.WorldGenWitherLab;
import zaexides.steamworld.worldgen.structure.tower.WorldGenTower;

@EventBusSubscriber
public class RegistryHandler 
{
	@SubscribeEvent
	public static void OnBlockRegister(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().registerAll(BlockInitializer.BLOCKS.toArray(new Block[0]));
	}
	
	@SubscribeEvent
	public static void OnItemRegister(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(ItemInitializer.ITEMS.toArray(new Item[0]));
		
		RegisterOreDictionaryEntries();
	}
	
	public static void RegisterOreDictionaryEntries()
	{
		for(Item item : ItemInitializer.ITEMS)
		{
			if(item instanceof IOreDictionaryRegisterable)
				((IOreDictionaryRegisterable)item).RegisterOreInDictionary();
		}
		for(Block block : BlockInitializer.BLOCKS)
		{
			if(block instanceof IOreDictionaryRegisterable)
				((IOreDictionaryRegisterable)block).RegisterOreInDictionary();
		}
	}
	
	@SubscribeEvent
	public static void OnModelRegister(ModelRegistryEvent event)
	{
		for(Block block : BlockInitializer.BLOCKS)
		{
			if(block instanceof IModeledObject)
				((IModeledObject)block).RegisterModels();
		}
		
		for(Item item : ItemInitializer.ITEMS)
		{
			if(item instanceof IModeledObject)
				((IModeledObject)item).RegisterModels();
		}
	}
	
	public static void RegisterWorldGen()
	{
		GameRegistry.registerWorldGenerator(new WorldGenerationOres(), 0);
		GameRegistry.registerWorldGenerator(new WorldGenCrypt(), 1000);
		GameRegistry.registerWorldGenerator(new WorldGenPortalBuilding(), 0);
		GameRegistry.registerWorldGenerator(new WorldGenWitherLab(), 1000);
		GameRegistry.registerWorldGenerator(new WorldGenTower(), 1000);
		
		DimensionManager.registerDimension(ConfigHandler.dimensionId, DimensionTypeSteamWorld.STEAMWORLD);
	}
	
	public static void RegisterMiscRecipes()
	{
		RegisterSmeltingRecipes();
		AssemblyRecipeHandler.RegisterRecipes();
		RegisterGrinderRecipes();
	}
	
	public static void RegisterSmeltingRecipes()
	{
		GameRegistry.addSmelting(new ItemStack(BlockInitializer.ORE, 1, SteamWorldBlockOre.EnumType.OVERWORLD_STEAITE.getMeta()),  new ItemStack(ItemInitializer.INGOT, 1, SWItemIngot.EnumVarietyMaterial.STEAITE.getMeta()), 1.2f);
		
		GameRegistry.addSmelting(new ItemStack(ItemInitializer.METAL_DUST, 1, ItemDust.EnumVarietyMaterial.IRON.getMeta()), new ItemStack(Items.IRON_INGOT), FurnaceRecipes.instance().getSmeltingExperience(new ItemStack(Items.IRON_INGOT)));
		GameRegistry.addSmelting(new ItemStack(ItemInitializer.METAL_DUST, 1, ItemDust.EnumVarietyMaterial.GOLD.getMeta()), new ItemStack(Items.GOLD_INGOT), FurnaceRecipes.instance().getSmeltingExperience(new ItemStack(Items.GOLD_INGOT)));
		GameRegistry.addSmelting(new ItemStack(ItemInitializer.METAL_DUST, 1, ItemDust.EnumVarietyMaterial.STEAITE.getMeta()), new ItemStack(ItemInitializer.INGOT, 1, SWItemIngot.EnumVarietyMaterial.STEAITE.getMeta()), 1.2f);
		GameRegistry.addSmelting(new ItemStack(ItemInitializer.METAL_DUST, 1, ItemDust.EnumVarietyMaterial.ANCITE.getMeta()), new ItemStack(ItemInitializer.INGOT, 1, SWItemIngot.EnumVarietyMaterial.ANCITE.getMeta()), 1.5f);
		GameRegistry.addSmelting(new ItemStack(BlockInitializer.BLOCK_DECORATIVE, 1, BlockDecorative.EnumType.SKY_COBBLE.getMeta()), new ItemStack(BlockInitializer.BLOCK_DECORATIVE, 1, BlockDecorative.EnumType.SKY_STONE.getMeta()), FurnaceRecipes.instance().getSmeltingExperience(new ItemStack(Blocks.COBBLESTONE)));
		GameRegistry.addSmelting(new ItemStack(ItemInitializer.METAL_DUST, 1, ItemDust.EnumVarietyMaterial.GALITE.getMeta()), new ItemStack(ItemInitializer.INGOT, 1, SWItemIngot.EnumVarietyMaterial.GALITE.getMeta()), 2.0f);
		GameRegistry.addSmelting(new ItemStack(ItemInitializer.METAL_DUST, 2, ItemDust.EnumVarietyMaterial.ESSEN.getMeta()), new ItemStack(ItemInitializer.INGOT, 1, SWItemIngot.EnumVarietyMaterial.ESSEN.getMeta()), 0.1f);
	
		GameRegistry.addSmelting(new ItemStack(BlockInitializer.ORE, 1, SteamWorldBlockOre.EnumType.SKY_COAL.getMeta()), new ItemStack(Items.COAL), FurnaceRecipes.instance().getSmeltingExperience(new ItemStack(Items.COAL)));
		GameRegistry.addSmelting(new ItemStack(BlockInitializer.ORE, 1, SteamWorldBlockOre.EnumType.SKY_IRON.getMeta()), new ItemStack(Items.IRON_INGOT), FurnaceRecipes.instance().getSmeltingExperience(new ItemStack(Items.IRON_INGOT)));
		GameRegistry.addSmelting(new ItemStack(BlockInitializer.ORE, 1, SteamWorldBlockOre.EnumType.SKY_GOLD.getMeta()), new ItemStack(Items.GOLD_INGOT), FurnaceRecipes.instance().getSmeltingExperience(new ItemStack(Items.GOLD_INGOT)));
		GameRegistry.addSmelting(new ItemStack(BlockInitializer.ORE, 1, SteamWorldBlockOre.EnumType.SKY_DIAMOND.getMeta()), new ItemStack(Items.DIAMOND), FurnaceRecipes.instance().getSmeltingExperience(new ItemStack(Items.DIAMOND)));
		GameRegistry.addSmelting(new ItemStack(BlockInitializer.ORE, 1, SteamWorldBlockOre.EnumType.SKY_STEAITE.getMeta()), new ItemStack(ItemInitializer.INGOT, 1, SWItemIngot.EnumVarietyMaterial.STEAITE.getMeta()), 1.2f);
		GameRegistry.addSmelting(new ItemStack(BlockInitializer.ORE, 1, SteamWorldBlockOre.EnumType.SKY_ANCITE.getMeta()), new ItemStack(ItemInitializer.INGOT, 1, SWItemIngot.EnumVarietyMaterial.ANCITE.getMeta()), 1.5f);
		GameRegistry.addSmelting(new ItemStack(BlockInitializer.ORE, 1, SteamWorldBlockOre.EnumType.SKY_GALITE.getMeta()), new ItemStack(ItemInitializer.INGOT, 1, SWItemIngot.EnumVarietyMaterial.GALITE.getMeta()), 2.0f);
	}
	
	public static void RegisterGrinderRecipes()
	{
		DustRecipeHandler.RegisterRecipe("oreRedstone", new ItemStack(Items.REDSTONE, 3), true);
		DustRecipeHandler.RegisterRecipe("oreDiamond", new ItemStack(ItemInitializer.ITEM_NUGGET, 10, SWItemNugget.EnumVarietyMaterial.DIAMOND.getMeta()), true);
		DustRecipeHandler.RegisterRecipe("oreEmerald", new ItemStack(ItemInitializer.ITEM_NUGGET, 10, SWItemNugget.EnumVarietyMaterial.EMERALD.getMeta()), true);
		DustRecipeHandler.RegisterRecipe("oreCoal", new ItemStack(Items.COAL, 3), true);
		DustRecipeHandler.RegisterRecipe("oreLapis", new ItemStack(Items.DYE, 3, 4), true);
		DustRecipeHandler.RegisterRecipe("oreQuartz", new ItemStack(Items.QUARTZ, 3), true);
		DustRecipeHandler.RegisterRecipe("blockPrismarine", new ItemStack(Items.PRISMARINE_CRYSTALS, 4), false);
		DustRecipeHandler.RegisterRecipe("gemTerrite", new ItemStack(ItemInitializer.METAL_DUST, 1, ItemDust.EnumVarietyMaterial.TERRITE.getMeta()), false);
	}
	
	public static void RegisterTileEntities()
	{
		GameRegistry.registerTileEntity(TileEntityPipe.class, ModInfo.MODID + "_Pipe");
		GameRegistry.registerTileEntity(TileEntitySteamGenerator.class, ModInfo.MODID + "_SteamGenerator");
		GameRegistry.registerTileEntity(TileEntityValve.class, ModInfo.MODID + "_Valve");
		GameRegistry.registerTileEntity(TileEntityDrain.class, ModInfo.MODID + "_Drain");
		GameRegistry.registerTileEntity(TileEntityFaucet.class, ModInfo.MODID + "_Faucet");
		GameRegistry.registerTileEntity(TileEntitySteamGeneratorNether.class, ModInfo.MODID + "_SteamGeneratorNether");
		GameRegistry.registerTileEntity(TileEntitySWFurnace.class, ModInfo.MODID + "_Furnace");
		GameRegistry.registerTileEntity(TileEntityGrinder.class, ModInfo.MODID + "_Grinder");
		GameRegistry.registerTileEntity(TileEntityFisher.class, ModInfo.MODID + "_Fisher");
		GameRegistry.registerTileEntity(TileEntityFarmer.class, ModInfo.MODID + "_Farmer");
		GameRegistry.registerTileEntity(TileEntityLumber.class, ModInfo.MODID + "_Lumber");
		GameRegistry.registerTileEntity(TileEntityFertilizer.class, ModInfo.MODID + "_Fertilizer");
		GameRegistry.registerTileEntity(TileEntityAssembler.class, ModInfo.MODID + "_Assembler");
		GameRegistry.registerTileEntity(TileEntityNetherAccelerator.class, ModInfo.MODID + "_NetherAccelerator");
		GameRegistry.registerTileEntity(TileEntityExperienceMachine.class, ModInfo.MODID + "_ExperienceMachine");
		GameRegistry.registerTileEntity(TileEntityTeleporter.class, ModInfo.MODID + "_Teleporter");
		GameRegistry.registerTileEntity(TileEntityLauncher.class, ModInfo.MODID + "_LaunchTrap");
		GameRegistry.registerTileEntity(TileEntityObilisk.class, ModInfo.MODID + "_Obilisk");
	
		GameRegistry.registerTileEntity(TileEntityDynamo.class, ModInfo.MODID + "_Dynamo");
		GameRegistry.registerTileEntity(TileEntitySteamGeneratorElectric.class, ModInfo.MODID + "_ElectricGenerator");
	}
	
	public static void RegisterFluids()
	{
		FluidSteam.fluidSteam = new FluidSteam();
		FluidPreservation.fluidPreservation = new FluidPreservation();
		FluidWithering.fluidWithering = new FluidWithering();
	}
	
	@SubscribeEvent
	public static void RecipeRegisterEvent(RegistryEvent.Register<IRecipe> event)
	{
		if(Loader.isModLoaded("tconstruct"))
    	{
	    	try
	    	{
	    		TCMaterials.RegisterToolForgeRecipes(event);
	    	}
	    	catch(Exception exception)
	    	{
	    		SteamWorld.logger.log(Level.ERROR, "Ah, uhm, yeah, mod compatibility with Tinkers' kinda failed here at recipe register. Send me this together with your forge logs, will ya? " + exception.toString());
	    	}
    	}
	}
}
