package org.cloudburstmc.protocol.bedrock.data.skin;

<<<<<<< ours
import java.util.List;

/**
 * PersonaPieceTintColour describes the tint colours of a specific piece of a persona skin.
 *
 * @param type   The type.
 * @param colors An array of four colours written in hex notation (note, that unlike the SkinColor field in
 *               the ClientData struct, this is actually ARGB, not just RGB). The colours refer to different
 *               parts of the skin piece. The 'persona_eyes' may have the following colours:
 *               ["#ffa12722","#ff2f1f0f","#ff3aafd9","#0"] The first hex colour represents the tint colour of
 *               the iris, the second hex colour represents the eyebrows and the third represents the sclera.
 *               The fourth is #0 because there are only 3 parts of the persona_eyes skin piece.
 */
public record PersonaPieceTintData(String type, List<String> colors) {
=======
import lombok.Data;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

@Data
public class PersonaPieceTintData {

    PersonaPieceType pieceType;
    /**
     * @deprecated since v2168, use colorsNew
     */
    List<String> colors;
    /**
     * @since v2168
     */
    List<Color> colorsNew;

    public PersonaPieceTintData(String type, List<String> colors) {
        this.pieceType = PersonaPieceType.fromName(type);
        this.colors = colors;
    }

    public PersonaPieceTintData(PersonaPieceType type, List<Color> colorsNew) {
        this.pieceType = type;
        this.colorsNew = colorsNew;
    }

    public String getType() {
        return pieceType.getSerializeName();
    }

    /**
     * @deprecated since v2168, use getColorsNew
     */
    public List<String> getColors() {
        if ((colors == null || colors.isEmpty()) && colorsNew != null && !colorsNew.isEmpty()) {
            colors = new ArrayList<>(colorsNew.size());
            for (Color c : colorsNew) {
                if (c.getAlpha() == 0) {
                    colors.add("#0");
                } else {
                    colors.add(String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue()));
                }
            }
        }
        return colors;
    }

    /**
     * @since v2168
     */
    public List<Color> getColorsNew() {
        if ((colorsNew == null || colorsNew.isEmpty()) && colors != null && !colors.isEmpty()) {
            colorsNew = new ArrayList<>(colors.size());
            for (String s : colors) {
                if (s.equals("#0")) {
                    colorsNew.add(new Color(0, true));
                } else {
                    colorsNew.add(new Color((int) Long.parseLong(s.startsWith("#") ? s.substring(1) : s, 16), true));
                }
            }
        }
        return colorsNew;
    }
>>>>>>> theirs
}
