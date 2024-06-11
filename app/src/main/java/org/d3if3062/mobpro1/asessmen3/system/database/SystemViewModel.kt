package org.d3if3062.mobpro1.asessmen3.system.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.d3if3062.mobpro1.asessmen3.system.database.model.ApiProfile
import org.d3if3062.mobpro1.asessmen3.system.database.model.ChatList
import org.d3if3062.mobpro1.asessmen3.system.database.model.User
import org.d3if3062.mobpro1.asessmen3.system.network.ChatAPI
import org.d3if3062.mobpro1.asessmen3.system.network.ChatProfileAPI
import org.d3if3062.mobpro1.asessmen3.system.network.ChatStatus

class SystemViewModel : ViewModel() {
    private val APIProfile_data = MutableLiveData<List<ApiProfile>>()
    private val APIChat_data = MutableLiveData<List<ChatList>>()

    val profileData: LiveData<List<ApiProfile>> get() = APIProfile_data
    val chatData: LiveData<List<ChatList>> get() = APIChat_data
    var chatStatus = MutableStateFlow(ChatStatus.LOADING)
        private set

    init {
        getChat()
    }
    //LogIn to Chat API
    fun LogIn(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ChatProfileAPI.retrofitService.addUser(user.email, user.name, user.photoUrl)
                APIProfile_data.postValue(response.resultsProfile)
                Log.d("MainViewModel", "[Profile]Success:${response.resultsProfile}")
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error: ${e.message}")
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////
    fun getChat() {
        viewModelScope.launch(Dispatchers.IO) {
            chatStatus.value = ChatStatus.LOADING
            try {
                val response = ChatAPI.retrofitService.getChats()
                APIChat_data.postValue(response.resultsChat)
                chatStatus.value = ChatStatus.SUCCESS
                Log.d("MainViewModel", "[Chat]Success:${response.resultsChat}")
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error: ${e.message}")
                chatStatus.value = ChatStatus.FAILED
            }
        }
    }
    fun sendChat(profile: ApiProfile, text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            chatStatus.value = ChatStatus.LOADING
            try {
                val response = ChatAPI.retrofitService.addChat(profile.id, profile.name, profile.photoUrl, text, null)
                APIChat_data.postValue(response.resultsChat)
                chatStatus.value = ChatStatus.SUCCESS
                getChat()
                Log.d("MainViewModel", "[Chat]Success:${response.resultsChat}")
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error: ${e.message}")
                chatStatus.value = ChatStatus.FAILED
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////
//    fun sendChat(apiProfile: ApiProfile, text: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            chatStatus.value = ChatStatus.LOADING
//            try {
//                val responseChat = ChatAPI.retrofitService.addChat(apiProfile.id, apiProfile.name, apiProfile.photoUrl, text, null)
//                APIChat_data.postValue(responseChat.chatResponses)
//                chatStatus.value = ChatStatus.SUCCESS
//                Log.d("MainViewModel", "[Chat]Success:${responseChat.chatResponses}")
//            } catch (e: Exception) {
//                Log.e("MainViewModel", "Error: ${e.message}")
//                chatStatus.value = ChatStatus.FAILED
//            }
//        }
//
//    }
}

/*fun getUserList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ChatProfileAPI.retrofitService.getUser()
                APIProfile_data.postValue(response.results)
                Log.d("MainViewModel", "[Profile]Success:${response.results}")
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error: ${e.message}")
                status.value = chatProfileStatus.FAILED
            }
        }
    }*/