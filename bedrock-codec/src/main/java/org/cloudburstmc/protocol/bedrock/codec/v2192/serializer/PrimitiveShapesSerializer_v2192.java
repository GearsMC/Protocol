package org.cloudburstmc.protocol.bedrock.codec.v2192.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v1001.serializer.PrimitiveShapesSerializer_v1001;
import org.cloudburstmc.protocol.bedrock.data.primitiveshape.PrimitiveText;

import java.awt.Color;

/**
 * Upstream {@code DebugDrawerSerializer_v2192} karşılığı. Fork'ta DebugDrawer/DebugShape API'si
 * PrimitiveShapes/PrimitiveShape adını taşıdığı için aynı değişiklik burada: metin şekline arka plan
 * renginden sonra {@code lineGapHeight} (float) eklendi.
 */
public class PrimitiveShapesSerializer_v2192 extends PrimitiveShapesSerializer_v1001 {

    public static final PrimitiveShapesSerializer_v2192 INSTANCE = new PrimitiveShapesSerializer_v2192();

    @Override
    protected void writeText(ByteBuf buffer, BedrockCodecHelper helper, PrimitiveText text) {
        helper.writeString(buffer, text.getText());
        buffer.writeBoolean(text.isUseRotation());
        helper.writeOptionalNull(buffer, text.getBackgroundColor(), (buf, color) -> buf.writeIntLE(color.getRGB()));
        buffer.writeFloatLE(text.getLineGapHeight());
        buffer.writeBoolean(text.isDepthTest());
        buffer.writeBoolean(text.isShowBackface());
        buffer.writeBoolean(text.isShowTextBackface());
    }

    @Override
    protected PrimitiveText readText(ByteBuf buffer, BedrockCodecHelper helper, long id, int dimension,
                                     Vector3f position, Float scale, Vector3f rotation, Float totalTimeLeft,
                                     Color color, Float maximumRenderDistance, Long attachedToEntityId) {
        String value = helper.readString(buffer);
        boolean useRotation = buffer.readBoolean();
        Color backgroundColor = helper.readOptional(buffer, null, buf -> new Color(buf.readIntLE(), true));
        float lineGapHeight = buffer.readFloatLE();
        boolean depthTest = buffer.readBoolean();
        boolean showBackface = buffer.readBoolean();
        boolean showTextBackface = buffer.readBoolean();
        return new PrimitiveText(id, dimension, position, scale, rotation, totalTimeLeft, color, value, useRotation,
                backgroundColor, lineGapHeight, depthTest, showBackface, showTextBackface, maximumRenderDistance,
                attachedToEntityId);
    }
}
