package com.example.Varosok;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public final class RequestHandler {
    public static final int timeOutTime = 10000;

    private RequestHandler(){}

    public static Response get(String url) throws IOException {
        HttpURLConnection connection = setUpConnection(url);
        connection.setRequestMethod("GET");
        return getResponse(connection);
    }

    public static Response post(String url, String data) throws IOException {
        HttpURLConnection connection = setUpConnection(url);
        connection.setRequestMethod("POST");

        addRequestBody(connection, data);

        return getResponse(connection);
    }

    public static Response delete(String url) throws IOException {
        HttpURLConnection connection = setUpConnection(url);
        connection.setRequestMethod("DELETE");

        return getResponse(connection);
    }
    public static Response put(String url, String data) throws IOException {
        HttpURLConnection connection = setUpConnection(url);
        connection.setRequestMethod("PUT");
        addRequestBody(connection, data);
        return getResponse(connection);
    }

    private static Response getResponse(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();

        InputStream is;
        if (responseCode >= 400) {
            is = connection.getErrorStream();
        } else {
            is = connection.getInputStream();
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = br.readLine();
        StringBuilder rows = new StringBuilder();
        while (line != null) {
            rows.append(line).append(System.lineSeparator());
            line = br.readLine();
        }
        br.close();
        is.close();
        connection.disconnect();

        String content = rows.toString().trim();
        return new Response(responseCode, content);
    }

    private static HttpURLConnection setUpConnection(String url) throws IOException {
        URL urlObj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
        connection.setConnectTimeout(timeOutTime);
        connection.setReadTimeout(timeOutTime);
        connection.setRequestProperty("Accept", "application/json");
        return connection;
    }

    private static void addRequestBody(HttpURLConnection connection, String data) throws IOException {
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
        bw.write(data);
        bw.flush();
        bw.close();
    }
}