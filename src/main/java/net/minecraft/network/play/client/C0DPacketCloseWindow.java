package net.minecraft.network.play.client;

import java.io.IOException;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;


public class C0DPacketCloseWindow implements Packet<INetHandlerPlayServer>
{

    public int getWindowId() {
        return windowId;
    }

    int windowId;

    public C0DPacketCloseWindow()
    {
    }

    public C0DPacketCloseWindow(int windowId)
    {
        this.windowId = windowId;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processCloseWindow(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.windowId = buf.readByte();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeByte(this.windowId);
    }
}
