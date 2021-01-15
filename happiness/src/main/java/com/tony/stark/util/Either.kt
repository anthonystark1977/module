package com.tony.stark.util

sealed class Either<out L, out R>

data class Left<out T>(val value: T) : Either<T, Nothing>()
data class Right<out T>(val value: T) : Either<Nothing, T>()

fun <T : Any?> T.left(): Either<T, Nothing> = Left(this)
fun <T : Any?> T.right(): Either<Nothing, T> = Right(this)

fun <L : Any?, R : Any?> Either<L, R>.getOrNull(): R? {
  return when (this) {
    is Left -> null
    is Right -> value
  }
}

inline fun <L, R, T> Either<L, R>.fold(left: (L) -> T, right: (R) -> T): T =
  when (this) {
    is Left -> left(value)
    is Right -> right(value)
  }

inline fun <L, R, T> Either<L, R>.flatMap(f: (R) -> Either<L, T>): Either<L, T> =
  fold({ this as Left }, f)

inline fun <L, R, T> Either<L, R>.map(f: (R) -> T): Either<L, T> =
  flatMap { Right(f(it)) }

fun <L, R, C, D> Either<L, R>.bimap(
  leftOperation: (L) -> C,
  rightOperation: (R) -> D,
): Either<C, D> = fold(
  { Left(leftOperation(it)) },
  { Right(rightOperation(it)) }
)