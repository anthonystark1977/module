package com.anthonystark.module.librarytest

import com.anthonystark.module.DomainError
import com.anthonystark.module.DomainResult
import com.anthonystark.module.SuccessResponse
import com.attractive.deer.util.data.*


/**
 * [flatMap]
 *
 * flatMap error시 밖으로 빠져나감.
 * map도 마찬가지 단순히 flatMap에서 right() 파싱해준 것.
 * @author Scarlett
 * @version 0.0.8
 * @since 2021-03-08 오전 11:54
 **/
fun flatMapTest() {
    val result: DomainResult<SuccessResponse> = SuccessResponse("Success Message").right()
    result.flatMap {
        val inlineResult = DomainError(404).left()
        inlineResult.map {
            SuccessResponse(
                "inline Success!!"
            )
        }
    }.fold({
        it
    }, {
        it
    }).let {

    }
}

/*
inline fun <L, R, T> Either<L, R>.flatMap(f: (R) -> Either<L, T>): Either<L, T> =
    fold({ this as Left }, f)*/
