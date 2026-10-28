package org.cloudburstmc.protocol.bedrock.codec.v2225.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v2192.serializer.ServerboundDiagnosticsSerializer_v2192;
import org.cloudburstmc.protocol.bedrock.data.MemoryCategoryCounter;
import org.cloudburstmc.protocol.bedrock.data.diagnostics.SystemCategory;
import org.cloudburstmc.protocol.bedrock.packet.ServerboundDiagnosticsPacket;
import org.cloudburstmc.protocol.bedrock.util.TypeMap;

import java.util.List;

/**
 * v2225: varlık tanı kaydındaki konum ve boyut isteğe bağlı oldu, sistem kategorileri listesinin önüne varlık
 * bayrağı geldi (Mojang protokol belgeleri).
 * <p>
 * Upstream bu kaydı {@code EntityDiagnosticTimingInfo} ile taşıyor; fork'ta paketin kendi
 * {@link ServerboundDiagnosticsPacket.EntityDiagnostics} sınıfı kullanıldığı için kancalar onun üzerinden.
 */
public class ServerboundDiagnosticsSerializer_v2225 extends ServerboundDiagnosticsSerializer_v2192 {

    public ServerboundDiagnosticsSerializer_v2225(TypeMap<MemoryCategoryCounter.Category> memoryCategoryTypes) {
        super(memoryCategoryTypes);
    }

    @Override
    protected void writeEntityDiagnostics(ByteBuf buffer, BedrockCodecHelper helper,
                                          ServerboundDiagnosticsPacket.EntityDiagnostics diagnostics) {
        helper.writeString(buffer, diagnostics.getDisplayName());
        helper.writeString(buffer, diagnostics.getEntity());
        buffer.writeLongLE(diagnostics.getTimeInNs());
        buffer.writeByte(diagnostics.getPercentOfTotal());
        helper.writeOptionalNull(buffer, diagnostics.getPosition(), helper::writeVector3f);
        helper.writeOptionalNull(buffer, diagnostics.getDimension(), helper::writeString);
    }

    @Override
    protected ServerboundDiagnosticsPacket.EntityDiagnostics readEntityDiagnostics(ByteBuf buffer, BedrockCodecHelper helper) {
        ServerboundDiagnosticsPacket.EntityDiagnostics diagnostics = new ServerboundDiagnosticsPacket.EntityDiagnostics();
        diagnostics.setDisplayName(helper.readString(buffer));
        diagnostics.setEntity(helper.readString(buffer));
        diagnostics.setTimeInNs(buffer.readLongLE());
        diagnostics.setPercentOfTotal(buffer.readUnsignedByte());
        diagnostics.setPosition(helper.readOptional(buffer, null, helper::readVector3f));
        diagnostics.setDimension(helper.readOptional(buffer, null, helper::readString));
        return diagnostics;
    }

    @Override
    protected void writeSystemCategories(ByteBuf buffer, BedrockCodecHelper helper, List<SystemCategory> categories) {
        boolean present = !categories.isEmpty();
        buffer.writeBoolean(present);
        if (present) {
            super.writeSystemCategories(buffer, helper, categories);
        }
    }

    @Override
    protected void readSystemCategories(ByteBuf buffer, BedrockCodecHelper helper, List<SystemCategory> categories) {
        if (buffer.readBoolean()) {
            super.readSystemCategories(buffer, helper, categories);
        }
    }
}
