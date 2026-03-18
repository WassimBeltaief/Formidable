package com.wassimbeltaief.formforge.core

import com.wassimbeltaief.formforge.core.state.FormStatus
import kotlinx.coroutines.flow.StateFlow

public interface FormController<T : Any> {
    public val data: T
    public val status: StateFlow<FormStatus>
    public fun validateAllSync(): Boolean
    public fun reset()
    public fun clear()
    public fun setFieldError(field: String, error: String)
}
