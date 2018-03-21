/*
The MIT License

Copyright for portions of OpenUnirest/uniresr-java are held by Mashape (c) 2013 as part of Kong/unirest-java.All other copyright for OpenUnirest/unirest-java are held by OpenUnirest (c) 2018.

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.openunirest.http;

import io.github.openunirest.http.exceptions.UnirestException;

public interface ObjectMapper {

	/**
	 * The Method to transform the response body to a object.
	 * The params should be passed to the underlying mapper
	 *  @return the deserialized body
	 *  @param <T> the return type
	 *  @param value The body of the response object as a string
	 *  @param valueType The body of the response object as a string
	 */
	<T> T readValue(String value, Class<T> valueType);

	/**
	 * The Method to transform the response body to a object.
	 * The params should be passed to the underlying mapper.
	 *
	 * This has been defaulted for easier upgrade.
	 *
	 *  @return the deserialized body
	 *  @param <T> The type to generate
	 *  @param value The body of the response object as a string
	 *  @param typeObject In order to produce generic types such as
	 *     List&lt;String&gt; or SomeFoo&lt;Bar&gt; many mappers have a
	 *     type object to avoid erasure. This should be the
	 *     Object passed. Popular examples include:
	 *     		Jackson: TypeReference
	 *          GSON: TypeToken
	 */
	default <T> T readValue(String value, Object typeObject){
		throw new UnirestException("Not Implemented");
	}

	/**
	 * The Method to transform the request object to a string for transport
	 *  @return the object as a string
	 *  @param value The object to be serialized by the underlying mapper
	 */
	String writeValue(Object value);
}
