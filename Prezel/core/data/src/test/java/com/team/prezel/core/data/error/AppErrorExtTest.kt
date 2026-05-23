package com.team.prezel.core.data.error

import com.team.prezel.core.common.error.AppError
import com.team.prezel.core.common.error.AppException
import com.team.prezel.core.network.model.ApiException
import com.team.prezel.core.network.model.ServerErrorCode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class AppErrorExtTest {
    @Test
    fun `지원하지 않는 파일 형식 에러는 잘못된 요청으로 변환한다`() {
        val result = Result
            .failure<Unit>(
                ApiException(
                    status = 400,
                    errorCode = ServerErrorCode.FILE_UPLOAD_FAILED,
                    message = "지원하지 않는 오디오 파일 형식입니다.",
                ),
            ).mapDomainFailure()

        val exception = assertIs<AppException>(result.exceptionOrNull())
        assertEquals(AppError.INVALID_REQUEST, exception.error)
        assertEquals("지원하지 않는 오디오 파일 형식입니다.", exception.message)
    }
}
