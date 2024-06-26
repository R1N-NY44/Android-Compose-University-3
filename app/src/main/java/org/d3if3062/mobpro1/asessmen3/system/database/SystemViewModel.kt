package org.d3if3062.mobpro1.asessmen3.system.database

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.d3if3062.mobpro1.asessmen3.system.database.model.ApiProfile
import org.d3if3062.mobpro1.asessmen3.system.database.model.ChatList
import org.d3if3062.mobpro1.asessmen3.system.database.model.User
import org.d3if3062.mobpro1.asessmen3.system.network.ChatAPI
import org.d3if3062.mobpro1.asessmen3.system.network.ChatProfileAPI
import org.d3if3062.mobpro1.asessmen3.system.network.ChatStatus
import org.d3if3062.mobpro1.asessmen3.system.network.UserDataStore
import java.io.ByteArrayOutputStream

class SystemViewModel : ViewModel() {
    private val APIProfile_data = MutableLiveData<List<ApiProfile>>()
    private val APIChat_data = MutableLiveData<List<ChatList>>()
    private val currentChatData = mutableListOf<ChatList>()


    val profileData: LiveData<List<ApiProfile>> get() = APIProfile_data
    val chatData: LiveData<List<ChatList>> get() = APIChat_data
    var chatStatus = MutableStateFlow(ChatStatus.LOADING)
        private set

    init {
        startChatPolling()
    }

    fun startChatPolling() {
        viewModelScope.launch {
            while (true) {
                getChat()
                delay(300000) // Auto Poll every 5 Minutes
            }
        }
    }


    //LogIn to Chat API
    fun LogIn(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    ChatProfileAPI.retrofitService.addUser(user.email, user.name, user.photoUrl)
                APIProfile_data.postValue(response.results)
                Log.d("MainViewModel", "[Profile]Success:${response.results}")
            } catch (e: Exception) {
                Log.e("MainViewModel", "[Profile]Error: ${e.message}")
            }
        }
    }

    suspend fun logOut() {
        delay(1500)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Reset APIProfile_data to null or an empty list
                APIProfile_data.postValue(emptyList())
                Log.d("MainViewModel", "[Profile]Logout successful")
            } catch (e: Exception) {
                Log.e("MainViewModel", "[Profile]Logout error: ${e.message}")
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    fun getChat() {
        viewModelScope.launch(Dispatchers.IO) {
            chatStatus.value = ChatStatus.LOADING
            try {
                val response = ChatAPI.retrofitService.getChats()
                val newChatData = response.results

                if (newChatData != currentChatData) {
                    currentChatData.clear()
                    currentChatData.addAll(newChatData)
                    APIChat_data.postValue(newChatData)
                }

                chatStatus.value = ChatStatus.SUCCESS
                Log.d("MainViewModel", "[Chat]Success:${response.results}")
            } catch (e: Exception) {
                Log.e("MainViewModel", "[Chat]Error: ${e.message}")
                chatStatus.value = ChatStatus.FAILED
            }
        }
    }

    fun sendChat(profile: ApiProfile, text: String?, image: Bitmap?) {
        viewModelScope.launch(Dispatchers.IO) {
            chatStatus.value = ChatStatus.LOADING
            try {
                val userId = profile.id.toRequestBody("text/plain".toMediaTypeOrNull())
                val name = profile.name.toRequestBody("text/plain".toMediaTypeOrNull())
                val photoUrl = profile.photoUrl.toRequestBody("text/plain".toMediaTypeOrNull())
                val textPart = text?.toRequestBody("text/plain".toMediaTypeOrNull())

                val imagePart: MultipartBody.Part? = image?.let {
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    it.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                    val requestBody = RequestBody.create(
                        "image/jpeg".toMediaTypeOrNull(),
                        byteArrayOutputStream.toByteArray()
                    )
                    MultipartBody.Part.createFormData("image", "image.jpg", requestBody)
                }

                // Jika tidak ada gambar, kirim imagePart sebagai null
                val response = if (imagePart != null) {
                    ChatAPI.retrofitService.addChat(userId, name, photoUrl, textPart, imagePart)
                } else {
                    // Kirim tanpa imagePart (null)
                    ChatAPI.retrofitService.addChat(userId, name, photoUrl, textPart, null)
                }

                APIChat_data.postValue(response.results)
                chatStatus.value = ChatStatus.SUCCESS
                getChat()
                Log.d("MainViewModel", "[Chat]Success:${response.results}")
            } catch (e: Exception) {
                Log.e("MainViewModel", "[Chat]Error: ${e.message}")
                chatStatus.value = ChatStatus.FAILED
            }
        }
    }

    fun deleteChat(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            chatStatus.value = ChatStatus.LOADING
            try {
                val response = ChatAPI.retrofitService.deleteChat(id)
                // Pastikan respons tidak null
                if (response != null) {
                    // Memeriksa jika ada 'results' dalam respons
                    if (response.results != null) {
                        // Memperbarui data chat jika respons sesuai
                        APIChat_data.postValue(response.results)
                        chatStatus.value = ChatStatus.SUCCESS
                        getChat()
                        Log.d("MainViewModel", "[Chat] Success deleting chat with id: $id")
                    } else {
                        // Handle case when 'results' is missing in response
                        Log.e("MainViewModel", "[Chat] Error: Results missing in response")
                        chatStatus.value = ChatStatus.FAILED
                    }
                } else {
                    // Handle case when response is null
                    Log.e("MainViewModel", "[Chat] Error: Null response")
                    chatStatus.value = ChatStatus.FAILED
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "[Chat] Error deleting chat: ${e.message}")
                chatStatus.value = ChatStatus.FAILED
            }
        }
    }



}