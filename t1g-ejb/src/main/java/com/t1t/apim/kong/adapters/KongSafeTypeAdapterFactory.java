package com.t1t.apim.kong.adapters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class KongSafeTypeAdapterFactory implements TypeAdapterFactory {

    private static final Logger log = LoggerFactory.getLogger(KongSafeTypeAdapterFactory.class);
    private static final String EXPECTED_ARRAY = "Expected BEGIN_ARRAY but was BEGIN_OBJECT";

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);

        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                try {
                    delegate.write(out, value);
                }
                catch (Exception ex) {
                    log.error("GSon Serialization error, skipping value: {}", ex.getMessage());
                    delegate.write(out, null);
                }
            }

            @Override
            public T read(JsonReader in) throws IOException {
                try {
                    return delegate.read(in);
                }
                catch (Exception ex) {
                    if (ex.getMessage().contains(EXPECTED_ARRAY)) {
                        log.error("GSon Deserialization error, skipping value: {}", ex.getMessage());
                        in.skipValue();
                        return null;
                    }
                    else throw ex;
                }
            }
        };
    }
}