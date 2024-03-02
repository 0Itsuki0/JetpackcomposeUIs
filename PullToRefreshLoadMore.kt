import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.random.Random


@Preview(showBackground = true)
@Composable
fun TablePreview() {
    val dataList = (1..15).toMutableList().toMutableStateList()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Table(dataList)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Table(initialData: SnapshotStateList<Int>) {
    val maxDataListLength = 50
    val dataList = remember { initialData }

    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState(initialFirstVisibleItemIndex = 0)
    var isLoadingMore by rememberSaveable { mutableStateOf(false) }

    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val pullToRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            coroutineScope.launch{
                isRefreshing = true
                lazyListState.animateScrollToItem(0)
                val newData = refreshData()
                dataList.clear()
                dataList.addAll(newData)
                lazyListState.animateScrollToItem(1)
                isRefreshing = false
            }
        },
        refreshThreshold = 100.dp,
    )

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(15.dp, alignment = Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,
        userScrollEnabled = true,
        state = lazyListState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .background(Color.White)
            .pullRefresh(pullToRefreshState)
    ) {
        item {
            PullToRefreshIndicator(pullToRefreshState.progress, isRefreshing)
        }


        itemsIndexed(dataList) { index, item ->
            Text(
                text = "$item",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .padding(vertical = 30.dp),
                textAlign = TextAlign.Center
            )

            if (index == dataList.size - 1 && dataList.size < maxDataListLength) {
                LaunchedEffect(null) {
                    isLoadingMore = true
                    val moreData = loadMore(dataList.last(), maxDataListLength)
                    dataList.addAll(moreData)
                    isLoadingMore = false
                }
            }
        }
        if (isLoadingMore) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .height(40.dp)
                        .width(30.dp),
                    color = Color.LightGray,
                    strokeWidth = 4.dp,
                    strokeCap = StrokeCap.Round,
                )
            }
        }
    }

//    if (!lazyListState.canScrollForward && dataList.size < maxDataListLength) {
//        LaunchedEffect(null) {
//            isLoadingMore = true
//            val moreData = loadMore(dataList.last(), maxDataListLength)
//            dataList.addAll(moreData)
//            isLoadingMore = false
//        }
//    }
}


@Composable
private fun PullToRefreshIndicator(progress: Float, isRefreshing: Boolean) {
    val rowHeight = if (isRefreshing) { 60.dp } else {
        (progress * 50).roundToInt().dp
    }
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(rowHeight)
            .background(Color.Yellow)
            .padding(top = 16.dp)
    ) {
        if (progress >= 1 || isRefreshing) {
            CircularProgressIndicator(
                modifier = Modifier
                    .width(30.dp)
                    .align(Alignment.Top),
                color = Color.LightGray,
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round,
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.arrow_down),
                contentDescription = "",
                modifier = Modifier
                    .width(16.dp)
                    .height(16.dp),
                colorFilter = ColorFilter.tint(Color.Black),
                alignment = Alignment.Center
            )
        }

    }
}


suspend fun loadMore(lastItem: Int, maxItem: Int): SnapshotStateList<Int> {
    delay(1000)
    return withContext(Dispatchers.IO) {
        val list = (lastItem + 1..min(maxItem, lastItem + 15)).toMutableList()
        list.toMutableStateList()
    }
}

suspend fun refreshData(): SnapshotStateList<Int> {
    delay(3000)
    return withContext(Dispatchers.IO) {
        val start = Random.nextInt(100)
        val list = (start..start + 14).toMutableList()
        list.toMutableStateList()
    }
}
