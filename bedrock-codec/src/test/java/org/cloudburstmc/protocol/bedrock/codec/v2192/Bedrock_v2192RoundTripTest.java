package org.cloudburstmc.protocol.bedrock.codec.v2192;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.cloudburstmc.math.vector.Vector2f;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v2169.Bedrock_v2169;
import org.cloudburstmc.protocol.bedrock.data.ControlScheme;
import org.cloudburstmc.protocol.bedrock.data.FurnaceOptions;
import org.cloudburstmc.protocol.bedrock.data.camera.CameraAudioListener;
import org.cloudburstmc.protocol.bedrock.data.camera.CameraPreset;
import org.cloudburstmc.protocol.bedrock.data.definitions.DimensionDefinition;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;
import org.cloudburstmc.protocol.bedrock.data.inventory.FullContainerName;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponse;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponseContainer;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponseSlot;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponseStatus;
import org.cloudburstmc.protocol.bedrock.data.primitiveshape.PrimitiveText;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.packet.BossEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.CameraPresetsPacket;
import org.cloudburstmc.protocol.bedrock.packet.DimensionDataPacket;
import org.cloudburstmc.protocol.bedrock.packet.ItemStackResponsePacket;
import org.cloudburstmc.protocol.bedrock.packet.PrimitiveShapesPacket;
import org.cloudburstmc.protocol.bedrock.packet.ServerboundDiagnosticsPacket;
import org.cloudburstmc.protocol.bedrock.packet.SetPlayerFurnaceOptionsPacket;
import org.cloudburstmc.protocol.bedrock.util.OptionalBoolean;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 1.26.50 (v2192) portunda elle uyarlanan serializer'ların gidiş-dönüş testi.
 * <p>
 * Tel biçimi upstream'den geliyor ve burada doğrulanamaz (elimizde 1.26.50 istemcisi yok). Doğrulanan
 * şey uyarlamanın kendisi: yazılan her alan aynı alana geri okunuyor mu ve paketin sonunda okunmamış
 * bayt kalıyor mu. {@link CameraPreset}'te aynı tipteki alanlar (radius, dönüş hızı, yaw sınırları)
 * kurucu sırası kayarsa derleyiciye takılmadan yer değiştirir; bu yüzden her alana farklı değer verilir.
 */
class Bedrock_v2192RoundTripTest {

    @Test
    void primitiveTextCarriesLineGapHeight() {
        var packet = new PrimitiveShapesPacket();
        packet.getShapes().add(new PrimitiveText(7L, 1, Vector3f.from(1, 2, 3), 1.5f, Vector3f.from(0, 90, 0), 4f,
                new Color(10, 20, 30, 40), "Merhaba", true, new Color(50, 60, 70, 80), 2.5f,
                true, false, true, 64f, 99L));

        var decoded = roundTrip(Bedrock_v2192.CODEC, packet);

        assertEquals(packet, decoded);
        assertEquals(2.5f, ((PrimitiveText) decoded.getShapes().getFirst()).getLineGapHeight());
    }

    @Test
    void primitiveTextStillRoundTripsWithoutLineGapHeightOnV2169() {
        var packet = new PrimitiveShapesPacket();
        packet.getShapes().add(new PrimitiveText(7L, 1, Vector3f.from(1, 2, 3), 1.5f, Vector3f.from(0, 90, 0), 4f,
                new Color(10, 20, 30, 40), "Merhaba", true, new Color(50, 60, 70, 80),
                true, false, true, 64f, 99L));

        assertEquals(packet, roundTrip(Bedrock_v2169.CODEC, packet));
    }

    @Test
    void cameraPresetKeepsEveryFieldInPlace() {
        var packet = new CameraPresetsPacket();
        packet.getPresets().add(distinctPreset()
                .applyInheritedStartingRotation(true)
                .startingRotation(Vector2f.from(27, 28))
                .build());

        assertEquals(packet, roundTrip(Bedrock_v2192.CODEC, packet));
    }

    @Test
    void cameraPresetKeepsEveryFieldInPlaceOnV2169() {
        var packet = new CameraPresetsPacket();
        packet.getPresets().add(distinctPreset().build());

        assertEquals(packet, roundTrip(Bedrock_v2169.CODEC, packet));
    }

    @Test
    void entityDiagnosticsCarryPositionAndDimension() {
        var entity = new ServerboundDiagnosticsPacket.EntityDiagnostics();
        entity.setDisplayName("Zombi");
        entity.setEntity("minecraft:zombie");
        entity.setTimeInNs(123_456_789L);
        entity.setPercentOfTotal(42);
        entity.setPosition(Vector3f.from(1.5f, 64f, -3.5f));
        entity.setDimension("overworld");
        var packet = new ServerboundDiagnosticsPacket();
        packet.setAvgFps(60f);
        packet.getEntityDiagnostics().add(entity);

        assertEquals(packet, roundTrip(Bedrock_v2192.CODEC, packet));
    }

