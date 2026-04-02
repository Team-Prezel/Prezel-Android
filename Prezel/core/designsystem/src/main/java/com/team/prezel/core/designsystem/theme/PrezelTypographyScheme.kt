package com.team.prezel.core.designsystem.theme

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.team.prezel.core.designsystem.foundation.typography.PrezelTextStyles
import com.team.prezel.core.designsystem.foundation.typography.PrezelTypography
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.preview.PreviewValueRow
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal object PrezelTypographyScheme {
    @Composable
    fun Default() =
        PrezelTypography(
            title1Medium = PrezelTextStyles.Title1Medium.toTextStyle(),
            title1Bold = PrezelTextStyles.Title1Bold.toTextStyle(),
            title1ExtraBold = PrezelTextStyles.Title1ExtraBold.toTextStyle(),
            title2Medium = PrezelTextStyles.Title2Medium.toTextStyle(),
            title2Bold = PrezelTextStyles.Title2Bold.toTextStyle(),
            title2ExtraBold = PrezelTextStyles.Title2ExtraBold.toTextStyle(),
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
    device = "spec:width=850dp,height=600dp",
)
private annotation class TypographyPreview

@TypographyPreview
@Composable
private fun PrezelTypographyTitlePreview() {
    val typography = PrezelTypographyScheme.Default()
    PrezelTypographyPreviewContent(
        title = "Title",
        description = "페이지나 섹션의 주요 제목 등 핵심 정보를 강조하는 데 사용하는 스타일입니다.",
        items = persistentListOf(
            "Title1 Medium" to typography.title1Medium,
            "Title1 Bold" to typography.title1Bold,
            "Title1 Extra Bold" to typography.title1ExtraBold,
            "Title2 Medium" to typography.title2Medium,
            "Title2 Bold" to typography.title2Bold,
            "Title2 Extra Bold" to typography.title2ExtraBold,
        ),
    )
}

@TypographyPreview
@Composable
private fun PrezelTypographyBodyPreview() {
    val typography = PrezelTypographyScheme.Default()
    PrezelTypographyPreviewContent(
        title = "Body",
        description = "일반 본문과 설명, 단일 문장 또는 단락 등의 주된 콘텐츠 전달에 사용하는 스타일입니다.",
        items = persistentListOf(
            "Body1 Regular" to typography.body1Regular,
            "Body1 Medium" to typography.body1Medium,
            "Body1 Bold" to typography.body1Bold,
            "Body2 Regular" to typography.body2Regular,
            "Body2 Medium" to typography.body2Medium,
            "Body2 Bold" to typography.body2Bold,
            "Body3 Regular" to typography.body3Regular,
            "Body3 Medium" to typography.body3Medium,
            "Body3 Bold" to typography.body3Bold,
        ),
    )
}

@TypographyPreview
@Composable
private fun PrezelTypographyCaptionPreview() {
    val typography = PrezelTypographyScheme.Default()
    PrezelTypographyPreviewContent(
        title = "Caption",
        description = "부가 정보, 안내, 설명, 작성 시간 등 메인 콘텐츠를 보조하는 짧은 정보를 전달할 때 사용하는 스타일입니다.",
        items = persistentListOf(
            "Caption1 Regular" to typography.caption1Regular,
            "Caption1 Medium" to typography.caption1Medium,
            "Caption2 Regular" to typography.caption2Regular,
            "Caption2 Medium" to typography.caption2Medium,
        ),
    )
}

@Composable
private fun PrezelTypographyPreviewContent(
    title: String,
    description: String,
    items: ImmutableList<Pair<String, TextStyle>>,
) {
    PreviewSection(
        title = title,
        description = description,
    ) {
        items.forEach { (name, style) ->
            PreviewValueRow(name = name) {
                TypographySampleText(style = style)
            }
        }
    }
}

@Composable
private fun TypographySampleText(
    style: TextStyle,
    sampleKo: String = "이 문장은 디자인과 레이아웃 테스트를 위한 의미 없는 더미 텍스트입니다.",
) {
    Text(
        text = sampleKo,
        style = style,
        color = PrezelTheme.colors.textRegular,
    )
}
