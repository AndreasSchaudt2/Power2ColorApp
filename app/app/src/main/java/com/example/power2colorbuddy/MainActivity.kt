package com.example.power2colorbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.power2colorbuddy.ui.theme.Power2ColorBuddyTheme
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private lateinit var configEditText: EditText
    private lateinit var statusTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Power2ColorBuddyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
        setContentView(R.layout.activity_main)
        configEditText = findViewById(R.id.configEditText)
        statusTextView = findViewById(R.id.statusTextView)
        val saveButton: Button = findViewById(R.id.saveButton)
        val restartButton: Button = findViewById(R.id.restartButton)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://power2colorpi:5000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)


        saveButton.setOnClickListener {
            val config = configEditText.text.toString()
            apiService.updateConfig(ConfigRequest(config))
                .enqueue(object : Callback<StatusResponse> {
                    override fun onResponse(
                        call: Call<StatusResponse>,
                        response: Response<StatusResponse>
                    ) {
                        statusTextView.text = response.body()?.status
                    }

                    override fun onFailure(call: Call<StatusResponse>, t: Throwable) {
                        statusTextView.text = "Error: ${t.message}"
                    }
                })
        }
        restartButton.setOnClickListener {
            apiService.restart().enqueue(object : Callback<StatusResponse> {
                override fun onResponse(
                    call: Call<StatusResponse>,
                    response: Response<StatusResponse>
                ) {
                    statusTextView.text = response.body()?.status
                }

                override fun onFailure(call: Call<StatusResponse>, t: Throwable) {
                    statusTextView.text = "Error: ${t.message}"
                }
            })
        }

        apiService.getConfig().enqueue(object : Callback<ConfigResponse> {
            override fun onResponse(call: Call<ConfigResponse>, response: Response<ConfigResponse>) {
                configEditText.setText(response.body()?.config)
            }

            override fun onFailure(call: Call<ConfigResponse>, t: Throwable) {
                statusTextView.text = "Error: ${t.message}"
            }
        })
    }


}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Power2ColorBuddyTheme {
        Greeting("Android")
    }
}