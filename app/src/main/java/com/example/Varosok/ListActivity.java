package com.example.Varosok;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private Button backButton;
    private TextView textView;
    private List<City> cities = new ArrayList<>();

    private String BASE_URL = "https://retoolapi.dev/r2ouVT/data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        init();
        RequestTask task = new RequestTask(BASE_URL, "GET");
        task.execute();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(ListActivity.this, MainActivity.class);
                ListActivity.this.startActivity(myIntent);
            }
        });
    }

    private void formBack() {
        textView.setVisibility(View.GONE);
        RequestTask task = new RequestTask(BASE_URL, "GET");
        task.execute();
    }

    private class RequestTask extends AsyncTask<Void, Void, Response>{
        private String requestUrl;
        private String requestMethod;
        private String requestBody;

        public RequestTask(String requestUrl) {
            this.requestUrl = requestUrl;
            this.requestMethod = "GET";
        }

        public RequestTask(String requestUrl, String requestMethod) {
            this.requestUrl = requestUrl;
            this.requestMethod = requestMethod;
        }

        public RequestTask(String requestUrl, String requestMethod, String requestBody) {
            this.requestUrl = requestUrl;
            this.requestMethod = requestMethod;
            this.requestBody = requestBody;
        }

        @Override
        protected Response doInBackground(Void... voids) {
            Response response = null;
            try {
                switch (requestMethod){
                    case "GET":
                        response = RequestHandler.get(BASE_URL);
                        break;
                    case "POST":
                        response = RequestHandler.post(requestUrl, requestBody);
                        break;
                    case "PUT":
                        response = RequestHandler.put(requestUrl, requestBody);
                        break;
                    case "DELETE":
                        response = RequestHandler.delete(requestUrl);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Response response){
            Gson converter = new Gson();
            super.onPostExecute(response);
            if(response == null){
                Toast.makeText(ListActivity.this, "unable_to_connect", Toast.LENGTH_SHORT).show();
                return;
            }
            if(response.getResponseCode() >= 400){
                Toast.makeText(ListActivity.this, response.getContent(), Toast.LENGTH_SHORT).show();
                return;
            }
            switch (requestMethod){
                case "GET":
                    String content = response.getContent();
                    cities = Arrays.asList(converter.fromJson(content, City[].class));
                    System.out.println("!"+ cities);
                    textView.setText(cities.toString());
                    break;
                default:
                    if (response.getResponseCode() >= 201 && response.getResponseCode() < 300){
                        textView.setText("");
                        RequestTask task = new RequestTask(BASE_URL);
                        task.execute();
                    }
                    break;
            }

        }
    }

    public void init(){
        backButton = findViewById(R.id.buttonExit);
        textView = findViewById(R.id.textViewList);
        textView.setMovementMethod(new ScrollingMovementMethod());
    }
}