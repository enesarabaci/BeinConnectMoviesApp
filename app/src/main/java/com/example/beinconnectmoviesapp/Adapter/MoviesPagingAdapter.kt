package com.example.beinconnectmoviesapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.beinconnectmoviesapp.Model.Movie
import com.example.beinconnectmoviesapp.R
import com.example.beinconnectmoviesapp.Util.Util.loadImage

class MoviesPagingAdapter : PagingDataAdapter<Movie, MoviesPagingAdapter.ViewHolder>(diffUtil) {

    private var onMovieClickListener: ((Movie) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MoviesPagingAdapter.ViewHolder {
        return MoviesPagingAdapter.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_fragment_movies, parent, false)
        )
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }

    fun setOnMovieClickListener(listener: (Movie) -> Unit) {
        onMovieClickListener = listener
    }

    override fun onBindViewHolder(holder: MoviesPagingAdapter.ViewHolder, position: Int) {
        val currentMovie = getItem(position)
        currentMovie?.let {
            holder.apply {
                image.contentDescription = currentMovie.title
                image.loadImage(currentMovie.image)
                name.setText(currentMovie.title)
                root.setOnClickListener {
                    onMovieClickListener?.invoke(currentMovie)
                }
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view.findViewById<FrameLayout>(R.id.row_fragment_movies_root)
        val image = view.findViewById<ImageView>(R.id.row_fragment_movies_image)
        val name = view.findViewById<TextView>(R.id.row_fragment_movies_name)
    }
}