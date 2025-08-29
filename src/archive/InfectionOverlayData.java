//package net.artmaster.masterianmod7.client;
//
//import net.minecraft.core.BlockPos;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class InfectionOverlayData {
//    private static final Map<BlockPos, Integer> infectedBlocksClient = new HashMap<>();
//
//    public static void infectClient(BlockPos pos, int level) {
//        infectedBlocksClient.put(pos.immutable(), level);
//    }
//
//    public static Map<BlockPos, Integer> getAllClient() {
//        return infectedBlocksClient;
//    }
//
//    public static boolean isInfectedClient(BlockPos pos) {
//        return infectedBlocksClient.containsKey(pos);
//    }
//
//    public static int getLevelClient(BlockPos pos) {
//        return infectedBlocksClient.getOrDefault(pos, 1);
//    }
//}
