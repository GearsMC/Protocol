package org.cloudburstmc.protocol.bedrock.data;

/**
 * Oyuncunun fırın arayüzü tercihleri.
 *
 * @param leftTabIndex sol sekmede açık olan bölüm
 * @param filtering    tarif süzgeci açık mı
 * @param layout       arayüz yerleşimi
 * @since v2192
 */
public record FurnaceOptions(FurnaceLeftTabIndex leftTabIndex, boolean filtering, FurnaceLayout layout) {

    public enum FurnaceLeftTabIndex {
        NONE,
        RECIPE_FOOD,
        RECIPE_ITEMS,
        RECIPE_BLOCKS,
        RECIPE_SEARCH,
        INVENTORY
    }

    public enum FurnaceLayout {
        NONE,
        INVENTORY_ONLY,
        DEFAULT
    }
}
