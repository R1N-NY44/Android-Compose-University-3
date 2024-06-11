package org.d3if3062.mobpro1.asessmen3.system.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if3062.mobpro1.asessmen3.system.database.model.ApiProfile
import org.d3if3062.mobpro1.asessmen3.system.database.model.ChatList
import org.d3if3062.mobpro1.asessmen3.system.database.model.User
import org.d3if3062.mobpro1.asessmen3.system.network.ChatAPI
import org.d3if3062.mobpro1.asessmen3.system.network.ChatProfileAPI
import org.d3if3062.mobpro1.asessmen3.system.network.ChatStatus

class SystemViewModel : ViewModel() {
    private val APIProfile_data = MutableLiveData<List<ApiProfile>>()
    private val APIChat_data = MutableLiveData<List<ChatList>>()
    private val currentChatData = mutableListOf<ChatList>()


    val profileData: LiveData<List<ApiProfile>> get() = APIProfile_data
    val chatData: LiveData<List<ChatList>> get() = APIChat_data
    var chatStatus = MutableStateFlow(ChatStatus.LOADING)
        private set

    init {
//        getChat()
        startChatPolling()
    }

    private fun startChatPolling() {
        viewModelScope.launch {
            while (true) {
                getChat()
                delay(5000) // Poll every 5 seconds
            }
        }
    }


    //LogIn to Chat API
    fun LogIn(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ChatProfileAPI.retrofitService.addUser(user.email, user.name, user.photoUrl)
                APIProfile_data.postValue(response.results)
                Log.d("MainViewModel", "[Profile]Success:${response.results}")
            } catch (e: Exception) {
                Log.e("MainViewModel", "[Profile]Error: ${e.message}")
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////
    private suspend fun getChat() {
        withContext(Dispatchers.IO) {
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


    /*fun getChat() {
        viewModelScope.launch(Dispatchers.IO) {
            chatStatus.value = ChatStatus.LOADING
            try {
                val response = ChatAPI.retrofitService.getChats()
                APIChat_data.postValue(response.results)
                chatStatus.value = ChatStatus.SUCCESS
                Log.d("MainViewModel", "[Chat]Success:${response.results}")
            } catch (e: Exception) {
                Log.e("MainViewModel", "[Chat]Error: ${e.message}")
                chatStatus.value = ChatStatus.FAILED
            }
        }
    }*/
    fun sendChat(profile: ApiProfile, text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            chatStatus.value = ChatStatus.LOADING
            try {
                val response = ChatAPI.retrofitService.addChat(profile.id, profile.name, profile.photoUrl, text, null)
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
}