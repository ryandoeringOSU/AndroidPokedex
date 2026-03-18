package com.example.pokedexapp

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pokedexapp.ui.PokemonViewModel
import com.example.pokedexapp.ui.home.PokeRed
import com.example.pokedexapp.ui.home.PokeWhite

// Card theme colors (matching DetailScreen)
val CardGold = Color(0xFFE8C84A)
val CardDarkGold = Color(0xFFB8972A)
val CardCream = Color(0xFFFFFDE7)
val CardBrown = Color(0xFF5A3E00)
val CardDarkBg = Color(0xFF2A1A00)

@Composable
fun PokemonCameraScreen(
    modifier: Modifier = Modifier,
    viewModel: PokemonViewModel? = null,
    onNavigateBack: () -> Unit = {}
) {
    var cameraStatus by rememberSaveable { mutableStateOf("Tap the camera button to capture a photo.") }
    var latestComposite by remember { mutableStateOf<Bitmap?>(null) }

    val pokemon = viewModel?.pokemon?.observeAsState()?.value
    val spriteUrl = pokemon?.sprites?.frontDefault ?: DEFAULT_PIKACHU_SPRITE_URL
    val spriteName = pokemon?.name ?: "pikachu"

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CardDarkBg)
            .padding(10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(4.dp, CardDarkGold, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(CardGold)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(12.dp)
                    .padding(bottom = 60.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Camera",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = CardBrown
                    )

                    Button(
                        onClick = onNavigateBack,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CardDarkGold,
                            contentColor = PokeWhite
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text("Back", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, CardDarkGold, RoundedCornerShape(10.dp))
                        .background(CardCream)
                        .padding(12.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Using sprite: ${spriteName.replaceFirstChar { it.uppercase() }}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = CardBrown
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        AsyncImage(
                            model = spriteUrl,
                            contentDescription = "Current Pokemon sprite",
                            modifier = Modifier.height(120.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(CardDarkGold)
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CameraWithPokemonSpriteButton(
                        viewModel = viewModel,
                        modifier = Modifier.fillMaxWidth(),
                        onCompositeCreated = { composite, _ ->
                            latestComposite = composite
                        },
                        onStatusChange = { cameraStatus = it }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, CardDarkGold, RoundedCornerShape(10.dp))
                        .background(CardCream)
                        .padding(12.dp)
                ) {
                    Text(
                        cameraStatus,
                        fontSize = 14.sp,
                        color = CardBrown,
                        textAlign = TextAlign.Center
                    )
                }

                latestComposite?.let { compositeBitmap ->
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "Latest Composite",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = CardBrown
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .border(2.dp, CardDarkGold, RoundedCornerShape(10.dp))
                            .background(Color(0xFF88BB88))
                            .padding(8.dp)
                    ) {
                        Image(
                            bitmap = compositeBitmap.asImageBitmap(),
                            contentDescription = "Latest composite image",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
