package org.cloudburstmc.protocol.bedrock.codec.v2225;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v2193.Bedrock_v2193;
import org.cloudburstmc.protocol.bedrock.data.PassengerOfBlockArguments;
import org.cloudburstmc.protocol.bedrock.data.definitions.DimensionDefinition;
import org.cloudburstmc.protocol.bedrock.data.diagnostics.SystemCategory;
import org.cloudburstmc.protocol.bedrock.data.inventory.HandSlot;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.data.inventory.transaction.InventoryTransactionType;
import org.cloudburstmc.protocol.bedrock.data.sound.FadeSoundData;
import org.cloudburstmc.protocol.bedrock.data.sound.StopSoundData;
import org.cloudburstmc.protocol.bedrock.packet.*;
import org.cloudburstmc.protocol.bedrock.util.VarInts;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 1.26.60 (v2225) portunda fork düzenine uyarlanan serializer'ların testi.
 * <p>
 * Tel biçimi CloudburstMC/Protocol 3.0'dan geliyor; burada doğrulanan uyarlamanın kendisi: yazılan her alan aynı
 * alana geri okunuyor mu, paketin sonunda okunmamış bayt kalıyor mu ve yeni paketler doğru kimlikte mi.
 */
class Bedrock_v2225Test {

    @Test
    void codecAdvertisesTheReleaseProtocol() {
        assertEquals(2225, Bedrock_v2225.CODEC.getProtocolVersion());
        assertEquals("1.26.60", Bedrock_v2225.CODEC.getMinecraftVersion());
        assertInstanceOf(BedrockCodecHelper_v2225.class, Bedrock_v2225.CODEC.createHelper());
    }

    @Test
    void newPacketsUseTheirMojangIds() {
        assertEquals(353, idOf(ClientboundMatchmakingStatePacket.class));
        assertEquals(354, idOf(ServerboundStonecutterSetRecipePacket.class));
        assertEquals(355, idOf(ClientboundStonecutterSetRecipePacket.class));
        assertEquals(356, idOf(ServerboundMatchmakingCancelPacket.class));
        assertEquals(357, idOf(SetPassengerOfBlockPacket.class));
        assertEquals(358, idOf(ServerboundCursorItemDragPacket.class));
        assertEquals(359, idOf(ClientboundPlayAudioContentPacket.class));
        assertEquals(360, idOf(ServerboundRegisterAudioContentPacket.class));
        assertNull(Bedrock_v2193.CODEC.getPacketDefinition(353), "1.26.50 istemcisi bu paketleri tanımıyor");
    }

    @Test
    void dimensionDefinitionCarriesClouds() {
        var packet = new DimensionDataPacket();
        packet.getDefinitions().add(new DimensionDefinition("gears:ada", 320, -64, 1, 0,
                UUID.fromString("5f0a8c3e-1d2b-4c5e-9f00-112233445566"), "minecraft:plains", 192, true));

        assertEquals(packet, roundTrip(Bedrock_v2225.CODEC, packet));
    }

    /**
     * v2225 tanımı v2192'nin "en alçak Y, yükseklik aralığı" sırasını korur ve bulut alanlarını sona ekler.
     */
    @Test
    void dimensionDefinitionKeepsMinimumYThenHeightRange() throws Exception {
        var packet = new DimensionDataPacket();
        packet.getDefinitions().add(new DimensionDefinition("gears:ada", 320, -64, 1, 0,
                UUID.fromString("5f0a8c3e-1d2b-4c5e-9f00-112233445566"), "minecraft:plains", 192, true));
        var helper = Bedrock_v2225.CODEC.createHelper();
        ByteBuf buf = Unpooled.buffer();
        try {
            Bedrock_v2225.CODEC.tryEncode(helper, buf, packet);
            assertEquals(1, VarInts.readUnsignedInt(buf));
            assertEquals("gears:ada", helper.readString(buf));
            assertEquals(-64, VarInts.readInt(buf), "ilk alan en alçak Y olmalı");
            assertEquals(384, VarInts.readInt(buf), "ikinci alan yükseklik aralığı olmalı");
        } finally {
            buf.release();
        }
    }

    @Test
    void animateCarriesHand() {
        var packet = new AnimatePacket();
        packet.setAction(AnimatePacket.Action.SWING_ARM);
        packet.setRuntimeEntityId(5L);
        packet.setHand(HandSlot.OFFHAND);

        var decoded = roundTrip(Bedrock_v2225.CODEC, packet);

        assertEquals(packet, decoded);
        assertEquals(HandSlot.OFFHAND, decoded.getHand());
    }

