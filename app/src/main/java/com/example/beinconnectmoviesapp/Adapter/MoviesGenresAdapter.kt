package com.example.beinconnectmoviesapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.beinconnectmoviesapp.Model.GenreX
import com.example.beinconnectmoviesapp.R

class MoviesGenresAdapter : RecyclerView.Adapter<MoviesGenresAdapter.ViewHolder>() {

    private var onGenreClickListener: ((GenreX) -> Unit)? = null

    private val genreList = ArrayList<GenreX>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MoviesGenresAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_fragment_movies_genres, parent, false))
    }

    fun setOnGenreClickListener(listener: ((GenreX) -> Unit)) {
        onGenreClickListener = listener
    }

    override fun onBindViewHolder(holder: MoviesGenresAdapter.ViewHolder, position: Int) {
        val currentGenreX = genreList.get(position)
        holder.genre.apply {
            setText(currentGenreX.name)
            setOnClickListener {
                onGenreClickListener?.invoke(currentGenreX)
            }
        }
    }

    override fun getItemCount(): Int {
        return genreList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val genre: TextView
        init {
            genre = view.findViewById(R.id.row_movies_genre)
        }
    }

    fun updateGenreList(list: List<GenreX>) {
        genreList.clear()
        genreList.addAll(list)
        notifyDataSetChanged()
    }

}