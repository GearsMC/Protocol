package org.cloudburstmc.protocol.bedrock.data.skin;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.*;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.json.internal.json_simple.JSONValue;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

import static org.cloudburstmc.protocol.bedrock.util.Preconditions.checkArgument;

/**
 * The serialised form of a player skin as sent in packets such as
 * {@link org.cloudburstmc.protocol.bedrock.packet.PlayerListPacket} and
 * {@link org.cloudburstmc.protocol.bedrock.packet.PlayerSkinPacket}.
 */
@Getter
@ToString(exclude = {"geometryData"})
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true, builderClassName = "Builder")
public class SerializedSkin {
    private static final int PIXEL_SIZE = 4;

    public static final int SINGLE_SKIN_SIZE = 64 * 32 * PIXEL_SIZE;
    public static final int DOUBLE_SKIN_SIZE = 64 * 64 * PIXEL_SIZE;
    public static final int SKIN_128_64_SIZE = 128 * 64 * PIXEL_SIZE;
    public static final int SKIN_128_128_SIZE = 128 * 128 * PIXEL_SIZE;

<<<<<<< ours
    /**
     * A unique identifier for the skin.
     */
    private final String skinId;
=======
    private static final Color DEFAULT_COLOR = new Color(0, true);

    private String skinId;
>>>>>>> theirs
    /**
     * The legacy geometry name used by older protocol versions.
     */
<<<<<<< ours
    private final String geometryName;
    /**
     * The RGBA skin image data.
     */
    private final ImageData skinData;
    /**
     * The RGBA cape image data.
     */
    private final ImageData capeData;
    /**
     * The JSON geometry payload describing bones, UVs, pivots and related model data.
     */
    private final String geometryData;
=======
    @Builder.Default
    private String playFabId = "";
    private String geometryName;
    private String skinResourcePatch;
    private ImageData skinData;
    @Builder.Default
    private List<AnimationData> animations = Collections.emptyList();
    @Builder.Default
    private ImageData capeData = ImageData.EMPTY;
    private String geometryData;
>>>>>>> theirs
    /**
     * Whether this skin is a premium marketplace skin.
     */
<<<<<<< ours
    private final boolean premium;
    /**
     * The JSON resource patch that points the client to the skin geometry to use.
     *
     * @since v388
     */
    private final String skinResourcePatch;
    /**
     * The animations.
     *
     * @since v388
     */
    private final List<AnimationData> animations;
    /**
     * Additional animation metadata payload.
     *
     * @since v388
     */
    private final String animationData;
    /**
     * Whether this skin was created with the in-game persona creator.
     *
     * @since v388
     */
    private final boolean persona;
    /**
     * Whether a persona cape is applied on a classic skin.
     *
     * @since v388
     */
    private final boolean capeOnClassic;
=======
    @Builder.Default
    private String geometryDataEngineVersion = "0.0.0";
    @Builder.Default
    private String animationData = "";
    private boolean premium;
    private boolean persona;
    private boolean capeOnClassic;
>>>>>>> theirs
    /**
     * The cape ID.
     *
     * @since v388
     */
<<<<<<< ours
    private final String capeId;
    /**
     * A full identifier for the combined skin and cape.
     *
     * @since v388
     */
    private final String fullSkinId;
    /**
     * The arm width variant, typically {@code wide} or {@code slim}.
     *
     * @since v390
     */
    private final String armSize;
    /**
     * The base skin colour in hex notation.
     *
     * @since v390
     */
    private final String skinColor;
    /**
     * The persona pieces.
     *
     * @since v390
     */
    private final List<PersonaPieceData> personaPieces;
    /**
     * The tint colors.
     *
     * @since v390
     */
    private final List<PersonaPieceTintData> tintColors;
    /**
     * The PlayFab identifier associated with the skin.
     *
     * @since v428
     */
    private final String playFabId;
    /**
     * The engine version associated with the geometry data.
     *
     * @since v465
     */
    private final String geometryDataEngineVersion;
    /**
     * Whether this skin belongs to the primary local user.
     *
     * @since v465
     */
    private final boolean primaryUser;
    /**
     * Whether this skin should override the player's locally equipped appearance.
     *
     * @since v568
     */
    private final boolean overridingPlayerAppearance;
=======
    private boolean primaryUser;
    @Builder.Default
    private String capeId = "";
    private String fullSkinId;
    @Builder.Default
    private String armSize = "wide";
    /**
     * @deprecated since v2168
     */
    private String skinColor;
    /**
     * @since v2168
     */
    @Builder.Default
    private Color color = DEFAULT_COLOR;
    @Builder.Default
    private List<PersonaPieceData> personaPieces = Collections.emptyList();
    @Builder.Default
    private List<PersonaPieceTintData> tintColors = Collections.emptyList();
    private boolean overridingPlayerAppearance;
    /**
     * @since v2168
     */
    @Builder.Default
    private boolean trusted = true;
    /**
     * @since v2168
     */
    @Builder.Default
    private String profileHash = "";
>>>>>>> theirs

