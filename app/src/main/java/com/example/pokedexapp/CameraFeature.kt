package com.example.pokedexapp

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL

/**
 * A composable screen that demonstrates the camera + sprite overlay feature.
 * Users can enter a Pokémon sprite URL, capture a photo, and the sprite will be overlaid
 * on the captured photo. The composite image is saved to the device gallery.
 *
 * @param modifier The modifier to apply to the screen
 * @param initialSpriteUrl Optional initial sprite URL to use
 * @param onCompositeSaved Optional callback when a composite image is saved successfully
 */
@Composable
fun CameraFeatureScreen(
    modifier: Modifier = Modifier,
    initialSpriteUrl: String = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png",
    onCompositeSaved: ((Uri) -> Unit)? = null
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var spriteUrl by rememberSaveable { mutableStateOf(initialSpriteUrl) }
    var spritePreview by remember { mutableStateOf<Bitmap?>(null) }
    var latestComposite by remember { mutableStateOf<Bitmap?>(null) }
    var statusMessage by rememberSaveable { mutableStateOf("Tap Capture Photo to start.") }

    LaunchedEffect(spriteUrl) {
        spritePreview = loadBitmapFromUrl(spriteUrl)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { capturedPhoto ->
        if (capturedPhoto == null) {
            statusMessage = "Photo capture was cancelled."
            return@rememberLauncherForActivityResult
        }

        scope.launch {
            val spriteBitmap = loadBitmapFromUrl(spriteUrl)
            if (spriteBitmap == null) {
                statusMessage = "Could not load sprite URL. Update it and try again."
                return@launch
            }

            val composite = overlaySprite(basePhoto = capturedPhoto, sprite = spriteBitmap)
            val savedUri = saveCompositeImage(context = context, bitmap = composite)
            latestComposite = composite
            statusMessage = if (savedUri != null) {
                onCompositeSaved?.invoke(savedUri)
                "Saved composite image to gallery."
            } else {
                "Created overlay, but saving failed."
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Camera + Sprite Overlay",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = spriteUrl,
            onValueChange = { spriteUrl = it },
            label = { Text("Pokemon sprite URL") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        spritePreview?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Selected Pokemon sprite",
                modifier = Modifier.height(96.dp)
            )
        } ?: Text("Sprite preview unavailable")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { cameraLauncher.launch(null) }) {
            Text("Capture Photo")
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(text = statusMessage, style = MaterialTheme.typography.bodyMedium)

        latestComposite?.let { bitmap ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Latest Composite", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Composite image",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Composable that provides a simple camera launcher button with sprite overlay functionality.
 * This is a minimal version that can be integrated into other screens.
 *
 * @param spriteUrl The URL of the sprite to overlay on captured photos
 * @param modifier The modifier to apply to the button
 * @param onCompositeSaved Callback when a composite image is saved successfully
 */
@Composable
fun CameraWithSpriteButton(
    spriteUrl: String,
    modifier: Modifier = Modifier,
    onCompositeSaved: ((Uri) -> Unit)? = null,
    onStatusChange: ((String) -> Unit)? = null
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { capturedPhoto ->
        if (capturedPhoto == null) {
            onStatusChange?.invoke("Photo capture was cancelled.")
            return@rememberLauncherForActivityResult
        }

        scope.launch {
            val spriteBitmap = loadBitmapFromUrl(spriteUrl)
            if (spriteBitmap == null) {
                onStatusChange?.invoke("Could not load sprite. Please try again.")
                return@launch
            }

            val composite = overlaySprite(basePhoto = capturedPhoto, sprite = spriteBitmap)
            val savedUri = saveCompositeImage(context = context, bitmap = composite)

            if (savedUri != null) {
                onCompositeSaved?.invoke(savedUri)
                onStatusChange?.invoke("Saved composite image to gallery.")
            } else {
                onStatusChange?.invoke("Failed to save image.")
            }
        }
    }

    Button(
        onClick = { cameraLauncher.launch(null) },
        modifier = modifier
    ) {
        Text("Capture Photo with Pokémon")
    }
}

/**
 * Loads a bitmap image from a URL.
 *
 * @param url The URL string of the image to load
 * @return The loaded Bitmap or null if loading fails
 */
suspend fun loadBitmapFromUrl(url: String): Bitmap? = withContext(Dispatchers.IO) {
    try {
        URL(url).openStream().use { input ->
            BitmapFactory.decodeStream(input)
        }
    } catch (_: IOException) {
        null
    }
}

/**
 * Overlays a sprite bitmap onto a base photo bitmap.
 * The sprite is scaled to 30% of the base photo's width and positioned in the bottom-right corner.
 *
 * @param basePhoto The background/base photo bitmap
 * @param sprite The sprite bitmap to overlay
 * @return A new bitmap with the sprite overlaid on the base photo
 */
fun overlaySprite(basePhoto: Bitmap, sprite: Bitmap): Bitmap {
    val output = basePhoto.copy(Bitmap.Config.ARGB_8888, true)
    val canvas = Canvas(output)

    val spriteTargetWidth = (output.width * 0.3f).toInt().coerceAtLeast(1)
    val spriteScale = spriteTargetWidth.toFloat() / sprite.width.toFloat().coerceAtLeast(1f)
    val spriteTargetHeight = (sprite.height * spriteScale).toInt().coerceAtLeast(1)
    val margin = 24

    val left = (output.width - spriteTargetWidth - margin).coerceAtLeast(0)
    val top = (output.height - spriteTargetHeight - margin).coerceAtLeast(0)

    canvas.drawBitmap(
        sprite,
        null,
        Rect(left, top, left + spriteTargetWidth, top + spriteTargetHeight),
        null
    )

    return output
}

/**
 * Saves a composite image to the device's gallery.
 *
 * @param context The Android context
 * @param bitmap The bitmap to save
 * @return The URI of the saved image or null if saving fails
 */
fun saveCompositeImage(context: Context, bitmap: Bitmap): Uri? {
    val resolver = context.contentResolver
    val displayName = "pokedex_${System.currentTimeMillis()}.jpg"

    val values = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(
                MediaStore.Images.Media.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + "/PokedexApp"
            )
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }
    }

    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values) ?: return null

    return try {
        resolver.openOutputStream(uri)?.use { stream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.clear()
            values.put(MediaStore.Images.Media.IS_PENDING, 0)
            resolver.update(uri, values, null, null)
        }

        uri
    } catch (_: IOException) {
        resolver.delete(uri, null, null)
        null
    }
}

