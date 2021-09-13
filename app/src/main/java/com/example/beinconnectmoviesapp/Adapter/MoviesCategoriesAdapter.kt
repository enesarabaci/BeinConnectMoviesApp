package com.example.beinconnectmoviesapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beinconnectmoviesapp.Model.Movie
import com.example.beinconnectmoviesapp.R
import com.example.beinconnectmoviesapp.Util.Resource

class MoviesCategoriesAdapter : RecyclerView.Adapter<MoviesCategoriesAdapter.ViewHolder>() {

    private var onMovieClickListener: ((Movie) -> Unit)? = null
    private var onCategoryClickListener: ((String) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MoviesCategoriesAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_fragment_movies_categories, parent, false)
        )
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Resource<Any>>() {
        override fun areItemsTheSame(
            oldItem: Resource<Any>,
            newItem: Resource<Any>
        ): Boolean {
            return oldItem.data == newItem.data
        }

        override fun areContentsTheSame(
            oldItem: Resource<Any>,
            newItem: Resource<Any>
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)
    var list: List<Resource<Any>>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    fun setOnMovieClickListener(listener: (Movie) -> Unit) {
        onMovieClickListener = listener
    }
    fun setOnCategoryClickListener(listener: (String) -> Unit) {
        onCategoryClickListener = listener
    }

    override fun onBindViewHolder(holder: MoviesCategoriesAdapter.ViewHolder, position: Int) {
        val currentResource = list.get(position)
        holder.apply {
            title.setText(currentResource.title)
            when (currentResource) {
                is Resource.Success -> {
                    val moviesAdapter = MoviesAdapter()
                    rv.apply {
                        layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                        adapter = moviesAdapter
                    }
                    moviesAdapter.apply {
                        list = currentResource.data as List<Movie>
                        setOnMovieClickListener { movie ->
                            onMovieClickListener?.invoke(movie)
                        }
                    }
                }
            }
            root.setOnClickListener {
                onCategoryClickListener?.invoke(currentResource.title ?: "")
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view.findViewById<ConstraintLayout>(R.id.row_movies_categories_root)
        val title = view.findViewById<TextView>(R.id.row_movies_categories_title)
        val rv = view.findViewById<RecyclerView>(R.id.row_movies_categories_rv)
    }

}