    public static SerializedSkin of(String skinId, String playFabId, ImageData skinData, ImageData capeData, String geometryName,
                                    String geometryData, boolean premiumSkin) {
        skinData.checkLegacySkinSize();
        capeData.checkLegacyCapeSize();

<<<<<<< ours
        return new SerializedSkin(skinId, geometryName, skinData, capeData, geometryData, premiumSkin, null,
                Collections.emptyList(), "", false, false, "", "", "wide", "#0",
                Collections.emptyList(), Collections.emptyList(), playFabId, "", true, true);
=======
        return new SerializedSkin(skinId, playFabId, geometryName, null, skinData, Collections.emptyList(), capeData,
                geometryData, "0.0.0", "", premiumSkin, false, false, true, "", "",
                "wide", null, DEFAULT_COLOR, Collections.emptyList(), Collections.emptyList(), true, true, "");
>>>>>>> theirs
    }

    public static SerializedSkin of(String skinId, String playFabId, String skinResourcePatch, ImageData skinData,
                                    List<AnimationData> animations, ImageData capeData, String geometryData,
                                    String animationData, boolean premium, boolean persona, boolean capeOnClassic,
                                    String capeId, String fullSkinId) {
        return of(skinId, playFabId, skinResourcePatch, skinData, Collections.unmodifiableList(new ObjectArrayList<>(animations)),
                capeData, geometryData, animationData, premium, persona, capeOnClassic, capeId, fullSkinId,
                "wide", "#0", Collections.emptyList(), Collections.emptyList());
    }

    public static SerializedSkin of(String skinId, String playFabId, String skinResourcePatch, ImageData skinData,
                                    List<AnimationData> animations, ImageData capeData, String geometryData,
                                    String animationData, boolean premium, boolean persona, boolean capeOnClassic,
                                    String capeId, String fullSkinId, String armSize, String skinColor,
                                    List<PersonaPieceData> personaPieces, List<PersonaPieceTintData> tintColors) {
        return of(skinId, playFabId, skinResourcePatch, skinData, animations, capeData, geometryData, animationData, premium, persona, capeOnClassic, true, capeId, fullSkinId, armSize, skinColor, personaPieces, tintColors);
    }

    public static SerializedSkin of(String skinId, String playFabId, String skinResourcePatch, ImageData skinData,
                                    List<AnimationData> animations, ImageData capeData, String geometryData,
                                    String animationData, boolean premium, boolean persona, boolean capeOnClassic,
                                    boolean primaryUser, String capeId, String fullSkinId, String armSize,
                                    String skinColor, List<PersonaPieceData> personaPieces,
                                    List<PersonaPieceTintData> tintColors) {
<<<<<<< ours
        return new SerializedSkin(skinId, null, skinData, capeData, geometryData, premium, skinResourcePatch,
                Collections.unmodifiableList(new ObjectArrayList<>(animations)), animationData, persona, capeOnClassic,
                capeId, fullSkinId, armSize, skinColor, personaPieces, tintColors, playFabId, "", primaryUser, true);
=======
        return new SerializedSkin(skinId, playFabId, null, skinResourcePatch, skinData,
                Collections.unmodifiableList(new ObjectArrayList<>(animations)), capeData, geometryData, "0.0.0", animationData,
                premium, persona, capeOnClassic, primaryUser, capeId, fullSkinId, armSize, skinColor, null, personaPieces, tintColors, true, true, "");
>>>>>>> theirs
    }

