package edu.nd.pmcburne.hello

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.nd.pmcburne.hello.ui.theme.MyApplicationTheme
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import edu.nd.pmcburne.hello.ui.theme.Coral80

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels{
        viewModelFactory {
            initializer {
                val app = application as MapApplication
                MainViewModel(app.db.dao())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel.fetchAllTags()
        viewModel.fetchPlacemarks()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize().background(Coral80)) { innerPadding ->
                    MainScreen(viewModel, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {



    val charlottesville = LatLng(38.03567, -78.50365)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(charlottesville, 10f)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Coral80
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Dropdown(
                viewModel.allTags,
                viewModel.selectedTag,
                        onTagSelected = {
                    viewModel.selectedTag = it
                    viewModel.fetchPlacemarks()
                },
            )

            Box {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    viewModel.allPlacemarks.forEach { placemark ->
                        Marker(
                            state = MarkerState(
                                LatLng(
                                    placemark.latitude,
                                    placemark.longitude
                                )
                            ),
                            title = placemark.name,
                            snippet = placemark.description
                        )
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dropdown(
    tags: List<Tag>,
    selectedTag: String,
    onTagSelected:  (String) -> Unit,
){

    var isExpanded by remember {mutableStateOf(false)}
    var selected by remember {mutableStateOf(selectedTag)}
    println(tags.toString())

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded},
//        modifier = Modifier.background()
    ) {
        DisableSelection{
            TextField(
                value = selected,
                onValueChange = {},
                readOnly = true,
//            label = {Text(selectedTag)},
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                },
                shape = RoundedCornerShape(12.dp),
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .menuAnchor()
                    .padding(16.dp)
                    .clickable {
                        isExpanded = true
                    }
            )
        }


        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
        ){
            tags.forEach { tag ->
                DropdownMenuItem(
                    text = { Text(tag.tagName) },
                    onClick = {
                        selected = tag.tagName
                        onTagSelected(selected)
                        isExpanded = false
                    }
                )
            }
        }
//
//        ExposedDropdownMenuBox(
//            expanded = isExpanded,
//            onExpandedChange = { isExpanded = !isExpanded }
//        ) {
//            TextField(
//                value = selected,
//                onValueChange = { selected = it},
//            )
//        }
    }
}
