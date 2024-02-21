
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBasicTextField(){
    val interactionSource = remember { MutableInteractionSource() }
    var input by rememberSaveable { mutableStateOf("Hello World! \n" +
            "Hello World!") }
    BasicTextField(
        value = input,
        onValueChange = {input = it},
        singleLine = false,
        modifier = Modifier
            .fillMaxWidth()
//            .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
//            .padding(horizontal = 5.dp, vertical = 5.dp)
            .height(70.dp),
        decorationBox = { innerTextField ->
            Box(
                Modifier
                .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                    .padding(horizontal = 5.dp, vertical = 5.dp)
            ) {
                innerTextField()
            }
        }
    )
}

@Composable
fun CustomOutlinedTextField(){
    var input by rememberSaveable { mutableStateOf("Hello World! \nHello World!") }
    OutlinedTextField(
        value = input,
        onValueChange = {input = it},
        singleLine = false,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        colors = OutlinedTextFieldDefaults.colors(
            errorBorderColor = Color.LightGray,
            focusedBorderColor =  Color.LightGray,
            unfocusedBorderColor =  Color.LightGray,
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewTextField() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(20.dp)
            .background(Color.White)
    ) {
        CustomBasicTextField()
    }
}