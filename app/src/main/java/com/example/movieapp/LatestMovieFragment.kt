// src/main/java/com/example/movieapp/LatestMovieFragment.kt
package com.example.movieapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers

private const val API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed"

class LatestMovieFragment : Fragment(), OnListFragmentInteractionListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LatestMovieRecyclerViewAdapter
    private lateinit var progressBar: ContentLoadingProgressBar
    private var movies: List<LatestMovie> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_list, container, false)
        progressBar = view.findViewById(R.id.progress)
        recyclerView = view.findViewById(R.id.list)
        recyclerView.layoutManager = GridLayoutManager(view.context, 1)
        val searchView = view.findViewById<SearchView>(R.id.searchView)

        val btnFAQ = view.findViewById<Button>(R.id.btnFAQ)
        btnFAQ.setOnClickListener {
            openFAQFragment()
        }

        updateAdapter()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    filterMovies(it)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    filterMovies(it)
                }
                return false
            }
        })

        return view
    }

    private fun updateAdapter() {
        progressBar.show()
        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api_key"] = API_KEY
        client[
            "https://api.themoviedb.org/3/movie/now_playing",
            params,
            object : JsonHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Headers,
                    json: JsonHttpResponseHandler.JSON
                ) {
                    progressBar.hide()
                    val results = json.jsonObject.get("results")
                    val moviesJSON = results.toString()
                    val gson = Gson()
                    val arrayMovieType = object : TypeToken<List<LatestMovie>>() {}.type
                    movies = gson.fromJson(moviesJSON, arrayMovieType)
                    adapter = LatestMovieRecyclerViewAdapter(context, movies, this@LatestMovieFragment)
                    recyclerView.adapter = adapter
                    Log.d("LatestMovieFragment", "response successful")
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    response: String,
                    throwable: Throwable?
                ) {
                    progressBar.hide()
                    throwable?.message?.let {
                        Log.e("LatestMovieFragment", response)
                    }
                }
            }
        ]
    }

    private fun filterMovies(query: String) {
        val filteredMovies = movies.filter {
            it.title?.contains(query, ignoreCase = true) == true ||
                    it.overview?.contains(query, ignoreCase = true) == true
        }
        adapter = LatestMovieRecyclerViewAdapter(context, filteredMovies, this)
        recyclerView.adapter = adapter
    }

    override fun onItemClick(item: LatestMovie) {
        // Handle item click
    }

    private fun openFAQFragment() {
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.content, FAQFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}
