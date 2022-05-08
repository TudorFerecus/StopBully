package com.example.bullyapp


import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface APIService {
    @POST("/api/login/")
    suspend fun getUsers(@Body user : RequestBody): Response<ResponseBody>

    @POST("/api/register/")
    suspend fun postUser(@Body requestBody: RequestBody) : Response<ResponseBody>

    @GET("/api/posts/")
    suspend fun getPosts(@Header("Authorization") authorization:  String) : Response<ResponseBody>

    @POST("/api/posts/")
    suspend fun post(@Header("Authorization") authorization: String, @Body requestBody: RequestBody) : Response<ResponseBody>

    @GET
    suspend fun getComments(@Url url: String, @Header("Authorization") authorization: String) : Response<ResponseBody>

    @POST("/api/comments/")
    suspend fun sendComment(@Header("Authorization") authorization: String, @Body requestBody: RequestBody) : Response<ResponseBody>

    @Multipart
    @POST("/face-detection/images/")
    suspend fun postPhoto(
        @Header("Authorization") authorization: String,
        @Body url: RequestBody
    ): Response<ResponseBody>

    @GET("/face-detection/images/52/49")
    suspend fun getPhoto(@Header("Authorization") authorization: String) : Response<ResponseBody>

}