package com.mieczkowskidev.audalize.API;

import com.mieczkowskidev.audalize.model.DataResources;
import com.mieczkowskidev.audalize.model.DataProfile;
import com.mieczkowskidev.audalize.model.User;
import com.mieczkowskidev.audalize.model.UserLogin;

import java.util.List;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;
import rx.Observable;

/**
 * Created by Patryk Mieczkowski on 2016-02-09
 */
public interface RestAPI {

    @POST("/register")
    Observable<User> registerUser(@Body UserLogin userLogin);

    @POST("/login")
    Observable<User> loginUser(@Body UserLogin userLogin);

    @POST("/logout")
    Observable<Response> logoutUser(@Header("AuthToken") String authToken);

    @POST("/unregister")
    Observable<Response> unregisterUser(@Header("AuthToken") String authToken);

    // multipart/form-data
    @Multipart
    @POST("/add-audio")
    Observable<Response> addAudio(@Header("AuthToken") String authToken,
                                  @Part("file") TypedFile file,
                                  @Part("name") String fileName);

    @GET("/get-resources")
    Observable<List<DataResources>> getResources(@Header("AuthToken") String authToken);

    @GET("/get-profile")
    Observable<DataProfile> getProfile(@Header("AuthToken") String authToken);

}
