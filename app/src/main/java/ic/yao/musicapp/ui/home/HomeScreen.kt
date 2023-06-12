package ic.yao.musicapp.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ic.yao.musicapp.R

@Composable
fun HomeScreen(
    navigateTo: () -> Unit = {}
){
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFF7FFF8),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "MUSE-IC",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF181818)
            )
            Text(
                text = "An app for music lovers",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF525252)
            )
            Spacer(modifier = Modifier.padding(28.dp))
            Image(
                painter = painterResource(id = R.drawable.home_vector),
                contentDescription = "Home main vector image",
                modifier = Modifier.size(280.dp)
            )
            Spacer(modifier = Modifier.padding(vertical = 28.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 106.dp),
                onClick = navigateTo,
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color(0xFFF5FFEC),
                    containerColor = Color(0xFF181818)
                )
            ) {
                Text(
                    text = "GET STARTED",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

    }
}