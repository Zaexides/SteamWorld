package zaexides.steamworld;

import org.omg.PortableServer.ServantActivator;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidRegistry.FluidRegisterEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import zaexides.steamworld.blocks.BlockAncite;
import zaexides.steamworld.blocks.BlockSteam;
import zaexides.steamworld.fluids.FluidSteam;
import zaexides.steamworld.items.SWItemVariant;
import zaexides.steamworld.recipe.handling.AssemblyRecipeHandler;
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
import zaexides.steamworld.te.TileEntitySWFurnace;
import zaexides.steamworld.te.TileEntitySteamGenerator;
import zaexides.steamworld.te.TileEntitySteamGeneratorNether;
import zaexides.steamworld.te.TileEntityValve;
import zaexides.steamworld.te.energy.TileEntityDynamo;
import zaexides.steamworld.te.energy.TileEntitySteamGeneratorElectric;
import zaexides.steamworld.utility.IModeledObject;
import zaexides.steamworld.utility.IOreDictionaryRegisterable;
import zaexides.steamworld.worldgen.DwarvenOutpostGenerator;
import zaexides.steamworld.worldgen.DwarvenStructureGenerator;
import zaexides.steamworld.worldgen.WorldGenerationOres;

@EventBusSubscriber
public class RegistryHandler 
{
	@SubscribeEvent
	public static void OnBlockRegister(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().registerAll(BlockInitializer.BLOCKS.toArray(new Block[0]));
		for(Block block : BlockInitializer.BLOCKS)
		{
			if(block instanceof IOreDictionaryRegisterable)
				((IOreDictionaryRegisterable)block).RegisterOreInDictionary();
		}
	}
	
	@SubscribeEvent
	public static void OnItemRegister(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(ItemInitializer.ITEMS.toArray(new Item[0]));
		for(Item item : ItemInitializer.ITEMS)
		{
			if(item instanceof IOreDictionaryRegisterable)
				((IOreDictionaryRegisterable)item).RegisterOreInDictionary();
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
	
	public static void MiscRegister()
	{
		GameRegistry.registerWorldGenerator(new WorldGenerationOres(), 0);
		GameRegistry.registerWorldGenerator(new DwarvenStructureGenerator(), 5);
		GameRegistry.registerWorldGenerator(new DwarvenOutpostGenerator(), 1);
		
		RegisterSmeltingRecipes();
		AssemblyRecipeHandler.RegisterRecipes();
	}
	
	public static void RegisterSmeltingRecipes()
	{
		GameRegistry.addSmelting(BlockInitializer.ORE_STEAITE, new ItemStack(ItemInitializer.INGOT_STEAITE), 1.2f);
		
		GameRegistry.addSmelting(new ItemStack(ItemInitializer.METAL_DUST, 1, SWItemVariant.EnumVarietyMaterial.IRON.getMeta()), new ItemStack(Items.IRON_INGOT), FurnaceRecipes.instance().getSmeltingExperience(new ItemStack(Items.IRON_INGOT)));
		GameRegistry.addSmelting(new ItemStack(ItemInitializer.METAL_DUST, 1, SWItemVariant.EnumVarietyMaterial.GOLD.getMeta()), new ItemStack(Items.GOLD_INGOT), FurnaceRecipes.instance().getSmeltingExperience(new ItemStack(Items.GOLD_INGOT)));
		GameRegistry.addSmelting(new ItemStack(ItemInitializer.METAL_DUST, 1, SWItemVariant.EnumVarietyMaterial.STEAITE.getMeta()), new ItemStack(ItemInitializer.INGOT_STEAITE), FurnaceRecipes.instance().getSmeltingExperience(new ItemStack(ItemInitializer.INGOT_STEAITE)));
		GameRegistry.addSmelting(new ItemStack(ItemInitializer.METAL_DUST, 1, SWItemVariant.EnumVarietyMaterial.ANCITE.getMeta()), new ItemStack(ItemInitializer.INGOT_ANCITE), 1.5f);
	}
	
	public static void RegisterTileEntities()
	{
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
	
		GameRegistry.registerTileEntity(TileEntityDynamo.class, ModInfo.MODID + "_Dynamo");
		GameRegistry.registerTileEntity(TileEntitySteamGeneratorElectric.class, ModInfo.MODID + "_ElectricGenerator");
	}
	
	public static void RegisterFluids()
	{
		FluidSteam.fluidSteam = new FluidSteam();
	}
}
