package net.satisfy.wildernature.util.contract;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.event.EventResult;
import dev.architectury.platform.Platform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.satisfy.wildernature.client.gui.handlers.BountyBlockScreenHandler;
import net.satisfy.wildernature.item.ContractItem;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;

public class ContractInProgress {
    public static final HashMap<UUID, ContractInProgress> progressPerPlayer = new HashMap<>();
    public static final long EXPIRY_TICKS = 60 * 60 * 20;

    private static ResourceLocation getContractResource(ContractInProgress progress) {
        return progress.contractResource;
    }

    public final ResourceLocation contractResource;
    public final long boardId;
    public int count;
    public final long startTick;

    private static long getId(Object o) {
        return ((ContractInProgress) o).boardId;
    }

    private static int getCount(Object o) {
        return ((ContractInProgress) o).count;
    }

    public static final Codec<ContractInProgress> SERVER_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("contract").forGetter(ContractInProgress::getContractResource),
            Codec.INT.fieldOf("count").forGetter(ContractInProgress::getCount),
            Codec.LONG.fieldOf("id").forGetter(ContractInProgress::getId),
            Codec.LONG.fieldOf("start_tick").forGetter(progress -> progress.startTick)
    ).apply(instance, ContractInProgress::new));

    public ContractInProgress(ResourceLocation contract, int count, long id, long startTick) {
        this.contractResource = contract;
        this.boardId = id;
        this.count = count;
        this.startTick = startTick;
    }

    public static ContractInProgress newInstance(ResourceLocation contract, int count, long boardId, long startTick) {
        return new ContractInProgress(contract, count, boardId, startTick);
    }

    public boolean isFinished() {
        return count <= 0;
    }

    public static EventResult onEntityDeath(LivingEntity entity, DamageSource damageSource) {
        var attacker = damageSource.getEntity();
        if (attacker instanceof ServerPlayer player) {
            var contract = progressPerPlayer.get(player.getUUID());
            if (contract != null) {
                contract.onEntityDeath(entity, damageSource, player);
            }
        }
        return EventResult.pass();
    }

    public void onEntityDeath(LivingEntity entity, DamageSource damageSource, Player player) {
        Contract contract = getContract();
        try {
            if (isFinished()) return;

            var lootParams = new LootParams.Builder((ServerLevel) entity.level())
                    .withParameter(LootContextParams.ORIGIN, entity.getPosition(0))
                    .withOptionalParameter(LootContextParams.THIS_ENTITY, entity)
                    .create(LootContextParamSets.COMMAND);
            var context = new LootContext.Builder(lootParams).create(null);

            var condition = (LootItemCondition) Objects.requireNonNull(entity.getServer())
                    .getLootData()
                    .getElement(LootDataType.PREDICATE, contract.targetPredicate());
            var result = condition != null && condition.test(context);

            var damageParams = new LootParams.Builder((ServerLevel) entity.level())
                    .withParameter(LootContextParams.THIS_ENTITY, entity)
                    .withParameter(LootContextParams.ORIGIN, entity.getPosition(0))
                    .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
                    .withParameter(LootContextParams.KILLER_ENTITY, damageSource.getEntity())
                    .withParameter(LootContextParams.DIRECT_KILLER_ENTITY, damageSource.getDirectEntity())
                    .withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player)
                    .create(LootContextParamSets.ENTITY);
            var damageContext = new LootContext.Builder(damageParams).create(null);

            var damageCondition = (LootItemCondition) entity.getServer().getLootData()
                    .getElement(LootDataType.PREDICATE, contract.damagePredicate());
            var damageResult = damageCondition == null || damageCondition.test(damageContext);

            if (damageCondition == null) {
                player.sendSystemMessage(Component.literal("Data error: contract " + contractResource + " has wrong damage predicate id (" + contract.damagePredicate() + "). Please check if name is correct"));
                return;
            }

            var targetMatches = (result || BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).equals(contract.targetPredicate()));
            if (Platform.isDevelopmentEnvironment()) {
                player.sendSystemMessage(Component.literal("_entity: " + targetMatches + "; damage: " + damageResult));
            }

            if (targetMatches && damageResult) {
                count--;
            }

            if (Platform.isDevelopmentEnvironment()) {
                player.sendSystemMessage(Component.literal("_Entities left: " + count));
            }

            if (isFinished() && Platform.isDevelopmentEnvironment()) {
                player.sendSystemMessage(Component.literal("_Finished contract \"" + contract.name() + "\""));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Objects.requireNonNull(player.getServer()).halt(false);
        }
    }

    @Override
    public String toString() {
        return "[" + boardId + ", " + contractResource.toString() + ", " + count + "]";
    }

    public void onFinish(ServerPlayer player) {
        MinecraftServer server = player.server;

        server.execute(() -> {
            var bountyHandler = ((BountyBlockScreenHandler) player.containerMenu);
            if (bountyHandler.targetEntity.boardId == boardId) {
                bountyHandler.targetEntity.addXp(getContract().reward().blockExpReward());
            } else if (Platform.isDevelopmentEnvironment()) {
                player.sendSystemMessage(Component.literal("_not given xp to block because contract taken in other board"));
            }

            if (getContract().reward().playerRewardLoot().isEmpty()) return;

            Objects.requireNonNull(player.level().getServer())
                    .getLootData()
                    .getLootTable(getContract().reward().playerRewardLoot().get())
                    .getRandomItems(
                            new LootParams.Builder((ServerLevel) player.level())
                                    .withParameter(LootContextParams.THIS_ENTITY, player)
                                    .withParameter(LootContextParams.ORIGIN, player.getPosition(0))
                                    .create(LootContextParamSets.ADVANCEMENT_REWARD),
                            player.getLootTableSeed(),
                            player::spawnAtLocation
                    );
        });
    }

    public Contract getContract() {
        return Contract.fromId(this.contractResource);
    }

    public static void onServerTick(MinecraftServer server) {
        long currentTick = server.overworld().getGameTime();
        Iterator<HashMap.Entry<UUID, ContractInProgress>> iterator = progressPerPlayer.entrySet().iterator();
        while (iterator.hasNext()) {
            HashMap.Entry<UUID, ContractInProgress> entry = iterator.next();
            UUID playerId = entry.getKey();
            ContractInProgress progress = entry.getValue();

            if (currentTick - progress.startTick >= EXPIRY_TICKS) {
                ServerPlayer player = server.getPlayerList().getPlayer(playerId);
                if (player != null) {
                    removeContractItem(player, "text.gui.wildernature.bounty.contract_expired");
                }
                iterator.remove();
            }
        }
    }

    public static void removeContractItem(ServerPlayer player, String messageKey) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof ContractItem) {
                if (stack.hasTag()) {
                    assert stack.getTag() != null;
                    if (stack.getTag().getUUID(ContractItem.TAG_PLAYER).equals(player.getUUID())) {
                        player.getInventory().removeItem(stack);
                        player.sendSystemMessage(Component.translatable(messageKey));
                        break;
                    }
                }
            }
        }
    }
}
