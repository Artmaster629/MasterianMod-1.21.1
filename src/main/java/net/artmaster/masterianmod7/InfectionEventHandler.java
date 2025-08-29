package net.artmaster.masterianmod7;

import net.artmaster.masterianmod7.data.InfectionData;
import net.artmaster.masterianmod7.network.SyncInfectionPayload;
import net.minecraft.client.multiplayer.chat.report.ReportEnvironment;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.*;

@Mod("masterianmod7")
@EventBusSubscriber(modid = MasterianMod.MODID)
public class InfectionEventHandler {
    private static int tickCounter = 0;
    private static final List<BlockPos> infectionQueue = new ArrayList<>();

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().level() instanceof ServerLevel serverLevel) {
            InfectionData data = InfectionData.get(serverLevel);
            var map = data.getAll();

            SyncInfectionPayload payload = new SyncInfectionPayload(map);
            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                serverPlayer.connection.send(payload);  // Никакой SimpleChannel больше не нужен
            }
        }
    }






    public static void queueInfection(BlockPos pos) {
        infectionQueue.add(pos);
    }

    public static void tickInfection(ServerLevel level) {
        if (!infectionQueue.isEmpty()) {
            // Выбираем случайный блок из очереди
            int randomIndex = level.getRandom().nextInt(infectionQueue.size());
            BlockPos pos = infectionQueue.remove(randomIndex);

            InfectionData data = InfectionData.get(level);
            data.setInfection(pos, 1);
        }
    }

    private static void checkAllBlocksInChunk(LevelChunk chunk, Level level) {
        ChunkPos chunkPos = chunk.getPos();
        int minX = chunkPos.getMinBlockX();
        int minZ = chunkPos.getMinBlockZ();


        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = minX + x;
                int worldZ = minZ + z;

                int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE, worldX, worldZ);
                BlockPos surfacePos = new BlockPos(worldX, surfaceY - 1, worldZ);
                BlockState surfaceBlock = level.getBlockState(surfacePos);

                if (!surfaceBlock.getBlock().getDescriptionId().contains("short_grass")
                        && !surfaceBlock.getBlock().getDescriptionId().contains("tall_grass")) {
                    queueInfection(surfacePos);
                }
            }
        }

    }



    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Pre event) {
        tickCounter++;
        if (tickCounter % 20 == 0) {
            MinecraftServer server = event.getServer();
            for (ServerLevel level : server.getAllLevels()) {
                tickInfection(level);
            }
        }
    }


    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getLevel().isClientSide()) return;
        BlockPos pos = event.getPos();
        ChunkAccess chunkAccess = event.getLevel().getChunk(pos);
        if (chunkAccess instanceof LevelChunk levelChunk) {
            checkAllBlocksInChunk(levelChunk, event.getLevel());
        }
    }
}

//        // игнорируем повторный клик по тому же блоку в пределах 5 тиков
//        if (lastLeftClicks.containsKey(pos) && tick - lastLeftClicks.get(pos) < 5) {
//            return;
//        }
//        lastLeftClicks.put(pos, tick);
//
//
//        if (event.getLevel() instanceof ServerLevel serverLevel) {
//            InfectionData data = InfectionData.get(serverLevel);
//            int current = data.getInfection(pos);
//            if (data.getInfection(pos) != 0) {
//                data.setInfection(pos, current - 1);
//                System.out.println(data.getInfection(pos)+" - server");
//            }
//            if (data.getInfection(pos) == 0) {
//                System.out.println("Infection cannot be less than zero!");
//            }
//            System.out.println(data.getInfection(pos)+" - server");
//        }




//    @SubscribeEvent
//    public static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
//        if (event.getLevel().isClientSide()) return;
//        if (event.getHand() != InteractionHand.MAIN_HAND) return;
//
//
//        BlockPos pos = event.getPos();
//        if (event.getLevel() instanceof ServerLevel serverLevel) {
//            InfectionData data = InfectionData.get(serverLevel);
//            int current = data.getInfection(pos);
//            data.setInfection(pos, current + 1);
//            System.out.println(data.getInfection(pos) + " - server");
//
//            var map = data.getAll();
//
//            SyncInfectionPayload payload = new SyncInfectionPayload(map);
//            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
//                serverPlayer.connection.send(payload);  // Никакой SimpleChannel больше не нужен
//            }
//
//
//        }
//
//
//    }

//
//        // Регистрируем новый BlockEntity в мире
//        level.setBlockEntity(newBe);

        // Пример для копирования звука или других данных:
        // level.playSound(null, pos, SoundEvents.BLOCK_GRASS_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);


//    @SubscribeEvent
//    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
//        var pos = event.getPos();
//        var level = event.getLevel();
//        System.out.println("Test!");
//
//
//        var player = event.getEntity();
//        var x =  player.getX();
//        var y = player.getY();
//        var z = player.getZ();
//        var world = player.level();
//
//        BlockState oldState = level.getBlockState(pos);
//        BlockState newState = ModRegistry.INFECTION_BLOCK.defaultBlockState();
//
//        // Копируем все свойства старого блока в новый
//        for (Property<?> prop : oldState.getProperties()) {
//            if (newState.hasProperty(prop)) {
//                newState = copyProperty(newState, oldState, prop);
//            }
//        }
//
//        // Устанавливаем новый блок
//        BlockPos _bp = BlockPos.containing(x, y, z);
//        BlockState _bs = ModRegistry.INFECTION_BLOCK.defaultBlockState();
//        BlockState _bso = world.getBlockState(_bp);
//        for (Map.Entry<Property<?>, Comparable<?>> entry : _bso.getValues().entrySet()) {
//            Property _property = _bs.getBlock().getStateDefinition().getProperty(entry.getKey().getName());
//            if (_property != null && _bs.getValue(_property) != null)
//                try {
//                    _bs = _bs.setValue(_property, (Comparable) entry.getValue());
//                } catch (Exception e) {
//                }
//        }
//        world.setBlock(_bp, _bs, 3);
//
//
//        // Получаем старый BlockEntity (если есть)
//        BlockEntity oldBe = level.getBlockEntity(pos);
//
//        // Если старый BlockEntity существует, копируем данные в новый BlockEntity
//        if (oldBe instanceof InfectionBlockEntity oldInfectionBe) {
//            BlockEntity newBe = level.getBlockEntity(pos);
//            if (newBe instanceof InfectionBlockEntity newInfectionBe) {
//                // Переносим данные, например, инкремент инфекции
//                newInfectionBe.setInfection(oldInfectionBe.getInfection());
//            }
//        }
//
//        // Пример для копирования скорости ломания или звука:
//        // (Не забудь, что это нужно обрабатывать в зависимости от типа блока)
//        // level.playSound(null, pos, SoundEvents.BLOCK_GRASS_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
//    }