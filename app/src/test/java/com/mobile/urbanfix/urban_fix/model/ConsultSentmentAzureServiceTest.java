package com.mobile.urbanfix.urban_fix.model;

import com.google.gson.Gson;
import com.mobile.urbanfix.urban_fix.Logger;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static org.junit.Assert.*;

public class ConsultSentmentAzureServiceTest {

    @Test
    public void testConsultarSentimento() {
        String conteudo =
                "Sensacional";
        String azureKey = "215ee64de86d4b5ebcd634fedb9c994d";
        String host = "https://brazilsouth.api.cognitive.microsoft.com/text/analytics/v2.0/sentiment";
        Documents documents = new Documents();
        documents.add("1", "pt", conteudo);
        String jsonContent = new Gson().toJson(documents);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, jsonContent);
        Request request = new Request.Builder()
                .addHeader("Ocp-Apim-Subscription-Key", azureKey)
                .url(host)
                .post(body)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();

        Documents documentsResult = new Gson().fromJson(response.body().string(), Documents.class);
        Document doc = documentsResult.getDocuments().get(0);

        double score = doc.getScore();
        System.out.printf("%.5f", score);
        assertTrue(score < 0);
        assertTrue(score < 1);
        assertEquals(8, score,0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}