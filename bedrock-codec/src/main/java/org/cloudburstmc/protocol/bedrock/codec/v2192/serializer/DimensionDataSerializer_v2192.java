package org.cloudburstmc.protocol.bedrock.codec.v2192.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v2168.serializer.DimensionDataSerializer_v2168;
import org.cloudburstmc.protocol.bedrock.data.definitions.DimensionDefinition;
import org.cloudburstmc.protocol.bedrock.util.VarInts;

import java.util.UUID;

/**
 * v2192: boyut tanımının ilk iki alanı Mojang şemasında "Minimum Y" ve "Height Range" oldu
 * (1.26.45'te "Height Maximum" ve "Height Minimum"), sonuna "Default Biome" eklendi.
 * <p>
 * Fork sapması: upstream bu serializer'a yalnızca {@code defaultBiome} ekleyip ilk iki alanı eski
 * anlamıyla (en yüksek, en alçak) yazmaya devam ediyor. {@link DimensionDefinition} sınırları hâlâ en
 * yüksek/en alçak olarak taşıdığı için burada en alçak Y ve aralık
 * ({@code maximumHeight - minimumHeight}) olarak çevrilir.
 */
public class DimensionDataSerializer_v2192 extends DimensionDataSerializer_v2168 {

    public static final DimensionDataSerializer_v2192 INSTANCE = new DimensionDataSerializer_v2192();

    @Override
    protected void writeDefinition(ByteBuf buffer, BedrockCodecHelper helper, DimensionDefinition definition) {
        helper.writeString(buffer, definition.id());
        VarInts.writeInt(buffer, definition.minimumHeight());
        VarInts.writeInt(buffer, definition.maximumHeight() - definition.minimumHeight());
        VarInts.writeInt(buffer, definition.generatorType());
        VarInts.writeInt(buffer, definition.dimensionType());
        helper.writeUuid(buffer, definition.packId());
        helper.writeString(buffer, definition.defaultBiome());
    }

    @Override
    protected DimensionDefinition readDefinition(ByteBuf buffer, BedrockCodecHelper helper) {
        String id = helper.readString(buffer);
        int minimumY = VarInts.readInt(buffer);
        int heightRange = VarInts.readInt(buffer);
        int generatorType = VarInts.readInt(buffer);
        int dimensionType = VarInts.readInt(buffer);
        UUID packId = helper.readUuid(buffer);
        String defaultBiome = helper.readString(buffer);
        return new DimensionDefinition(id, minimumY + heightRange, minimumY, generatorType, dimensionType, packId, defaultBiome);
    }
}
