package com.team.prezel.feature.my.impl

import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.my.impl.contract.MyUiEffect
import com.team.prezel.feature.my.impl.contract.MyUiIntent
import com.team.prezel.feature.my.impl.contract.MyUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class MyViewModel @Inject constructor() : BaseViewModel<MyUiState, MyUiIntent, MyUiEffect>(MyUiState()) {
    override fun onIntent(intent: MyUiIntent) {
        // todo: 프로필 화면 구현 작업에서 진행
    }
}
