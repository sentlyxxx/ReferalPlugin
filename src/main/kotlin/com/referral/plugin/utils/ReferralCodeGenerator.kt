package com.referral.plugin.utils

import com.referral.plugin.utils.ReferralCodeGenerator
import kotlin.random.Random

object ReferralCodeGenerator {
    private const val CODE_LENGTH = 8
    private val CHAR_POOL: List<Char> = ('A'..'Z') + ('0'..'9')

    fun generateCode(): String {
        return (1..CODE_LENGTH)
            .map { Random.nextInt(0, CHAR_POOL.size) }
            .map(CHAR_POOL::get)
            .joinToString("")
    }
}