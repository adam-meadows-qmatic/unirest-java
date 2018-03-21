package io.github.openunirest.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GsonObjectMapper implements ObjectMapper {
    private Gson gson = new Gson();

    @Override
    public <T> T readValue(String value, Class<T> valueType) {
        return gson.fromJson(value, valueType);
    }

    @Override
    public <T> T readValue(String value, Object typeObject) {
        return gson.fromJson(value, ((TypeToken<T>)typeObject).getType());
    }

    @Override
    public String writeValue(Object value) {
        return gson.toJson(value);
    }
}
