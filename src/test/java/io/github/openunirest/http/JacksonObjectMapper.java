package io.github.openunirest.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.guava.GuavaModule;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

public class JacksonObjectMapper implements ObjectMapper {

	private com.fasterxml.jackson.databind.ObjectMapper om = new com.fasterxml.jackson.databind.ObjectMapper();

	public JacksonObjectMapper(){
		om.registerModule(new GuavaModule());
	}

	public <T> T readValue(String value, Class<T> valueType) {
		try {
			return om.readValue(value, valueType);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String writeValue(Object value) {
		try {
			return om.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

    public <T> T readValue(InputStream rawBody, Class<T> as) {
        try {
            return om.readValue(rawBody, as);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

	@Override
	public <T> T readValue(String rawBody, Object type) {
		try {
			return om.readValue(rawBody, (TypeReference<T>)type);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
