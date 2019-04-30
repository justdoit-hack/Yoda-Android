package com.justdoit.yoda

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

object GeneralSystem {
    @SuppressLint("SimpleDateFormat")
    fun parseFromISO8601(dateString: String): String {
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'")
        val dt = df.parse(dateString)
        val df2 = SimpleDateFormat("yyyy/MM/dd HH:mm")
        return df2.format(dt)
    }

    val suggests = listOf(
        // 配列で扱いたいため0スタート
        listOf("ﾜ", "ｦ", "ﾝ", "ﾞ", "ﾟ", "6", "7", "8", "9", "0"),
        listOf("ｱ", "ｲ", "ｳ", "ｴ", "ｵ", "A", "B", "C", "D", "E"),
        listOf("ｶ", "ｷ", "ｸ", "ｹ", "ｺ", "F", "G", "H", "I", "J"),
        listOf("ｻ", "ｼ", "ｽ", "ｾ", "ｿ", "K", "L", "M", "N", "O"),
        listOf("ﾀ", "ﾁ", "ﾂ", "ﾃ", "ﾄ", "P", "Q", "R", "S", "T"),
        listOf("ﾅ", "ﾆ", "ﾇ", "ﾈ", "ﾉ", "U", "V", "W", "X", "Y"),
        listOf("ﾊ", "ﾋ", "ﾌ", "ﾍ", "ﾎ", "Z", "?", "!", "-", "/"),
        listOf("ﾏ", "ﾐ", "ﾑ", "ﾒ", "ﾓ", "¥", "&", "", "☎", ""),
        listOf("ﾔ", "(", "ﾕ", ")", "ﾖ", "*", "#", "空白", "♥", ""),
        listOf("ﾗ", "ﾘ", "ﾙ", "ﾚ", "ﾛ", "1", "2", "3", "4", "5"),
        listOf("ｱ", "ｶ", "ｻ", "ﾀ", "ﾅ", "ﾊ", "ﾏ", "ﾔ", "ﾗ", "ﾜ") //何も入力されてない or 入力終了時
    )

    fun getSuggest(index: Int): SuggestArray {
        return SuggestArray(suggests[index])
    }

    class SuggestArray(val list: List<String>) {
        fun getItem(index: Int): String {
            return list[index]
        }
    }

    // 全変換
    fun toParse(number: String): String {
        var num = ""
        var text = ""
        for (i in 0 until number.length) {
            num += number[i]
            if (num.length == 2) {
                text = "$text${toParseFromTwoNumber(num.toInt())}"
                num = ""
            }
        }
        return text
    }

    // 二桁のみ変換
    fun toParseFromTwoNumber(twoNumber: Int): String {
        val one = twoNumber / 10
        var two = (twoNumber % 10) - 1
        if (two < 0) {
            two = 9
        }
        return suggests[one][two]
    }
}