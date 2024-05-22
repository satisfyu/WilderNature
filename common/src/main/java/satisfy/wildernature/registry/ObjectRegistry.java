package satisfy.wildernature.registry;

import dev.architectury.core.item.ArchitecturySpawnEggItem;
import dev.architectury.registry.fuel.FuelRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import satisfy.wildernature.WilderNature;
import satisfy.wildernature.block.*;
import satisfy.wildernature.item.*;
import satisfy.wildernature.util.WilderNatureIdentifier;
import satisfy.wildernature.util.WilderNatureUtil;

import java.util.function.Consumer;
import java.util.function.Supplier;


public class ObjectRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(WilderNature.MOD_ID, Registries.ITEM);
    public static final Registrar<Item> ITEM_REGISTRAR = ITEMS.getRegistrar();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(WilderNature.MOD_ID, Registries.BLOCK);
    public static final Registrar<Block> BLOCK_REGISTRAR = BLOCKS.getRegistrar();
    public static final RegistrySupplier<Item> BISON_MEAT = registerItem("bison_meat", () -> new Item(getSettings().food(Foods.BEEF)));
    public static final RegistrySupplier<Item> COOKED_BISON_MEAT = registerItem("cooked_bison_meat", () -> new Item(getSettings().food(Foods.COOKED_BEEF)));
    public static final RegistrySupplier<Item> VENISON = registerItem("venison", () -> new Item(getSettings().food(Foods.MUTTON)));
    public static final RegistrySupplier<Item> COOKED_VENISON = registerItem("cooked_venison", () -> new Item(getSettings().food(Foods.COOKED_MUTTON)));
    public static final RegistrySupplier<Item> TURKEY_MEAT = registerItem("turkey_meat", () -> new Item(getSettings().food(Foods.CHICKEN)));
    public static final RegistrySupplier<Item> COOKED_TURKEY_MEAT = registerItem("cooked_turkey_meat", () -> new Item(getSettings().food(Foods.COOKED_CHICKEN)));
    public static final RegistrySupplier<Item> PELICAN_MEAT = registerItem("pelican_meat", () -> new Item(getSettings().food(Foods.SALMON)));
    public static final RegistrySupplier<Item> COOKED_PELICAN_MEAT = registerItem("cooked_pelican_meat", () -> new Item(getSettings().food(Foods.COOKED_SALMON)));
    public static final RegistrySupplier<Item> HAZELNUT = registerItem("hazelnut", () -> new Item(getSettings().food((new FoodProperties.Builder()).nutrition(4).saturationMod(0.3F).fast().build())));

    public static final RegistrySupplier<Item> BLUNDERBUSS = registerItem("blunderbuss", BlunderBussItem::new);
    public static final RegistrySupplier<Item> AMMO_BAG = registerItem("ammo_bag", () -> new BulletItem(getSettings(), 8));

    public static final RegistrySupplier<Item> FUR_CLOAK = registerItem("fur_cloak", () -> new FurCloakItem(ArmorMaterials.LEATHER, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    public static final RegistrySupplier<Item> FISH_OIL = registerItem("fish_oil", () -> new Item(getSettings().stacksTo(16)));
    public static final RegistrySupplier<Item> LOOT_BAG = registerItem("loot_bag", () -> new LootBagItem(getSettings()));
    public static final RegistrySupplier<Item> BISON_HORN = registerItem("bison_horn", () -> new BisonHornItem(new Item.Properties().stacksTo(1), SoundRegistry.BISON_HORN.get()));

    public static final RegistrySupplier<Block> DEER_TROPHY = registerWithItem("deer_trophy", () -> new DeerTrophyBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistrySupplier<Block> RED_WOLF_TROPHY = registerWithItem("red_wolf_trophy", () -> new RedWolfTrophyBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistrySupplier<Block> BISON_TROPHY = registerWithItem("bison_trophy", () -> new BisonTrophyBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));

    //Spawn Eggs
    public static final RegistrySupplier<Item> DEER_SPAWN_EGG = registerItem("deer_spawn_egg", () -> new ArchitecturySpawnEggItem(EntityRegistry.DEER, -1, -1, getSettings()));
    public static final RegistrySupplier<Item> RED_WOLF_SPAWN_EGG = registerItem("red_wolf_spawn_egg", () -> new ArchitecturySpawnEggItem(EntityRegistry.RED_WOLF, -1, -1, getSettings()));
    public static final RegistrySupplier<Item> RACCOON_SPAWN_EGG = registerItem("raccoon_spawn_egg", () -> new ArchitecturySpawnEggItem(EntityRegistry.RACCOON, -1, -1, getSettings()));
    public static final RegistrySupplier<Item> SQUIRREL_SPAWN_EGG = registerItem("squirrel_spawn_egg", () -> new ArchitecturySpawnEggItem(EntityRegistry.SQUIRREL, -1, -1, getSettings()));
    public static final RegistrySupplier<Item> PELICAN_SPAWN_EGG = registerItem("pelican_spawn_egg", () -> new ArchitecturySpawnEggItem(EntityRegistry.PELICAN, -1, -1, getSettings()));
    public static final RegistrySupplier<Item> OWL_SPAWN_EGG = registerItem("owl_spawn_egg", () -> new ArchitecturySpawnEggItem(EntityRegistry.OWL, -1, -1, getSettings()));
    public static final RegistrySupplier<Item> BOAR_SPAWN_EGG = registerItem("boar_spawn_egg", () -> new ArchitecturySpawnEggItem(EntityRegistry.BOAR, -1, -1, getSettings()));
    public static final RegistrySupplier<Item> BISON_SPAWN_EGG = registerItem("bison_spawn_egg", () -> new ArchitecturySpawnEggItem(EntityRegistry.BISON, -1, -1, getSettings()));
    public static final RegistrySupplier<Item> TURKEY_SPAWN_EGG = registerItem("turkey_spawn_egg", () -> new ArchitecturySpawnEggItem(EntityRegistry.TURKEY, -1, -1, getSettings()));

    public static final RegistrySupplier<Block> COMPLETIONIST_BANNER = registerWithItem("completionist_banner", () -> new CompletionistBannerBlock(BlockBehaviour.Properties.of().strength(1F).instrument(NoteBlockInstrument.BASS).noCollission().sound(SoundType.WOOD)));
    public static final RegistrySupplier<Block> COMPLETIONIST_WALL_BANNER = registerWithoutItem("completionist_wall_banner", () -> new CompletionistWallBannerBlock(BlockBehaviour.Properties.of().strength(1F).instrument(NoteBlockInstrument.BASS).noCollission().sound(SoundType.WOOD)));

    
    /**
     * Ideas for Items:
     * Animal Compendium

     *  Ideas for Animals:
     * Ram, rideable - just like a slow Horse with LOTS of health
     * Birds & birdnest & treehouses
     * A REAL Dog?
     * Penguin
     * Koala
     * Lions
     * Chameleon
     * Hippos
     * Elephants
     * Bears
     * Moose
     * Beaver
     * Kangaroos
     * Crocodiles
     * Jaguars
     * Porcupines
     * Hedgehog
     * A mini sheep
     */

    public static void init() {
        WilderNature.LOGGER.debug("Registering Mod Block and Items for " + WilderNature.MOD_ID);
        ITEMS.register();
        BLOCKS.register();
        commonInit();
    }

    public static void commonInit() {
        FuelRegistry.register(1400, FISH_OIL.get());
    }

    public static BlockBehaviour.Properties properties(float strength) {
        return properties(strength, strength);
    }

    public static BlockBehaviour.Properties properties(float breakSpeed, float explosionResist) {
        return BlockBehaviour.Properties.of().strength(breakSpeed, explosionResist);
    }

    private static Item.Properties getSettings(Consumer<Item.Properties> consumer) {
        Item.Properties settings = new Item.Properties();
        consumer.accept(settings);
        return settings;
    }

    static Item.Properties getSettings() {
        return getSettings(settings -> {
        });
    }

    public static <T extends Block> RegistrySupplier<T> registerWithItem(String name, Supplier<T> block) {
        return WilderNatureUtil.registerWithItem(BLOCKS, BLOCK_REGISTRAR, ITEMS, ITEM_REGISTRAR, new WilderNatureIdentifier(name), block);
    }

    public static <T extends Block> RegistrySupplier<T> registerWithoutItem(String path, Supplier<T> block) {
        return WilderNatureUtil.registerWithoutItem(BLOCKS, BLOCK_REGISTRAR, new WilderNatureIdentifier(path), block);
    }

    public static <T extends Item> RegistrySupplier<T> registerItem(String path, Supplier<T> itemSupplier) {
        return WilderNatureUtil.registerItem(ITEMS, ITEM_REGISTRAR, new WilderNatureIdentifier(path), itemSupplier);
    }
}
