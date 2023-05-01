package com.example.chatsapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.chatsapp.databinding.ActivityChatBinding
import com.google.firebase.firestore.FirebaseFirestore

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = intent.getStringExtra("email")
        db = FirebaseFirestore.getInstance()

        val sharedPrefs = this.getPreferences(Context.MODE_PRIVATE) ?: return
        val isLogin = sharedPrefs.getString("email", "1")

        binding.logoutBtn.setOnClickListener {
            sharedPrefs.edit().remove("Email").apply()
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        if (isLogin?.equals("1") == true) {

            if (email != null) {
                setText(email)
                with(sharedPrefs.edit()) {
                    putString("email", email)
                }.apply()
            } else {
                var intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        } else {

            setText(isLogin)
        }

    }

    private fun setText(email: String?) {
        email?.let {
            db.collection("users").document(email).get()
                .addOnSuccessListener { it ->
                    Log.d("anusha test", it.get("Name").toString())
                    binding.nameTv.text = it.get("Name").toString()
                    binding.emaiTv.text = it.get("Email").toString()
                    binding.phoneTv.text = it.get("Phone").toString()

                }

        }

    }
}