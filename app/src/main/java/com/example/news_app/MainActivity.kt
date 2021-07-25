package com.example.news_app

import android.net.Uri
import  android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.Request
import com.android.volley.Response

class MainActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var mAdapter:NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager( this)
        fetchData()
        mAdapter= NewsListAdapter( this)
        recyclerView.adapter= mAdapter
    }


    private fun fetchData() {
       val url="https://newsapi.org/v2/top-headlines?sources=bbc-news&apiKey=1905de9b24cc4e32ac89c7af3f8caff2"
        val jsonObjectRequest= JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener{
                val newsJsonArray = it.getJSONArray("articles")
                val newsArray= ArrayList<News>()
                for(i in 0 until newsJsonArray.length()){
                    val newsJsonObject= newsJsonArray.getJSONObject(i)
                    val news=News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString( "urlImage")
                    )
                    newsArray.add(news)
                }
                mAdapter.updateNews(newsArray)
            },
        Response.ErrorListener {

         }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: News) {
        val builder =  CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }
}