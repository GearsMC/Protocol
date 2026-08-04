package org.cloudburstmc.protocol.bedrock.data.inventory.descriptor;

<<<<<<< ours
/**
 * ItemType represents a consistent combination of network ID and metadata value of an item. It
 * cannot usually be changed unless a new item is obtained.
 */
=======
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

>>>>>>> theirs
public enum ItemDescriptorType {
    INVALID("empty"),
    DEFAULT("name"),
    MOLANG("molang"),
    ITEM_TAG("item_tag"),
    /**
     * @deprecated since v2168
     */
    DEFERRED("DEFERRED_DEPRECATED"),
    /**
     * @since v575
     * @deprecated since v2168
     */
<<<<<<< ours
    COMPLEX_ALIAS
=======
    COMPLEX_ALIAS("COMPLEX_ALIAS_DEPRECATED");

    private static final Map<String, ItemDescriptorType> serializeNames = new HashMap<>(values().length, 1);
    static {
        for (ItemDescriptorType value : values()) {
            serializeNames.put(value.serializeName, value);
        }
    }

    @Getter
    private final String serializeName;

    ItemDescriptorType(String serializeName) {
        this.serializeName = serializeName;
    }

    public static ItemDescriptorType fromName(String serializeName) {
        return serializeNames.get(serializeName);
    }
>>>>>>> theirs
}
