package com.stveo.stevebowling.budget.Network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stveo.stevebowling.budget.BudgetApplication;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by stevebowling on 10/31/16.
 */

public class RestClient {
    private ApiService apiService;

    public RestClient(){
        GsonBuilder builder=new GsonBuilder();
        builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Gson gson=builder.create();

        HttpLoggingInterceptor log = new HttpLoggingInterceptor();
        log.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new SessionRequestInterceptor())
                .addInterceptor(log)
                .build();

        Retrofit restAdpter =new Retrofit.Builder()
                .baseUrl(BudgetApplication.API_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiService = restAdpter.create(ApiService.class);
    }

    public ApiService getApiService() {
        return apiService;
    }
}
