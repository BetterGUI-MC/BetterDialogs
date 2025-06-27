package me.hsgamer.bettergui.betterdialogs;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCustomClickAction;

import static com.github.retrooper.packetevents.wrapper.common.client.WrapperCommonClientCustomClickAction.MAX_PAYLOAD_SIZE;

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
        String dialogId = wrapper.readString();
        NBT nbt = wrapper.readLengthPrefixed(MAX_PAYLOAD_SIZE, PacketWrapper::readNBTRaw);
        cloneEvent.cleanUp();

        WrapperPlayClientCustomClickAction packet = new WrapperPlayClientCustomClickAction(event);
        NBT data = packet.getPayload();

        instance.getLogger().log("Received custom click action for dialog: " + dialogId);
        instance.getLogger().log("Data: " + data + " (Type: " + data.getClass().getSimpleName() + ")");
        instance.getLogger().log("Actual Data: " + nbt + " (Type: " + nbt.getClass().getSimpleName() + ")");
        instance.getLogger().log("Byte Array: ");
        StringBuilder byteArrayString = new StringBuilder();
        for (byte b : byteArray) {
            byteArrayString.append(String.format("%02X ", b));
        }
        instance.getLogger().log(byteArrayString.toString());
    }
}
