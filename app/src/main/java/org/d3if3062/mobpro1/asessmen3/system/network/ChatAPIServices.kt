package org.d3if3062.mobpro1.asessmen3.system.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.d3if3062.mobpro1.asessmen3.system.database.model.ApiProfile
import org.d3if3062.mobpro1.asessmen3.system.database.model.ChatList
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

data class ChatResponse(
    val results: List<ChatList>
)
interface ChatServices {

    @FormUrlEncoded
    @POST("AddChat.php")
    suspend fun addChat(
        @Field("user_id") userId: String,
        @Field("photoUrl") photoUrl: String?,
        @Field("text") text: String?,
        @Field("image") image: String?
    ): ChatResponse

    @GET("GetChat.php")
    suspend fun getChats(): ChatResponse
}

object ChatAPI {
    val retrofitService: ChatServices by lazy {
        retrofit.create(ChatServices::class.java)
    }
}

enum class ChatStatus { LOADING, SUCCESS, FAILED }
