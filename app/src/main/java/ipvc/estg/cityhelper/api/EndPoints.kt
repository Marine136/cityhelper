package ipvc.estg.cityhelper.api

import retrofit2.Call
import retrofit2.http.*

interface EndPoints {

    //GET USERS BY EMAIL
    @GET("users/{email}")
    fun getUsersByEmail(@Path("email") email: String): Call<User>

    @GET("markers")
    fun getMarkers(): Call<List<Marker>>

    @FormUrlEncoded
    @POST("checklogin")
    fun checkLogin(
        @Field("email") email: String,
        @Field("password") password: String?): Call<LoginResponse>

}
