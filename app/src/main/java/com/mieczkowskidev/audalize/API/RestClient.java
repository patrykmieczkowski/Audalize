package com.mieczkowskidev.audalize.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mieczkowskidev.audalize.Config;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class RestClient {

    private RestAdapter restAdapter;

    public RestClient() {
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Content-Type", "application/json");
            }
        };

        Gson gson = new GsonBuilder().create();

        restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(Config.ENDPOINT)
                .setConverter(new GsonConverter(gson))
                .setRequestInterceptor(requestInterceptor)
                .build();
    }

    public RestAdapter getRestAdapter() {

        return restAdapter;
    }
}