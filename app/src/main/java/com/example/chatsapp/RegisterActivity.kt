package com.example.chatsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chatsapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            binding.registerBtn.setOnClickListener {
                val name = binding.nameInput.text.toString()
                val email = binding.emailInput.text.toString()
                val phoneNo = binding.phoneInput.text.toString()
                val password = binding.passwordInput.text.toString()
                val confirmPassword = binding.confirmPassword.text.toString()
                auth = FirebaseAuth.getInstance()
                db = FirebaseFirestore.getInstance()

                if (name.trim().isEmpty() || email.trim().isEmpty() || phoneNo.trim().isEmpty() || password.trim().isEmpty() || confirmPassword.trim().isEmpty()) {
                    Toast.makeText(this,"Please enter vaid credentials", Toast.LENGTH_SHORT).show()
                } else if(password.trim() != confirmPassword.trim()) {
                    Toast.makeText(this,"password and confirm password not matching", Toast.LENGTH_SHORT).show()
                } else {
                    val user = hashMapOf("Name" to name, "Email" to email, "Phone" to phoneNo)

                    val users = db.collection("users")


                    val query = users.whereEqualTo("Email",email).get().addOnSuccessListener {
                        if (it.isEmpty) {

                            auth.createUserWithEmailAndPassword(email,password)
                                .addOnCompleteListener(this) {task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(this,"user registered successfuly", Toast.LENGTH_SHORT).show()
                                        users.document(email).set(user)
                                        val intent = Intent(this, ChatActivity::class.java)
                                        intent.putExtra("email",email)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(this,"user not registered", Toast.LENGTH_SHORT).show()
                                    }
                                }

                        } else {
                            Toast.makeText(this, "user already registered", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }




                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

    }
}