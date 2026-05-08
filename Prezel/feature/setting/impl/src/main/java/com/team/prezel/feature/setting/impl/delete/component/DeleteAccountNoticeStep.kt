package com.team.prezel.feature.setting.impl.delete.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Bullet
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.em
import com.team.prezel.core.designsystem.component.PrezelCheckbox
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.util.noRippleClickable
import com.team.prezel.feature.setting.impl.R

private const val HIGHLIGHT_START_TAG = "<highlight>"
private const val HIGHLIGHT_END_TAG = "</highlight>"
private const val BOLD_START_TAG = "<bold>"
private const val BOLD_END_TAG = "</bold>"

private val supportedTags = listOf(
    HIGHLIGHT_START_TAG,
    HIGHLIGHT_END_TAG,
    BOLD_START_TAG,
    BOLD_END_TAG,
)

@Composable
internal fun DeleteAccountNoticeStep(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = PrezelTheme.spacing.V20)
            .padding(top = PrezelTheme.spacing.V16, bottom = PrezelTheme.spacing.V24),
    ) {
        DeleteAccountNoticeHeader()

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))

        DeleteAccountNoticeDetailBox()

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V36))

        DeletionAgreement(isChecked = isChecked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun DeleteAccountNoticeHeader(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.feature_setting_impl_delete_account_notice_title),
            style = PrezelTheme.typography.title2Bold,
            color = PrezelTheme.colors.textLarge,
        )

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V8))

        DeleteAccountNoticeDescription()
    }
}

@Composable
private fun DeletionAgreement(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable { onCheckedChange(!isChecked) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PrezelCheckbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            extraTouchPadding = PaddingValues(),
        )

        Spacer(modifier = Modifier.width(PrezelTheme.spacing.V8))

        Text(
            text = stringResource(R.string.feature_setting_impl_delete_account_notice_check),
            style = PrezelTheme.typography.body3Regular,
            color = PrezelTheme.colors.textRegular,
        )
    }
}

@Composable
private fun DeleteAccountNoticeDescription(modifier: Modifier = Modifier) {
    val highlightStyle = SpanStyle(color = PrezelTheme.colors.feedbackBadRegular)
    val boldStyle = SpanStyle(
        fontSize = PrezelTheme.typography.body2Bold.fontSize,
        fontWeight = PrezelTheme.typography.body2Bold.fontWeight,
        letterSpacing = PrezelTheme.typography.body2Bold.letterSpacing,
    )

    Text(
        text = buildAnnotatedString {
            appendTaggedText(
                text = stringResource(R.string.feature_setting_impl_delete_account_notice_description),
                highlightStyle = highlightStyle,
                boldStyle = boldStyle,
            )
        },
        modifier = modifier.fillMaxWidth(),
        style = PrezelTheme.typography.body2Regular,
        color = PrezelTheme.colors.textMedium,
    )
}

@Composable
private fun DeleteAccountNoticeDetailBox(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = PrezelTheme.colors.bgMedium,
                shape = PrezelTheme.shapes.V8,
            ).padding(horizontal = PrezelTheme.spacing.V8, vertical = PrezelTheme.spacing.V12),
        verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V8),
    ) {
        DeleteAccountNoticeDetailItem(
            text = stringResource(R.string.feature_setting_impl_delete_account_notice_detail_1),
        )
        DeleteAccountNoticeDetailItem(
            text = stringResource(R.string.feature_setting_impl_delete_account_notice_detail_2),
        )
        DeleteAccountNoticeDetailItem(
            text = stringResource(R.string.feature_setting_impl_delete_account_notice_detail_3),
        )
        DeleteAccountNoticeDetailItem(
            text = stringResource(R.string.feature_setting_impl_delete_account_notice_detail_4),
        )
    }
}

