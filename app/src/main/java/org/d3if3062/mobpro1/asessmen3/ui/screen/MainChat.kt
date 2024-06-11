package org.d3if3062.mobpro1.asessmen3.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.d3if3062.mobpro1.asessmen3.system.database.SystemViewModel
import org.d3if3062.mobpro1.asessmen3.system.database.model.ApiProfile
import org.d3if3062.mobpro1.asessmen3.system.database.model.ChatList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicChat(systemViewModel: SystemViewModel, apiProfile: List<ApiProfile>, modifier: Modifier) {
//    val apiProfile by systemViewModel.profileData.observeAsState(initial = emptyList())
    var textState by rememberSaveable { mutableStateOf("") }
    Scaffold(
        modifier = modifier,
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.surface,
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
                            /* TODO: Send message */
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AddCircle,
                            contentDescription = "Send Photo"
                        )
                    }
                    IconButton(
                        onClick = {
                            apiProfile.firstOrNull()?.let { profile ->
                                systemViewModel.sendChat(profile, textState)
                            }
                            textState = ""
                        },
                    ) {
                        Icon(imageVector = Icons.Filled.Send, contentDescription = "Send Message")
                    }
                }
            }
        }
    ) { padding ->
        Modifier.padding(padding)
        ChatContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            systemViewModel = systemViewModel,
            apiProfile
        )
    }

}

@Composable
fun ChatContent(
    modifier: Modifier,
    systemViewModel: SystemViewModel,
    apiProfile: List<ApiProfile>
) {
    val status by systemViewModel.chatStatus.collectAsState()
    val chatData by systemViewModel.chatData.observeAsState(initial = emptyList())
    val listState = rememberLazyListState()

    LaunchedEffect(chatData) {
        listState.scrollToItem(0)
    }

    LazyColumn(
        modifier = modifier,
        state = listState,
        reverseLayout = true
    ) {
        items(chatData) { chat ->
            if (chat.name == apiProfile.first().name) {
                    SentMessageBox(chat)
            } else {
                ReceivedMessageBox(chat)
            }
//            ReceivedMessageBox(chat)
//            ChatBox(chat)
        }
    }
    ///////////////////////////////////////////////////////////////////////
}







@Composable
fun SentMessageBox(chat: ChatList) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Surface(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp), shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
                .widthIn(max = 250.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = chat.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,

                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = chat.text,
                    fontSize = 16.sp,

                )
            }
        }
    }
}

@Composable
fun ReceivedMessageBox(chat: ChatList) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Start
    ) {
//        SubcomposeAsyncImage(
//            model = ImageRequest.Builder(LocalContext.current)
//                .data(chat.photoUrl)
//                .crossfade(true)
//                .build(),
//            loading = {
//                CircularProgressIndicator(
//                    modifier = Modifier
//                        .size(40.dp)
//                        .padding(8.dp)
//                )
//            },
//            contentDescription = null,
//            modifier = Modifier
//                .size(40.dp)
//                .background(Color.Gray, shape = RoundedCornerShape(20.dp))
//        )
        Surface(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp), shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
                .widthIn(max = 250.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = chat.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = chat.text,
                    fontSize = 16.sp,
                )
            }
        }
    }
}



















@Composable
fun ChatBox(chat: ChatList) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = chat.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = chat.text,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                )
            }

//            SubcomposeAsyncImage(
//                model = ImageRequest.Builder(LocalContext.current)
//                    .data(chat.photoUrl)
//                    .crossfade(true)
//                    .build(),
//                loading = {
//                    CircularProgressIndicator(
//                        modifier = Modifier
//                            .size(40.dp)
//                            .padding(8.dp)
//                    )
//                },
//                contentDescription = null,
//                modifier = Modifier
//                    .size(40.dp)
//                    .background(Color.Gray, shape = RoundedCornerShape(20.dp))
//            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun PublicChatPreview() {
    PublicChat(systemViewModel = SystemViewModel(), apiProfile = emptyList(), modifier = Modifier)
}



/*when (status) {
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
                onClick = { *//*systemViewModel.retrieveData()*//* },
                    modifier = Modifier.padding(8.dp),
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
                ) {
                    Text(text = stringResource(id = R.string.try_again))
                }
            }
        }
    }*/