package me.kumaravel.cloudvideoplayer.ui.screens

import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun PlayerScreen(videoUrl: String, onBack: () -> Unit) {
    val context = LocalContext.current

    // Use remember for the player to ensure it's not recreated on every recomposition
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(videoUrl)
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    // Reference to the PlayerView to call showController()
    var playerViewInstance by remember { mutableStateOf<PlayerView?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .focusable() // Makes the Box focusable to catch events
    ) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = true

                    // 1. FIX RESOLUTION BACKGROUND (Set to Black)
                    setBackgroundColor(android.graphics.Color.BLACK)
                    setShutterBackgroundColor(android.graphics.Color.BLACK)

                    // 2. STABLE FOCUS HANDLING
                    // Use Boolean setters instead of View.FOCUSABLE constants
                    isFocusable = true
                    isFocusableInTouchMode = true
                    // Allow focus to pass to media controls' buttons
                    descendantFocusability = ViewGroup.FOCUS_AFTER_DESCENDANTS

                    // 3. PREVENT SCREENSAVER
                    keepScreenOn = true

                    controllerShowTimeoutMs = 5000

                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }.also { playerViewInstance = it }
            },
            update = { view ->
                // Ensure the view requests focus once attached
                view.requestFocus()
            },
            modifier = Modifier
                .fillMaxSize()
                .onKeyEvent { keyEvent ->
                    // 4. REMOTE CONTROL FIX
                    // Fire TV Remotes send ACTION_DOWN.
                    // We catch any button press to wake up the controls.
                    if (keyEvent.nativeKeyEvent.action == KeyEvent.ACTION_DOWN) {
                        playerViewInstance?.showController()
                    }
                    // Return false so the key event still passes through to the player
                    // (allowing play/pause/seek to work via the native controller)
                    false
                }
        )
    }
}