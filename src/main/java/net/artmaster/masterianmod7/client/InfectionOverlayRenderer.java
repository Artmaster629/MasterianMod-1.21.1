package net.artmaster.masterianmod7.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.artmaster.masterianmod7.MasterianMod;
import net.artmaster.masterianmod7.data.InfectionData;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import org.joml.Matrix4f;

import java.util.HashSet;

@OnlyIn(Dist.CLIENT)
@Mod(value = MasterianMod.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = MasterianMod.MODID, value = Dist.CLIENT)
public class InfectionOverlayRenderer {



    private static final ResourceLocation TEXTURE_0 = ResourceLocation.fromNamespaceAndPath("masterianmod7", "textures/block/empty.png");
    private static final ResourceLocation TEXTURE_1 = ResourceLocation.fromNamespaceAndPath("masterianmod7", "textures/block/infection_1.png");
    private static final ResourceLocation TEXTURE_2 = ResourceLocation.fromNamespaceAndPath("masterianmod7", "textures/block/infection_2.png");
    private static final ResourceLocation TEXTURE_3 = ResourceLocation.fromNamespaceAndPath("masterianmod7", "textures/block/infection_3.png");
    private static final ResourceLocation TEXTURE_4 = ResourceLocation.fromNamespaceAndPath("masterianmod7", "textures/block/infection_4.png");
    private static final ResourceLocation TEXTURE_5 = ResourceLocation.fromNamespaceAndPath("masterianmod7", "textures/block/infection_5.png");

    @SubscribeEvent
    public static void renderWorld(RenderLevelStageEvent event) {






        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) return;

        PoseStack poseStack = event.getPoseStack();
        Camera camera = event.getCamera();

        Level level = Minecraft.getInstance().level;
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();

        for (BlockPos pos : new HashSet<>(InfectionData.getAll().keySet())) {
            poseStack.pushPose();
            poseStack.translate(pos.getX() - camera.getPosition().x,
                    pos.getY() - camera.getPosition().y,
                    pos.getZ() - camera.getPosition().z);

            // Пример — infection_1.png
            RenderType translucent = RenderType.entityTranslucent(TEXTURE_1);
            VertexConsumer buffer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(translucent);

            int levelInfection = InfectionData.getInfection(pos);
            //if (levelInfection <= 0) return;
            ResourceLocation texture = switch (levelInfection) {
                case 0 -> TEXTURE_0;
                default -> TEXTURE_1;
                case 2 -> TEXTURE_2;
                case 3 -> TEXTURE_3;
                case 4 -> TEXTURE_4;
                case 5 -> TEXTURE_5;
            };
            renderOverlayFace(poseStack, texture); // наложение

            poseStack.popPose();
        }

//        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS) return;
//
//        var level = Minecraft.getInstance().level;
//        var camera = Minecraft.getInstance().gameRenderer.getMainCamera();
//        var camPos = camera.getPosition();
//        var poseStack = event.getPoseStack();
//
//        poseStack.pushPose();
//        poseStack.translate(-camPos.x, -camPos.y, -camPos.z);
//
//        var bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
//        var dispatcher = Minecraft.getInstance().getBlockRenderer();
//
//        for (var entry : InfectionOverlayData.getAll().entrySet()) {
//            BlockPos pos = entry.getKey();
//            if (!level.isLoaded(pos)) continue;
//
//            int levelInfection = entry.getValue();
//            ResourceLocation texture = switch (levelInfection) {
//                case 2 -> TEXTURE_2;
//                case 3 -> TEXTURE_3;
//                default -> TEXTURE_1;
//            };
//
//            BlockState state = level.getBlockState(pos);
//            BakedModel model = dispatcher.getBlockModel(state);
//
//            // Привязываем текстуру и рисуем
//            Minecraft.getInstance().getTextureManager().bindForSetup(texture);
//
//            dispatcher.getModelRenderer().renderModel(
//                    poseStack.last(),
//                    bufferSource.getBuffer(RenderType.translucent()),
//                    state,
//                    model,
//                    1f, 1f, 1f,
//                    LevelRenderer.getLightColor(level, pos),
//                    OverlayTexture.NO_OVERLAY
//            );
//        }

//        poseStack.popPose();
//        bufferSource.endBatch(RenderType.translucent());
    }

    private static void renderOverlayFace(PoseStack poseStack, ResourceLocation texture) {
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);


        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buf = tess.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

        Matrix4f mat = poseStack.last().pose();
        float alpha = 0.5f;
        float min = 0f;
        float max = 1f;

        // FRONT
        buf.addVertex(mat, min, min, max).setUv(0f, 1f).setColor(1f, 1f, 1f, alpha);
        buf.addVertex(mat, max, min, max).setUv(1f, 1f).setColor(1f, 1f, 1f, alpha);
        buf.addVertex(mat, max, max, max).setUv(1f, 0f).setColor(1f, 1f, 1f, alpha);
        buf.addVertex(mat, min, max, max).setUv(0f, 0f).setColor(1f, 1f, 1f, alpha);

        // BACK
        buf.addVertex(mat, max, min, min).setUv(0f, 1f).setColor(1f, 1f, 1f, alpha);
        buf.addVertex(mat, min, min, min).setUv(1f, 1f).setColor(1f, 1f, 1f, alpha);
        buf.addVertex(mat, min, max, min).setUv(1f, 0f).setColor(1f, 1f, 1f, alpha);
        buf.addVertex(mat, max, max, min).setUv(0f, 0f).setColor(1f, 1f, 1f, alpha);

        // LEFT
        buf.addVertex(mat, min, min, min).setUv(0f, 1f).setColor(1f, 1f, 1f, alpha);
        buf.addVertex(mat, min, min, max).setUv(1f, 1f).setColor(1f, 1f, 1f, alpha);
        buf.addVertex(mat, min, max, max).setUv(1f, 0f).setColor(1f, 1f, 1f, alpha);
        buf.addVertex(mat, min, max, min).setUv(0f, 0f).setColor(1f, 1f, 1f, alpha);

        // RIGHT
        buf.addVertex(mat, max, min, max).setUv(0f, 1f).setColor(1f, 1f, 1f, alpha);
        buf.addVertex(mat, max, min, min).setUv(1f, 1f).setColor(1f, 1f, 1f, alpha);
        buf.addVertex(mat, max, max, min).setUv(1f, 0f).setColor(1f, 1f, 1f, alpha);
        buf.addVertex(mat, max, max, max).setUv(0f, 0f).setColor(1f, 1f, 1f, alpha);

        // TOP
        buf.addVertex(mat, min, max, max).setUv(0f, 1f).setColor(1f, 1f, 1f, alpha);
        buf.addVertex(mat, max, max, max).setUv(1f, 1f).setColor(1f, 1f, 1f, alpha);
        buf.addVertex(mat, max, max, min).setUv(1f, 0f).setColor(1f, 1f, 1f, alpha);
        buf.addVertex(mat, min, max, min).setUv(0f, 0f).setColor(1f, 1f, 1f, alpha);

        // BOTTOM
        buf.addVertex(mat, min, min, min).setUv(0f, 1f).setColor(1f, 1f, 1f, alpha);
        buf.addVertex(mat, max, min, min).setUv(1f, 1f).setColor(1f, 1f, 1f, alpha);
        buf.addVertex(mat, max, min, max).setUv(1f, 0f).setColor(1f, 1f, 1f, alpha);
        buf.addVertex(mat, min, min, max).setUv(0f, 0f).setColor(1f, 1f, 1f, alpha);
        BufferUploader.drawWithShader(buf.buildOrThrow());

        RenderSystem.disableBlend();
    }




}
