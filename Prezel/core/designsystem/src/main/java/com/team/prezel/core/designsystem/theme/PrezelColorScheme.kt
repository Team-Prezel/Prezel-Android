package com.team.prezel.core.designsystem.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.foundation.color.ColorTokens
import com.team.prezel.core.designsystem.foundation.color.PrezelColors
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.preview.PreviewValueRow
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
        bgLarge = ColorTokens.CoolGray100,
        bgDisabled = ColorTokens.CoolGray50,
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
        accentTealSmall = ColorTokens.Teal10,
        accentTealRegular = ColorTokens.Teal600,
        solidWhite = ColorTokens.Common0,
        solidBlack = ColorTokens.Common1000,
        chipContainerSecondaryFilledDefault = ColorTokens.CoolGray50,
        scrimContainer = ColorTokens.Common1000.copy(alpha = 0.32f),
    )

    val Dark = PrezelColors(
        interactiveXSmall = ColorTokens.Blue10,
        interactiveSmall = ColorTokens.Blue100,
        interactiveRegular = ColorTokens.Blue500,
        bgRegular = ColorTokens.CoolGray950,
        bgMedium = ColorTokens.CoolGray900,
        bgLarge = ColorTokens.CoolGray800,
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
        feedbackBadRegular = ColorTokens.Coral500,
        feedbackWarningSmall = ColorTokens.Orange10,
        feedbackWarningRegular = ColorTokens.Orange500,
        accentPurpleSmall = ColorTokens.Purple10,
        accentPurpleRegular = ColorTokens.Purple500,
        accentTealSmall = ColorTokens.Teal10,
        accentTealRegular = ColorTokens.Teal600,
        solidWhite = ColorTokens.Common0,
        solidBlack = ColorTokens.Common1000,
        chipContainerSecondaryFilledDefault = ColorTokens.CoolGray50,
        scrimContainer = ColorTokens.Common1000.copy(alpha = 0.32f),
    )
}

@BasicPreview
@Composable
private fun PrezelColorSchemeInteractivePreview() {
    PrezelColorsPreviewSection(
        title = "Interactive",
        description = "브랜드 색으로 활성화 상태 및 강조가 필요한 요소에 사용하는 색상입니다.",
        items = persistentListOf(
            "XSmall" to PrezelColorScheme.Light.interactiveXSmall,
            "Small" to PrezelColorScheme.Light.interactiveSmall,
            "Regular" to PrezelColorScheme.Light.interactiveRegular,
        ),
    )
}

@BasicPreview
@Composable
private fun PrezelColorSchemeBackgroundPreview() {
    PrezelColorsPreviewSection(
        title = "Background",
        description = "콘텐츠의 구조와 계층을 구분하는 색상입니다.",
        items = persistentListOf(
            "Regular" to PrezelColorScheme.Light.bgRegular,
            "Medium" to PrezelColorScheme.Light.bgMedium,
            "Large" to PrezelColorScheme.Light.bgLarge,
            "Disabled" to PrezelColorScheme.Light.bgDisabled,
        ),
    )
}

@BasicPreview
@Composable
private fun PrezelColorSchemeTextPreview() {
    PrezelColorsPreviewSection(
        title = "Text",
        description = "정보와 콘텐츠를 명확하게 전달하기 위해 사용하는 색상입니다.",
        items = persistentListOf(
            "Small" to PrezelColorScheme.Light.textSmall,
            "Regular" to PrezelColorScheme.Light.textRegular,
            "Medium" to PrezelColorScheme.Light.textMedium,
            "Large" to PrezelColorScheme.Light.textLarge,
            "Disabled" to PrezelColorScheme.Light.textDisabled,
        ),
    )
}

