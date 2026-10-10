package com.team.prezel.core.common.fcm

data class AnalysisResultFcmPayload(
    val presentationId: Long,
    val analysisResultId: Long?,
    val status: AnalysisResultStatus,
    val cardStatus: String,
    val errorType: String?,
    val script: String?,
    val audioUrl: String?,
) {
    val isCompleted: Boolean = status == AnalysisResultStatus.COMPLETED

    fun toDataMap(): Map<String, String> =
        buildMap {
            put(KEY_TYPE, TYPE_ANALYSIS_RESULT)
            put(KEY_PRESENTATION_ID, presentationId.toString())
            analysisResultId?.let { put(KEY_ANALYSIS_RESULT_ID, it.toString()) }
            put(KEY_STATUS, status.value)
            put(KEY_CARD_STATUS, cardStatus)
            put(KEY_ERROR_TYPE, errorType.orEmpty())
            put(KEY_SCRIPT, script.orEmpty())
            put(KEY_AUDIO_URL, audioUrl.orEmpty())
        }

    companion object {
        const val TYPE_ANALYSIS_RESULT = "ANALYSIS_RESULT"

        const val KEY_TYPE = "type"
        const val KEY_PRESENTATION_ID = "presentationId"
        const val KEY_ANALYSIS_RESULT_ID = "analysisResultId"
        const val KEY_STATUS = "status"
        const val KEY_CARD_STATUS = "cardStatus"
        const val KEY_ERROR_TYPE = "errorType"
        const val KEY_SCRIPT = "script"
        const val KEY_AUDIO_URL = "audioUrl"

        fun from(data: Map<String, String>): AnalysisResultFcmPayload? {
            if (data[KEY_TYPE] != TYPE_ANALYSIS_RESULT) return null

            val presentationId = data[KEY_PRESENTATION_ID]?.toLongOrNull() ?: return null
            val status = data[KEY_STATUS]?.let(AnalysisResultStatus::from) ?: return null

            return AnalysisResultFcmPayload(
                presentationId = presentationId,
                analysisResultId = data[KEY_ANALYSIS_RESULT_ID]?.toLongOrNull(),
                status = status,
                cardStatus = data[KEY_CARD_STATUS].orEmpty(),
                errorType = data[KEY_ERROR_TYPE].emptyToNull(),
                script = data[KEY_SCRIPT].emptyToNull(),
                audioUrl = data[KEY_AUDIO_URL].emptyToNull(),
            )
        }
    }
}

enum class AnalysisResultStatus(
    val value: String,
) {
    COMPLETED("COMPLETED"),
    ERR_VOICE_RECOG("ERR_VOICE_RECOG"),
    ERR_FILE_RECOG("ERR_FILE_RECOG"),
    ERR_ANALYSIS("ERR_ANALYSIS"),
    ;

    companion object {
        fun from(value: String): AnalysisResultStatus? = entries.firstOrNull { it.value == value }
    }
}

private fun String?.emptyToNull(): String? = this?.takeIf(String::isNotBlank)
