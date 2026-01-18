package com.team.prezel.core.designsystem.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.prezel.core.designsystem.foundation.color.ColorTokens
import com.team.prezel.core.designsystem.preview.ThemePreview
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.util.Locale

internal object PrezelColorScheme {
    val Light = PrezelColors(
        interactiveXSmall = ColorTokens.Blue10,
        interactiveSmall = ColorTokens.Blue100,
        interactiveRegular = ColorTokens.Blue500,
        bgRegular = ColorTokens.Common0,
        bgMedium = ColorTokens.CoolGray10,
        bgLarge = ColorTokens.CoolGray50,
        bgScrim = ColorTokens.Common1000.copy(alpha = 0.32f),
        bgDisabled = ColorTokens.CoolGray100,
        textSmall = ColorTokens.CoolGray300,
        textRegular = ColorTokens.CoolGray500,
        textMedium = ColorTokens.CoolGray700,
        textLarge = ColorTokens.CoolGray950,
        textDisabled = ColorTokens.CoolGray200,
        iconRegular = ColorTokens.CoolGray600,
        iconMedium = ColorTokens.CoolGray800,
        iconDisabled = ColorTokens.CoolGray300,
        borderSmall = ColorTokens.CoolGray500,
        borderRegular = ColorTokens.CoolGray100,
        borderMedium = ColorTokens.CoolGray200,
        borderLarge = ColorTokens.CoolGray500,
        borderDisabled = ColorTokens.CoolGray300,
        feedbackGoodSmall = ColorTokens.Blue10,
        feedbackGoodRegular = ColorTokens.Blue500,
        feedbackBadSmall = ColorTokens.Coral10,
        feedbackBadRegular = ColorTokens.Coral500,
        feedbackWarningSmall = ColorTokens.Orange10,
        feedbackWarningRegular = ColorTokens.Orange500,
        accentPurpleSmall = ColorTokens.Purple10,
        accentPurpleRegular = ColorTokens.Purple500,
        accentMagentaSmall = ColorTokens.Magenta10,
        accentMagentaRegular = ColorTokens.Magenta500,
        solidWhite = ColorTokens.Common0,
        solidBlack = ColorTokens.Common1000,
    )

    val Dark = PrezelColors(
        interactiveXSmall = ColorTokens.Blue10,
        interactiveSmall = ColorTokens.Blue100,
        interactiveRegular = ColorTokens.Blue500,
        bgRegular = ColorTokens.CoolGray950,
        bgMedium = ColorTokens.CoolGray900,
        bgLarge = ColorTokens.CoolGray800,
        bgScrim = ColorTokens.Common1000.copy(alpha = 0.32f),
        bgDisabled = ColorTokens.CoolGray700,
        textSmall = ColorTokens.CoolGray600,
        textRegular = ColorTokens.CoolGray400,
        textMedium = ColorTokens.CoolGray200,
        textLarge = ColorTokens.CoolGray10,
        textDisabled = ColorTokens.CoolGray700,
        iconRegular = ColorTokens.CoolGray400,
        iconMedium = ColorTokens.CoolGray500,
        iconDisabled = ColorTokens.CoolGray600,
        borderSmall = ColorTokens.CoolGray900,
        borderRegular = ColorTokens.CoolGray800,
        borderMedium = ColorTokens.CoolGray700,
        borderLarge = ColorTokens.CoolGray400,
        borderDisabled = ColorTokens.CoolGray600,
        feedbackGoodSmall = ColorTokens.Blue10,
        feedbackGoodRegular = ColorTokens.Blue500,
        feedbackBadSmall = ColorTokens.Coral10,
        feedbackBadRegular = ColorTokens.Coral600,
        feedbackWarningSmall = ColorTokens.Orange10,
        feedbackWarningRegular = ColorTokens.Orange500,
        accentPurpleSmall = ColorTokens.Purple10,
        accentPurpleRegular = ColorTokens.Purple500,
        accentMagentaSmall = ColorTokens.Magenta10,
        accentMagentaRegular = ColorTokens.Magenta500,
        solidWhite = ColorTokens.Common0,
        solidBlack = ColorTokens.Common1000,
    )
}

@ThemePreview
@Composable
private fun PrezelColorSchemePreview() {
    PrezelTheme {
        PrezelColorsPreview()
    }
}

