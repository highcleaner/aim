package co.kr.aimmobilefrontend.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


/**
 * navGraph 에 삽입할 각 화면에 대한 정보 규정
 */
sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Dashboard : Screen("dashboard") // 자산배분 화면
    data object ETFDetailScreen : Screen("ETFDetail") // etf detail 하면
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        /**
         * 로그인 화면 진입
         */
        composable(Screen.Login.route) {
            val viewModel : LoginViewModel = hiltViewModel()
            //로그인 여부 체크하는 상태관리 변수
            val isLogin by viewModel.isLoggedIn.collectAsStateWithLifecycle()

            // login view 에 대한 상태관리 폼
            val uiState by viewModel.formState.collectAsStateWithLifecycle()
            LoginScreen(
                formState = uiState,
                onFieldChange = { s1, s2 ->
                    //텍스트 변경에 따른 ui 수정
                    viewModel.onFieldChange(s1, s2)
                },
                onFieldFocusLost = { s1 ->
                    // 포커스 변경에 따른 ui 수정
                    viewModel.onFieldFocusLost(s1)
                },
                isLoggedIn = isLogin,
                isAutoLogin = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onLoginClick = { id, pwd, email, phone ->
                    /**
                     * 로그인 버튼 클릭 시, 콜백
                     */
                    viewModel.login(id, pwd, email, phone)
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Dashboard.route) {
            /**
             * 자산관리 대쉬보드 화면 진입
             */
            val viewModel : DashboardViewModel = hiltViewModel()
            // 요청한 더미데이터 저장하는 변수
            val dataSet by viewModel.allocations.collectAsStateWithLifecycle()

            DashboardView(
                backButtonCallback = {
                    navController.popBackStack()
                },
                dataSet = dataSet,
                etfDetailCallback = {
                    navController.navigate(Screen.ETFDetailScreen.route)
                }
            )
        }
        composable(Screen.ETFDetailScreen.route) {
            /**
             * 종목에 대한 상세정보 보여주는 화면 진입
             */
            EtfDetailScreen(
                backButtonCallback = {
                    navController.popBackStack()
                },
                /**
                 * 임의의 더미데이터 전달하는 코드
                 */
                etfList =
                    listOf(
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


            )
        }
    }
}
