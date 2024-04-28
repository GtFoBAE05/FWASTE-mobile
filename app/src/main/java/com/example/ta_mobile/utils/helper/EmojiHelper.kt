package com.example.ta_mobile.utils.helper

object EmojiHelper {
    fun getEmoji(unicode: Int): String {
        return String(Character.toChars(unicode))
    }
}