@Composable
@Suppress("LongMethod")
private fun PrezelColorsPreview(colors: PrezelColors = PrezelTheme.colors) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bgRegular)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        ColorSection(
            title = "Interactive",
            description = "브랜드 색으로 활성화 상태 및 강조가 필요한 요소에 사용하는 색상입니다.",
            items = persistentListOf(
                "XSmall" to colors.interactiveXSmall,
                "Small" to colors.interactiveSmall,
                "Regular" to colors.interactiveRegular,
            ),
            dividerColor = colors.borderRegular,
            textColor = colors.textLarge,
        )

        ColorSection(
            title = "Background",
            description = "콘텐츠의 구조와 계층을 구분하는 색상입니다.",
            items = persistentListOf(
                "Regular" to colors.bgRegular,
                "Medium" to colors.bgMedium,
                "Large" to colors.bgLarge,
                "Scrim" to colors.bgScrim,
                "Disabled" to colors.bgDisabled,
            ),
            dividerColor = colors.borderRegular,
            textColor = colors.textLarge,
        )

        ColorSection(
            title = "Text",
            description = "정보와 콘텐츠를 명확하게 전달하기 위해 사용하는 색상입니다.",
            items = persistentListOf(
                "Small" to colors.textSmall,
                "Regular" to colors.textRegular,
                "Medium" to colors.textMedium,
                "Large" to colors.textLarge,
                "Disabled" to colors.textDisabled,
            ),
            dividerColor = colors.borderRegular,
            textColor = colors.textLarge,
        )

        ColorSection(
            title = "Icon",
            description = "기능적 액션과 시각적 커뮤니케이션을 돕기 위해 사용하는 색상입니다.",
            items = persistentListOf(
                "Regular" to colors.iconRegular,
                "Medium" to colors.iconMedium,
                "Disabled" to colors.iconDisabled,
            ),
            dividerColor = colors.borderRegular,
            textColor = colors.textLarge,
        )

        ColorSection(
            title = "Border",
            description = "콘텐츠의 영역과 구조를 구분하는 색상입니다.",
            items = persistentListOf(
                "Small" to colors.borderSmall,
                "Regular" to colors.borderRegular,
                "Medium" to colors.borderMedium,
                "Large" to colors.borderLarge,
                "Disabled" to colors.borderDisabled,
            ),
            dividerColor = colors.borderRegular,
            textColor = colors.textLarge,
        )

        ColorSection(
            title = "Feedback",
            description = "성공, 오류, 경고 상황을 명확하게 전달하기 위해 의미별로 정의된 색상입니다.",
            items = persistentListOf(
                "GoodSmall" to colors.feedbackGoodSmall,
                "GoodRegular" to colors.feedbackGoodRegular,
                "BadSmall" to colors.feedbackBadSmall,
                "BadRegular" to colors.feedbackBadRegular,
                "WarningSmall" to colors.feedbackWarningSmall,
                "WarningRegular" to colors.feedbackWarningRegular,
            ),
            dividerColor = colors.borderRegular,
            textColor = colors.textLarge,
        )

        ColorSection(
            title = "Accent",
            description = "콘텐츠의 주목성과 인지도를 높이기 위해 버튼, 액션, 강조 요소 등에 사용되는 포인트 색상입니다.",
            items = persistentListOf(
                "PurpleSmall" to colors.accentPurpleSmall,
                "PurpleRegular" to colors.accentPurpleRegular,
                "MagentaSmall" to colors.accentMagentaSmall,
                "MagentaRegular" to colors.accentMagentaRegular,
            ),
            dividerColor = colors.borderRegular,
            textColor = colors.textLarge,
        )

        ColorSection(
            title = "Solid",
            description = "흰색과 검정색을 제공하여 시각적 대비, 보조, 구분 등에 활용되는 절대값 색상입니다.",
            items = persistentListOf(
                "White" to colors.solidWhite,
                "Black" to colors.solidBlack,
            ),
            dividerColor = colors.borderRegular,
            textColor = colors.textLarge,
        )
    }
}

@Composable
private fun ColorSection(
    title: String,
    description: String,
    items: ImmutableList<Pair<String, Color>>,
    dividerColor: Color,
    textColor: Color,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        HorizontalDivider(color = dividerColor)

        Text(text = title, color = textColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Text(text = description, color = textColor.copy(alpha = 0.7f))

        HorizontalDivider(color = dividerColor)

        items.forEach { (name, color) ->
            ColorRow(color = color, name = name, textColor = textColor)
        }
    }
}

@Composable
private fun ColorRow(
    color: Color,
    name: String,
    textColor: Color,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(0.3.dp, PrezelTheme.colors.solidBlack, RoundedCornerShape(8.dp))
                .background(color),
        )

        Text(
            text = name,
            color = textColor,
            modifier = Modifier.weight(1f),
        )

        Text(
            text = String.format(Locale.ROOT, "#%08X", color.toArgb()),
            color = textColor.copy(alpha = 0.7f),
        )
    }
}
