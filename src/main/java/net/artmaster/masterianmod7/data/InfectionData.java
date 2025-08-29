package net.artmaster.masterianmod7.data;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.core.HolderLookup;

import java.util.HashMap;
import java.util.Map;

public class InfectionData extends SavedData {
    private static final Map<BlockPos, Integer> infectionMap = new HashMap<>();

    public static final String ID = "infection_data";

    public InfectionData() {
    }

    public static final SavedData.Factory<InfectionData> FACTORY = new SavedData.Factory<>(
            InfectionData::new,
            InfectionData::load
    );

    public static InfectionData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(FACTORY, ID);
    }

    public static int getInfection(BlockPos pos) {
        return infectionMap.getOrDefault(pos.immutable(), 0);
    }

    public static Map<BlockPos, Integer> getAll() {
        return infectionMap;
    }

    public void setInfection(BlockPos pos, int infection) {
        infectionMap.put(pos.immutable(), infection);
        setDirty(); // Помечаем как изменённое
    }

    public void remove(BlockPos pos) {
        infectionMap.remove(pos.immutable());
        setDirty();
    }

    public static InfectionData load(CompoundTag tag, HolderLookup.Provider provider) {
        InfectionData data = new InfectionData();
        CompoundTag infections = tag.getCompound("infections");
        for (String key : infections.getAllKeys()) {
            CompoundTag entry = infections.getCompound(key);

            BlockPos pos = NbtUtils.readBlockPos(entry, "pos").get();
            int value = entry.getInt("value");
            data.infectionMap.put(pos.immutable(), value);
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        CompoundTag infections = new CompoundTag();
        int index = 0;
        for (Map.Entry<BlockPos, Integer> entry : infectionMap.entrySet()) {
            CompoundTag entryTag = new CompoundTag();
            entryTag.put("pos", NbtUtils.writeBlockPos(entry.getKey()));
            entryTag.putInt("value", entry.getValue());
            infections.put("entry_" + index++, entryTag);
        }
        tag.put("infections", infections);
        return tag;
    }
}
