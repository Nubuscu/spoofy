package me.nubuscu.spoofy.recommendations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.nubuscu.spoofy.R
import me.nubuscu.spoofy.utils.DataManager
import me.nubuscu.spoofy.utils.TitledFragment

class GenreRecFragment : TitledFragment() {
    override val title: String
        get() = DataManager.instance.context.get()?.getString(R.string.genre_rec) ?: "Genre"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_genre_rec, container, false)
    }
}