package com.example.newsclient

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.newsclient.databinding.ActivityMainBinding
import com.google.zxing.integration.android.IntentIntegrator
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var qrScanIntegrator: IntentIntegrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("credentials", Context.MODE_PRIVATE)

        if (sharedPreferences.getString("news_api_key","") != "") {
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        binding.setUpProfile.setOnClickListener{
            qrScanIntegrator = IntentIntegrator(this)
            qrScanIntegrator.setOrientationLocked(false)
            resultLauncher.launch(qrScanIntegrator.createScanIntent())
        }
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val qrData = IntentIntegrator.parseActivityResult(it.resultCode, it.data)

        if (qrData.contents != null) {
            val data = JSONObject(qrData.contents)

            val editor = sharedPreferences.edit()

            editor.putString("profile_image_url",data.getString("profile_image_url"))
            editor.putString("profile_first_name",data.getString("profile_first_name"))
            editor.putString("news_api_key",data.getString("news_api_key"))
            editor.apply()
            editor.commit()

            startActivity(Intent(this, DashboardActivity::class.java))
        }
    }
}