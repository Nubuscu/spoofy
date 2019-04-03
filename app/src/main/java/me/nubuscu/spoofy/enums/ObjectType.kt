package me.nubuscu.spoofy.enums

import me.nubuscu.spoofy.R
import me.nubuscu.spoofy.utils.DataManager

/**
 * Enum for distinguishing music object types - artists, songs, etc.
 */
enum class ObjectType {
    ARTISTS {
        override val prettyName: String
            get() = DataManager.instance.context.get()?.getString(R.string.obj_type_artists) ?: value
        override val value: String
            get() = "artists"
    },
    SONGS {
        override val prettyName: String
            get() = DataManager.instance.context.get()?.getString(R.string.obj_type_songs) ?: value
        override val value: String
            get() = "songs"
    };

    abstract val value: String
    abstract val prettyName: String

    override fun toString(): String = prettyName
}
