package ipvc.estg.cityhelper.api

import retrofit2.Call
import retrofit2.http.*

interface EndPoints {

    //GET USERS BY EMAIL
    @GET("users/{email}")
    fun getUsersByEmail(@Path("email") email: String): Call<User>

    @FormUrlEncoded
    @POST("checklogin")
    fun checkLogin(
        @Field("email") email: String,
        @Field("password") password: String?
    ): Call<OutputPost>

    //GET ALL MARKERS
    @GET("markers")
    fun getMarkers(): Call<List<Marker>>

    //add marker
    @FormUrlEncoded
    @POST("markers/add")
    fun postMarker(
        @Field("descricao") descricao: String?,
        @Field("problemType") problemType: Int?,
        @Field("lat") lat: Double?,
        @Field("lng") lng: Double?,
        @Field("users_id") users_id: Int?
    ): Call<OutputPost>


    //GET marker BY ID
    @GET("markers/add/{id}")
    fun getMarkerById(
        @Path("id") id: Int?,
    ): Call<Marker>

    //UPDATE marker
    @FormUrlEncoded
    @POST("markers/update/{id}")
    fun postEditMarker(
        @Field("id") id: Int?,
        @Field("descricao") problem: String?,
        @Field("problemType") problemType: Int?,
    ): Call<OutputPost>


    //DELETE PROBLEM BY ID
    @DELETE("markers/delete/{id}")
    fun deleteProblemById(@Path("id") id: Int?): Call<OutputPost>
}
