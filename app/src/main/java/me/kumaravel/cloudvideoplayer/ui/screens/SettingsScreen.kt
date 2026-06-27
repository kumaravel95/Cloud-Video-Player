import kotlinx.coroutines.launch
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
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import me.kumaravel.cloudvideoplayer.util.SettingsManager


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SettingsScreen(
    settingsManager: SettingsManager,
    onBack: () -> Unit
) {
    val currentUrl by settingsManager.configUrl.collectAsState(initial = "")
    var textState by remember(currentUrl) { mutableStateOf(currentUrl) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(48.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Configuration URL", style = MaterialTheme.typography.headlineMedium)

        androidx.compose.material3.OutlinedTextField(
            value = textState,
            onValueChange = { textState = it },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(16.dp),
            label = { Text("Enter JSON URL") }
        )

        Button(onClick = {
            scope.launch {
                settingsManager.saveConfigUrl(textState)
                onBack()
            }
        }) {
            Text("Save and Reload")
        }
    }
}