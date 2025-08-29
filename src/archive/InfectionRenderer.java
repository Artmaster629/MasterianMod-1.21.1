//package net.artmaster.masterianmod7.client;
//
//import com.mojang.blaze3d.systems.RenderSystem;
//import com.mojang.blaze3d.vertex.*;
//import net.artmaster.masterianmod7.block.InfectionBlockEntity;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.GameRenderer;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.client.renderer.RenderType;
//import net.minecraft.client.renderer.block.BlockRenderDispatcher;
//import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
//import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
//import net.minecraft.client.renderer.texture.OverlayTexture;
//import net.minecraft.client.resources.model.BakedModel;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.level.block.state.BlockState;
//import net.neoforged.api.distmarker.Dist;
//import net.neoforged.api.distmarker.OnlyIn;
//import org.joml.Matrix4f;
//
//import java.util.Map;
//
//@OnlyIn(Dist.CLIENT)
//public class InfectionRenderer implements BlockEntityRenderer<InfectionBlockEntity> {
//    private static final ResourceLocation TEXTURE_1 = ResourceLocation.fromNamespaceAndPath("masterianmod7", "textures/block/infection_1.png");
//    private static final ResourceLocation TEXTURE_2 = ResourceLocation.fromNamespaceAndPath("masterianmod7", "textures/block/infection_2.png");
//    private static final ResourceLocation TEXTURE_3 = ResourceLocation.fromNamespaceAndPath("masterianmod7", "textures/block/infection_3.png");
//
//    public InfectionRenderer(BlockEntityRendererProvider.Context context) {
//    }
//
//    @Override
//    public void render(InfectionBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
//        poseStack.pushPose();
//
//        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
//        BlockState state = be.getBlockState();
//        BakedModel model = dispatcher.getBlockModel(state);
//
//        PoseStack.Pose pose = poseStack.last();
//
//        // Включаем смешивание
//        RenderSystem.enableBlend();
//        RenderSystem.defaultBlendFunc();
//        RenderSystem.depthMask(false);
//
//        // 1. Рендерим основную текстуру блока
//        dispatcher.getModelRenderer().renderModel(
//                pose,
//                bufferSource.getBuffer(RenderType.solid()),  // Это для основной текстуры
//                state,
//                model,
//                1f, 1f, 1f,
//                light,
//                OverlayTexture.NO_OVERLAY
//        );
//
//        // 2. Рендерим полупрозрачную текстуру поверх
//        ResourceLocation overlayTexture = switch (be.getInfection()) {
//            case 2 -> TEXTURE_2;
//            case 3 -> TEXTURE_3;
//            default -> TEXTURE_1;
//        };
//
//        // Привязываем полупрозрачную текстуру
//        Minecraft.getInstance().getTextureManager().bindForSetup(overlayTexture);
//
//        // Рендерим полупрозрачную текстуру поверх основной
//        dispatcher.getModelRenderer().renderModel(
//                pose,
//                bufferSource.getBuffer(RenderType.translucent()),  // Это для полупрозрачной текстуры
//                state,
//                model,
//                1f, 1f, 1f,
//                light,
//                OverlayTexture.NO_OVERLAY
//        );
//
//        // Включаем обратно маску глубины
//        RenderSystem.depthMask(true);
//
//        RenderSystem.disableBlend();
//
//        poseStack.popPose();
//    }
//}
//
//
//
