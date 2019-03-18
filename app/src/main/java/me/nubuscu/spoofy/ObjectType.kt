package me.nubuscu.spoofy

/**
 * Enum for distinguishing music object types - artists, songs, etc.
 */
enum class ObjectType {
    ARTISTS {
        override val value: String
            get() = "artists"
    },
    SONGS {
        override val value: String
            get() = "songs"
    };

    abstract val value: String

    override fun toString(): String = value
}
