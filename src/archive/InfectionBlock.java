//package net.artmaster.masterianmod7.block;
//
//import net.artmaster.masterianmod7.block.InfectionBlockEntity;
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.level.BlockGetter;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.EntityBlock;
//import net.minecraft.world.level.block.RenderShape;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.state.BlockState;
//
//import javax.annotation.Nullable;
//import java.util.Properties;
//
//public class InfectionBlock extends Block implements EntityBlock {
//    public InfectionBlock(Properties props) {
//        super(props);
//    }
//
//
//    @Nullable
//    @Override
//    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
//        return new InfectionBlockEntity(pos, state);
//    }
//
//    @Override
//    public RenderShape getRenderShape(BlockState state) {
//        return RenderShape.INVISIBLE; // Скрываем дефолтную текстуру, отрисовываем вручную
//    }
//
//
//}
