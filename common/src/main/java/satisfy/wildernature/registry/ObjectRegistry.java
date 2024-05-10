package satisfy.wildernature.registry;

import de.cristelknight.doapi.Util;
import de.cristelknight.doapi.common.block.FacingBlock;
import dev.architectury.core.item.ArchitecturySpawnEggItem;
import dev.architectury.registry.fuel.FuelRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.InstrumentTags;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import satisfy.wildernature.WilderNature;
import satisfy.wildernature.block.DeerTrophyBlock;
import satisfy.wildernature.item.BisonHornItem;
import satisfy.wildernature.item.WilderNatureStandardItem;
import satisfy.wildernature.util.WilderNatureIdentifier;

import java.util.function.Consumer;
import java.util.function.Supplier;


public class ObjectRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(WilderNature.MOD_ID, Registries.ITEM);
    public static final Registrar<Item> ITEM_REGISTRAR = ITEMS.getRegistrar();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(WilderNature.MOD_ID, Registries.BLOCK);
    public static final Registrar<Block> BLOCK_REGISTRAR = BLOCKS.getRegistrar();
    public static final RegistrySupplier<Item> WILDERNATURE_STANDARD = registerItem("wildernature_standard", () -> new WilderNatureStandardItem(new Item.Properties().stacksTo(16).rarity(Rarity.EPIC)));
    public static final RegistrySupplier<Item> DEER_SPAWN_EGG = registerItem("deer_spawn_egg", () -> new ArchitecturySpawnEggItem(EntityRegistry.DEER, -1, -1, getSettings()));
    public static final RegistrySupplier<Item> RED_WOLF_SPAWN_EGG = registerItem("red_wolf_spawn_egg", () -> new ArchitecturySpawnEggItem(EntityRegistry.RED_WOLF, -1, -1, getSettings()));
    public static final RegistrySupplier<Item> RACCOON_SPAWN_EGG = registerItem("raccoon_spawn_egg", () -> new ArchitecturySpawnEggItem(EntityRegistry.RACCOON, -1, -1, getSettings()));
    public static final RegistrySupplier<Item> SQUIRREL_SPAWN_EGG = registerItem("squirrel_spawn_egg", () -> new ArchitecturySpawnEggItem(EntityRegistry.SQUIRREL, -1, -1, getSettings()));
    public static final RegistrySupplier<Item> MOSSY_SHEEP_SPAWN_EGG = registerItem("mossy_sheep_spawn_egg", () -> new ArchitecturySpawnEggItem(EntityRegistry.MOSSY_SHEEP, -1, -1, getSettings()));
    public static final RegistrySupplier<Item> MUDDY_PIG_SPAWN_EGG = registerItem("muddy_pig_spawn_egg", () -> new ArchitecturySpawnEggItem(EntityRegistry.MUDDY_PIG, -1, -1, getSettings()));
    public static final RegistrySupplier<Item> PELICAN_SPAWN_EGG = registerItem("pelican_spawn_egg", () -> new ArchitecturySpawnEggItem(EntityRegistry.PELICAN, -1, -1, getSettings()));
    public static final RegistrySupplier<Item> OWL_SPAWN_EGG = registerItem("owl_spawn_egg", () -> new ArchitecturySpawnEggItem(EntityRegistry.OWL, -1, -1, getSettings()));
    public static final RegistrySupplier<Item> BOAR_SPAWN_EGG = registerItem("boar_spawn_egg", () -> new ArchitecturySpawnEggItem(EntityRegistry.BOAR, -1, -1, getSettings()));
    public static final RegistrySupplier<Item> BISON_SPAWN_EGG = registerItem("bison_spawn_egg", () -> new ArchitecturySpawnEggItem(EntityRegistry.BISON, -1, -1, getSettings()));
    public static final RegistrySupplier<Item> TURKEY_SPAWN_EGG = registerItem("turkey_spawn_egg", () -> new ArchitecturySpawnEggItem(EntityRegistry.TURKEY, -1, -1, getSettings()));
    public static final RegistrySupplier<Item> PELICAN_MEAT = registerItem("pelican_meat", () -> new Item(getSettings().food(Foods.SALMON)));
    public static final RegistrySupplier<Item> COOKED_PELICAN_MEAT = registerItem("cooked_pelican_meat", () -> new Item(getSettings().food(Foods.COOKED_SALMON)));
    public static final RegistrySupplier<Item> FISH_OIL = registerItem("fish_oil", () -> new Item(getSettings().stacksTo(16)));
    public static final RegistrySupplier<Item> BISON_HORN = registerItem("bison_horn", () -> new BisonHornItem(new Item.Properties().stacksTo(1), SoundRegistry.BISON_HORN.get()));
    public static final RegistrySupplier<Block> DEER_TROPHY = registerWithItem("deer_trophy", () -> new DeerTrophyBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));


    /**
     * TODO:
     * Add:
     * Venison
     * Bison Meat
     * Turkey Meat
     * Cooked Venison
     * Cooked Bison Meat
     * Cooked Turkey Meat

     * Ideas for Items:
     * Animal Compendium
     * Trophies? - Can be placed on Walls. Only for Deers, Red Wolfs, Bisons and maybe Boars?

     * A Red Wolf Fur Cloak - adds a 'Fear' Effect to aggressive Animals and Creepers
     * Raccoons LootBag - rare drop when killing a raccoon - contains apples, bones, bonemeal, seeds
     * Blunderbuss - a mix between a bow and crossbow - VERY LOUD!
     * Truffle - will be dropped on the floor (low chance) when boars eating grass block - combine it with any edible item to give it 20% more hunger / saturation,
     -> Theres a Mod thats already have a functionality like this: The Salt Mod. I've just looked at the code and... its really complicated

     *  Ideas for Animals:
     * Ram, rideable - just like a slow Horse with LOTS of health
     * Birds & birdnest & treehouses
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
        return Util.registerWithItem(BLOCKS, BLOCK_REGISTRAR, ITEMS, ITEM_REGISTRAR, new WilderNatureIdentifier(name), block);
    }

    public static <T extends Block> RegistrySupplier<T> registerWithoutItem(String path, Supplier<T> block) {
        return Util.registerWithoutItem(BLOCKS, BLOCK_REGISTRAR, new WilderNatureIdentifier(path), block);
    }

    public static <T extends Item> RegistrySupplier<T> registerItem(String path, Supplier<T> itemSupplier) {
        return Util.registerItem(ITEMS, ITEM_REGISTRAR, new WilderNatureIdentifier(path), itemSupplier);
    }
}
