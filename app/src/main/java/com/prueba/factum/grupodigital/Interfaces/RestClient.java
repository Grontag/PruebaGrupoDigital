package com.prueba.factum.grupodigital.Interfaces;

import com.prueba.factum.grupodigital.Beans.Estudiante;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by David on 03/07/2017.
 */

public interface RestClient {
    @GET ("http://hello-world.innocv.com/api/user/getall")
    Call<ResponseBody> getAllStudents();

    @POST("http://hello-world.innocv.com/api/user/create")
    Call<ResponseBody> createNewUser(@Body Estudiante user);

    @POST("http://hello-world.innocv.com/api/user/update")
    Call<ResponseBody> updateUser(@Body Estudiante user);

    @GET("http://hello-world.innocv.com/api/user/remove/{id}")
    Call<ResponseBody> deleteUser(@Path("id") int id);
}
