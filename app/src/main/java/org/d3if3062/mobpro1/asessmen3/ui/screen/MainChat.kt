package org.d3if3062.mobpro1.asessmen3.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.d3if3062.mobpro1.asessmen3.R
import org.d3if3062.mobpro1.asessmen3.system.database.SystemViewModel
import org.d3if3062.mobpro1.asessmen3.system.database.model.ChatList
import org.d3if3062.mobpro1.asessmen3.system.network.ChatStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicChat(systemViewModel: SystemViewModel) {
    val apiProfile by systemViewModel.profileData.observeAsState(initial = emptyList())
    var textState by rememberSaveable { mutableStateOf("") }
    Scaffold(
        bottomBar = {
            BottomAppBar(
                /*modifier = Modifier.padding(8.dp)*/
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // TextField with 70% width
                    TextField(
                        value = textState,
                        onValueChange = { textState = it },
                        modifier = Modifier
                            .weight(0.9f),
                        placeholder = { Text("Enter text") },
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    // IconButton with 15% width each
                    IconButton(
                        onClick = {
                            apiProfile?.firstOrNull()?.let { profile ->
                                systemViewModel.sendChat(profile, textState)
                            }
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AddCircle,
                            contentDescription = "Send Photo"
                        )
                    }
                    IconButton(
                        onClick = { /* TODO: Send message */ },
                    ) {
                        Icon(imageVector = Icons.Filled.Send, contentDescription = "Send Message")
                    }
                }
            }
        }
    ) { padding ->
        Modifier.padding(padding)
        ChatContent(
            modifier = Modifier.fillMaxSize(),
            systemViewModel = systemViewModel
        )
    }

}

@Composable
fun ChatContent(modifier: Modifier, systemViewModel: SystemViewModel) {
    val status by systemViewModel.chatStatus.collectAsState()
    val chatData by systemViewModel.chatData.observeAsState(initial = emptyList())

    when (status) {
        ChatStatus.LOADING -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        ChatStatus.SUCCESS -> {
            LazyVerticalGrid(
                modifier = modifier
                    .fillMaxSize()
                    .padding(4.dp),
                columns = GridCells.Fixed(2)
            ) {
                items(chatData) {
                    Text(text = it.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = it.text, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }

        ChatStatus.FAILED -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = stringResource(id = R.string.error))
                Button(
                    onClick = { /*systemViewModel.retrieveData()*/ },
                    modifier = Modifier.padding(8.dp),
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
                ) {
                    Text(text = stringResource(id = R.string.try_again))
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun PublicChatPreview() {
    PublicChat(systemViewModel = SystemViewModel())
}