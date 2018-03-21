package io.github.openunirest.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectMapperTest {

    @Test
    public void parsingGenericTestsWithJackson(){
        ObjectMapper jom = new JacksonObjectMapper();

        Foo<String> foo = new Foo<>("hi mom");

        String s = jom.writeValue(foo);

        Foo<String> back = jom.readValue(s, new TypeReference<Foo<String>>(){});

        assertEquals(foo, back);
    }

    @Test
    public void parsingGenericTestsWithGSON(){
        ObjectMapper jom = new GsonObjectMapper();

        Foo<String> foo = new Foo<>("hi mom");

        String s = jom.writeValue(foo);

        Foo<String> back = jom.readValue(s, new TypeToken<Foo<String>>(){});

        assertEquals(foo, back);
    }
}