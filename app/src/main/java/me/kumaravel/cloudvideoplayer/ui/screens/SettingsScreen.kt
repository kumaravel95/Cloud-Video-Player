import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import me.kumaravel.cloudvideoplayer.util.SettingsManager

@Composable
fun SettingsScreen(
    settingsManager: SettingsManager,
    onBack: () -> Unit
) {
    val currentUrl by settingsManager.configUrl.collectAsState(initial = "")
    var textState by remember(currentUrl) { mutableStateOf(currentUrl) }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val isTv = remember { context.packageManager.hasSystemFeature(PackageManager.FEATURE_LEANBACK) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(48.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isTv) {
            androidx.tv.material3.Text("Configuration URL", style = androidx.tv.material3.MaterialTheme.typography.headlineMedium)
        } else {
            androidx.compose.material3.Text("Configuration URL", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
        }

        androidx.compose.material3.OutlinedTextField(
            value = textState,
            onValueChange = { textState = it },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(16.dp),
            label = {
                if (isTv) {
                    androidx.tv.material3.Text("Enter JSON URL")
                } else {
                    androidx.compose.material3.Text("Enter JSON URL")
                }
            }
        )

        if (isTv) {
            androidx.tv.material3.Button(onClick = {
                scope.launch {
                    settingsManager.saveConfigUrl(textState)
                    onBack()
                }
            }) {
                androidx.tv.material3.Text("Save and Reload")
            }
        } else {
            androidx.compose.material3.Button(onClick = {
                scope.launch {
                    settingsManager.saveConfigUrl(textState)
                    onBack()
                }
            }) {
                androidx.compose.material3.Text("Save and Reload")
            }
        }
    }
}