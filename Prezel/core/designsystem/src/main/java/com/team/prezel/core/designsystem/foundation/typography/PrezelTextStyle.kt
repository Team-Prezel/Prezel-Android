import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import com.team.prezel.core.designsystem.foundation.typography.PrezelFontLetterSpacing
import com.team.prezel.core.designsystem.foundation.typography.PrezelFontLineHeight
import com.team.prezel.core.designsystem.foundation.typography.PrezelFontSize
import com.team.prezel.core.designsystem.foundation.typography.PrezelFontWeight

data class PrezelTextStyle(
    val fontWeight: PrezelFontWeight,
    val fontSize: PrezelFontSize,
    val lineHeight: PrezelFontLineHeight,
    val letterSpacing: PrezelFontLetterSpacing,
) {
    @Composable
    fun toTextStyle(): TextStyle {
        val size = fontSize.sp()

        return TextStyle(
            fontWeight = fontWeight.value,
            fontSize = size,
            lineHeight = lineHeight.value,
            letterSpacing = letterSpacing.multiply(size),
        )
    }
}
