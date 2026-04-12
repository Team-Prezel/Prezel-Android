package com.team.prezel.feature.home.impl.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.onHeightChanged
import com.team.prezel.feature.home.impl.R
import com.team.prezel.feature.home.impl.component.body.HomeBottomSheetContent
import com.team.prezel.feature.home.impl.component.body.HomeBottomSheetTitle
import com.team.prezel.feature.home.impl.component.title.HomeHeroLayout

private data class HomeBottomSheetLayoutState(
    val sheetPeekHeight: Dp,
    val updateTitleSectionHeight: (Dp) -> Unit,
)

private val HomeBottomSheetShadowTopInset = 18.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomePageLayout(
    maxHeight: Dp,
    headerHeight: Dp,
    modifier: Modifier = Modifier,
    sheetContent: @Composable ColumnScope.() -> Unit,
    heroContent: @Composable BoxScope.() -> Unit,
) {
    val layoutState = rememberHomeBottomSheetLayoutState(
        maxHeight = maxHeight,
        headerHeight = headerHeight,
    )

    BottomSheetScaffold(
        modifier = modifier.fillMaxSize(),
        sheetPeekHeight = layoutState.sheetPeekHeight,
        sheetShape = PrezelTheme.shapes.V16,
        sheetContent = {
            Box(modifier = Modifier.padding(top = HomeBottomSheetShadowTopInset)) {
                with(this@BottomSheetScaffold) {
                    sheetContent()
                }
            }
        },
        sheetDragHandle = null,
        containerColor = Color.Transparent,
        sheetContainerColor = Color.Transparent,
        sheetShadowElevation = 0.dp,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = headerHeight),
        ) {
            Box(
                modifier = Modifier.onHeightChanged(layoutState.updateTitleSectionHeight),
                content = heroContent,
            )
        }
    }
}

@Composable
private fun rememberHomeBottomSheetLayoutState(
    maxHeight: Dp,
    headerHeight: Dp,
): HomeBottomSheetLayoutState {
    var titleSectionHeight by remember { mutableStateOf(0.dp) }
    val sheetPeekHeight = remember(maxHeight, headerHeight, titleSectionHeight) {
        (maxHeight - headerHeight - titleSectionHeight + HomeBottomSheetShadowTopInset).coerceAtLeast(0.dp)
    }

    return remember(titleSectionHeight, sheetPeekHeight) {
        HomeBottomSheetLayoutState(
            sheetPeekHeight = sheetPeekHeight,
            updateTitleSectionHeight = { newHeight -> titleSectionHeight = newHeight },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@BasicPreview
@Composable
private fun HomeBodySectionPreview() {
    PrezelTheme {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            HomePageLayout(
                maxHeight = maxHeight,
                headerHeight = 0.dp,
                sheetContent = {
                    HomeBottomSheetContent(
                        contentPadding = PaddingValues(vertical = 32.dp, horizontal = 20.dp),
                    ) {
                        item { HomeBottomSheetTitle(title = "지금부터 연습해보세요") }
                    }
                },
                heroContent = {
                    HomeHeroLayout(
                        backgroundResId = R.drawable.feature_home_impl_section_title_empty,
                    ) { }
                },
            )
        }
    }
}
