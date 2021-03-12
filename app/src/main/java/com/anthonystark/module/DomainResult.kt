package com.anthonystark.module

import com.attractive.deer.util.data.Either

data class DomainError(val errorCode: Int)
data class SuccessResponse(val message: String)

typealias DomainResult<T> = Either<DomainError, T>