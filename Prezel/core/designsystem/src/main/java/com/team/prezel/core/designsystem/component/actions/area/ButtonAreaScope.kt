package com.team.prezel.core.designsystem.component.actions.area

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team.prezel.core.designsystem.component.actions.button.PrezelButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonDefault

/**
 * [PrezelButtonArea] 안에서 액션 버튼을 선언할 때 사용하는 scope입니다.
 *
 * `MainButton`을 먼저 선언하고 이어서 보조 버튼 하나를 추가하는 구성을 전제로 합니다.
 */
@LayoutScopeMarker
@Suppress("FunctionName")
interface ButtonAreaScope {
    /** 주요 액션 버튼을 추가합니다. */
    fun MainButton(
        @DrawableRes iconResId: Int? = null,
        label: String,
        enabled: Boolean = true,
        onClick: () -> Unit,
    )

    /** 보조 액션 버튼을 추가합니다. */
    fun SubButton(
        @DrawableRes iconResId: Int? = null,
        label: String,
        enabled: Boolean = true,
        onClick: () -> Unit,
    )

    /** 버튼 프리셋을 직접 지정해 커스텀 액션 버튼을 추가합니다. */
    fun CustomButton(
        @DrawableRes iconResId: Int? = null,
        label: String,
        enabled: Boolean,
        onClick: () -> Unit,
        config: PrezelButtonDefault,
    )
}

internal class DefaultButtonAreaScope : ButtonAreaScope {
    private val _buttons = mutableListOf<@Composable (Modifier) -> Unit>()
    internal val buttons: List<@Composable (Modifier) -> Unit> get() = _buttons.toList()

    override fun MainButton(
        @DrawableRes iconResId: Int?,
        label: String,
        enabled: Boolean,
        onClick: () -> Unit,
    ) {
        validateButtonCount()

        _buttons += { modifier ->
            PrezelButton(
                modifier = modifier,
                text = label,
                iconResId = iconResId,
                onClick = onClick,
                enabled = enabled,
                type = ButtonType.FILLED,
                hierarchy = ButtonHierarchy.PRIMARY,
            )
        }
    }

    override fun SubButton(
        @DrawableRes iconResId: Int?,
        label: String,
        enabled: Boolean,
        onClick: () -> Unit,
    ) {
        validateButtonCount()

        _buttons += { modifier ->
            PrezelButton(
                modifier = modifier,
                text = label,
                iconResId = iconResId,
                onClick = onClick,
                enabled = enabled,
                type = ButtonType.FILLED,
                hierarchy = ButtonHierarchy.SECONDARY,
            )
        }
    }

    override fun CustomButton(
        @DrawableRes iconResId: Int?,
        label: String,
        enabled: Boolean,
        onClick: () -> Unit,
        config: PrezelButtonDefault,
    ) {
        validateButtonCount()

        _buttons += { modifier ->
            PrezelButton(
                modifier = modifier,
                text = label,
                iconResId = iconResId,
                onClick = onClick,
                enabled = enabled,
                config = config,
            )
        }
    }

    private fun validateButtonCount() {
        require(buttons.size <= 2) { "버튼은 최대 2개까지 선언할 수 있습니다." }
    }
}
