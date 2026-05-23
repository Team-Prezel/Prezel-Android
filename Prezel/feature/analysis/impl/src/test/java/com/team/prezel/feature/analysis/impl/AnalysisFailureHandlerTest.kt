package com.team.prezel.feature.analysis.impl

import com.team.prezel.core.common.error.AppError
import com.team.prezel.core.common.error.AppException
import com.team.prezel.feature.analysis.impl.contract.AnalysisUploadType
import com.team.prezel.feature.analysis.impl.model.AnalysisUiMessage
import kotlin.test.Test
import kotlin.test.assertEquals

class AnalysisFailureHandlerTest {
    @Test
    fun `잘못된 요청은 음성 파일 재업로드 화면으로 처리한다`() {
        val action = AppException(
            error = AppError.INVALID_REQUEST,
            message = "지원하지 않는 오디오 파일 형식입니다.",
        ).toAnalysisFailureAction()

        assertEquals(
            AnalysisFailureAction.RetryFileUpload(uploadType = AnalysisUploadType.AUDIO),
            action,
        )
    }

    @Test
    fun `인증 에러는 인증 만료 메시지로 처리한다`() {
        val action = AppException(
            error = AppError.UNAUTHORIZED,
            message = "인증 자격 증명이 유효하지 않습니다.",
        ).toAnalysisFailureAction()

        assertEquals(
            AnalysisFailureAction.ShowMessage(message = AnalysisUiMessage.AUTH_EXPIRED),
            action,
        )
    }

    @Test
    fun `서버 에러는 분석 실패 메시지로 처리한다`() {
        val action = AppException(
            error = AppError.SERVER_ERROR,
            message = "AI 분석 서버와의 통신 중 오류가 발생했습니다.",
        ).toAnalysisFailureAction()

        assertEquals(
            AnalysisFailureAction.ShowMessage(message = AnalysisUiMessage.ANALYSIS_FAILED),
            action,
        )
    }
}
