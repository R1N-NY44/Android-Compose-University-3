package org.d3if3062.mobpro1.asessmen3.system.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.d3if3062.mobpro1.asessmen3.system.database.model.ApiProfile
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

private const val BASE_URL = "https://ghastly-delicate-dragon.ngrok-free.app/link/files/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

data class ApiResponse(
    val resultsProfile: List<ApiProfile>
)

interface ChatProfileServices {

    @FormUrlEncoded
    @POST("AddTest.php")
    suspend fun addUser(
        @Field("email") email: String,
        @Field("name") name: String,
        @Field("photoUrl") photoUrl: String
    ): ApiResponse


    /*@GET("AddTest.php")
    suspend fun AddUser(
        @Query("email") email: String,
        @Query("photoUrl") photoUrl: String
    ): ApiResponse*/

    @GET("getUser.php")
    suspend fun getUser(): ApiResponse
}

object ChatProfileAPI {
    val retrofitService: ChatProfileServices by lazy {
        retrofit.create(ChatProfileServices::class.java)
    }
}