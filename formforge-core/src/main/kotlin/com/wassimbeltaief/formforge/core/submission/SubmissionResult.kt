package com.wassimbeltaief.formforge.core.submission

public sealed class SubmissionResult {
    public data class Success<T>(val result: T) : SubmissionResult()
    public data class Error(val error: Throwable) : SubmissionResult()
    public object Cancelled : SubmissionResult()
}
