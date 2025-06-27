package me.hsgamer.bettergui.betterdialogs;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCustomClickAction;

public class DialogCustomClickListener extends PacketListenerAbstract {
    private final BetterDialogs instance;

    public DialogCustomClickListener(BetterDialogs instance) {
        this.instance = instance;
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.CUSTOM_CLICK_ACTION) return;

        PacketReceiveEvent cloneEvent = event.clone();
        Object buffer = cloneEvent.getByteBuf();
        byte[] byteArray = ByteBufHelper.copyBytes(buffer);
        PacketWrapper<?> wrapper = new PacketWrapper<>(cloneEvent);
        ResourceLocation dialogId = ResourceLocation.read(wrapper);
        byte[] nbtData = wrapper.readLengthPrefixed(65536, (ew) -> wrapper.readRemainingBytes());
        cloneEvent.cleanUp();

        WrapperPlayClientCustomClickAction packet = new WrapperPlayClientCustomClickAction(event);
        NBT data = packet.getPayload();

        instance.getLogger().log("Received custom click action for dialog: " + dialogId);
        instance.getLogger().log("Data: " + data + " (Type: " + data.getClass().getSimpleName() + ")");
        instance.getLogger().log("Byte Array: ");
        StringBuilder byteArrayString = new StringBuilder();
        for (byte b : byteArray) {
            byteArrayString.append(String.format("%02X ", b));
        }
        instance.getLogger().log(byteArrayString.toString());
        instance.getLogger().log("NBT Byte Array: ");
        StringBuilder nbtByteArrayString = new StringBuilder();
        for (byte b : nbtData) {
            nbtByteArrayString.append(String.format("%02X ", b));
        }
        instance.getLogger().log(nbtByteArrayString.toString());
    }
}
