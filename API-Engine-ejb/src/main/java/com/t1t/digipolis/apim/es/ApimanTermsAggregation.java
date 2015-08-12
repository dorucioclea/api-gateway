package com.t1t.digipolis.apim.es;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.searchbox.core.search.aggregation.Aggregation;
import io.searchbox.core.search.aggregation.Bucket;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.LinkedList;
import java.util.List;

import static io.searchbox.core.search.aggregation.AggregationField.*;

/**
 * A custom terms aggregation because the default jest one doesn't work.
 */
@SuppressWarnings("nls")
public class ApimanTermsAggregation extends Aggregation {

    public static final String TYPE = "terms";

    private List<Entry> buckets = new LinkedList<>();

    public ApimanTermsAggregation(String name, JsonObject termAggregation) {
        super(name, termAggregation);
        if(termAggregation.has(String.valueOf(BUCKETS)) && termAggregation.get(String.valueOf(BUCKETS)).isJsonArray()) {
            parseBuckets(termAggregation.get(String.valueOf(BUCKETS)).getAsJsonArray());
        }
    }

    private void parseBuckets(JsonArray bucketsSource) {
        for(JsonElement bucketElement : bucketsSource) {
            JsonObject bucket = (JsonObject) bucketElement;
            Entry entry = new Entry(bucket, bucket.get(String.valueOf(KEY)).getAsString(), bucket.get(String.valueOf(DOC_COUNT)).getAsLong());
            buckets.add(entry);
        }
    }

    public List<Entry> getBuckets() {
        return buckets;
    }

    public class Entry extends Bucket {
        private String key;

        public Entry(JsonObject bucket, String key, Long count) {
            super(bucket, count);
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            if (obj.getClass() != getClass()) {
                return false;
            }

            Entry rhs = (Entry) obj;
            return new EqualsBuilder()
                    .appendSuper(super.equals(obj))
                    .append(key, rhs.key)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder()
                    .append(getCount())
                    .append(getKey())
                    .toHashCode();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }

        ApimanTermsAggregation rhs = (ApimanTermsAggregation) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(buckets, rhs.buckets)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(buckets)
                .toHashCode();
    }

}
