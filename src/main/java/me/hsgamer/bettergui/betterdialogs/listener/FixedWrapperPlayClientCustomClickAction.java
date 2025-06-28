package me.hsgamer.bettergui.betterdialogs.listener;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCustomClickAction;
import org.jetbrains.annotations.Nullable;

public class FixedWrapperPlayClientCustomClickAction extends WrapperPlayClientCustomClickAction {
    public FixedWrapperPlayClientCustomClickAction(PacketReceiveEvent event) {
        super(event);
    }

    public FixedWrapperPlayClientCustomClickAction(ResourceLocation id, @Nullable NBT payload) {
        super(id, payload);
    }

    @Override
    public void read() {
        setId(ResourceLocation.read(this));
        setPayload(this.readLengthPrefixed(MAX_PAYLOAD_SIZE, PacketWrapper::readNBTRaw));
    }

    @Override
    public void write() {
        ResourceLocation.write(this, getId());
        this.writeLengthPrefixed(getPayload(), PacketWrapper::writeNBTRaw);
    }
}
