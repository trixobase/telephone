package cm.trixobase.telephone.common;

import android.content.ContentValues;

/**
 * Created by noumianguebissie on 12/22/18.
 */

public class DictionaryStructure {

    public static class Builder {

        ContentValues data;

        private Builder() {
            data = new ContentValues();
        }

        public Builder withEntry(String key, String value) {
            data.put(key, value);
            return this;
        }

        public Builder withEntry(String key, int value) {
            data.put(key, value);
            return this;
        }

        public Builder withEntry(String key, long value) {
            data.put(key, value);
            return this;
        }

        public Builder withEntry(String key, boolean value) {
            data.put(key, value);
            return this;
        }

        public ContentValues build() {
            return data;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

}
