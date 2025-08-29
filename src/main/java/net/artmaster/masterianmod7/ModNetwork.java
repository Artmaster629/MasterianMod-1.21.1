package net.artmaster.masterianmod7;

import net.artmaster.masterianmod7.data.InfectionData;
import net.artmaster.masterianmod7.network.SyncInfectionPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;


@Mod("masterianmod7")
@EventBusSubscriber(modid = MasterianMod.MODID)
public class ModNetwork {
    @SubscribeEvent
    public static void onRegisterPayload(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("masterianmod7");

        registrar.playToClient(
                SyncInfectionPayload.TYPE,
                SyncInfectionPayload.STREAM_CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        InfectionData.getAll().clear();
                        InfectionData.getAll().putAll(payload.map());
                    });
                }
        );
    }
}
