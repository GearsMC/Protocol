package org.cloudburstmc.protocol.bedrock.data.skin;

<<<<<<< ours
/**
 * Represents a piece of a persona skin. All pieces are sent separately.
 *
 * @param id        The ID.
 * @param type      The type.
 * @param packId    A UUID that identifies the pack that the persona piece belongs to.
 * @param isDefault Whether default.
 * @param productId A UUID that identifies the piece when it comes to purchases. It is empty for pieces that have
 *                  the 'IsDefault' field set to true.
 */
public record PersonaPieceData(String id, String type, String packId, boolean isDefault, String productId) {
=======
import lombok.Data;

import java.util.UUID;

@Data
public class PersonaPieceData {

    String id;
    PersonaPieceType pieceType;
    UUID packUuid;
    boolean isDefault;
    String productId;

    public PersonaPieceData(String id,
                            String type,
                            String packId,
                            boolean isDefault,
                            String productId) {
        this.id = id;
        this.pieceType = PersonaPieceType.fromName(type);
        this.packUuid = UUID.fromString(packId);
        this.isDefault = isDefault;
        this.productId = productId;
    }

    public PersonaPieceData(String id,
                            PersonaPieceType pieceType,
                            UUID packId,
                            boolean isDefault,
                            String productId) {
        this.id = id;
        this.pieceType = pieceType;
        this.packUuid = packId;
        this.isDefault = isDefault;
        this.productId = productId;
    }

    public String getPackId() {
        return packUuid.toString();
    }

    public String getType() {
        return pieceType.getSerializeName();
    }
>>>>>>> theirs
}
