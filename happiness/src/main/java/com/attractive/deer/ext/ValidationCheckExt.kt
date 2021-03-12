package com.attractive.deer.ext

import java.util.regex.Pattern

const val PATTERN_01 = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~`])[A-Za-z[0-9]!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~`]{8,}$" // 숫자,영문 특수문자 포함 8자리이상
const val PATTERN_02 = "^(?=.*[0-9])(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~`])[[0-9]!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~`]{8,}$" // 숫자, 특수문자 포함 8자리이상
const val PATTERN_03 = "^(?=.*[A-Za-z])(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~`])[A-Za-z!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~`]{8,}$" // 영문, 특수문자 포함 8자리이상
const val PATTERN_04 = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{8,}$" // 영문, 숫자, 포함 8자리 이상
const val PATTERN_EMAIL = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$"


/**
 * 비밀번호 유효성 검사
 * 아래 항목을 모두 충족해야 함.
 * 1) 숫자,영문 특수문자 8자리 이상
 * 2) 숫자, 특수문자 8자리 이상
 * 3) 영문, 특수문자 8자리 이상
 * 4) 영문, 숫자, 포함 8자리 이상
 **/
fun String.passwordValidationCheck(): Boolean {
    val match0 = Pattern.compile(PATTERN_01).matcher(this)
    val match1 = Pattern.compile(PATTERN_02).matcher(this)
    val match2 = Pattern.compile(PATTERN_03).matcher(this)
    val match3 = Pattern.compile(PATTERN_04).matcher(this)

    // 특수문자, 영문, 숫자 조합 (8~ 자리)
    if (!(match1.find() || match2.find() || match3.find() || match0.find())) {
        return false
    }

    if (isContinuePattern()) {
        return false
    }
    return true
}


fun String.isContinuePattern(): Boolean {
    var array = arrayListOf("111", "222", "333", "444", "555", "666", "777", "888", "999", "000")
    for (i in array.indices) {
        if (contains(array[i])) {
            return true
        }
    }
    return false
}


fun String.checkEmailPattern(): Boolean {
    val match = Pattern.compile(PATTERN_EMAIL).matcher(this)
    if (!match.matches()) {
        return false
    }
    return true
}