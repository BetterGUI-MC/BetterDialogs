package me.hsgamer.bettergui.betterdialogs;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCustomClickAction;

public class DialogCustomClickListener extends PacketListenerAbstract {
    private final BetterDialogs instance;

    public DialogCustomClickListener(BetterDialogs instance) {
        this.instance = instance;
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.CUSTOM_CLICK_ACTION) return;

        WrapperPlayClientCustomClickAction packet = new WrapperPlayClientCustomClickAction(event);
        ResourceLocation dialogId = packet.getId();
        NBT data = packet.getPayload();

        instance.getLogger().log("Received custom click action for dialog: " + dialogId);
        instance.getLogger().log("Data: " + data);
    }
}
