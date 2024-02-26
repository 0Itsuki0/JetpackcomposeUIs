
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch

@Composable
fun CustomTabBar() {
    val tabTitles = listOf("Tab 1", "Tab 2 - long", "Tab 3", "Tab 4", "Tab 5", "Tab 6", "Tab 7", "Tab 8 - long", "Tab 9", "Tab 10")
    var selectTabIndex by rememberSaveable { mutableIntStateOf(8) }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.toFloat()
    val lazyScrollState = rememberLazyListState(initialFirstVisibleItemIndex = selectTabIndex )
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(null) {
        lazyScrollState.scrollToItem(selectTabIndex, -(screenWidth).toInt())
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {

        // tab bar
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            LazyRow(
                state = lazyScrollState,
                horizontalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
                userScrollEnabled = true,
            ) {
                items(tabTitles.size) { index ->
                    CustomTab(
                        title = tabTitles[index],
                        isSelected = index == selectTabIndex,
                        onTabClick = {
                            selectTabIndex = index
                            coroutineScope.launch {
                                lazyScrollState.animateScrollToItem(index, -(screenWidth).toInt())
                            }
                        }
                    )
                }
            }
        }


        // Indicator
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(5F)
        ) {
            if (lazyScrollState.canScrollBackward) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(40.dp)
                        .height(35.dp)
                        .clickable {
                            coroutineScope.launch {
                                lazyScrollState.animateScrollBy(-screenWidth)
                            }
                        }
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.White,
                                    Color(android.graphics.Color.parseColor("#F2FFFFFF")),
                                    Color(android.graphics.Color.parseColor("#99FFFFFF")),
                                    Color.Unspecified
                                )
                            )
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow_left),
                        contentDescription = "",
                        modifier = Modifier
                            .width(25.dp)
                            .height(16.dp),
                        colorFilter = ColorFilter.tint(Color.Black),
                        alignment = Alignment.CenterStart
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(40.dp))
            }


            if (lazyScrollState.canScrollForward) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(40.dp)
                        .height(35.dp)
                        .clickable {
                            coroutineScope.launch {
                                lazyScrollState.animateScrollBy(screenWidth)
                            }
                        }
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.White,
                                    Color(android.graphics.Color.parseColor("#F2FFFFFF")),
                                    Color(android.graphics.Color.parseColor("#99FFFFFF")),
                                    Color.Unspecified
                                ).reversed()
                            )
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow),
                        contentDescription = "",
                        modifier = Modifier
                            .width(25.dp)
                            .height(16.dp),
                        colorFilter = ColorFilter.tint(Color.Black),
                        alignment = Alignment.CenterEnd
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(40.dp))
            }

        }
    }
}





@Composable
fun CustomTab(title: String, isSelected: Boolean, onTabClick: () -> Unit) {
    var boxModifier: Modifier = Modifier
        .widthIn(min = 80.dp)
        .height(32.dp)
        .clip(CircleShape)
        .clickable { onTabClick() }
    if (isSelected) {
        boxModifier = boxModifier.background(CustomColors.Blue, CircleShape)
    } else {
        boxModifier = boxModifier
            .background(Color.White)
            .border(1.dp, Color.LightGray, CircleShape)
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = boxModifier
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = if (isSelected) {Color.White} else {Color.Black},
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )
    }
}



@Preview(showBackground = true)
@Composable
fun TabBarPreview() {
    Column(
//        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(16.dp)
            .background(Color.White)
    ) {

        CustomTabBar()
    }
}
