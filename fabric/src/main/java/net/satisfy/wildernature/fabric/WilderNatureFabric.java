package net.satisfy.wildernature.fabric;

import com.google.common.base.Preconditions;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.*;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.satisfy.wildernature.WilderNature;
import net.satisfy.wildernature.util.contract.ContractReloader;
import net.satisfy.wildernature.fabric.config.ConfigFabric;
import net.satisfy.wildernature.fabric.world.PlacedFeatures;
import net.satisfy.wildernature.registry.EntityRegistry;
import net.satisfy.wildernature.registry.TagsRegistry;
import net.satisfy.wildernature.util.WilderNatureIdentifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class WilderNatureFabric implements ModInitializer {
    private static Predicate<BiomeSelectionContext> getWilderNatureSelector(String path) {
        return BiomeSelectors.tag(TagKey.create(Registries.BIOME, new WilderNatureIdentifier(path)));
    }

    private static Predicate<BiomeSelectionContext> getBloomingNatureSelector() {
        return BiomeSelectors.tag(TagKey.create(Registries.BIOME, new WilderNatureIdentifier("spawns_patch_hazelnut_bush")));
    }


    @Override
    public void onInitialize() {
        AutoConfig.register(ConfigFabric.class, GsonConfigSerializer::new);
        WilderNature.init();
        WilderNature.commonInit();
        addSpawns();
        addBiomeModification();
        addResourcerLoader();
    }

    void addBiomeModification() {
        ConfigFabric config = AutoConfig.getConfigHolder(ConfigFabric.class).getConfig();
        BiomeModification world = BiomeModifications.create(new WilderNatureIdentifier("world_features"));
        Predicate<BiomeSelectionContext> spawns_patch_hazelnut_bush = getBloomingNatureSelector();

        if (config.spawnHazelnutBush) {
            world.add(ModificationPhase.ADDITIONS, spawns_patch_hazelnut_bush, ctx -> ctx.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeatures.PATCH_HAZELNUT_BUSH));
        } else {
            world.add(ModificationPhase.REMOVALS, spawns_patch_hazelnut_bush, ctx -> ctx.getGenerationSettings().removeFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeatures.PATCH_HAZELNUT_BUSH));
        }
    }

    private void addResourcerLoader() {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new IdentifiableResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return new WilderNatureIdentifier("contractloader");
            }
            final ContractReloader dataReloader = new ContractReloader();
            @Override
            public @NotNull CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller2, Executor executor, Executor executor2) {
                return dataReloader.reload(preparationBarrier, resourceManager, profilerFiller, profilerFiller2, executor, executor2);
            }
            
            
        });
    }

    void addSpawns() {
        ConfigFabric config = AutoConfig.getConfigHolder(ConfigFabric.class).getConfig();
        addMobSpawn(BiomeTags.IS_BEACH, EntityRegistry.PELICAN.get(), config.PelicanSpawnWeight, config.PelicanMinGroupSize, config.PelicanMaxGroupSize);
        addMobSpawn(TagsRegistry.SPAWNS_DEER, EntityRegistry.DEER.get(), config.DeerSpawnWeight, config.DeerMinGroupSize, config.DeerMaxGroupSize);
        addMobSpawn(TagsRegistry.SPAWNS_RACCOON, EntityRegistry.RACCOON.get(), config.RaccoonSpawnWeight, config.RaccoonMinGroupSize, config.RaccoonMaxGroupSize);
        addMobSpawn(TagsRegistry.SPAWNS_SQUIRREL, EntityRegistry.SQUIRREL.get(), config.SquirrelSpawnWeight, config.SquirrelMinGroupSize, config.SquirrelMaxGroupSize);
        addMobSpawn(TagsRegistry.SPAWNS_RED_WOLF, EntityRegistry.RED_WOLF.get(), config.RedWolfSpawnWeight, config.RedWolfMinGroupSize, config.RedWolfMaxGroupSize);
        addMobSpawn(TagsRegistry.SPAWNS_OWL, EntityRegistry.OWL.get(), config.OwlSpawnWeight, config.OwlMinGroupSize, config.OwlMaxGroupSize);
        addMobSpawn(TagsRegistry.SPAWNS_BOAR, EntityRegistry.BOAR.get(), config.BoarSpawnWeight, config.BoarMinGroupSize, config.BoarMaxGroupSize);
        addMobSpawn(TagsRegistry.SPAWNS_BISON, EntityRegistry.BISON.get(), config.BisonSpawnWeight, config.BisonMinGroupSize, config.BisonMaxGroupSize);
        addMobSpawn(TagsRegistry.SPAWNS_TURKEY, EntityRegistry.TURKEY.get(), config.TurkeySpawnWeight, config.TurkeyMinGroupSize, config.TurkeyMaxGroupSize);
        addMobSpawn(TagsRegistry.SPAWNS_DOG, EntityRegistry.DOG.get(), config.DogSpawnWeight, config.DogMinGroupSize, config.DogMaxGroupSize);
        addMobSpawn(TagsRegistry.SPAWNS_MINISHEEP, EntityRegistry.MINISHEEP.get(), config.MiniSheepSpawnWeight, config.MiniSheepMinGroupSize, config.MiniSheepMaxGroupSize);
        addMobSpawn(TagsRegistry.SPAWNS_PENGUIN, EntityRegistry.PENGUIN.get(), config.PenguinSpawnWeight, config.PenguinMinGroupSize, config.PenguinMaxGroupSize);
        addMobSpawn(TagsRegistry.SPAWNS_CASSOWARY, EntityRegistry.CASSOWARY.get(), config.CassowarySpawnWeight, config.CassowaryMinGroupSize, config.CassowaryMaxGroupSize);

        if (config.removeSavannaAnimals) {
            removeSpawn(BiomeTags.IS_SAVANNA, List.of(EntityType.SHEEP, EntityType.PIG, EntityType.CHICKEN, EntityType.COW));
        }
        if (config.removeForestAnimals) {
            removeSpawn(BiomeTags.IS_FOREST, List.of(EntityType.PIG, EntityType.CHICKEN));
        }
        if (config.removeSwampAnimals) {
            removeSpawn(ConventionalBiomeTags.SWAMP, List.of(EntityType.SHEEP, EntityType.PIG, EntityType.CHICKEN, EntityType.COW));
        }
        if (config.removeJungleAnimals) {
            removeSpawn(BiomeTags.IS_JUNGLE, List.of(EntityType.PIG, EntityType.CHICKEN, EntityType.COW));
        }
        if (config.addJungleAnimals) {
            addMobSpawn(BiomeTags.IS_JUNGLE, EntityType.FROG, 8, 3, 4);
        }

        SpawnPlacements.register(EntityRegistry.SQUIRREL.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AmbientCreature::checkMobSpawnRules);
        SpawnPlacements.register(EntityRegistry.OWL.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING, AmbientCreature::checkMobSpawnRules);
        SpawnPlacements.register(EntityRegistry.TURKEY.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AmbientCreature::checkMobSpawnRules);
        SpawnPlacements.register(EntityRegistry.RACCOON.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AmbientCreature::checkMobSpawnRules);
        SpawnPlacements.register(EntityRegistry.PELICAN.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AmbientCreature::checkMobSpawnRules);
        SpawnPlacements.register(EntityRegistry.DEER.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AmbientCreature::checkMobSpawnRules);
        SpawnPlacements.register(EntityRegistry.RED_WOLF.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AmbientCreature::checkMobSpawnRules);
        SpawnPlacements.register(EntityRegistry.BOAR.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AmbientCreature::checkMobSpawnRules);
        SpawnPlacements.register(EntityRegistry.BISON.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AmbientCreature::checkMobSpawnRules);
        SpawnPlacements.register(EntityRegistry.DOG.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AmbientCreature::checkMobSpawnRules);
        SpawnPlacements.register(EntityRegistry.MINISHEEP.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AmbientCreature::checkMobSpawnRules);
        SpawnPlacements.register(EntityRegistry.PENGUIN.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AmbientCreature::checkMobSpawnRules);
        SpawnPlacements.register(EntityRegistry.CASSOWARY.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AmbientCreature::checkMobSpawnRules);
    }

    void addMobSpawn(TagKey<Biome> tag, EntityType<?> entityType, int weight, int minGroupSize, int maxGroupSize) {
        BiomeModifications.addSpawn(biomeSelector -> biomeSelector.hasTag(tag), MobCategory.CREATURE, entityType, weight, minGroupSize, maxGroupSize);
    }

    void removeSpawn(TagKey<Biome> tag, List<EntityType<?>> entityTypes) {
        entityTypes.forEach(entityType -> {
            ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
            Preconditions.checkState(BuiltInRegistries.ENTITY_TYPE.containsKey(id), "Unregistered entity tier: %s", entityType);
            BiomeModifications.create(id).add(ModificationPhase.REMOVALS, biomeSelector -> biomeSelector.hasTag(tag), context -> context.getSpawnSettings().removeSpawnsOfEntityType(entityType));
        });
    }
}
