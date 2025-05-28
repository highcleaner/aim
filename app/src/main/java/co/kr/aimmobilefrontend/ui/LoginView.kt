package co.kr.aimmobilefrontend.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LoginScreen()
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    formState: FormState = FormState(),
    onFieldChange: (String, String) -> Unit = { s: String, s1: String -> },
    onFieldFocusLost: (String) -> Unit = { s: String -> },
    isLoggedIn : Boolean = false,
    isAutoLogin : ()-> Unit = {},
    onLoginClick: (String, String, String,String) -> Unit = {
            id : String, pwd : String, email : String, phoneNumber : String ->}
){
    /**
     * 로그인이 되어있는지 아닌지 체크하는 코드
     */
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            isAutoLogin()
        }
    }

    val isDarkMode = isSystemInDarkTheme()
    val headerColor = if (isDarkMode) Color(0xFF1C1C1E) else Color(0xFFF7F7F7)
    val textColor = if (isDarkMode) Color.White else Color.Black

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "로그인",
                        color = textColor
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = headerColor
                )
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            //로그인이 안되어있을때만 로그인 화면 활성화
            if (!isLoggedIn) {
                LoginFormUI(
                    formState = formState,
                    onFieldChange = onFieldChange,
                    onFieldFocusLost = onFieldFocusLost,
                    onLoginClick = onLoginClick
                )
            }
        }
    }
}

/**
 * 오토 로그인 기능 적용을 위한 view 분리
 */
@Composable
fun LoginFormUI(
    formState: FormState = FormState(),
    onFieldChange: (String, String) -> Unit = { s: String, s1: String -> },
    onFieldFocusLost: (String) -> Unit = { s: String -> },
    onLoginClick: (String, String, String,String) -> Unit = {
        id : String, pwd : String, email : String, phoneNumber : String ->}
) {

    //포커스 인식 및 포커스 전달을 위한 변수 리스트
    val focusManager = LocalFocusManager.current
    val idFocus = remember { FocusRequester() }
    val pwFocus = remember { FocusRequester() }
    val pwConfirmFocus = remember { FocusRequester() }
    val emailFocus = remember { FocusRequester() }
    val phoneFocus = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {


        /**
         * 입력폼 리스트
         */
        Column(Modifier.padding(16.dp)) {
            ValidatedTextField(
                label = "아이디",
                value = formState.id.text,
                error = formState.id.error,
                imeAction = ImeAction.Next,
                modifier = Modifier.focusRequester(idFocus)
                ,
                onImeAction = { pwFocus.requestFocus() },
                onValueChange = { onFieldChange("id", it) },
                onFocusLost = { onFieldFocusLost("id") }
            )

            ValidatedTextField(
                label = "비밀번호",
                value = formState.password.text,
                error = formState.password.error,
                modifier = Modifier.focusRequester(pwFocus)
                ,
                onValueChange = { onFieldChange("password", it) },
                onFocusLost = { onFieldFocusLost("password") },
                visualTransformation = PasswordVisualTransformation(),
                imeAction = ImeAction.Next,
                onImeAction = { pwConfirmFocus.requestFocus() }
            )
            ValidatedTextField(
                label = "비밀번호 확인",
                value = formState.passwordConfirm.text,
                error = formState.passwordConfirm.error,
                modifier = Modifier.focusRequester(pwConfirmFocus)
                ,
                onValueChange = { onFieldChange("passwordConfirm", it) },
                onFocusLost = { onFieldFocusLost("passwordConfirm") },
                visualTransformation = PasswordVisualTransformation(),
                imeAction = ImeAction.Next,
                onImeAction = { emailFocus.requestFocus() }
            )

            ValidatedTextField(
                label = "이메일",
                value = formState.email.text,
                error = formState.email.error,
                modifier = Modifier.focusRequester(emailFocus)
                ,
                onValueChange = { onFieldChange("email", it) },
                onFocusLost = { onFieldFocusLost("email") },
                imeAction = ImeAction.Next,
                onImeAction = { phoneFocus.requestFocus() }
            )

            ValidatedTextField(
                label = "휴대전화 번호",
                value = formState.phone.text,
                error = formState.phone.error,
                onValueChange = { onFieldChange("phone", it) },
                onFocusLost = { onFieldFocusLost("phone") },
                modifier = Modifier.focusRequester(phoneFocus)
                ,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                imeAction = ImeAction.Done,
                onImeAction = { focusManager.clearFocus() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    onLoginClick(
                        formState.id.text,
                        formState.password.text,
                        formState.email.text,
                        formState.phone.text
                    )
                },
                enabled = formState.isValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("로그인")
            }

            Spacer(modifier = Modifier.height(12.dp))

        }
    }
}

/**
 * Input text field 컴포넌트 작성
 */
@Composable
fun ValidatedTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    error: String?,
    onFocusLost: () -> Unit,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: (() -> Unit)? = null

) {
    val interactionSource = remember { MutableInteractionSource() }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = error != null,
        interactionSource = interactionSource,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions.copy(imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onNext = { onImeAction?.invoke() },
            onDone = { onImeAction?.invoke() }
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .onFocusChanged {
                if (!it.isFocused) onFocusLost()
            },
        singleLine = true

    )
    if (error != null) {
        Text(
            text = error,
            color = Color.Red,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
        )
    }
}
