import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.zapcom.android.app.R
import com.zapcom.android.app.state.SectionViewModel
import com.zapcom.android.app.state.SectionsStateHolder


@Composable
fun HomeScreen(
    viewModel: SectionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle(lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current)
    Column {
        Image(
            modifier = Modifier.padding(top = 50.dp, start = 8.dp, end = 8.dp, bottom = 8.dp),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = ""
        )

        when (val uiState = state.sectionsUiState) {
            is SectionsStateHolder.UiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            }

            is SectionsStateHolder.UiState.Success -> {
                DisplaySections(
                    uiState = uiState,
                )
            }

            is SectionsStateHolder.UiState.Error -> {
                ErrorScreen(
                    message = uiState.message,
                    onRetryClick = {
                        /* viewModel.fetchItems(LocalContext.current)*/
                    }
                )
            }

            is SectionsStateHolder.UiState.NoInternet -> {
                NoInternetScreen(
                    message = uiState.message,
                    onRetryClick = {
                        /*viewModel.fetchItems(LocalContext.current)*/
                    })
            }
        }
    }
}

@Composable
private fun NoInternetScreen(
    message: String,
    onRetryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Text(text = message)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally), onClick = onRetryClick
        ) {
            Text(text = "Retry")
        }
    }
}

@Composable
private fun ErrorScreen(message: String, onRetryClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Text(text = message, color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetryClick) {
            Text(text = "Retry")
        }
    }
}

@Composable
private fun DisplaySections(uiState: SectionsStateHolder.UiState.Success) {

    Column {
        uiState.sectionUiStates.forEach { section ->
            when (section) {
                is SectionsStateHolder.UiState.SectionType.Banner -> Banner(section.item)
                is SectionsStateHolder.UiState.SectionType.HorizontalFreeScroll -> HorizontalFreeScroll(
                    section.items
                )

                is SectionsStateHolder.UiState.SectionType.SplitBanner -> SplitBanner(section.items)
            }
        }
    }
}

@Composable
private fun Banner(item: SectionsStateHolder.UiState.ItemUiState) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(Color.Gray)
    ) {
        Box(contentAlignment = Alignment.BottomCenter) {
            ImageWithPlaceholder(item = item)
            Text(
                text = item.title,
                color = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomStart)
            )
        }
    }
}

@Composable
private fun HorizontalFreeScroll(items: List<SectionsStateHolder.UiState.ItemUiState>) {
    LazyRow(modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(16.dp)) {
        items(items.size) { index ->
            Card(
                modifier = Modifier
                    .width(124.dp)
                    .height(124.dp)
                    .padding(end = 16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(Color.LightGray)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    ImageWithPlaceholder(item = items[index])
                    Text(text = items[index].title, modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}

@Composable
private fun SplitBanner(items: List<SectionsStateHolder.UiState.ItemUiState>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items.forEach { item ->
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(240.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(Color.Gray)
            ) {
                Box(contentAlignment = Alignment.BottomCenter) {
                    /*  val painter = rememberAsyncImagePainter(
                          model = ImageRequest.Builder(LocalContext.current)
                              .data(item.image)
                              .size(Size.ORIGINAL)
                              .build()
                      )

                      Image(
                          painter = painter,
                          contentDescription = item.title,
                          contentScale = ContentScale.Crop,
                          modifier = Modifier.fillMaxSize()
                      )*/
                    ImageWithPlaceholder(item = item)
                    Text(
                        text = item.title,
                        color = Color.White,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.BottomStart)
                    )
                }
            }
        }
    }
}

@Composable
fun ImageWithPlaceholder(item: SectionsStateHolder.UiState.ItemUiState) {
    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            modifier = Modifier.fillMaxWidth(),
            model = item.image, // URL of the image
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.loading),
            error = painterResource(id = R.drawable.error)
        )
    }

}

@Composable
@Preview(showBackground = true)
fun PreviewUI() {
    DisplaySections(SectionsStateHolder.UiState.preview())
}

@Preview(showBackground = true)
@Composable
fun ErrorPreview() {
    ErrorScreen(message = "An error occurred", onRetryClick = {})
}
