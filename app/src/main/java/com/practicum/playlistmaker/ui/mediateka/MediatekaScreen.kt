package com.practicum.playlistmaker.ui.mediateka

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Tab
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.mediateka.model.PlayList
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.ui.mediateka.fragment.MediatekaFragmentDirections
import com.practicum.playlistmaker.ui.mediateka.view_model.FavoriteTracksViewModel
import com.practicum.playlistmaker.ui.mediateka.view_model.PlayListViewModel
import com.practicum.playlistmaker.ui.search.TrackItem
import com.practicum.playlistmaker.ui.theme.YsDisplayFont
import com.practicum.playlistmaker.util.getTrackWordForm
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediatekaScreen(navController: NavController) {

    val favoriteTracksViewModel : FavoriteTracksViewModel = koinViewModel()
    val playListViewModel : PlayListViewModel = koinViewModel()

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val selectedTabIndex by remember { derivedStateOf { pagerState.currentPage } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.mediateka),
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

        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth(),
                indicator = { tabPositions ->
                    TabRowDefaults.PrimaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        width = 148.dp,
                    )
                },
                divider = {}
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    },
                    text = { Text(stringResource(R.string.favoriteTracks)) }
                )

                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    },
                    text = { Text(stringResource(R.string.playList)) }
                )
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when(page) {
                    0 -> FavoriteTrackScreen(favoriteTracksViewModel, navController)
                    1 -> PlayListScreen(playListViewModel, navController)
                }
            }
        }
    }

}


@Composable
fun FavoriteTrackScreen(viewModel: FavoriteTracksViewModel, navController: NavController) {

    val favoriteState by viewModel.stateLiveDate.observeAsState()

    fun handlerTrackClick (track: Track) {
        val action = MediatekaFragmentDirections.actionMediatekaFragmentToMediaFragment(track)
        navController.navigate(action)
    }

    viewModel.getFavoriteTrack()


   Column(
       horizontalAlignment = Alignment.CenterHorizontally,
       verticalArrangement = Arrangement.Center
   ) {
       when(val state = favoriteState) {

           is FavoriteState.Content -> {
               Spacer(modifier = Modifier
                   .height(24.dp))
               LazyColumn(
                   modifier = Modifier.fillMaxSize()
               ) { items(state.track) { track ->
                   TrackItem(
                       track = track,
                       onClick = {handlerTrackClick(track)}
                   )
               }
               }
           }

           is FavoriteState.Empty -> {

               Column(
                   modifier = Modifier.fillMaxSize(),
                   horizontalAlignment = Alignment.CenterHorizontally,
                   verticalArrangement = Arrangement.Center
               ) {
                   Image(
                       painter = painterResource(R.drawable.ic_not_found),
                       contentDescription = null
                   )

                   Spacer(modifier = Modifier.height(16.dp))

                   Text(
                       modifier = Modifier.padding(
                           bottom = 378.dp
                       ),
                       text = stringResource(R.string.emptyFavoriteTracks),
                       style = TextStyle(
                           fontSize = 19.sp,
                           fontFamily = YsDisplayFont,
                           fontWeight = FontWeight.Medium
                       ),
                       textAlign = TextAlign.Center
                   )

               }
           }

           else -> {}
       }

   }

}

@Composable
fun PlayListScreen(viewModel: PlayListViewModel,
                   navController: NavController) {

    val playListState by viewModel.stateLiveData.observeAsState()

    fun handlerPlayListClick (playList: PlayList) {
        val action = MediatekaFragmentDirections.actionMediatekaFragmentToPlayListInfo(playList)
        navController.navigate(action)
    }

    viewModel.getPlayList()

   Column(
       modifier = Modifier
           .fillMaxSize(),
       horizontalAlignment = Alignment.CenterHorizontally,
       verticalArrangement = Arrangement.Center
   ) {
       Button(
           modifier = Modifier
               .padding(top = 24.dp),
           onClick = { navController.navigate(R.id.newPlayListFragment) }
       ) {
           Text(
               text = stringResource(R.string.newPlayList)
           )
       }

       when(val state = playListState) {


           is PlayListState.Content -> {

               Spacer(modifier = Modifier.height(16.dp))

               LazyVerticalGrid(
                   columns = GridCells.Fixed(2),
                   modifier = Modifier
                       .fillMaxSize()
                       .padding(horizontal = 16.dp)
               ) {
                   items(state.playList) { playList ->
                       PlayListItem(
                           playList = playList,
                           onClick = { handlerPlayListClick(playList) }
                       )
                   }
               }
           }

           is PlayListState.Empty -> {
               Column(
                   horizontalAlignment = Alignment.CenterHorizontally,
                   verticalArrangement = Arrangement.Center
               ) {

                   Image(
                       painter = painterResource(R.drawable.ic_not_found),
                       contentDescription = null
                   )

                   Spacer(modifier = Modifier.height(16.dp))

                   Text(
                       modifier = Modifier.padding(
                           bottom = 378.dp
                       ),
                       text = stringResource(R.string.emptyPlayList),
                       style = TextStyle(
                           fontSize = 19.sp,
                           fontFamily = YsDisplayFont,
                           fontWeight = FontWeight.Medium
                       ),
                       textAlign = TextAlign.Center
                   )

               }
           }

           else -> {}
       }
   }

}

@Composable
fun PlayListItem(
    playList: PlayList,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .clickable { onClick() }
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = playList.urlImager,
                fallback = painterResource(R.drawable.pc_placeholder_playlist) // Когда картинка равна null
            ),
            modifier = Modifier
                .size(160.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )

        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = playList.playListName!!,
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = YsDisplayFont,
                fontWeight = FontWeight.Normal,

            )
        )

        Text(
            text = "${playList.tracksCount} ${playList.tracksCount?.let { getTrackWordForm(it) }}",
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = YsDisplayFont,
                fontWeight = FontWeight.Normal,
            )
        )

    }
}

@Preview
@Composable
fun MediatekaScreenPreview() {

}

