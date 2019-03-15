package me.nubuscu.spoofy

enum class TimeRange {
    LONG_TERM {
        override val value: String
            get() = "long_term"

    },
    MEDIUM_TERM {
        override val value: String
            get() = "medium_term"

    },
    SHORT_TERM {
        override val value: String
            get() = "short_term"

    };

    abstract val value: String
}
