package net.artmaster.masterianmod7.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public record SyncInfectionPayload(Map<BlockPos, Integer> map) implements CustomPacketPayload {

    public static final Type<SyncInfectionPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("masterianmod7", "sync_infection"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncInfectionPayload> STREAM_CODEC =
            StreamCodec.of(
                    (buf, payload) -> {
                        buf.writeInt(payload.map().size());
                        for (Map.Entry<BlockPos, Integer> entry : payload.map().entrySet()) {
                            buf.writeBlockPos(entry.getKey());
                            buf.writeInt(entry.getValue());
                        }
                    },
                    buf -> {
                        int size = buf.readInt();
                        Map<BlockPos, Integer> map = new java.util.HashMap<>();
                        for (int i = 0; i < size; i++) {
                            BlockPos pos = buf.readBlockPos();
                            int val = buf.readInt();
                            map.put(pos, val);
                        }
                        return new SyncInfectionPayload(map);
                    }
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
