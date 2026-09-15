package org.cloudburstmc.protocol.bedrock.codec.v2193;

import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketDefinition;
import org.cloudburstmc.protocol.bedrock.codec.v2192.BedrockCodecHelper_v2192;
import org.cloudburstmc.protocol.bedrock.codec.v2192.Bedrock_v2192;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 1.26.50 tam sürümü (v2193) kodeğinin testi.
 * <p>
 * Mojang'ın {@code bedrock-protocol-docs} deposunda {@code v1.26.50-preview.26} ile {@code v1.26.50}
 * etiketleri yapısal olarak karşılaştırıldı: tel biçiminde fark yok. Değişen yalnızca istemcinin
 * bildirdiği protokol numarası; komut adlarındaki 512 karakter sınırı ise önizlemeye özgüydü ve tam
 * sürümde 1.26.45'teki 1000'e geri döndü. Bu yüzden v2193, v2192'nin eşlemelerini birebir taşımalı.
 */
class Bedrock_v2193Test {

    /**
     * Paket kimlikleri şu an 352'de bitiyor; tavan, ileride eklenecek kimlikleri de kapsayacak kadar geniş.
     */
    private static final int PACKET_ID_CEILING = 1024;

    @Test
    void codecAdvertisesTheReleaseProtocol() {
        assertEquals(2193, Bedrock_v2193.CODEC.getProtocolVersion());
        assertEquals("1.26.50", Bedrock_v2193.CODEC.getMinecraftVersion());
    }

    @Test
    void everyPacketDefinitionMatchesV2192() {
        int registered = 0;
        for (int id = 0; id < PACKET_ID_CEILING; id++) {
            BedrockPacketDefinition<?> preview = Bedrock_v2192.CODEC.getPacketDefinition(id);
            BedrockPacketDefinition<?> release = Bedrock_v2193.CODEC.getPacketDefinition(id);
            assertEquals(preview, release, "paket kimliği " + id + " v2192 ile v2193 arasında farklı");
            if (preview != null) {
                registered++;
            }
        }
        assertTrue(registered > 100, "karşılaştırılan paket sayısı şüpheli derecede az: " + registered);
    }

    @Test
    void helperKeepsTheV2192Behaviour() {
        assertInstanceOf(BedrockCodecHelper_v2192.class, Bedrock_v2193.CODEC.createHelper());
    }
}
