package com.team.prezel.core.designsystem.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import com.team.prezel.core.designsystem.foundation.typography.PrezelTextStyles
import com.team.prezel.core.designsystem.foundation.typography.PrezelTypography
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal object PrezelTypographyScheme {
    @Composable
    fun Default() =
        PrezelTypography(
            title1Medium = PrezelTextStyles.Title1Medium.toTextStyle(),
            title1Bold = PrezelTextStyles.Title1Bold.toTextStyle(),
            title2Medium = PrezelTextStyles.Title2Medium.toTextStyle(),
            title2Bold = PrezelTextStyles.Title2Bold.toTextStyle(),
            body1Regular = PrezelTextStyles.Body1Regular.toTextStyle(),
            body1Medium = PrezelTextStyles.Body1Medium.toTextStyle(),
            body1Bold = PrezelTextStyles.Body1Bold.toTextStyle(),
            body2Regular = PrezelTextStyles.Body2Regular.toTextStyle(),
            body2Medium = PrezelTextStyles.Body2Medium.toTextStyle(),
            body2Bold = PrezelTextStyles.Body2Bold.toTextStyle(),
            body3Regular = PrezelTextStyles.Body3Regular.toTextStyle(),
            body3Medium = PrezelTextStyles.Body3Medium.toTextStyle(),
            body3Bold = PrezelTextStyles.Body3Bold.toTextStyle(),
            caption1Regular = PrezelTextStyles.Caption1Regular.toTextStyle(),
            caption1Medium = PrezelTextStyles.Caption1Medium.toTextStyle(),
            caption2Regular = PrezelTextStyles.Caption2Regular.toTextStyle(),
            caption2Medium = PrezelTextStyles.Caption2Medium.toTextStyle(),
        )
}

@Preview(
    showBackground = true,
    device = "spec:width=800dp,height=1900dp",
    fontScale = 10f,
)
@Composable
private fun PrezelTypographySchemePreview() {
    PrezelTheme {
        PrezelTypographyPreviewContent()
    }
}

@Composable
private fun PrezelTypographyPreviewContent(
    typography: PrezelTypography = PrezelTheme.typography,
    items: ImmutableList<Triple<String, String, TextStyle>> = persistentListOf(
        Triple("Title", "Title1 Medium", typography.title1Medium),
        Triple("Title", "Title1 Bold", typography.title1Bold),
        Triple("Title", "Title2 Medium", typography.title2Medium),
        Triple("Title", "Title2 Bold", typography.title2Bold),
        Triple("Body", "Body1 Regular", typography.body1Regular),
        Triple("Body", "Body1 Medium", typography.body1Medium),
        Triple("Body", "Body1 Bold", typography.body1Bold),
        Triple("Body", "Body2 Regular", typography.body2Regular),
        Triple("Body", "Body2 Medium", typography.body2Medium),
        Triple("Body", "Body2 Bold", typography.body2Bold),
        Triple("Body", "Body3 Regular", typography.body3Regular),
        Triple("Body", "Body3 Medium", typography.body3Medium),
        Triple("Body", "Body3 Bold", typography.body3Bold),
        Triple("Caption", "Caption1 Regular", typography.caption1Regular),
        Triple("Caption", "Caption1 Medium", typography.caption1Medium),
        Triple("Caption", "Caption2 Regular", typography.caption2Regular),
        Triple("Caption", "Caption2 Medium", typography.caption2Medium),
    ),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrezelTheme.colors.bgRegular)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items
            .groupBy { it.first }
            .forEach { (group, groupedItems) ->
                HorizontalDivider()
                Text(text = group, style = typography.title1Bold)
                HorizontalDivider()

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    groupedItems.forEach { item ->
                        TypographyRow(name = item.second, style = item.third)
                    }
                }
            }
    }
}

@Composable
private fun TypographyRow(
    name: String,
    style: TextStyle,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = PrezelTheme.colors.bgRegular,
                shape = RoundedCornerShape(12.dp),
            ),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            val fontSize = style.fontSize
            val lineHeight = style.lineHeight
            val letterSpacing = style.letterSpacing
            val fontWeight = style.fontWeight

            Text(
                text = buildString {
                    append("$name (")
                    append("size: $fontSize")
                    if (lineHeight.isSpecified) append(" · lh: $lineHeight")
                    if (letterSpacing.isSpecified) append(" · ls: $letterSpacing")
                    if (fontWeight != null) append(" · w: ${fontWeight.weight}")
                    append(")")
                },
                style = PrezelTheme.typography.body3Bold,
                color = PrezelTheme.colors.textMedium,
            )
        }

        TypographySampleText(style = style)
    }
}

@Composable
private fun TypographySampleText(
    style: TextStyle,
    sampleKo: String = "이 문장은 글꼴의 형태와 가독성을 확인하기 위한 예시입니다.",
    sampleEn: String = "This sentence is an example for checking font shape and readability.",
) {
    Column(
        modifier = Modifier
            .background(
                color = PrezelTheme.colors.bgLarge,
                shape = RoundedCornerShape(12.dp),
            ).padding(8.dp),
    ) {
        Text(
            text = sampleKo,
            style = style,
            color = PrezelTheme.colors.textLarge,
        )
        Text(
            text = sampleEn,
            style = style,
            color = PrezelTheme.colors.textLarge,
        )
    }
}
