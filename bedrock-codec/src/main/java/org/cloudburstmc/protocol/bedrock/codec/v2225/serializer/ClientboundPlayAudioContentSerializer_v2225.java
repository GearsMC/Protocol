package org.cloudburstmc.protocol.bedrock.codec.v2225.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.codec.v2192.serializer.PlaySoundSerializer_v2192;
import org.cloudburstmc.protocol.bedrock.packet.ClientboundPlayAudioContentPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlaySoundPacket;

public class ClientboundPlayAudioContentSerializer_v2225 implements BedrockPacketSerializer<ClientboundPlayAudioContentPacket> {

    public static final ClientboundPlayAudioContentSerializer_v2225 INSTANCE = new ClientboundPlayAudioContentSerializer_v2225();

    @Override
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, ClientboundPlayAudioContentPacket packet) {
        helper.writeString(buffer, packet.getSharedMetadata());
        helper.writeString(buffer, packet.getPlaybackContent());
        helper.writeString(buffer, packet.getPlaybackType().getName());
        PlaySoundSerializer_v2192.INSTANCE.serialize(buffer, helper, packet.getPlaySound());
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, ClientboundPlayAudioContentPacket packet) {
        packet.setSharedMetadata(helper.readString(buffer));
        packet.setPlaybackContent(helper.readString(buffer));
        packet.setPlaybackType(ClientboundPlayAudioContentPacket.AudioContentPlaybackType.fromName(helper.readString(buffer)));
        // Paketin playSound alanı varsayılan olarak boş; okunan ses yeni bir pakete çözülür.
        PlaySoundPacket playSound = new PlaySoundPacket();
        PlaySoundSerializer_v2192.INSTANCE.deserialize(buffer, helper, playSound);
        packet.setPlaySound(playSound);
    }
}
