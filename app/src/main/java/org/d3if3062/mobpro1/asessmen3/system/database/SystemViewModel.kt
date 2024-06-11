package org.d3if3062.mobpro1.asessmen3.system.database

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
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
import org.d3if3062.mobpro1.asessmen3.system.network.UserDataStore
import org.d3if3062.mobpro1.asessmen3.system.network.chatProfileStatus

class SystemViewModel : ViewModel() {
    private val APIProfile_data = MutableLiveData<List<ApiProfile>>()
    private val APIChat_data = MutableLiveData<List<ChatList>>()

    val profileData: LiveData<List<ApiProfile>> get() = APIProfile_data
    var status = MutableStateFlow(chatProfileStatus.LOADING)
        private set

    init {
        getChat()
    }
    //LogIn to Chat API
    fun LogIn(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = chatProfileStatus.LOADING
            try {
                val response = ChatProfileAPI.retrofitService.addUser(user.email, user.photoUrl)
                APIProfile_data.postValue(response.results)
                status.value = chatProfileStatus.SUCCESS
                Log.d("MainViewModel", "[Profile]Success:${response.results}")
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error: ${e.message}")
                status.value = chatProfileStatus.FAILED
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////
    fun getChat() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ChatAPI.retrofitService.getChats()
                APIChat_data.postValue(response.results)
                Log.d("MainViewModel", "[Chat]Success:${response.results}")
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error: ${e.message}")
                status.value = chatProfileStatus.FAILED
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////
    fun sendChat(apiProfile: ApiProfile, text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = chatProfileStatus.LOADING
            try {
                val response = ChatAPI.retrofitService.addChat(apiProfile.id, apiProfile.photoUrl, text, null)
                APIChat_data.postValue(response.results)
                status.value = chatProfileStatus.SUCCESS
                Log.d("MainViewModel", "[Chat]Success:${response.results}")
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error: ${e.message}")
                status.value = chatProfileStatus.FAILED
            }
        }

    }
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