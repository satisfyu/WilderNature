package net.satisfy.wildernature.registry;

import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.satisfy.wildernature.WilderNature;
import net.satisfy.wildernature.block.entity.BountyBoardBlockEntity;
import net.satisfy.wildernature.block.entity.CompletionistBannerEntity;
import net.satisfy.wildernature.client.gui.handlers.BountyBlockScreenHandler;
import net.satisfy.wildernature.entity.*;
import net.satisfy.wildernature.util.WilderNatureIdentifier;

import java.util.function.Supplier;

public class EntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(WilderNature.MOD_ID, Registries.BLOCK_ENTITY_TYPE);
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(WilderNature.MOD_ID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<CompletionistBannerEntity>> COMPLETIONIST_BANNER_ENTITY = createBlockEntity("completionist_banner", () -> BlockEntityType.Builder.of(CompletionistBannerEntity::new, ObjectRegistry.WOLF_TRAPPER_BANNER.get(), ObjectRegistry.WOLF_TRAPPER_WALL_BANNER.get(), ObjectRegistry.BUNNY_STALKER_BANNER.get(), ObjectRegistry.BUNNY_STALKER_WALL_BANNER.get(), ObjectRegistry.COD_CATCHER_BANNER.get(), ObjectRegistry.COD_CATCHER_WALL_BANNER.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<BountyBoardBlockEntity>> BOUNTY_BOARD_ENTITY = createBlockEntity("bounty_board", () -> BlockEntityType.Builder.of(BountyBoardBlockEntity::new, ObjectRegistry.BOUNTY_BOARD.get()).build(null));

    public static final RegistrySupplier<EntityType<FlamingoEntity>> FLAMINGO = createEntity("flamingo", () -> EntityType.Builder.of(FlamingoEntity::new, MobCategory.CREATURE).sized(0.3f, 0.3f).clientTrackingRange(10).build(new WilderNatureIdentifier("flamingo").toString()));
    public static final RegistrySupplier<EntityType<HedgehogEntity>> HEDGEHOG = createEntity("hedgehog", () -> EntityType.Builder.of(HedgehogEntity::new, MobCategory.CREATURE).sized(0.3f, 0.3f).clientTrackingRange(10).build(new WilderNatureIdentifier("hedgehog").toString()));
    public static final RegistrySupplier<EntityType<CassowaryEntity>> CASSOWARY = createEntity("cassowary", () -> EntityType.Builder.of(CassowaryEntity::new, MobCategory.CREATURE).sized(0.7f, 0.7f).clientTrackingRange(10).build(new WilderNatureIdentifier("cassowary").toString()));
    public static final RegistrySupplier<EntityType<PenguinEntity>> PENGUIN = createEntity("penguin", () -> EntityType.Builder.of(PenguinEntity::new, MobCategory.CREATURE).sized(0.7f, 0.9f).clientTrackingRange(10).build(new WilderNatureIdentifier("penguin").toString()));
    public static final RegistrySupplier<EntityType<RedWolfEntity>> RED_WOLF = createEntity("red_wolf", () -> EntityType.Builder.of(RedWolfEntity::new, MobCategory.CREATURE).sized(0.4f, 1.5f).clientTrackingRange(10).build(new WilderNatureIdentifier("red_wolf").toString()));
    public static final RegistrySupplier<EntityType<PelicanEntity>> PELICAN = createEntity("pelican", () -> EntityType.Builder.of(PelicanEntity::new, MobCategory.CREATURE).sized(0.6F, 1.0F).build(new WilderNatureIdentifier("pelican").toString()));
    public static final RegistrySupplier<EntityType<RaccoonEntity>> RACCOON = createEntity("raccoon", () -> EntityType.Builder.of(RaccoonEntity::new, MobCategory.CREATURE).sized(0.6f, 0.6f).build(new WilderNatureIdentifier("raccoon").toString()));
    public static final RegistrySupplier<EntityType<SquirrelEntity>> SQUIRREL = createEntity("squirrel", () -> EntityType.Builder.of(SquirrelEntity::new, MobCategory.CREATURE).sized(0.4f, 0.9f).build(new WilderNatureIdentifier("squirrel").toString()));
    public static final RegistrySupplier<EntityType<TurkeyEntity>> TURKEY = createEntity("turkey", () -> EntityType.Builder.of(TurkeyEntity::new, MobCategory.CREATURE).sized(0.6F, 1.0F).build(new WilderNatureIdentifier("turkey").toString()));
    public static final RegistrySupplier<EntityType<DeerEntity>> DEER = createEntity("deer", () -> EntityType.Builder.of(DeerEntity::new, MobCategory.CREATURE).sized(1.3F, 1.6F).build(new WilderNatureIdentifier("deer").toString()));
    public static final RegistrySupplier<EntityType<OwlEntity>> OWL = createEntity("owl", () -> EntityType.Builder.of(OwlEntity::new, MobCategory.CREATURE).sized(0.6F, 1.0F).build(new WilderNatureIdentifier("owl").toString()));
    public static final RegistrySupplier<EntityType<BoarEntity>> BOAR = createEntity("boar", () -> EntityType.Builder.of(BoarEntity::new, MobCategory.CREATURE).sized(0.9f, 0.9f).build(new WilderNatureIdentifier("boar").toString()));
    public static final RegistrySupplier<EntityType<BisonEntity>> BISON = createEntity("bison", () -> EntityType.Builder.of(BisonEntity::new, MobCategory.CREATURE).sized(1.8f, 2.2f).build(new WilderNatureIdentifier("bison").toString()));
    public static final RegistrySupplier<EntityType<DogEntity>> DOG = createEntity("dog", () -> EntityType.Builder.of(DogEntity::new, MobCategory.CREATURE).sized(0.9f, 1.3f).build(new WilderNatureIdentifier("dog").toString()));
    public static final RegistrySupplier<EntityType<MiniSheepEntity>> MINISHEEP = createEntity("minisheep", () -> EntityType.Builder.of(MiniSheepEntity::new, MobCategory.CREATURE).sized(0.9f, 1.3f).build(new WilderNatureIdentifier("minisheep").toString()));
    public static final RegistrySupplier<EntityType<BulletEntity>> BULLET = createEntity("bullet", () -> EntityType.Builder.<BulletEntity>of(BulletEntity::new, MobCategory.MISC).sized(0.3125f, 0.3125f).clientTrackingRange(64).updateInterval(2).build(new WilderNatureIdentifier("bullet").toString()));

    public static <T extends EntityType<?>> RegistrySupplier<T> createEntity(final String path, final Supplier<T> type) {
        return ENTITY_TYPES.register(new WilderNatureIdentifier(path), type);
    }

    private static <T extends BlockEntityType<?>> RegistrySupplier<T> createBlockEntity(final String path, final Supplier<T> type) {
        return BLOCK_ENTITIES.register(new WilderNatureIdentifier(path), type);
    }

    public static void init() {
        WilderNature.LOGGER.debug("Registering Entities for " + WilderNature.MOD_ID);
        ENTITY_TYPES.register();
        BLOCK_ENTITIES.register();
        EntityAttributeRegistry.register(FLAMINGO, FlamingoEntity::createMobAttributes);
        EntityAttributeRegistry.register(CASSOWARY, CassowaryEntity::createMobAttributes);
        EntityAttributeRegistry.register(PENGUIN, PenguinEntity::createMobAttributes);
        EntityAttributeRegistry.register(DEER, DeerEntity::createMobAttributes);
        EntityAttributeRegistry.register(PELICAN, PelicanEntity::createMobAttributes);
        EntityAttributeRegistry.register(RED_WOLF, RedWolfEntity::createMobAttributes);
        EntityAttributeRegistry.register(RACCOON, RaccoonEntity::createMobAttributes);
        EntityAttributeRegistry.register(SQUIRREL, SquirrelEntity::createMobAttributes);
        EntityAttributeRegistry.register(OWL, OwlEntity::createMobAttributes);
        EntityAttributeRegistry.register(BOAR, BoarEntity::createMobAttributes);
        EntityAttributeRegistry.register(BISON, BisonEntity::createMobAttributes);
        EntityAttributeRegistry.register(TURKEY, TurkeyEntity::createMobAttributes);
        EntityAttributeRegistry.register(DOG, DogEntity::createMobAttributes);
        EntityAttributeRegistry.register(HEDGEHOG, HedgehogEntity::createMobAttributes);
        EntityAttributeRegistry.register(MINISHEEP, MiniSheepEntity::createMobAttributes);
        BountyBlockScreenHandler.registerMenuTypes();
    }
}
