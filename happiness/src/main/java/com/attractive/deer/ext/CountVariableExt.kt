package com.attractive.deer.ext

import java.util.*


fun Int.getCountFormated(): String {
    var followerCount: Float
    val countResult: String
    if (this < 1000) {   //그대로 표시
        return this.toString()
    } else if (this < 10000) {
        //2천 or 1.2천 형태로 표시
        followerCount = this / 1000f
        //내림함수를 이용해서 소수점 첫째자리 까지만 남김.
        followerCount = (Math.floor((followerCount * 10).toDouble()) / 10.0).toFloat()
        //나머지 연산 결과가 0.1 보다 낮으면 소수점 제거하여 보여줌
        countResult = if (followerCount % 1 < 0.1f) {
            (followerCount as Int).toString()+"천"
        } else {
            String.format(Locale.KOREA, "%.1f", followerCount) + "천"
        }
        return countResult
    } else if (this < 100000) {
        //2만 or 1.3만 형태로 표시
        followerCount = this / 10000f

        //내림함수를 이용해서 소수점 첫째자리 까지만 남김.
        followerCount = (Math.floor((followerCount * 10).toDouble()) / 10.0).toFloat()
        //나머지 연산 결과가 0.1 보다 낮으면 소수점 제거하여 보여줌
        countResult = if (followerCount % 1 < 0.001f) {
            (followerCount as Int).toString()+"만"
        } else {
            String.format(Locale.KOREA, "%.1f", followerCount) + "만"
        }
    } else {
        //14만 형태로 표시
        followerCount = this / 10000f
        countResult = (followerCount as Int).toString()+"만"
        return countResult
    }
    return ""

//        when {
//            it.followerCount < 1000 -> //그대로 표시
//            countResult = it.followerCount.toString()
//            it.followerCount < 10000 -> {
//                //2천 or 1.2천 형태로 표시
//                followerCount = it.followerCount.toFloat() / 1000f
//                //내림함수를 이용해서 소수점 첫째자리 까지만 남김.
//                followerCount = (floor(followerCount.toDouble() * 10) /10.0).toFloat()
//
//                //나머지 연산 결과가 0.1 보다 낮으면 소수점 제거하여 보여줌
//                countResult = if((followerCount % 1) < 0.1f){
//                    "${followerCount.toInt()}천"
//                }else {
//                    "${String.format("%.1f", followerCount)}천"
//                }
//            }
//            it.followerCount < 100000 -> {
//                //2만 or 1.3만 형태로 표시
//                followerCount = it.followerCount.toFloat() / 10000f
//
//                //내림함수를 이용해서 소수점 첫째자리 까지만 남김.
//                followerCount = (floor(followerCount.toDouble() * 10) /10.0).toFloat()
//                //나머지 연산 결과가 0.1 보다 낮으면 소수점 제거하여 보여줌
//                countResult = if((followerCount % 1) < 0.001f){
//                    "${followerCount.toInt()}만"
//                }else {
//                    "${String.format("%.1f", followerCount)}만"
//                }
//            }
//                    else -> {
//                //14만 형태로 표시
//                followerCount = it.followerCount.toFloat() / 10000f
//                countResult = "${followerCount.toInt()}만"
//            }
//        }
}