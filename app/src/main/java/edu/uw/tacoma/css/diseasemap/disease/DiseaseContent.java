package edu.uw.tacoma.css.diseasemap.disease;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DiseaseContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DiseaseItem> ITEMS = new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DiseaseItem> ITEM_MAP = new HashMap<>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(DiseaseItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DiseaseItem createDummyItem(int position) {
        return new DiseaseItem(String.valueOf(position), "Item " + position);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DiseaseItem {
        public final String id;
        public final String content;

        public DiseaseItem(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
