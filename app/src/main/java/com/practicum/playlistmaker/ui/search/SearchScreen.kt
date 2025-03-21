package com.practicum.playlistmaker.ui.search


import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import coil.compose.rememberAsyncImagePainter
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.ui.search.fragment.SearchFragmentDirections
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import com.practicum.playlistmaker.ui.theme.Blue
import com.practicum.playlistmaker.ui.theme.LightDark
import com.practicum.playlistmaker.ui.theme.YsDisplayFont
import com.practicum.playlistmaker.util.debounce
import java.text.SimpleDateFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: SearchViewModel, navController: NavController) {

    val screenState by viewModel.screenState.observeAsState()
    var search by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val debouncedSearch = remember {
        debounce<String>(2000L, coroutineScope, useLastParam = true) { query ->
            viewModel.search(query)
        }
    }

     fun handlerTrackClick (track: Track) {
         viewModel.addTrackToHistory(track)
         val action = SearchFragmentDirections.actionSearchFragmentToMediaFragment(track)
         navController.navigate(action)
    }

    Scaffold(
        topBar = {
            TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.search),
                    style = TextStyle(
                    fontSize = 22.sp,
                    fontFamily = YsDisplayFont,
                    fontWeight = FontWeight.Medium
                )
                )
            }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BasicTextField(
                value = search,
                onValueChange = {
                    search = it
                    debouncedSearch(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .padding(horizontal = 16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    ),
                singleLine = true,
                cursorBrush = SolidColor(Blue),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.padding(start = 14.dp),
                            painter = painterResource(R.drawable.ic_search_16dp),
                            contentDescription = "Поиск",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Box(modifier = Modifier.weight(1f)) {
                            if (search.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.search),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontFamily = YsDisplayFont,
                                        fontWeight = FontWeight.Normal,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                            innerTextField()
                        }

                        if (search.isNotEmpty()) {
                            IconButton(onClick = { search = "" }) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_close),
                                    contentDescription = "Очистить",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            )

            when(val state = screenState) {

                is SearchScreenState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(top = 140.dp)
                            .align(Alignment.CenterHorizontally),
                        color = Blue
                    )
                }

                is SearchScreenState.Content -> {
                    Spacer(modifier = Modifier
                        .height(24.dp))
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) { items(state.tracks) { track ->
                        TrackItem(
                            track = track,
                            onClick = {handlerTrackClick(track)}
                        )
                    }
                    }
                }

                is SearchScreenState.NotFound -> {
                    NotFoundAndInternetScreen(
                        iconRes = painterResource(R.drawable.ic_not_found),
                        textRes = R.string.notFound
                    )
                }

                is SearchScreenState.Error -> {
                    Column(
                        modifier = Modifier.wrapContentSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        NotFoundAndInternetScreen(
                            iconRes = painterResource(R.drawable.ic_not_internet),
                            textRes = R.string.errorInternet
                        )
                        Text(
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .padding(horizontal = 24.dp),
                            text = stringResource(R.string.errorInternet2),
                            style = TextStyle(
                                fontSize = 19.sp,
                                fontFamily = YsDisplayFont,
                                fontWeight = FontWeight.Medium
                            ),
                            textAlign = TextAlign.Center

                        )
                        Button(
                            modifier = Modifier.padding(top = 24.dp),
                            enabled = true,
                            shape = RoundedCornerShape(54.dp),
                            onClick = {
                                debouncedSearch(search)
                            }

                        ) {
                            Text(
                                text = stringResource(R.string.buttonUpdate),
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontFamily = YsDisplayFont,
                                    fontWeight = FontWeight.Medium
                                ),
                                textAlign = TextAlign.Center

                            )
                        }
                    }
                }

                is SearchScreenState.History -> {
                    if (state.historyTracks.isNotEmpty()) {

                        Text(
                            modifier = Modifier
                                .padding(
                                    top = 45.dp,
                                    bottom = 20.dp),
                            text = stringResource(R.string.historyTitle),
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontSize = 19.sp,
                                fontFamily = YsDisplayFont,
                                fontWeight = FontWeight.Medium

                            )
                        )

                        LazyColumn(
                            modifier = Modifier.wrapContentSize()
                        ) { items(state.historyTracks) { track ->
                            TrackItem(
                                track = track,
                                onClick = {handlerTrackClick(track) }
                            )
                        }
                        }

                            Button(
                                onClick = {viewModel.clearHistory()},
                                shape = RoundedCornerShape(54.dp)
                            ) {
                                Text(text = stringResource(R.string.clearButton))
                            }

                    }
                }

                else -> {}
            }
        }
    }
}

@Composable
fun TrackItem(track: Track,
              onClick: () -> Unit) {
    Row(
        modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp)
        .padding(horizontal = 13.dp)
        .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically

    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = track.artworkUrl100,
                placeholder = painterResource(R.drawable.pc_placeholder)// Когда картинка ещё не прогрузилась
            ),
            contentDescription = "Обложка",
            modifier = Modifier
                .size(45.dp)
                .clip(RoundedCornerShape(2.dp))
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = track.trackName ?: "",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = YsDisplayFont,
                    fontWeight = FontWeight.Normal
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = track.artistName ?: "",
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontFamily = YsDisplayFont,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.secondary
                    ),
                     maxLines = 1,
                     overflow = TextOverflow.Ellipsis,
                )

                Image(
                    painter = painterResource(R.drawable.ic_ellipse),
                    contentDescription = null
                )

                Text(
                    text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis),
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontFamily = YsDisplayFont,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.secondary
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Image(
            modifier = Modifier.padding(end = 12.dp),
            painter = painterResource(R.drawable.ic_arrow_forward),
            contentDescription = null
        )
    }
}

@Composable
fun NotFoundAndInternetScreen(
    iconRes: Painter,
    @StringRes textRes: Int
) {
    Column(
        modifier = Modifier.padding(top = 102.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = iconRes,
            contentDescription = null,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = stringResource(textRes),
            style = TextStyle(
                fontSize = 19.sp,
                fontFamily = YsDisplayFont,
                fontWeight = FontWeight.Medium
            ),
            textAlign = TextAlign.Center

        )

    }
}


@Preview
@Composable
fun SearchScreenPreview() {

}