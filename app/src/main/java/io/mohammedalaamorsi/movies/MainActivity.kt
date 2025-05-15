package io.mohammedalaamorsi.movies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.mohammedalaamorsi.movies.navigation.RootNavHost
import io.mohammedalaamorsi.movies.presentation.theme.MoviesTheme
import org.koin.androidx.compose.KoinAndroidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoviesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    KoinAndroidContext {
                        RootNavHost(modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MoviesTheme {
        RootNavHost()
    }
}
