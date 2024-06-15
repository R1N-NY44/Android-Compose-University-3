package org.d3if3062.mobpro1.asessmen3.system.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.d3if3062.mobpro1.asessmen3.system.database.model.ChatList
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

private const val BASE_URL = "https://fenris-api-host.000webhostapp.com/files/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

data class ChatResponse(
    val results: List<ChatList>
)

interface ChatServices {
    @Multipart
    @POST("addChat.php")
    suspend fun addChat(
        @Part("user_id") userId: RequestBody,
        @Part("name") name: RequestBody,
        @Part("photoUrl") photoUrl: RequestBody,
        @Part("text") text: RequestBody?,
        @Part image: MultipartBody.Part?
    ): ChatResponse

    @GET("getChat.php")
    suspend fun getChats(): ChatResponse

    @POST("deleteChat.php")
    @FormUrlEncoded
    suspend fun deleteChat(
        @Field("id") id: String
    ): ChatResponse


}

object ChatAPI {
    val retrofitService: ChatServices by lazy {
        retrofit.create(ChatServices::class.java)
    }
    fun imgUrl(imageId: String): String {
        return "$BASE_URL$imageId"
    }
}

enum class ChatStatus { LOADING, SUCCESS, FAILED }
