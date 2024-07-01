package com.example.movieapp

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val MOVIE_DETAIL = "MOVIE_DETAIL"
class LatestMovieRecyclerViewAdapter(
    private val context: Context?,
    private val movies: List<LatestMovie>,
    private val mListener: OnListFragmentInteractionListener?
    ) : RecyclerView.Adapter<LatestMovieRecyclerViewAdapter.MovieViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_movie, parent, false)
            return MovieViewHolder(view)
        }

        //Inner class lets us refer to all the different view elements

    inner class MovieViewHolder(val mView: View) : RecyclerView.ViewHolder(mView),
            View.OnClickListener
         {
            var mItem: LatestMovie? = null
            val mMovieTitle: TextView = mView.findViewById<View>(R.id.title) as TextView
            val mPoster: ImageView = mView.findViewById<View>(R.id.poster) as ImageView
            val mDesc: TextView = mView.findViewById<View>(R.id.description) as TextView
             override fun onClick(p0: View?) {
                 Toast.makeText(context, "Here", Toast.LENGTH_LONG).show()
             }
         }

    /**
     * Binding each View in the ViewHolder to its' actual data
     */
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.mItem = movie
        holder.mMovieTitle.text = movie.title
        holder.mDesc.text = movie.overview

        Glide.with(holder.mView)
            .load("https://image.tmdb.org/t/p/w500/" + movie.movie_image)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.error)
            .centerCrop()
            .into(holder.mPoster)
        holder.itemView.setOnClickListener {
            val params = RequestParams()
            val client = AsyncHttpClient()
            params["api_key"] = "a07e22bc18f5cb106bfe4cc1f83ad8ed"
            val movieId = movie.id.toString()
            client[
                    "https://api.themoviedb.org/3/movie/${movieId}",
                    params,
                    object : JsonHttpResponseHandler()
                    {
                        override fun onFailure(
                            statusCode: Int,
                            headers: okhttp3.Headers?,
                            response: String,
                            throwable: Throwable?
                        ) {
                            // If error is not null, log it!
                            throwable?.message?.let {
                                Log.e("LatestMovieFragment", response)
                            }
                        }

                        override fun onSuccess(
                            statusCode: Int,
                            headers: okhttp3.Headers?,
                            json: JSON?
                        ) {
                            val response = json?.jsonObject.toString()
                            val gson = Gson()
                            val detailType = object : TypeToken<MovieDetails>() {}.type
                            val model: MovieDetails = gson.fromJson(response, detailType)
                            // Pass through intent
                            val intent = Intent(context, SecondActivity::class.java)
                            intent.putExtra(MOVIE_DETAIL, model)
                            context?.startActivity(intent)
                        }
                    }
            ]
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

}