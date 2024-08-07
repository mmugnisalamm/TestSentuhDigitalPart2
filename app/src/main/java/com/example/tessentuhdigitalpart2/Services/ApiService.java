package com.example.tessentuhdigitalpart2.Services;

import com.example.tessentuhdigitalpart2.URL.Url;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiService {
    @Headers({"Accept: application/json"})
    @GET(Url.GET_DATA_CHUCK_NORRIS)
    Call<JsonObject> getDataChuckNorris(
            @Query("query") String query
    );
}
