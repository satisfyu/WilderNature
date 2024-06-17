package satisfy.wildernature.bountyboard;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledHeapByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import satisfy.wildernature.WilderNature;
import satisfy.wildernature.bountyboard.contract.Contract;
import satisfy.wildernature.bountyboard.contract.ContractInProgress;
import satisfy.wildernature.bountyboard.contract.ContractItem;
import satisfy.wildernature.bountyboard.contract.ContractReloader;
import satisfy.wildernature.registry.ObjectRegistry;

public class BountyBlockScreenHandler extends AbstractContainerMenu {

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(WilderNature.MOD_ID, Registries.MENU);

    public static final RegistrySupplier<MenuType<BountyBlockScreenHandler>> BOUNTY_BLOCK = MENUS.register("bounty_menu",
            () -> MenuRegistry.ofExtended((id, inventory, buf) -> {
                BountyBlockScreenHandler bountyBlockScreenHandler = BountyBlockScreenHandler.c_createClient(id, inventory);
                bountyBlockScreenHandler.c_onServerUpdate(inventory.player,buf);
                return bountyBlockScreenHandler;
            })
    );
    public static void registerMenuTypes(){
        MENUS.register();
    }
    private BountyBlockScreenHandler(int id, Inventory inventory, BountyBoardBlockEntity s_targetEntity) {
        super(BOUNTY_BLOCK.get(), id);
        this.s_targetEntity = s_targetEntity;
        if(s_targetEntity!=null)
        {
            s_targetEntity.onTick.subscribe(() -> {
                if(s_targetEntity.rerollCooldownLeft % 20 == 0 && inventory.player.containerMenu==this){
                    var buf = new FriendlyByteBuf(new UnpooledHeapByteBuf(ByteBufAllocator.DEFAULT,0,BountyBlockNetworking.MAX_SIZE));
                    s_writeBlockDataChange(buf, this.s_targetEntity.rerollsLeft, this.s_targetEntity.rerollCooldownLeft, this.s_targetEntity.boardId,s_targetEntity.tier,s_targetEntity.xp);
                    NetworkManager.sendToPlayer((ServerPlayer) inventory.player,BountyBlockNetworking.ID_SCREEN_UPDATE,buf);
                }
            });
            s_targetEntity.onBlockDataChange.subscribe(()->{
                if(inventory.player.containerMenu==this){
                    var buf = new FriendlyByteBuf(new UnpooledHeapByteBuf(ByteBufAllocator.DEFAULT,0,BountyBlockNetworking.MAX_SIZE));
                    /////
                    buf.writeEnum(BountyBlockNetworking.BountyServerUpdateType.MULTI);
                    buf.writeShort(3);
                    s_writeBlockDataChange(buf, this.s_targetEntity.rerollsLeft, this.s_targetEntity.rerollCooldownLeft, this.s_targetEntity.boardId,s_targetEntity.tier,s_targetEntity.xp);
                    s_writeUpdateContracts(buf,s_targetEntity.getContractsNbt());
                    s_writeActiveContractInfo(buf, (ServerPlayer) inventory.player);
                    NetworkManager.sendToPlayer((ServerPlayer) inventory.player,BountyBlockNetworking.ID_SCREEN_UPDATE,buf);

                }
            });
        }

        for(int i=0;i<3;i++){
            for(int j=0;j<9;j++){
                addSlot(new Slot(inventory,i*9+j+9,j*18+8,i*18+86));
            }
        }
        for(int j=0;j<9;j++){
            addSlot(new Slot(inventory,j,j*18+8,144));
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    //////////////////////////////// SERVER STUFF ////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////


    public static AbstractContainerMenu s_createServer(int i, Inventory inventory, BountyBoardBlockEntity bountyBoardBlockEntity) {
        return new BountyBlockScreenHandler(i,inventory,bountyBoardBlockEntity);
    }

    public BountyBoardBlockEntity s_targetEntity;

    public static void s_writeBlockDataChange(FriendlyByteBuf buf, int rerolls, int time, long boardId, ResourceLocation tierId, int xp) {
        buf.writeEnum(BountyBlockNetworking.BountyServerUpdateType.SEND_BOARD_DATA);
        buf.writeInt(time);
        buf.writeByte(rerolls);
        buf.writeLong(boardId);
        buf.writeResourceLocation(tierId);
        buf.writeFloat(BountyBoardTier.byId(tierId).get().progress(xp));
    }
    public static void s_writeActiveContractInfo(FriendlyByteBuf buf,ServerPlayer player){
        buf.writeEnum(BountyBlockNetworking.BountyServerUpdateType.SEND_ACTIVE_CONTRACT);
        var contract = ContractInProgress.progressPerPlayer.get(player.getUUID());
        if(contract==null){
            buf.writeBoolean(false);
        }
        else{
            buf.writeBoolean(true);
            buf.writeNbt((CompoundTag) ContractInProgress.SERVER_CODEC.encode(contract,NbtOps.INSTANCE,new CompoundTag()).get().left().get());
        }
    }

    public static void s_writeUpdateContracts(FriendlyByteBuf friendlyByteBuf, CompoundTag contracts) {
        friendlyByteBuf.writeEnum(BountyBlockNetworking.BountyServerUpdateType.UPDATE_CONTRACTS);
        friendlyByteBuf.writeNbt(contracts);
    }
    public void s_handleClientAction(ServerPlayer player, FriendlyByteBuf buf) {
        var action = buf.readEnum(BountyBlockNetworking.BountyClientActionType.class);
        if(action == BountyBlockNetworking.BountyClientActionType.REROLL){
            WilderNature.info("Player {} Rerolled",player.getScoreboardName());
            s_targetEntity.tryReroll();
            s_targetEntity.getLevel();
        }
        if(action == BountyBlockNetworking.BountyClientActionType.CONFIRM_CONTRACT){
            var playerContract = ContractInProgress.progressPerPlayer.get(player.getUUID());
            var hasContract = playerContract == null;
            if(!hasContract){
                player.sendSystemMessage(Component.literal("Error: you already have contract"));
                return;
            }
            var id = buf.readByte();
            var contract = s_targetEntity.getContracts()[id];
            s_targetEntity.setRandomContactInSlot(id);
            var newBuf = new FriendlyByteBuf(new UnpooledHeapByteBuf(ByteBufAllocator.DEFAULT,0,BountyBlockNetworking.MAX_SIZE));
            var stack = Contract.fromId(contract).contractStack();
            stack.setTag(new CompoundTag());
            stack.getTag().putString(ContractItem.TAG_CONTRACT_ID,contract.toString());
            stack.getTag().putUUID(ContractItem.TAG_PLAYER,player.getUUID());
            player.spawnAtLocation(stack);
            ContractInProgress.progressPerPlayer.put(player.getUUID(),new ContractInProgress(contract, Contract.fromId(contract).count(),s_targetEntity.boardId));
            /////
            newBuf.writeEnum(BountyBlockNetworking.BountyServerUpdateType.MULTI);
            newBuf.writeShort(2);
            BountyBlockScreenHandler.s_writeActiveContractInfo(newBuf,player);
            BountyBlockScreenHandler.s_writeUpdateContracts(newBuf,s_targetEntity.getContractsNbt());
            NetworkManager.sendToPlayer(player,BountyBlockNetworking.ID_SCREEN_UPDATE,newBuf);
        }
        if(action == BountyBlockNetworking.BountyClientActionType.FINISH_CONTRACT){
            var contract = ContractInProgress.progressPerPlayer.get(player.getUUID());
            if(contract == null) {
                player.sendSystemMessage(Component.literal("Error: player does not have a contract"));
                return;
            }
            if(!contract.isFinished()){
                player.sendSystemMessage(Component.literal("Error: contract is not finished"));
                return;
            }
            player.sendSystemMessage(Component.translatable("text.gui.wildernature.bounty.finished",Component.translatable(contract.getS_contract().name())));
            ContractInProgress.progressPerPlayer.remove(player.getUUID());

            contract.onFinish(player);

            var newBuf = new FriendlyByteBuf(new UnpooledHeapByteBuf(ByteBufAllocator.DEFAULT,0,BountyBlockNetworking.MAX_SIZE));
            NetworkManager.sendToPlayer(player,BountyBlockNetworking.ID_SCREEN_UPDATE,newBuf);
            BountyBlockScreenHandler.s_writeActiveContractInfo(newBuf,player);
        }
        s_targetEntity.setChanged();
    }


    //////////////////////////////////////////////////////////////////////////////
    //////////////////////////////// CLIENT STUFF ////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////

    private static BountyBlockScreenHandler c_createClient(int id, Inventory inventory) {
        return new BountyBlockScreenHandler(id,inventory,null);
    }

    public Event c_onContractUpdate = new Event();
    public ContractInProgress c_activeContract;
    public long c_boardId;
    public Contract[] c_contracts;
    public int c_time;
    public int c_rerolls;
    public float c_progress;
    public ResourceLocation c_tierId;
    public void c_onServerUpdate(Player player, FriendlyByteBuf buf) {
        var updateType = buf.readEnum(BountyBlockNetworking.BountyServerUpdateType.class);
        try {
            if (Platform.isDevelopmentEnvironment()) {
                //WilderNature.info("_Trying to handle server update of type {}",updateType.toString());
            }
            if (updateType == BountyBlockNetworking.BountyServerUpdateType.MULTI) {
                int count = buf.readShort();
                for (int i = 0; i < count; i++) {
                    c_onServerUpdate(player, buf);
                }
            }
            if (updateType == BountyBlockNetworking.BountyServerUpdateType.UPDATE_CONTRACTS) {
                this.c_contracts = Contract.CODEC.listOf().decode(NbtOps.INSTANCE, buf.readNbt().get("list")).getOrThrow(false, (er) -> {
                    throw new RuntimeException(er);
                }).getFirst().toArray(new Contract[3]);
                c_onContractUpdate.invoke();
            }
            if (updateType == BountyBlockNetworking.BountyServerUpdateType.SEND_BOARD_DATA) {
                this.c_time = buf.readInt();
                this.c_rerolls = buf.readByte();
                this.c_boardId = buf.readLong();
                this.c_tierId = buf.readResourceLocation();
                this.c_progress = buf.readFloat();
            }
            if (updateType == BountyBlockNetworking.BountyServerUpdateType.SEND_ACTIVE_CONTRACT) {
                if(Platform.isDevelopmentEnvironment()){
                    Minecraft.getInstance().gui.getChat().addMessage(Component.literal("_handling SEND_ACTIVE_CONTRACT"));
                }
                var hasContract = buf.readBoolean();
                if(Platform.isDevelopmentEnvironment()){
                    Minecraft.getInstance().gui.getChat().addMessage(Component.literal("_hasContract: %b".formatted(hasContract)));
                }
                if (hasContract) {
                    var nbt = buf.readNbt();
                    var contract = ContractInProgress.SERVER_CODEC.decode(NbtOps.INSTANCE, nbt).getOrThrow(false, (er) -> {
                        throw new RuntimeException(er);
                    }).getFirst();
                    if(Platform.isDevelopmentEnvironment()){
                        Minecraft.getInstance().gui.getChat().addMessage(Component.literal("_active contract: %b".formatted(contract)));
                    }
                    this.c_activeContract = contract;
                } else {
                    this.c_activeContract = null;
                    if(Platform.isDevelopmentEnvironment()){
                        Minecraft.getInstance().gui.getChat().addMessage(Component.literal("_active contract is now null"));
                    }
                }
            }
        }
        catch(Exception e){
            Minecraft.getInstance().gui.getChat().addMessage(Component.literal("Error handling %s screen update packet: %s".formatted(updateType.toString(),e.getMessage())));
            throw new RuntimeException(e);
        }
    }
}
