package com.example.beinconnectmoviesapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.beinconnectmoviesapp.Model.Movie
import com.example.beinconnectmoviesapp.R
import com.example.beinconnectmoviesapp.Util.Util.loadImage

class MoviesPagerAdapter : RecyclerView.Adapter<MoviesPagerAdapter.ViewHolder>() {

    private var onMovieClickListener: ((Movie) -> Unit)? = null
    private val imageList = ArrayList<Movie>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MoviesPagerAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_fragment_movies_images, parent, false))
    }

    fun setOnMovieClickListener(listener: (Movie) -> Unit) {
        onMovieClickListener = listener
    }

    override fun onBindViewHolder(holder: MoviesPagerAdapter.ViewHolder, position: Int) {
        val currentMovie = imageList.get(position)
        holder.root.apply {
            loadImage(currentMovie.image)
            setOnClickListener {
                onMovieClickListener?.invoke(currentMovie)
            }
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root: ImageView = view.findViewById(R.id.row_movies_image)
    }

    fun updateImageList(list: List<Movie>) {
        imageList.clear()
        imageList.addAll(list)
        notifyDataSetChanged()
    }

}