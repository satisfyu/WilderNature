package net.satisfy.wildernature.bountyboard;

import dev.architectury.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.wildernature.registry.EntityRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.satisfy.wildernature.bountyboard.contract.Contract;
import net.satisfy.wildernature.bountyboard.contract.ContractReloader;
import net.satisfy.wildernature.util.WilderNatureIdentifier;

import java.util.Arrays;
import java.util.Random;

public class BountyBoardBlockEntity extends BlockEntity implements MenuProvider {
    private static final String KEY_CONTRACTS = "contracts";
    private static final int rerollCooldown = 15 * 60 * 20; //15 minutes
    public int rerollCooldownLeft = 0;
    private static final String KEY_REROLL_COOLDOWN_LEFT = "reroll_cooldown_left";

    public static final int rerolls = 3;
    public int rerollsLeft;
    private static final String KEY_REROLLS_LEFT = "rerolls_left";
    private static final String KEY_TIER = "tier";
    private static final String KEY_EXP = "experience";
    private static final String KEY_LONGID = "longid";


    public Event onTick = new Event();
    public Event onBlockDataChange = new Event();
    public long boardId = new Random().nextInt();
    private ResourceLocation[] contracts = new ResourceLocation[3];
    public ResourceLocation tier = new WilderNatureIdentifier("tier1");
    public int xp=0;

    public ResourceLocation[] getContracts(){
        if(contracts==null){
            contracts = new ResourceLocation[3];
            fillWithRandomContracts();
        }
        if(contracts.length!=3){
            contracts=null;
            contracts = getContracts();
            return contracts;
        }
        for(int i=0;i<3;i++){
            if(contracts[i] == null)// || ContractReloader.contracts.get(contracts[i]) == null) //uncomment this to autogenerate new contract on error
                contracts[i] = getRandomContract();
        }
        return contracts.clone();
    }
    public void setContracts(ResourceLocation[] contracts){
        this.contracts = contracts.clone();
    }


    public BountyBoardBlockEntity(BlockPos a, BlockState b) {
        super(EntityRegistry.BOUNTY_BLOCK.get(), a, b);
        //load(new CompoundTag());
    }

    @Override
    public void load(CompoundTag compoundTag) {
        this.rerollCooldownLeft = compoundTag.contains(KEY_REROLL_COOLDOWN_LEFT) ? compoundTag.getInt(KEY_REROLL_COOLDOWN_LEFT) : 0;
        this.rerollsLeft = compoundTag.contains(KEY_REROLLS_LEFT) ? compoundTag.getInt(KEY_REROLLS_LEFT) : 3;
        this.boardId = compoundTag.contains(KEY_LONGID) ? compoundTag.getLong(KEY_LONGID) : new Random().nextInt();
        this.tier = compoundTag.contains(KEY_TIER) ? new ResourceLocation(compoundTag.getString(KEY_TIER)): new WilderNatureIdentifier("tier1");
        this.xp = compoundTag.contains(KEY_EXP) ? compoundTag.getInt(KEY_EXP) : 0;
        if(compoundTag.contains(KEY_CONTRACTS)){
            setContracts(ResourceLocation.CODEC.listOf().parse(NbtOps.INSTANCE,(compoundTag.get(KEY_CONTRACTS))).getOrThrow(false,(error)->new RuntimeException(error)).toArray(new ResourceLocation[3]));
        }
        else {
            fillWithRandomContracts();
        }
    }

    public boolean tryReroll(){
        if(rerollsLeft<=0){
            return false;
        }
        if(rerollsLeft == rerolls){
            rerollCooldownLeft = rerollCooldown;
        }
        rerollsLeft--;
        fillWithRandomContracts();
        onBlockDataChange.invoke();
        return true;
    }
    private void fillWithRandomContracts() {
        for(int i=0;i<3;i++){
            setRandomContactInSlot(i);
        }
    }

    public void setRandomContactInSlot(int i){
        var contracts = getContracts();
        contracts[i] = getRandomContract();
        setContracts(contracts);
    }
    private ResourceLocation getRandomContract(){
        return ContractReloader.getRandomContractOfTier(this.tier);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        compoundTag.putInt(KEY_REROLL_COOLDOWN_LEFT,rerollCooldownLeft);
        compoundTag.putInt(KEY_REROLLS_LEFT,rerollsLeft);
        compoundTag.putLong(KEY_LONGID, boardId);
        compoundTag.putString(KEY_TIER, tier.toString());
        compoundTag.putInt(KEY_EXP, xp);
        compoundTag.put(
                KEY_CONTRACTS,
                ResourceLocation.CODEC.listOf().encode(Arrays.stream(getContracts()).toList(),NbtOps.INSTANCE,new ListTag()).getOrThrow(false,(err)->{throw new RuntimeException(err);})
        );
    }

    public CompoundTag getContractsNbt() {
        var encode = Contract.CODEC.listOf().encode(Arrays.stream(getContracts()).map(resourceLocation -> Contract.fromId(resourceLocation)).toList(), NbtOps.INSTANCE, new ListTag());
        Tag orThrow = encode.getOrThrow(
                false,
                (error) -> {
                    throw new RuntimeException(error);
                }
        );


        var tag = new CompoundTag();
        tag.put("list",orThrow);
        return tag;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("[Replace me 1]");
    }


    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return BountyBlockScreenHandler.s_createServer(i,inventory,this);
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, BountyBoardBlockEntity abstractFurnaceBlockEntity) {
        abstractFurnaceBlockEntity.serverTick(level,blockPos,blockState);
    }

    private void serverTick(Level level, BlockPos blockPos, BlockState blockState) {
        rerollCooldownLeft--;
        if(rerollCooldownLeft<0)
            rerollsLeft = rerolls;
        onTick.invoke();
    }
    public BountyBoardTier getTier(){
        return BountyBoardTier.byId(this.tier).get();
    }
    public void addXp(int addXp) {
        //e
        var nextTierXp = getTier().experience();
        this.xp+=addXp;
        if(this.xp>=nextTierXp){
            addXp=xp-nextTierXp;
            this.xp = 0;

            if(getTier().nextTier().isEmpty()) {
                if(Platform.isDevelopmentEnvironment()) {
                    this.getLevel().getServer().getPlayerList().broadcastSystemMessage(Component.literal("_info: next tier is empty, impossible to upgrade"),true);
                }
                return;
            }
            this.tier = getTier().nextTier().get();
            onBlockDataChange.invoke();
            addXp(addXp);
        }
    }
}