@Composable
private fun DeleteAccountNoticeDetailItem(
    text: String,
    modifier: Modifier = Modifier,
) {
    val highlightStyle = SpanStyle(color = PrezelTheme.colors.feedbackBadRegular)
    val boldStyle = SpanStyle(
        fontSize = PrezelTheme.typography.body3Regular.fontSize,
        fontWeight = PrezelTheme.typography.body3Regular.fontWeight,
        letterSpacing = PrezelTheme.typography.body3Regular.letterSpacing,
    )

    Text(
        text = buildAnnotatedString {
            withBulletList(bullet = Bullet.Default.copy(padding = 0.5.em)) {
                withBulletListItem {
                    appendTaggedText(
                        text = text,
                        highlightStyle = highlightStyle,
                        boldStyle = boldStyle,
                    )
                }
            }
        },
        modifier = modifier.fillMaxWidth(),
        style = PrezelTheme.typography.body3Regular,
        color = PrezelTheme.colors.textMedium,
    )
}

private fun AnnotatedString.Builder.appendTaggedText(
    text: String,
    highlightStyle: SpanStyle,
    boldStyle: SpanStyle,
) {
    if (!isValidTaggedText(text)) {
        append(text)
        return
    }

    var currentIndex = 0
    var tagState = TagState()

    while (currentIndex < text.length) {
        val nextTag = findNextTag(text = text, startIndex = currentIndex)

        if (nextTag == null) {
            appendWithStyle(
                text = text.substring(currentIndex),
                tagState = tagState,
                highlightStyle = highlightStyle,
                boldStyle = boldStyle,
            )
            return
        }

        val (tag, tagIndex) = nextTag
        appendWithCurrentStyle(
            text = text,
            currentIndex = currentIndex,
            tagIndex = tagIndex,
            tagState = tagState,
            highlightStyle = highlightStyle,
            boldStyle = boldStyle,
        )

        tagState = tagState.next(tag) ?: return
        currentIndex = tagIndex + tag.length
    }
}

private fun findNextTag(
    text: String,
    startIndex: Int,
): Pair<String, Int>? =
    supportedTags
        .map { tag -> tag to text.indexOf(tag, startIndex = startIndex) }
        .filter { (_, index) -> index >= 0 }
        .minByOrNull { (_, index) -> index }

private fun AnnotatedString.Builder.appendWithCurrentStyle(
    text: String,
    currentIndex: Int,
    tagIndex: Int,
    tagState: TagState,
    highlightStyle: SpanStyle,
    boldStyle: SpanStyle,
) {
    appendWithStyle(
        text = text.substring(currentIndex, tagIndex),
        tagState = tagState,
        highlightStyle = highlightStyle,
        boldStyle = boldStyle,
    )
}

private fun isValidTaggedText(text: String): Boolean {
    var currentIndex = 0
    var tagState = TagState()

    while (currentIndex < text.length) {
        val nextTag = findNextTag(text = text, startIndex = currentIndex) ?: break
        val (tag, tagIndex) = nextTag

        tagState = tagState.next(tag) ?: return false
        currentIndex = tagIndex + tag.length
    }

    return !tagState.isHighlighting && !tagState.isBold
}

private fun AnnotatedString.Builder.appendWithStyle(
    text: String,
    tagState: TagState,
    highlightStyle: SpanStyle,
    boldStyle: SpanStyle,
) {
    if (text.isEmpty()) return

    val style = tagState.toSpanStyle(
        highlightStyle = highlightStyle,
        boldStyle = boldStyle,
    )

    if (style == null) {
        append(text)
        return
    }

    withStyle(style) {
        append(text)
    }
}

private data class TagState(
    val isHighlighting: Boolean = false,
    val isBold: Boolean = false,
) {
    fun next(tag: String): TagState? =
        when (tag) {
            HIGHLIGHT_START_TAG -> if (isHighlighting) null else copy(isHighlighting = true)
            HIGHLIGHT_END_TAG -> if (!isHighlighting || isBold) null else copy(isHighlighting = false)
            BOLD_START_TAG -> if (isBold) null else copy(isBold = true)
            BOLD_END_TAG -> if (!isBold) null else copy(isBold = false)
            else -> null
        }

    fun toSpanStyle(
        highlightStyle: SpanStyle,
        boldStyle: SpanStyle,
    ): SpanStyle? =
        when {
            isHighlighting && isBold -> boldStyle.merge(highlightStyle)
            isHighlighting -> highlightStyle
            isBold -> boldStyle
            else -> null
        }
}

@BasicPreview
@Composable
private fun DeleteAccountNoticeStepPreview() {
    PrezelTheme {
        DeleteAccountNoticeStep(
            isChecked = false,
            onCheckedChange = {},
        )
    }
}
