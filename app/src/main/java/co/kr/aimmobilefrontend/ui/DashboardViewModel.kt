package co.kr.aimmobilefrontend.ui

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import co.kr.aimmobilefrontend.data.AssetItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


/**
 * 전달받은 자산에 관한 데이터셋을
 * ui 로 출력할 수 있도록 convert 하는 목적의 데이터셋
 */
data class AssetAllocation(
    val group: String,
    val name: String,
    val percentage: Float,
    val color: Color
)


@HiltViewModel
class DashboardViewModel @Inject constructor(
    /**
     * 자산에 관한 더미데이터를 가져오기 위한 모듈.
     * 추후 api 를 통해 호출한다 하여도 기능 추가가 용이하도록 구현.
     * DashBoardModule.kt 파일 참조
     */
    assetItems: List<AssetItem>
) : ViewModel() {

    private val _allocations = MutableStateFlow<List<AssetAllocation>>(emptyList())
    val allocations: StateFlow<List<AssetAllocation>> = _allocations

    init {
        /**
         * 전달받은 데이터셋을 convert 하는 코드
         */
        _allocations.value = assetItems.map {
            val group = when (it.type) {
                "stock" -> "주식형 자산"
                "bond" -> "채권형 자산"
                else -> "기타 자산"
            }
            val color = when (it.type) {
                "stock" -> Color(0xFF80DEEA)
                "bond" -> Color(0xFF2196F3)
                else -> Color(0xFFFFEB3B)
            }
            AssetAllocation(group, it.security_name, it.ratio, color)
        }
    }


}