package com.example.randompet

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class MainActivity : AppCompatActivity() {

    private lateinit var petList: MutableList<String>
    private lateinit var rvPets: RecyclerView
    private lateinit var adapter: PetAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Keep your inset padding logic ONLY here
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        petList = mutableListOf()
        rvPets = findViewById(R.id.pet_list)
        adapter = PetAdapter(petList)

        rvPets.layoutManager = LinearLayoutManager(this)
        rvPets.adapter = adapter
        rvPets.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        fetchDogImages()
        rvPets.layoutManager = LinearLayoutManager(this)
        rvPets.adapter = adapter
        rvPets.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )
    }

    private fun fetchDogImages() {
        val client = AsyncHttpClient()
        client["https://dog.ceo/api/breeds/image/random/20", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                try {
                    val arr = json.jsonObject.getJSONArray("message")
                    petList.clear()
                    for (i in 0 until arr.length()) {
                        petList.add(arr.getString(i))
                    }
                    adapter.notifyDataSetChanged()
                    Log.d("Dog Success", "Loaded ${petList.size} images")
                } catch (e: Exception) {
                    Log.d("Dog Parse Error", "${e.message}")
                }
            }

            override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String, throwable: Throwable?) {
                Log.d("Dog Error", "status=$statusCode body=$errorResponse")
            }
        }]
    }
}