    @Test
    void itemUseOnEntityCarriesHand() {
        var packet = new InventoryTransactionPacket();
        packet.setTransactionType(InventoryTransactionType.ITEM_USE_ON_ENTITY);
        packet.setRuntimeEntityId(9L);
        packet.setActionType(1);
        packet.setHotbarSlot(4);
        packet.setHand(HandSlot.OFFHAND);
        packet.setItemInHand(ItemData.AIR);
        packet.setPlayerPosition(Vector3f.from(1, 65, 2));
        packet.setClickPosition(Vector3f.from(0.5f, 0.5f, 0.5f));

        var decoded = roundTrip(Bedrock_v2225.CODEC, packet);

        assertEquals(packet, decoded);
        assertEquals(HandSlot.OFFHAND, decoded.getHand());
    }

    @Test
    void itemReleaseCarriesHand() {
        var packet = new InventoryTransactionPacket();
        packet.setTransactionType(InventoryTransactionType.ITEM_RELEASE);
        packet.setActionType(0);
        packet.setHotbarSlot(2);
        packet.setItemInHand(ItemData.AIR);
        packet.setHeadPosition(Vector3f.from(3, 66, 4));
        packet.setHand(HandSlot.OFFHAND);

        var decoded = roundTrip(Bedrock_v2225.CODEC, packet);

        assertEquals(packet, decoded);
        assertEquals(HandSlot.OFFHAND, decoded.getHand());
    }

    /**
     * Yazıcı önce süreyi, sonra hedef sesi yazar; okuyucu da aynı sırayla okumalı. Farklı değerler sıranın kaymasını
     * yakalar.
     */
    @Test
    void soundDataFadeKeepsFieldOrder() {
        var packet = new ClientboundUpdateSoundDataPacket();
        packet.setServerSoundHandle(42L);
        packet.setEvent(new FadeSoundData(0.25f, 3f));

        var decoded = roundTrip(Bedrock_v2225.CODEC, packet);

        assertEquals(new FadeSoundData(0.25f, 3f), decoded.getEvent());
    }

    @Test
    void soundDataStopRoundTrips() {
        var packet = new ClientboundUpdateSoundDataPacket();
        packet.setServerSoundHandle(7L);
        packet.setEvent(new StopSoundData());

        assertEquals(packet, roundTrip(Bedrock_v2225.CODEC, packet));
    }

    @Test
    void passengerOfBlockRoundTrips() {
        var packet = new SetPassengerOfBlockPacket();
        packet.setEntityUniqueId(-12L);
        packet.setData(new PassengerOfBlockArguments(Vector3i.from(1, 2, 3), Vector3f.from(0.5f, 0.25f, 0.75f),
                90f, 45f, PassengerOfBlockArguments.EmoteType.LAYING));

        assertEquals(packet, roundTrip(Bedrock_v2225.CODEC, packet));

        packet.setData(null);
        assertEquals(packet, roundTrip(Bedrock_v2225.CODEC, packet));
    }

    @Test
    void entityDiagnosticsPositionAndDimensionAreOptional() {
        var withLocation = new ServerboundDiagnosticsPacket.EntityDiagnostics();
        withLocation.setDisplayName("Zombi");
        withLocation.setEntity("minecraft:zombie");
        withLocation.setTimeInNs(123_456_789L);
        withLocation.setPercentOfTotal(42);
        withLocation.setPosition(Vector3f.from(1.5f, 64f, -3.5f));
        withLocation.setDimension("overworld");
        var withoutLocation = new ServerboundDiagnosticsPacket.EntityDiagnostics();
        withoutLocation.setDisplayName("Koyun");
        withoutLocation.setEntity("minecraft:sheep");
        withoutLocation.setTimeInNs(7L);
        withoutLocation.setPercentOfTotal(1);
        var packet = new ServerboundDiagnosticsPacket();
        packet.setAvgFps(60f);
        packet.getEntityDiagnostics().add(withLocation);
        packet.getEntityDiagnostics().add(withoutLocation);
        packet.getSystemCategories().add(new SystemCategory("render", 3L));

        assertEquals(packet, roundTrip(Bedrock_v2225.CODEC, packet));
    }

    @Test
    void emptySystemCategoriesRoundTrip() {
        var packet = new ServerboundDiagnosticsPacket();
        packet.setAvgFps(30f);

        assertEquals(packet, roundTrip(Bedrock_v2225.CODEC, packet));
    }

    private static int idOf(Class<? extends BedrockPacket> type) {
        return Bedrock_v2225.CODEC.getPacketDefinition(type).id();
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
