package com.mobile.urbanfix.urban_fix.services;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.mobile.urbanfix.urban_fix.Logger;
import com.mobile.urbanfix.urban_fix.model.Callback;
import com.mobile.urbanfix.urban_fix.model.Document;
import com.mobile.urbanfix.urban_fix.model.Documents;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ConsultSentmentAzureService extends AsyncTask<String, Void, Double> {


    private Callback.SimpleAsync<Double> callBack;

    public ConsultSentmentAzureService(Callback.SimpleAsync<Double> callBack) {
        this.callBack = callBack;
    }

    @Override
    protected Double doInBackground(String... strings) {
        Double score = 0.0;
        try {
            String azureKey = strings[0];
            String host = strings[1];
            String commentContent = strings[2];
            Documents documents = new Documents();
            documents.add("0", "pt", commentContent);
            String jsonContent = new Gson().toJson(documents);
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON, jsonContent);
            Request request = new Request.Builder()
                    .addHeader("Ocp-Apim-Subscription-Key", azureKey)
                    .url(host)
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            Logger.logI(response.toString());
            Documents documentsResult = new Gson().fromJson(response.body().string(), Documents.class);
            for (Document d : documentsResult.getDocuments()) {
                Logger.logI("O score é: " + d.getScore());
                score = d.getScore();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return score;
    }

    @Override
    protected void onPostExecute(Double score) {
        super.onPostExecute(score);
        Logger.logI("Resultado é: " + score);
        callBack.onTaskDone(score, true);
    }

}
