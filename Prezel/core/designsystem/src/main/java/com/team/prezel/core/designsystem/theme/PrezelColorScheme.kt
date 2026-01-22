package com.team.prezel.core.designsystem.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.foundation.color.ColorTokens
import com.team.prezel.core.designsystem.foundation.color.PrezelColors
import com.team.prezel.core.designsystem.preview.PreviewScaffold
import com.team.prezel.core.designsystem.preview.SectionTitle
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.preview.TokenRow
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
        iconLarge = ColorTokens.CoolGray900,
        iconDisabled = ColorTokens.CoolGray300,
        borderSmall = ColorTokens.CoolGray50,
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
        iconRegular = ColorTokens.CoolGray500,
        iconMedium = ColorTokens.CoolGray300,
        iconLarge = ColorTokens.Common0,
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
private fun PrezelColorSchemeInteractivePreview() {
    PrezelTheme {
        PrezelColorsPreviewSection(
            title = "Interactive",
            description = "브랜드 색으로 활성화 상태 및 강조가 필요한 요소에 사용하는 색상입니다.",
            items = persistentListOf(
                "XSmall" to PrezelTheme.colors.interactiveXSmall,
                "Small" to PrezelTheme.colors.interactiveSmall,
                "Regular" to PrezelTheme.colors.interactiveRegular,
            ),
        )
    }
}

@ThemePreview
@Composable
private fun PrezelColorSchemeBackgroundPreview() {
    PrezelTheme {
        PrezelColorsPreviewSection(
            title = "Background",
            description = "콘텐츠의 구조와 계층을 구분하는 색상입니다.",
            items = persistentListOf(
                "Regular" to PrezelTheme.colors.bgRegular,
                "Medium" to PrezelTheme.colors.bgMedium,
                "Large" to PrezelTheme.colors.bgLarge,
                "Scrim" to PrezelTheme.colors.bgScrim,
                "Disabled" to PrezelTheme.colors.bgDisabled,
            ),
        )
    }
}

@ThemePreview
@Composable
private fun PrezelColorSchemeTextPreview() {
    PrezelTheme {
        PrezelColorsPreviewSection(
            title = "Text",
            description = "정보와 콘텐츠를 명확하게 전달하기 위해 사용하는 색상입니다.",
            items = persistentListOf(
                "Small" to PrezelTheme.colors.textSmall,
                "Regular" to PrezelTheme.colors.textRegular,
                "Medium" to PrezelTheme.colors.textMedium,
                "Large" to PrezelTheme.colors.textLarge,
                "Disabled" to PrezelTheme.colors.textDisabled,
            ),
        )
    }
}

@ThemePreview
@Composable
private fun PrezelColorSchemeIconPreview() {
    PrezelTheme {
        PrezelColorsPreviewSection(
            title = "Icon",
            description = "기능적 액션과 시각적 커뮤니케이션을 돕기 위해 사용하는 색상입니다.",
            items = persistentListOf(
                "Regular" to PrezelTheme.colors.iconRegular,
                "Medium" to PrezelTheme.colors.iconMedium,
                "Disabled" to PrezelTheme.colors.iconDisabled,
            ),
        )
    }
}

@ThemePreview
@Composable
private fun PrezelColorSchemeBorderPreview() {
    PrezelTheme {
        PrezelColorsPreviewSection(
            title = "Border",
            description = "콘텐츠의 영역과 구조를 구분하는 색상입니다.",
            items = persistentListOf(
                "Small" to PrezelTheme.colors.borderSmall,
                "Regular" to PrezelTheme.colors.borderRegular,
                "Medium" to PrezelTheme.colors.borderMedium,
                "Large" to PrezelTheme.colors.borderLarge,
                "Disabled" to PrezelTheme.colors.borderDisabled,
            ),
        )
    }
}

@ThemePreview
@Composable
private fun PrezelColorSchemeFeedbackPreview() {
    PrezelTheme {
        PrezelColorsPreviewSection(
            title = "Feedback",
            description = "성공, 오류, 경고 상황을 명확하게 전달하기 위해 의미별로 정의된 색상입니다.",
            items = persistentListOf(
                "GoodSmall" to PrezelTheme.colors.feedbackGoodSmall,
                "GoodRegular" to PrezelTheme.colors.feedbackGoodRegular,
                "BadSmall" to PrezelTheme.colors.feedbackBadSmall,
                "BadRegular" to PrezelTheme.colors.feedbackBadRegular,
                "WarningSmall" to PrezelTheme.colors.feedbackWarningSmall,
                "WarningRegular" to PrezelTheme.colors.feedbackWarningRegular,
            ),
        )
    }
}

@ThemePreview
@Composable
private fun PrezelColorSchemeAccentPreview() {
    PrezelTheme {
        PrezelColorsPreviewSection(
            title = "Accent",
            description = "콘텐츠의 주목성과 인지도를 높이기 위해 버튼, 액션, 강조 요소 등에 사용되는 포인트 색상입니다.",
            items = persistentListOf(
                "PurpleSmall" to PrezelTheme.colors.accentPurpleSmall,
                "PurpleRegular" to PrezelTheme.colors.accentPurpleRegular,
                "MagentaSmall" to PrezelTheme.colors.accentMagentaSmall,
                "MagentaRegular" to PrezelTheme.colors.accentMagentaRegular,
            ),
        )
    }
}

@ThemePreview
@Composable
private fun PrezelColorSchemeSolidPreview() {
    PrezelTheme {
        PrezelColorsPreviewSection(
            title = "Solid",
            description = "흰색과 검정색을 제공하여 시각적 대비, 보조, 구분 등에 활용되는 절대값 색상입니다.",
            items = persistentListOf(
                "White" to PrezelTheme.colors.solidWhite,
                "Black" to PrezelTheme.colors.solidBlack,
            ),
        )
    }
}

@Composable
private fun PrezelColorsPreviewSection(
    title: String,
    description: String,
    items: ImmutableList<Pair<String, Color>>,
    colors: PrezelColors = PrezelTheme.colors,
) {
    PreviewScaffold {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            SectionTitle(title = title)
            Text(text = description, color = colors.textLarge.copy(alpha = 0.7f))

            items.forEach { (name, color) ->
                TokenRow(
                    name = name,
                    valueLabel = String.format(Locale.ROOT, "#%08X", color.toArgb()),
                    preview = {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(color)
                                .border(0.3.dp, PrezelTheme.colors.solidBlack, RoundedCornerShape(8.dp)),
                        )
                    },
                )
            }
        }
    }
}