@BasicPreview
@Composable
private fun PrezelColorSchemeIconPreview() {
    PrezelColorsPreviewSection(
        title = "Icon",
        description = "기능적 액션과 시각적 커뮤니케이션을 돕기 위해 사용하는 색상입니다.",
        items = persistentListOf(
            "Regular" to PrezelColorScheme.Light.iconRegular,
            "Medium" to PrezelColorScheme.Light.iconMedium,
            "Disabled" to PrezelColorScheme.Light.iconDisabled,
        ),
    )
}

@BasicPreview
@Composable
private fun PrezelColorSchemeBorderPreview() {
    PrezelColorsPreviewSection(
        title = "Border",
        description = "콘텐츠의 영역과 구조를 구분하는 색상입니다.",
        items = persistentListOf(
            "Small" to PrezelColorScheme.Light.borderSmall,
            "Regular" to PrezelColorScheme.Light.borderRegular,
            "Medium" to PrezelColorScheme.Light.borderMedium,
            "Large" to PrezelColorScheme.Light.borderLarge,
            "Disabled" to PrezelColorScheme.Light.borderDisabled,
        ),
    )
}

@BasicPreview
@Composable
private fun PrezelColorSchemeFeedbackPreview() {
    PrezelColorsPreviewSection(
        title = "Feedback",
        description = "성공, 오류, 경고 상황을 명확하게 전달하기 위해 의미별로 정의된 색상입니다.",
        items = persistentListOf(
            "GoodSmall" to PrezelColorScheme.Light.feedbackGoodSmall,
            "GoodRegular" to PrezelColorScheme.Light.feedbackGoodRegular,
            "BadSmall" to PrezelColorScheme.Light.feedbackBadSmall,
            "BadRegular" to PrezelColorScheme.Light.feedbackBadRegular,
            "WarningSmall" to PrezelColorScheme.Light.feedbackWarningSmall,
            "WarningRegular" to PrezelColorScheme.Light.feedbackWarningRegular,
        ),
    )
}

@BasicPreview
@Composable
private fun PrezelColorSchemeAccentPreview() {
    PrezelColorsPreviewSection(
        title = "Accent",
        description = "콘텐츠의 주목성과 인지도를 높이기 위해 버튼, 액션, 강조 요소 등에 사용되는 포인트 색상입니다.",
        items = persistentListOf(
            "PurpleSmall" to PrezelColorScheme.Light.accentPurpleSmall,
            "PurpleRegular" to PrezelColorScheme.Light.accentPurpleRegular,
            "TealSmall" to PrezelColorScheme.Light.accentTealSmall,
            "TealRegular" to PrezelColorScheme.Light.accentTealRegular,
        ),
    )
}

@BasicPreview
@Composable
private fun PrezelColorSchemeSolidPreview() {
    PrezelColorsPreviewSection(
        title = "Solid",
        description = "흰색과 검정색을 제공하여 시각적 대비, 보조, 구분 등에 활용되는 절대값 색상입니다.",
        items = persistentListOf(
            "White" to PrezelColorScheme.Light.solidWhite,
            "Black" to PrezelColorScheme.Light.solidBlack,
        ),
    )
}

@BasicPreview
@Composable
private fun PrezelColorSchemeComponentPreview() {
    PrezelColorsPreviewSection(
        title = "Component",
        description = "컴포넌트를 위한 색상입니다.",
        items = persistentListOf(
            "Chip_Container_Secondary_Filled_Default" to PrezelColorScheme.Light.chipContainerSecondaryFilledDefault,
            "Scrim_Container" to PrezelColorScheme.Light.scrimContainer,
        ),
    )
}

@Composable
private fun PrezelColorsPreviewSection(
    title: String,
    description: String,
    items: ImmutableList<Pair<String, Color>>,
) {
    PreviewSection(
        title = title,
        description = description,
    ) {
        items.forEach { (name, color) ->
            PreviewValueRow(
                name = name,
                valueLabel = String.format(Locale.ROOT, "#%08X", color.toArgb()),
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(color)
                        .border(0.3.dp, PrezelTheme.colors.solidBlack, RoundedCornerShape(8.dp)),
                )
            }
        }
    }
}
