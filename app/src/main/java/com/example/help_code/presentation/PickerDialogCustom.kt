//package com.example.help_code.presentation
//
//import android.annotation.SuppressLint
//import androidx.compose.foundation.ExperimentalFoundationApi
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.ColumnScope
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.pager.PagerDefaults
//import androidx.compose.foundation.pager.PagerSnapDistance
//import androidx.compose.foundation.pager.VerticalPager
//import androidx.compose.foundation.pager.rememberPagerState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.AlertDialog
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.MutableState
//import androidx.compose.runtime.mutableIntStateOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.res.vectorResource
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import au.com.crownresorts.crma.R
//import au.com.crownresorts.crma.compose.clickableSingle
//import au.com.crownresorts.crma.compose.theme.CrownTextStyles.BodyEmphasized
//import au.com.crownresorts.crma.compose.theme.CrownThemeWrapper
//import timber.log.Timber
//
//@Composable
//fun PickerDialogCustom(
//    isShow: MutableState<Boolean> = mutableStateOf(true),
//    timeState: MutableState<Pair<Int, Int>> = mutableStateOf(Pair(0, 0)),
//    positiveName: String? = "Ok",
//    negativeName: String? = "Cancel",
//    onClick: (Boolean, String, Pair<Int, Int>?) -> Unit,
//) {
//    var time: Pair<Int, Int> = Pair(0, 0)
//    if (!isShow.value) return
//    AlertDialog(
//        text = {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.Center
//            ) {
//                val hours = VerticalPagerComponent(28, timeState.value.first, "h")
//                Spacer(Modifier.width(30.dp))
//                val minute = VerticalPagerComponent(60, timeState.value.second, "min")
//                time = Pair(hours, minute)
//                Timber.e("PickerDate currentPage hours=$hours, minute=$minute")
//            }
//        },
//        confirmButton = {
//            positiveName?.let {
//                Text(
//                    text = it,
//                    modifier = Modifier
//                        .clickableSingle {
//                            onClick(true, it, time)
//                            isShow.value = false
//                        }
//                        .padding(start = 32.dp, end = 8.dp),
//                    textAlign = TextAlign.End,
//                    style = BodyEmphasized.copy(color = colorResource(R.color.text_color))
//                )
//            }
//
//        },
//        dismissButton = {
//            negativeName?.let {
//                Text(
//                    text = it,
//                    modifier = Modifier
//                        .clickableSingle {
//                            onClick(false, it, null)
//                            isShow.value = false
//                        }
//                        .padding(end = 8.dp),
//                    textAlign = TextAlign.End,
//                    style = BodyEmphasized.copy(color = colorResource(R.color.text_color))
//                )
//            }
//        },
//        onDismissRequest = { },
//        shape = RoundedCornerShape(0.dp, 0.dp, 0.dp, 0.dp),
//        modifier = Modifier.padding(horizontal = 16.dp)
//    )
//}
//
//
//@SuppressLint("UnrememberedMutableState")
//@Preview
//@Composable
//fun PreviewPickerDialogCustom() = CrownThemeWrapper {
//    PickerDialogCustom(mutableStateOf(true)) { _, _, pair ->
//
//    }
//}
//
//@OptIn(ExperimentalFoundationApi::class)
//@SuppressLint("AutoboxingStateValueProperty")
//@Composable
//fun VerticalPagerComponent(init: Int, selected: Int, suffix: String): Int {
//    val pagerState = rememberPagerState(pageCount = { init })
//
//    val currentPageIndex = remember { mutableIntStateOf(selected) }
//    val heightCell = 70.dp
//    val widthCell = 100.dp
//    LaunchedEffect(currentPageIndex.intValue) {
//        pagerState.animateScrollToPage(currentPageIndex.intValue)
//    }
//    val fling = PagerDefaults.flingBehavior(
//        state = pagerState,
//        pagerSnapDistance = PagerSnapDistance.atMost(init)
//    )
//    Column(
//        modifier = Modifier.width(widthCell)
//    ) {
//        IconChevron(ImageVector.vectorResource(id = R.drawable.ic_arrow_up)) { currentPageIndex.intValue = pagerState.currentPage + 1 }
//
//        VerticalPager(
//            state = pagerState,
//            modifier = Modifier.height(heightCell),
//            beyondViewportPageCount = init,
//            flingBehavior = fling,
//        ) { page ->
//            Timber.e("PickerDate currentPage $page")
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = "$page $suffix",
//                    modifier = Modifier
//                        .padding(8.dp)
//                        .fillMaxWidth(),
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis,
//                    fontSize = 22.sp,
//                    textAlign = TextAlign.Center
//                )
//            }
//        }
//        IconChevron(ImageVector.vectorResource(id = R.drawable.ic_arrow_down)) { currentPageIndex.intValue = pagerState.currentPage - 1 }
//    }
//    return pagerState.currentPage
//}
//
//@Composable
//private fun ColumnScope.IconChevron(
//    imageVector: ImageVector,
//    onClick: () -> Unit
//) {
//    IconButton(
//        onClick = { onClick() },
//        modifier = Modifier
//            .fillMaxWidth()
//            .align(Alignment.CenterHorizontally)
//    ) {
//        Icon(
//            imageVector = imageVector,
//            contentDescription = "",
//            modifier = Modifier.fillMaxSize()
//        )
//    }
//}
//
//@Preview
//@Composable
//fun PreviewVerticalPagerComponent() = CrownThemeWrapper() {
//    VerticalPagerComponent(25, 3, "min")
//}