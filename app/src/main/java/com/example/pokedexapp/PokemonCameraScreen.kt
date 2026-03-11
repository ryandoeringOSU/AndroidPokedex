package com.example.pokedexapp

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pokedexapp.ui.PokemonViewModel

@Composable
fun PokemonCameraScreen(
    modifier: Modifier = Modifier,
    viewModel: PokemonViewModel? = null
) {
    var cameraStatus by rememberSaveable { mutableStateOf("Tap the camera button to capture a photo.") }
    var latestComposite by remember { mutableStateOf<Bitmap?>(null) }

    val pokemon = viewModel?.pokemon?.observeAsState()?.value
    val spriteUrl = pokemon?.sprites?.frontDefault ?: DEFAULT_PIKACHU_SPRITE_URL
    val spriteName = pokemon?.name ?: "pikachu"

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Camera Feature", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Using sprite: $spriteName",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(10.dp))

        AsyncImage(
            model = spriteUrl,
            contentDescription = "Current Pokemon sprite",
            modifier = Modifier.height(120.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        CameraWithPokemonSpriteButton(
            viewModel = viewModel,
            modifier = Modifier.fillMaxWidth(),
            onCompositeCreated = { composite, _ ->
                latestComposite = composite
            },
            onStatusChange = { cameraStatus = it }
        )

        Spacer(modifier = Modifier.height(10.dp))
        Text(cameraStatus, style = MaterialTheme.typography.bodyMedium)

        latestComposite?.let { compositeBitmap ->
            Spacer(modifier = Modifier.height(16.dp))
            Text("Latest Composite", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                bitmap = compositeBitmap.asImageBitmap(),
                contentDescription = "Latest composite image",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
