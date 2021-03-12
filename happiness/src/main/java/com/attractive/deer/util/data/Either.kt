package com.attractive.deer.util.data

/**
 * Separate the left and right values.
 *
 * T: read/write
 * ? extends T: only read
 * ? super T: only write
 *
 * @author 권혁신
 * @version 0.0.8
 * @since 2021-03-08 오전 11:12
 **/
sealed class Either<out L, out R>

/**
 * If the value is left, T determines the type.
 * @author 권혁신
 * @version 0.0.8
 * @since 2021-03-08 오전 11:20
 **/
data class Left<out T>(val value: T) : Either<T, Nothing>()

/**
 * If the value is right, T determines the type.
 * @author 권혁신
 * @version 0.0.8
 * @since 2021-03-08 오전 11:20
 **/
data class Right<out T>(val value: T) : Either<Nothing, T>()

/**
 * Parsing with the left value.
 * @author 권혁신
 * @version 0.0.8
 * @since 2021-03-08 오전 11:21
 **/
fun <T : Any?> T.left(): Either<T, Nothing> = Left(this)

/**
 * Parsing with the right value.
 * @author 권혁신
 * @version 0.0.8
 * @since 2021-03-08 오전 11:21
 **/
fun <T : Any?> T.right(): Either<Nothing, T> = Right(this)

/**
 * Returns to left or right value, null if left value.
 * @author 권혁신
 * @version 0.0.8
 * @since 2021-03-08 오전 11:21
 **/
fun <L : Any?, R : Any?> Either<L, R>.getOrNull(): R? {
    return when (this) {
        is Left -> null
        is Right -> value
    }
}

/**
 * After receiving the Either <L,R> type, parse with if left and right if right
 * - AppResponse<Note> <==> Either<AppError,Note>
 * - L: left type
 * - R: right type
 * - T: result type
 * @author 권혁신
 * @version 0.0.8
 * @since 2021-03-08 오전 11:22
 **/
inline fun <L, R, T> Either<L, R>.fold(left: (L) -> T, right: (R) -> T): T =
    when (this) {
        is Left -> {
            left(value)
        }
        is Right -> {
            right(value)
        }
    }

/**
 * If the response is true, run the inline method and return an error if not.
 * @author 권혁신
 * @version 0.0.8
 * @since 2021-03-08 오전 11:29
 **/
inline fun <L, R, T> Either<L, R>.flatMap(f: (R) -> Either<L, T>): Either<L, T> =
    fold({ this as Left }, f)


/**
 * If the response is true, run the inline method and return an error if not.
 * The difference is that the true result is written as a parameter.
 *
 * @author 권혁신
 * @version 0.0.8
 * @since 2021-03-08 오후 12:48
 **/
inline fun <L, R, T> Either<L, R>.map(f: (R) -> T): Either<L, T> =
    flatMap { Right(f(it)) }

fun <L, R, C, D> Either<L, R>.bimap(
    leftOperation: (L) -> C,
    rightOperation: (R) -> D,
): Either<C, D> = fold(
    { Left(leftOperation(it)) },
    { Right(rightOperation(it)) }
)