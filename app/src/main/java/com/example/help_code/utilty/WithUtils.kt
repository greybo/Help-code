package com.example.help_code.utilty

inline fun <T, R> T?.withNotNull(block: T.() -> R): R? {
    return this?.let {
        it.block()
    }
}

inline fun <T, R> T?.withIfTrue(b: Boolean?, block: T.() -> R): R? {
    return if (this != null && b == true) {
        this.block()
    } else null
}

inline fun <R> Boolean?.ifTrue(block: () -> R): R? {
    return if (this == true) {
        block()
    } else null
}
