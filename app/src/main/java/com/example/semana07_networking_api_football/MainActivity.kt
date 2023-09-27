package com.example.semana07_networking_api_football

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.semana07_networking_api_football.adapter.TeamAdapter
import com.example.semana07_networking_api_football.models.ApiResponseHeader
import com.example.semana07_networking_api_football.models.Team
import com.example.semana07_networking_api_football.network.TeamService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MainActivity : AppCompatActivity() {
    lateinit var teamRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        teamRecyclerView = findViewById(R.id.rvTeams)

        loadTeams(this)
    }

    private fun loadTeams(context: Context) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api-football-v1.p.rapidapi.com/v2/teams/league/1341")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val teamService: TeamService
        teamService = retrofit.create(TeamService::class.java)

        val request = teamService.getTeams(
            host = "api-football-v1.p.rapidapi.com/",
            apikey = "0bee96e232msh91abfc697a99ee1p1f3b1fjsn3694ca9ffc56"
        )

        request.enqueue(object : Callback<ApiResponseHeader>{
            override fun onResponse(
                call: Call<ApiResponseHeader>,
                response: Response<ApiResponseHeader>
            ) {
                if (response.isSuccessful){
                    val teams: List<Team> = response.body()!!.api.teams ?: ArrayList()
                    teamRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                    teamRecyclerView.adapter = TeamAdapter(teams, context)
                }
            }

            override fun onFailure(call: Call<ApiResponseHeader>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}