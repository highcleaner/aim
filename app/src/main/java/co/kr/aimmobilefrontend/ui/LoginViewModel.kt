package co.kr.aimmobilefrontend.ui

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.aimmobilefrontend.data.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * input field 에 대한 data set 작성
 */
data class InputField(
    val text: String = "",
    val error: String? = null
)

/**
 * 로그인 시 입력받아야 하는 데이터 리스트 구성
 */
data class FormState(
    val id: InputField = InputField(),
    val password: InputField = InputField(),
    val passwordConfirm: InputField = InputField(),
    val email: InputField = InputField(),
    val phone: InputField = InputField(),
    val isValid: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    /**
     * data store 에 로그인 정보를 저장하기 위한 모듈
     */
    private val userPrefs: UserPreferences
) : ViewModel() {

    private val _formState = MutableStateFlow(FormState())
    val formState: StateFlow<FormState> = _formState

    /**
     * input field 의 입력값이 변할때 호출되는 함수.
     */
    fun onFieldChange(field: String, value: String) {
        _formState.update { state ->
            when (field) {
                "id" -> state.copy(id = InputField(value, validateId(value)))
                "password" -> state.copy(password = InputField(value, validatePassword(value)))
                "passwordConfirm" -> state.copy(
                    passwordConfirm = InputField(
                        value,
                        validateConfirmPassword(value)
                    )
                )

                "email" -> state.copy(email = InputField(value, validateEmail(value)))
                "phone" -> state.copy(phone = InputField(value, validatePhone(value)))
                else -> state
            }.copy(isValid = isAllValid(state))
        }
    }

    /**
     * input field 의 포커스가 해제될때 호출되는 함수
     */
    fun onFieldFocusLost(field: String) {
        val value = when (field) {
            "id" -> _formState.value.id.text
            "password" -> _formState.value.password.text
            "passwordConfirm" -> _formState.value.passwordConfirm.text
            "email" -> _formState.value.email.text
            "phone" -> _formState.value.phone.text
            else -> ""
        }
        onFieldChange(field, value)
    }

    /**
     * 각 로그인 폼의 유효성 검사에 대한 결과를 리턴해주는 함수
     */
    private fun isAllValid(state: FormState): Boolean {
        return listOf(
            state.id.error,
            state.password.error,
            state.passwordConfirm.error,
            state.email.error,
            state.phone.error
        ).all { it == null }
    }

    /**
     * 각 로그인 폼의 유효성 검사
     */
    private fun validateId(value: String): String? =
        if (value.isBlank() || value.length <= 7 ) "아이디를 입력해주세요" else null

    //    private fun validatePassword(value: String): String? =
//        if (value.length < 6) "비밀번호는 6자 이상이어야 합니다" else null
    private fun validatePassword(value: String): String? {
        val lengthValid = value.length >= 10
        val hasUpper = value.any { it.isUpperCase() }
        val hasLower = value.any { it.isLowerCase() }
        val hasDigit = value.any { it.isDigit() }
        val hasSpecial = value.any { !it.isLetterOrDigit() }

        return if (!lengthValid || !hasUpper || !hasLower || !hasDigit || !hasSpecial) {
            "비밀번호는 영문 대소문자, 숫자, 특수문자를 모두 포함하여 10자 이상이어야 합니다"
        } else {
            null
        }
    }

    private fun validateConfirmPassword(value: String): String? =
        if (value != formState.value.password.text) "입력한 비밀번호와 다릅니다" else null

    private fun validateEmail(value: String): String? =
        if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) "유효한 이메일 형식이 아닙니다" else null

    private fun validatePhone(value: String): String? =
        if (!value.matches(Regex("^\\d{10,11}$"))) "휴대전화 번호는 숫자 10~11자리로 입력해주세요" else null

    private val _isLoggedIn = MutableStateFlow<Boolean>(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    /**
     * 앱 실행 시, 로그인 여부를 체크하여 콜백해줌.
     */
    init {
        viewModelScope.launch {
            userPrefs.idFlow.collect { id ->
                _isLoggedIn.value = !id.isNullOrEmpty()
            }
        }
    }

    /**
     * 로그인 요청 함수.
     * data store 에 id, pw, emial, phone number 저장
     */
    fun login(id: String, pw: String, email: String, phoneNumber: String) {
        viewModelScope.launch {
            userPrefs.saveUserInfo(
                id,
                pw,
                email,
                phoneNumber
            )
        }
    }

    /**
     * 로그아웃 기능 구현 시 활용
     */
    fun logout() {
        viewModelScope.launch {
            userPrefs.clear()
        }
    }
}