    public static SerializedSkin of(String skinId, String playFabId, String skinResourcePatch, ImageData skinData,
                                    List<AnimationData> animations, ImageData capeData, String geometryData,
                                    String geometryDataEngineVersion, String animationData, boolean premium,
                                    boolean persona, boolean capeOnClassic, boolean primaryUser, String capeId,
                                    String fullSkinId, String armSize, String skinColor, List<PersonaPieceData> personaPieces,
                                    List<PersonaPieceTintData> tintColors) {

<<<<<<< ours
        return new SerializedSkin(skinId, null, skinData, capeData, geometryData, premium, skinResourcePatch,
                Collections.unmodifiableList(new ObjectArrayList<>(animations)), animationData, persona, capeOnClassic,
                capeId, fullSkinId, armSize, skinColor, personaPieces, tintColors, playFabId,
                geometryDataEngineVersion, primaryUser, true);
=======
        return new SerializedSkin(skinId, playFabId, null, skinResourcePatch, skinData,
                Collections.unmodifiableList(new ObjectArrayList<>(animations)), capeData, geometryData, geometryDataEngineVersion, animationData,
                premium, persona, capeOnClassic, primaryUser, capeId, fullSkinId, armSize, skinColor, null, personaPieces, tintColors, true, true, "");
>>>>>>> theirs
    }

    public static SerializedSkin of(String skinId, String playFabId, String skinResourcePatch, ImageData skinData,
                                    List<AnimationData> animations, ImageData capeData, String geometryData,
                                    String geometryDataEngineVersion, String animationData, boolean premium,
                                    boolean persona, boolean capeOnClassic, boolean primaryUser, String capeId,
                                    String fullSkinId, String armSize, String skinColor, List<PersonaPieceData> personaPieces,
                                    List<PersonaPieceTintData> tintColors, boolean overridingPlayerAppearance) {

<<<<<<< ours
        return new SerializedSkin(skinId, null, skinData, capeData, geometryData, premium, skinResourcePatch,
                Collections.unmodifiableList(new ObjectArrayList<>(animations)), animationData, persona, capeOnClassic,
                capeId, fullSkinId, armSize, skinColor, personaPieces, tintColors, playFabId,
                geometryDataEngineVersion, primaryUser, overridingPlayerAppearance);
=======
        return new SerializedSkin(skinId, playFabId, null, skinResourcePatch, skinData,
                Collections.unmodifiableList(new ObjectArrayList<>(animations)), capeData, geometryData, geometryDataEngineVersion, animationData,
                premium, persona, capeOnClassic, primaryUser, capeId, fullSkinId, armSize, skinColor, null, personaPieces, tintColors, overridingPlayerAppearance, true, "");
>>>>>>> theirs
    }

    public static SerializedSkin of(String skinId, String playFabId, String skinResourcePatch, ImageData skinData,
                                    List<AnimationData> animations, ImageData capeData, String geometryData,
                                    String geometryDataEngineVersion, String animationData, boolean premium,
                                    boolean persona, boolean capeOnClassic, boolean primaryUser, String capeId,
                                    String fullSkinId, String armSize, Color color, List<PersonaPieceData> personaPieces,
                                    List<PersonaPieceTintData> tintColors, boolean overridingPlayerAppearance, boolean trusted, String profileHash) {

        return new SerializedSkin(skinId, playFabId, null, skinResourcePatch, skinData,
                Collections.unmodifiableList(new ObjectArrayList<>(animations)), capeData, geometryData, geometryDataEngineVersion, animationData,
                premium, persona, capeOnClassic, primaryUser, capeId, fullSkinId, armSize, null, color, personaPieces, tintColors, overridingPlayerAppearance, trusted, profileHash);
    }

