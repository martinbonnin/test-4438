import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.api.ScalarTypeAdapters;
import com.apollographql.apollo.api.internal.json.InputFieldJsonWriter;
import com.apollographql.apollo.api.internal.json.JsonWriter;
import com.apollographql.apollo.exception.ApolloException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.Buffer;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import type.SomeInput;

import java.io.IOException;

public class MainTest {
    @Test
    public void testNull() throws IOException {
        SomeInput input = SomeInput.builder()
                .addressLine1("10 Downing Street")
                .addressLine2(null)
                .build();

        Buffer buffer = new Buffer();
        JsonWriter jsonWriter = JsonWriter.of(buffer);
        jsonWriter.setSerializeNulls(true);
        InputFieldJsonWriter writer = new InputFieldJsonWriter(jsonWriter, ScalarTypeAdapters.DEFAULT);
        jsonWriter.beginObject();
        input.marshaller().marshal(writer);
        jsonWriter.endObject();

        buffer.flush();
        System.out.println("test result:");
        System.out.println(buffer.readUtf8());
    }

    @Test
    public void testWithMockServer() throws IOException, InterruptedException {
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody("yo dog"));
        server.start();

        ApolloClient apolloClient = ApolloClient.builder()
                .serverUrl(server.url("/"))
                .build();

        SomeInput input = SomeInput.builder()
                .addressLine1("10 Downing Street")
                .addressLine2(null)
                .build();

        apolloClient.query(new GetRandomQuery(input)).enqueue(new ApolloCall.Callback<GetRandomQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<GetRandomQuery.Data> response) {

            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                // we will get an error here but we're not using it
            }
        });

        Thread.sleep(1000);
        Assert.assertTrue(server.takeRequest().getBody().readUtf8().contains("\"addressLine2\":null"));

        server.close();
    }
}
