//package net.artmaster.masterianmod7.block;
//
//import net.artmaster.masterianmod7.ModRegistry;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.HolderLookup;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.state.BlockState;
//
//
//public class InfectionBlockEntity extends BlockEntity {
//    private int infection = 1;
//
//    public InfectionBlockEntity(BlockPos pos, BlockState state) {
//        super(ModRegistry.INFECTION_BE, pos, state);
//    }
//
//    public int getInfection() {
//        return infection;
//    }
//
//    public void setInfection(int infection) {
//        this.infection = infection;
//        setChanged();
//    }
//
//    @Override
//    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
//        super.loadAdditional(tag, registries);
//        this.infection = tag.getInt("infection");
//    }
//
//    @Override
//    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
//        super.saveAdditional(tag, registries);
//        tag.putInt("infection", this.infection);
//    }
//}
