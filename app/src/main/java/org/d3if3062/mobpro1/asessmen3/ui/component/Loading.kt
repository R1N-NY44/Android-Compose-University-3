package org.d3if3062.mobpro1.asessmen3.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Odading() {
    Box(Modifier.padding(60.dp), Alignment.Center) {
        CircularProgressIndicator()
    }
}