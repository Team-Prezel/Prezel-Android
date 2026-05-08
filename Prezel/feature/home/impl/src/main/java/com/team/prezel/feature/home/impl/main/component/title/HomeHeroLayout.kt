package com.team.prezel.feature.home.impl.main.component.title

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.home.impl.R

@Composable
internal fun HomeHeroLayout(
    @DrawableRes backgroundResId: Int,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(4f / 3f)
            .paint(
                painter = painterResource(id = backgroundResId),
                contentScale = ContentScale.FillWidth,
            ).padding(all = PrezelTheme.spacing.V20),
        content = content,
    )
}

@BasicPreview
@Composable
private fun HomeHeroLayoutPreview() {
    PrezelTheme {
        HomeHeroLayout(
            backgroundResId = R.drawable.feature_home_impl_section_title_empty,
        ) {
            Text(
                text = "Home Title Section",
                style = PrezelTheme.typography.title1Bold,
                color = PrezelTheme.colors.textRegular,
            )
        }
    }
}
