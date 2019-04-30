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

    fun getSuggest(index: Int): List<String> {
        return suggests[index]
    }
}

class SuggestArray() {

}