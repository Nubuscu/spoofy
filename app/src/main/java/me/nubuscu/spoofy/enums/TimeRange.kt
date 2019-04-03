package me.nubuscu.spoofy.enums

import me.nubuscu.spoofy.R
import me.nubuscu.spoofy.utils.DataManager


enum class TimeRange {
    LONG_TERM {
        override val prettyName: String
            get() = DataManager.instance.context.get()?.getString(R.string.time_range_long) ?: "what"
        override val value: String
            get() = "long_term"

    },
    MEDIUM_TERM {
        override val prettyName: String
            get() = DataManager.instance.context.get()?.getString(R.string.time_range_medium) ?: value
        override val value: String
            get() = "medium_term"

    },
    SHORT_TERM {
        override val prettyName: String
            get() = DataManager.instance.context.get()?.getString(R.string.time_range_short) ?: value
        override val value: String
            get() = "short_term"

    };

    abstract val value: String
    abstract val prettyName: String

    override fun toString(): String {
        return prettyName
    }
}