    @Test
    void bossEventNoLongerCarriesPlayerId() {
        var packet = new BossEventPacket();
        packet.setBossUniqueEntityId(77L);
        packet.setAction(BossEventPacket.Action.UPDATE_NAME);
        packet.setPlayerUniqueEntityId(88L);
        packet.setTitle("Ejder");
        packet.setFilteredTitle("E***r");
        packet.setHealthPercentage(0.5f);
        packet.setColor(3);
        packet.setOverlay(2);

        var decoded = roundTrip(Bedrock_v2192.CODEC, packet);

        assertEquals(0L, decoded.getPlayerUniqueEntityId(), "v2192 oyuncu kimliğini taşımıyor");
        packet.setPlayerUniqueEntityId(0L);
        assertEquals(packet, decoded);
    }

    @Test
    void dimensionDefinitionCarriesDefaultBiome() {
        var packet = new DimensionDataPacket();
        packet.getDefinitions().add(new DimensionDefinition("gears:ada", 320, -64, 1, 0,
                UUID.fromString("5f0a8c3e-1d2b-4c5e-9f00-112233445566"), "minecraft:plains"));

        assertEquals(packet, roundTrip(Bedrock_v2192.CODEC, packet));
    }

    @Test
    void furnaceOptionsRoundTrip() {
        var packet = new SetPlayerFurnaceOptionsPacket();
        packet.setType(SetPlayerFurnaceOptionsPacket.FurnaceType.BLAST_FURNACE);
        packet.setOptions(new FurnaceOptions(FurnaceOptions.FurnaceLeftTabIndex.RECIPE_ITEMS, true,
                FurnaceOptions.FurnaceLayout.INVENTORY_ONLY));

        assertEquals(packet, roundTrip(Bedrock_v2192.CODEC, packet));
    }

    /**
     * Boş süzülmüş ad "yok" olarak yazılır (fork sapması, bkz. {@code BedrockCodecHelper_v2168#writeItemEntry}).
     * <p>
     * Ağ kimliği 0 verilir: v2168 okuyucusu bu alan için çift boolean bekliyor, yazıcısı tek boolean
     * yazıyor. Upstream'den gelen ve sunucunun hiç kullanmadığı okuma yolu bu testin konusu değil.
     */
    @Test
    void emptyFilteredCustomNameIsWrittenAsAbsent() {
        for (BedrockCodec codec : List.of(Bedrock_v2169.CODEC, Bedrock_v2192.CODEC)) {
            var packet = new ItemStackResponsePacket();
            packet.getEntries().add(new ItemStackResponse(ItemStackResponseStatus.OK, 9, List.of(
                    new ItemStackResponseContainer(ContainerSlotType.HOTBAR_AND_INVENTORY, List.of(
                            new ItemStackResponseSlot(0, 0, 1, 0, "Kılıç", 0, ""),
                            new ItemStackResponseSlot(1, 1, 2, 0, "Kötü ad", 3, "*** ad")),
                            new FullContainerName(ContainerSlotType.HOTBAR_AND_INVENTORY, null)))));

            var slots = roundTrip(codec, packet).getEntries().getFirst().containers().getFirst().items();

            assertNull(slots.get(0).getFilteredCustomName(), () -> codec.getMinecraftVersion() + ": boş ad yok yazılmalı");
            assertEquals("Kılıç", slots.get(0).getCustomName());
            assertEquals("*** ad", slots.get(1).getFilteredCustomName());
            assertEquals(3, slots.get(1).getDurabilityCorrection());
        }
    }

    private static CameraPreset.CameraPresetBuilder distinctPreset() {
        return CameraPreset.builder()
                .identifier("gears:kamera")
                .parentPreset("minecraft:free")
                .pos(Vector3f.from(1, 2, 3))
                .yaw(11f)
                .pitch(12f)
                .listener(CameraAudioListener.CAMERA)
                .playEffect(OptionalBoolean.of(true))
                .viewOffset(Vector2f.from(13, 14))
                .radius(15f)
                .rotationSpeed(16f)
                .snapToTarget(OptionalBoolean.of(false))
                .entityOffset(Vector3f.from(17, 18, 19))
                .horizontalRotationLimit(Vector2f.from(20, 21))
                .verticalRotationLimit(Vector2f.from(22, 23))
                .continueTargeting(OptionalBoolean.of(true))
                .blockListeningRadius(24f)
                .minYawLimit(25f)
                .maxYawLimit(26f)
                .controlScheme(ControlScheme.CAMERA_RELATIVE);
    }

    @SuppressWarnings("unchecked")
    private static <T extends BedrockPacket> T roundTrip(BedrockCodec codec, T packet) {
        BedrockCodecHelper helper = codec.createHelper();
        ByteBuf buf = Unpooled.buffer();
        try {
            codec.tryEncode(helper, buf, packet);
            T decoded = (T) codec.tryDecode(helper, buf, codec.getPacketDefinition(packet.getClass()).id());
            assertFalse(buf.isReadable(), () -> packet.getClass().getSimpleName() + " sonunda "
                    + buf.readableBytes() + " okunmamış bayt kaldı (" + codec.getMinecraftVersion() + ")");
            return decoded;
        } catch (Exception e) {
            throw new AssertionError(packet.getClass().getSimpleName() + " kodlanamadı (" + codec.getMinecraftVersion() + ")", e);
        } finally {
            buf.release();
        }
    }
}
