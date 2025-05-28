package co.kr.aimmobilefrontend.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Preview(showBackground = true)
@Composable
fun PreviewETFDetailScreen() {
    EtfDetailScreen(sampleETFList)
}

data class ETFItem(
    val title: String,
    val description: String,
    val changeRate: String,
    val shares: String
)

val sampleETFList = listOf(
    ETFItem(
        "MSCI Australia ETF",
        "커먼웰스은행, 알루미나, 시드니공항 등 호주를 대표하는 70여개 기업을 포함하는 MSCI 지수 종목에 투자하는 ETF",
        "-0.60%",
        "1주"
    ),
    ETFItem(
        "MSCI Germany ETF",
        "SAP, 지멘스, 아디다스 등 독일을 대표하는 59개 기업에 투자하는 ETF",
        "-0.63%",
        "1주"
    ),
    ETFItem(
        "MSCI HONG KONG ETF",
        "AIA생명 등 홍콩거래소에 상장된 상위 우량기업 45개로 구성된 대표지수 구성 종목에 투자하는 ETF",
        "-3.71%",
        "1주"
    ),
    ETFItem(
        "MSCI United Kingdom ETF",
        "HSBC, 보다폰, 유니레버 등 영국을 대표하는 런던증시 상장 109개 기업에 투자하는 ETF",
        "-0.81%",
        "1주"
    )
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EtfDetailScreen(
    etfList: List<ETFItem> = listOf(),
    backButtonCallback: () -> Unit = {}
) {
    Scaffold(

        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF26C6DA), // ✅ 배경색
                ),
                title = {

                },
                actions = {
                    Text(
                        text = "ETF란?",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = Color.White,
                                shape = RoundedCornerShape(50) // pill shape
                            )
                            .background(Color.Transparent)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .clickable {

                            }
                    )
                },
                navigationIcon = {
                    Column {
                        IconButton(onClick = { backButtonCallback() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                tint = Color.White,
                                contentDescription = "Localized description",
                            )
                        }
                    }
                })
        },
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .padding(paddingValue)
                .fillMaxSize()
                .background(Color(0xfff9f9f9))
        ) {
//             상단 헤더
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF26C6DA)) // 청록색
                    .padding(start = 20.dp, bottom = 20.dp)
            ) {
                Column {
                    Text(
                        text = "선진시장 주식",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "각 ETF 종목별 기본 정보, 보유 수량,\n최근 1일 수익률 정보입니다",
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }


            //  ETF 리스트 출력
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-20).dp) //  TopBar와 겹치게 배치
                    .padding(horizontal = 12.dp)
            ) {
                items(etfList) { item ->
                    ETFCard(item) // ETF 상세 카드 컴포저블 호출
                }
            }


        }
    }
}

// 개별 ETF 카드 출력
@Composable
fun ETFCard(item: ETFItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White // ✅ 배경색 설정
        ),
        shape = RoundedCornerShape(0.dp),// 직각 카드
        ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 상단 타이틀 영역 (브랜드 + 수익률 설명)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.weight(3f),
                    text ="iShares", color = Color.Gray, fontWeight = FontWeight.Bold)
                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End,
                    text = "전일대비 수익률", color = Color.Gray, fontSize = 10.sp)
            }
            // 본문 정보 영역
            Row {
                // 좌측: ETF 제목 및 설명
                Column(modifier = Modifier.weight(3f)) {
                    Text(
                        text = item.title,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = item.description,
                        fontSize = 13.sp,
                        color = Color.DarkGray
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                // 우측: 수익률 및 보유 수량
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .align(alignment = Alignment.Bottom)
                    ,
                ) {

                    Box(modifier = Modifier.weight(1f))

                    Text(
                        text = item.changeRate,
                        color = Color.Blue,
                        fontSize = 14.sp,
                        modifier = Modifier.align(Alignment.End)
                    )
                    Text(
                        text = item.shares,
                        color = Color(0xff6cc7cd),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.End) // ✅ 아래 텍스트도 오른쪽 정렬
                    )
                }


            }
        }
    }
}

