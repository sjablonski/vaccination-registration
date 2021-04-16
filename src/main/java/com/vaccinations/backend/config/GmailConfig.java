package com.vaccinations.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.vaccinations.backend.model.AccessToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class GmailConfig {
    private static final String APPLICATION_NAME = "Covid-19-vaccinations";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final File filePath = new File(System.getProperty("user.dir") + "/credentials.json");

    @Bean
    public Gmail gmail() throws IOException, GeneralSecurityException {
        InputStream in = new FileInputStream(filePath);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        Credential credential = new GoogleCredential.Builder()
                .setTransport(GoogleNetHttpTransport.newTrustedTransport())
                .setJsonFactory(JSON_FACTORY)
                .setClientSecrets(clientSecrets.getDetails().getClientId(), clientSecrets.getDetails().getClientSecret())
                .build()
                .setAccessToken(getAccessToken())
                .setRefreshToken("<refresh_token>");

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
    }

    private String getAccessToken() {
        HttpURLConnection conn = null;
        String accessToken = null;

        try {

            URL url = new URL("https://accounts.google.com/o/oauth2/token");

            Map<String, Object> params = new LinkedHashMap<>();
            params.put("client_id", "<client_id>");
            params.put("client_secret", "<client_secret>");
            params.put("refresh_token", "<refresh_token>");
            params.put("grant_type", "refresh_token");

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), StandardCharsets.UTF_8));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), StandardCharsets.UTF_8));
            }
            byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            conn.setRequestProperty("Content-Length",
                    String.valueOf(postDataBytes.length));
            conn.setRequestProperty("Content-language", "en-US");
            conn.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(
                    conn.getOutputStream());
            wr.write(postDataBytes);
            wr.close();

            StringBuilder sb = new StringBuilder();
            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            for (int c = in.read(); c != -1; c = in.read()) {
                sb.append((char) c);
            }

            String respString = sb.toString();
            
            ObjectMapper mapper = new ObjectMapper();
            AccessToken accessTokenObj = mapper.readValue(respString, AccessToken.class);
            accessToken = accessTokenObj.getAccessToken();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return (accessToken);
    }
}
