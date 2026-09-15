package org.cloudburstmc.protocol.bedrock.codec.v2193;

import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.v2192.BedrockCodecHelper_v2192;
import org.cloudburstmc.protocol.bedrock.codec.v2192.Bedrock_v2192;

/**
 * Minecraft 1.26.50 tam sürümü.
 * <p>
 * v2192, 1.26.50'nin son önizlemesinin ({@code 1.26.50-preview.26}) numarasıdır; tam sürüm istemcisi 2193
 * bildirir. Mojang şemasına göre ikisi arasında tel farkı yok, bu yüzden eşlemeler ve yardımcı olduğu gibi
 * devralınır. v2192 kodeği önizleme istemcileri için yerinde kalır.
 */
public class Bedrock_v2193 extends Bedrock_v2192 {

    public static final BedrockCodec CODEC = Bedrock_v2192.CODEC.toBuilder()
            .protocolVersion(2193)
            .minecraftVersion("1.26.50")
            .helper(() -> new BedrockCodecHelper_v2192(ENTITY_DATA, GAME_RULE_TYPES, ITEM_STACK_REQUEST_TYPES, CONTAINER_SLOT_TYPES, PLAYER_ABILITIES, TEXT_PROCESSING_ORIGINS))
            .build();
}
