package co.kr.aimmobilefrontend.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.kr.aimmobilefrontend.R
import co.kr.aimmobilefrontend.data.AssetItem
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin


@Preview(showBackground = true)
@Composable
fun PreviewDashboardView() {

    AssetItem("TEST1", "stock", 1, 10.05f, "test security 1", "테스트를 위한 종목1")
    AssetItem("TEST2", "stock", 1, 8.03f, "test security 2", "테스트를 위한 종목2")
    AssetItem("TEST3", "stock", 1, 6.5f, "test security 3", "테스트를 위한 종목3")
    AssetItem("TEST4", "stock", 1, 8.5f, "test security 4", "테스트를 위한 종목4")
    AssetItem("TEST5", "bond", 3, 9.5f, "test security 5", "테스트를 위한 종목5")
    AssetItem("TEST6", "bond", 1, 8.5f, "test security 6", "테스트를 위한 종목6")
    AssetItem("TEST7", "bond", 1, 13.42f, "test security 7", "테스트를 위한 종목7")
    AssetItem("usd_cash", "etc", 1, 35.5f, "cash", "현금")

    val list = listOf(
        AssetAllocation("주식형 자산", "선진시장 주식", 46.08f, Color(0xFF80DEEA)),
        AssetAllocation("주식형 자산", "신흥시장 주식", 12.00f, Color(0xFF00BCD4)),
        AssetAllocation("채권형 자산", "선진시장 채권", 0.00f, Color(0xFF2196F3)),
        AssetAllocation("채권형 자산", "신흥시장 채권", 0.00f, Color(0xFF1976D2)),
        AssetAllocation("기타 자산", "미화 현금", 35.45f, Color(0xFFFFEB3B)),
        AssetAllocation("기타 자산", "대체자산", 6.47f, Color(0xFFFFC107))
    )

    DashboardView(list)
}

/**
 * 원형차트 컴포넌트
 */
@Composable
fun CircleGraphViewCompose(
    modifier: Modifier = Modifier,

    data: List<AssetAllocation>,
) {
    // 각 섹션의 애니메이션 값 초기화 (각각 Animatable 사용)
    val sweepAngles = remember { List(data.size) { Animatable(0f) } }

    // 퍼센트 값을 360도 기준으로 변환할 targetProgresses
    val targetProgresses = data.map { percentage -> percentage.percentage }.toList()

    // 애니메이션 실행
    LaunchedEffect(targetProgresses) {
        sweepAngles.forEachIndexed { i, animatable ->
            launch {
                animatable.animateTo(
                    targetValue = targetProgresses.getOrNull(i)?.let { 360f * (it / 100f) } ?: 0f,
                    animationSpec = tween(durationMillis = 1000)
                )
            }
        }
    }

    // 실제 도넛 차트 그리기
    Canvas(modifier = modifier) {
        val strokeWidth = 110f
        var startAngle = -90f

        sweepAngles.forEachIndexed { i, animatedAngle ->
            /**
             * drawArc 활용
             */
            drawArc(
                color = data[i].color,
                startAngle = startAngle,
                sweepAngle = animatedAngle.value,
                useCenter = false,
                style = Stroke(width = strokeWidth)
            )

            startAngle += animatedAngle.value
        }
    }
}

/**
 * 그룹별 막대 그래프 뷰
  */
@Composable
fun GroupedAssetBarChart(assets: List<AssetAllocation>) {
    // 자산을 group 기준으로 묶음
    val grouped = assets.groupBy { it.group }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2C3239))
            .padding(16.dp)
    ) {
        // 각 그룹 및 항목 출력
        grouped.forEach { (groupTitle, items) ->
            Text(
                text = groupTitle,
                color = Color.LightGray,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 12.dp)
            )

            items.forEach { item ->
                AssetBarItem(item)
            }
        }
    }
}


// 개별 자산 막대 항목 출력
@Composable
fun AssetBarItem(item: AssetAllocation) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = item.name, color = Color.White, fontSize = 16.sp)
            Text(
                text = String.format("%.2f%%", item.percentage),
                color = item.color,
                fontWeight = FontWeight.Bold
            )
        }

        LinearProgressIndicator(
            progress = { item.percentage / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .padding(top = 4.dp),
            color = item.color,
            trackColor = Color.DarkGray,
        )
    }
}

/**
 * Dashboard 화면 구성 목적의 enum (탭용)
  */
enum class Destination(
    val route: String,
    val label: String
) {
    MY("my", "MY AIM"),
    TREND("trend", "자산추이"),
    ALLOCATION("allocation", "자산배분")
}


/**
 *메인 대시보드 뷰
  */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardView(
    dataSet: List<AssetAllocation> = listOf(),
    backButtonCallback: () -> Unit = {},
    etfDetailCallback: () -> Unit = {}
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("평생안정 은퇴자금")
                },
                navigationIcon = {
                    IconButton(onClick = { backButtonCallback() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description",
                        )
                    }
                })
        },
    ) { contentPadding ->

        val navController = rememberNavController()
        val startDestination = Destination.ALLOCATION
        var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }


        Column {
            // 상단 탭바
            PrimaryTabRow(
                selectedTabIndex = selectedDestination,
                modifier = Modifier.padding(contentPadding)
            ) {
                Destination.entries.forEachIndexed { index, destination ->
                    Tab(
                        selected = selectedDestination == index,
                        onClick = {
                            navController.navigate(route = destination.route)
                            selectedDestination = index
                        },
                        text = {
                            Text(
                                text = destination.label,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }
            // 하단 컨텐츠
            DashboardNavGraph(
                navController,
                startDestination,
                dataSet,
                etfDetailCallback
            )

        }


    }
}

/**
 *탭 전환에 따라 네비게이션 처리
  */
@Composable
fun DashboardNavGraph(
    navController: NavHostController,
    startDestination: Destination,
    dataSet: List<AssetAllocation>,
    etfDetailCallback: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route
    ) {
        composable(Destination.ALLOCATION.route) {
            DashFormUI(
                dataSet,
                etfDetailCallback
            )
        }
        composable(Destination.MY.route) {

        }
        composable(Destination.TREND.route) {

        }
    }
}


/**
 *  "자산배분" 탭의 상세 UI
 */
@Composable
fun DashFormUI(
    dataSet: List<AssetAllocation>,
    etfDetailCallback: () -> Unit
) {

    var animateChart by rememberSaveable { mutableIntStateOf(0) }

    // 최초 진입 시점에 1로 설정하여 애니메이션 트리거
    LaunchedEffect(Unit) {
        animateChart = 1
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xff2c3239)) // 다크 테마
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Row {

            Box(
                modifier = Modifier
                    .clickable {
                        etfDetailCallback()
                    }
                    .width(150.dp)
                    .background(Color.Transparent)
            ) {

                // 원형 도넛 차트
                Box(modifier = Modifier.size(100.dp)) {
                    CircleGraphViewCompose(
                        modifier = Modifier.fillMaxSize(),
                        data = dataSet
                    )
                }
            }

            // 오른쪽 텍스트 설명
            Column {
                Text(
                    text = "장기투자에 적합한\n적극적인 자산배분",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(Modifier.height(14.dp))
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("'평생안정 은퇴자금'")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = Color.LightGray,
                                fontWeight = FontWeight.Normal
                            )
                        ) {
                            append("에\n최적화된 자산배분입니다")
                        }
                    },

                    fontSize = 14.sp,
                    color = Color.White,
                    modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                )
            }
        }

        // 자산 항목 막대그래프 출력
        GroupedAssetBarChart(dataSet)
    }
}

