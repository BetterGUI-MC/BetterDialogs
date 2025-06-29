package me.hsgamer.bettergui.betterdialogs.listener;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCustomClickAction;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;

public class DialogCustomClickListener extends PacketListenerAbstract {
    private static final String NAMESPACE = "betterdialogs";

    private final Map<String, BiConsumer<Player, NBT>> actions = new HashMap<>();

    private static String normalizeActionName(String actionName) {
        return actionName.replaceAll("[^a-zA-Z0-9_]", "_").toLowerCase(Locale.ROOT);
    }

    public ResourceLocation registerAction(String actionName, BiConsumer<Player, NBT> action) {
        actionName = normalizeActionName(actionName);
        actions.put(actionName, action);
        ResourceLocation resourceLocation = new ResourceLocation(NAMESPACE, actionName);
        System.out.println("Registered custom click action: " + resourceLocation);
        return new ResourceLocation(NAMESPACE, actionName);
    }

    public void clearActions() {
        actions.clear();
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.CUSTOM_CLICK_ACTION) return;

        WrapperPlayClientCustomClickAction packet = new WrapperPlayClientCustomClickAction(event);
        ResourceLocation namespacedId = packet.getId();
        NBT data = packet.getPayload();

        System.out.println("Custom Click Action ID: " + namespacedId);
        System.out.println("Custom Click Action Data: " + data);

        if (!namespacedId.getNamespace().equals(NAMESPACE)) return;
        String actionName = namespacedId.getKey();

        BiConsumer<Player, NBT> action = actions.get(actionName);
        if (action == null) return;

        Player player = event.getPlayer();
        action.accept(player, data);
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() != PacketType.Play.Server.SHOW_DIALOG) return;

        PacketSendEvent clonedEvent = event.clone();
        PacketWrapper<?> wrapper = new PacketWrapper<>(clonedEvent);
        int id = wrapper.readVarInt();
        NBT dialogNBT = wrapper.readNBTRaw();
        clonedEvent.cleanUp();

        System.out.println("Dialog ID: " + id);
        System.out.println("Dialog NBT: " + dialogNBT);
    }
}
