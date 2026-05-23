package com.team.prezel.feature.analysis.impl.cache

import java.io.File

/**
 * 발표 분석에 업로드할 파일 Uri를 앱 내부 캐시 파일로 복사한다.
 *
 * 파일 피커에서 받은 content Uri는 네트워크 multipart 업로드에서 바로 File로 다루기 어렵기 때문에,
 * ViewModel은 이 인터페이스를 통해 Android 파일 접근 세부사항을 숨기고 캐시된 File만 전달받는다.
 */
internal interface AnalysisFileCache {
    /**
     * [uriString]이 가리키는 파일 내용을 cacheDir의 임시 파일로 복사하고, 생성된 File을 반환한다.
     *
     * [prefix]는 임시 파일 이름 구분용으로 사용된다. 예: audio, script.
     */
    fun copyUriToCache(
        uriString: String,
        prefix: String,
    ): File
}