    public boolean isValid() {
        return isValidSkin() && isValidResourcePatch();
    }

    private boolean isValidSkin() {
        return skinId != null && !skinId.trim().isEmpty() &&
               skinData != null && skinData.getWidth() >= 64 && skinData.getHeight() >= 32 &&
               skinData.getImage().length >= SINGLE_SKIN_SIZE;
    }

    public String getSkinResourcePatch() {
        if (skinResourcePatch == null && geometryName != null) {
            return convertLegacyGeometryName(geometryName);
        }
        return skinResourcePatch;
    }

    public String getGeometryName() {
        if (geometryName == null && skinResourcePatch != null) {
            return convertSkinPatchToLegacy(skinResourcePatch);
        }
        return geometryName;
    }

    private static String convertLegacyGeometryName(String geometryName) {
        return "{\"geometry\" : {\"default\" : \"" + JSONValue.escape(geometryName) + "\"}}";
    }

    private static String convertSkinPatchToLegacy(String skinResourcePatch) {
        checkArgument(validateSkinResourcePatch(skinResourcePatch), "Invalid skin resource patch");
        JSONObject object = (JSONObject) JSONValue.parse(skinResourcePatch);
        JSONObject geometry = (JSONObject) object.get("geometry");
        return (String) geometry.get("default");
    }

    private boolean isValidResourcePatch() {
        return skinResourcePatch != null && validateSkinResourcePatch(skinResourcePatch);
    }

    private static boolean validateSkinResourcePatch(String skinResourcePatch) {
        try {
            JSONObject object = (JSONObject) JSONValue.parse(skinResourcePatch);
            JSONObject geometry = (JSONObject) object.get("geometry");
            return geometry.containsKey("default") && geometry.get("default") instanceof String;
        } catch (ClassCastException | NullPointerException e) {
            return false;
        }
    }

    /**
     * @deprecated since v2168, use color
     */
    public String getSkinColor() {
        if ((skinColor == null || skinColor.isEmpty()) && color != null) {
            if (color.getAlpha() == 0) {
                skinColor = "#0";
            } else {
                skinColor = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
            }
        }
        return skinColor;
    }

    /**
     * @since v2168
     */
    public Color getColor() {
        if ((color == null || color == DEFAULT_COLOR) && skinColor != null && !skinColor.isEmpty()) {
            if (skinColor.equals("#0")) {
                color = new Color(0, true);
            } else {
                color = new Color((int) Long.parseLong(skinColor.startsWith("#") ? skinColor.substring(1) : skinColor, 16), true);
            }
        }
        return color;
    }

<<<<<<< ours
        public String toString() {
            return "SerializedSkin.Builder(skinId=" + this.skinId +
                   ", playFabId=" + this.playFabId +
                   ", geometryName=" + this.geometryName +
                   ", skinResourcePatch=" + this.skinResourcePatch +
                   ", skinData=" + this.skinData +
                   ", animations=" + this.animations +
                   ", capeData=" + this.capeData +
                   ", geometryData=" + this.geometryData +
                   ", animationData=" + this.animationData +
                   ", premium=" + this.premium +
                   ", persona=" + this.persona +
                   ", capeOnClassic=" + this.capeOnClassic +
                   ", capeId=" + this.capeId +
                   ", fullSkinId=" + this.fullSkinId +
                   ", armSize=" + this.armSize +
                   ", skinColor=" + this.skinColor +
                   ", personaPieces=" + this.personaPieces +
                   ", tintColors=" + this.tintColors +
                   ", geometryDataEngineVersion=" + this.geometryDataEngineVersion +
                   ", primaryUser=" + this.primaryUser +
                   ", overridingPlayerAppearance=" + this.overridingPlayerAppearance +
                   ")";
=======
    public String getFullSkinId() {
        if (fullSkinId == null) {
            fullSkinId = skinId + capeId;
>>>>>>> theirs
        }
        return fullSkinId;
    }
}
