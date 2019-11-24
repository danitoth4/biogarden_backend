package util;

import org.springframework.boot.json.JacksonJsonParser;

import java.io.*;
import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Helper
{
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    public static String getAccessToken(String username, String password, String clientId, String clientSecret)
    {
        try
        {
            Map<Object, Object> data = new HashMap<>();
            data.put("grant_type", "password");
            data.put("username", username);
            data.put("password", password);
            String auth = clientId + ":" + clientSecret;

            HttpRequest request = HttpRequest.newBuilder()
                    .POST(buildBodyFromMap(data))
                    .uri(URI.create("http://localhost:8081/oauth/token"))
                    .setHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString(auth.getBytes()))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            JacksonJsonParser jsonParser = new JacksonJsonParser();
            return jsonParser.parseMap(response.body()).get("access_token").toString();
        }
        catch(Exception e)
        {
            return null;
        }
    }

    public static HttpRequest.BodyPublisher buildBodyFromMap(Map<Object, Object> data) throws UnsupportedEncodingException
    {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        System.out.println(builder.toString());
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }

}
