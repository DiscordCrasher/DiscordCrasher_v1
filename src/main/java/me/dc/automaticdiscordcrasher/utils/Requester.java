package me.dc.automaticdiscordcrasher.utils;



import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class Requester {
    MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    public String getToken(String email, String password) {
        JSONObject response = null;
        try {
            response = new JSONObject(post("https://discordapp.com/api/auth/login", new JSONObject().put("email", email).put("password", password).toString()));
        } catch (IOException e) {
        }
        try {

            if (!(response != null || response.getString("token") != null)) {
                return null;
            } else {
                return response.getString("token");
            }
        } catch (JSONException e) {
            return null;
        }

    }

    private String post(String url, String json) throws IOException {

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

}