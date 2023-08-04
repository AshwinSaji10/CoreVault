package com.asj.corevault

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnRegister: Button = findViewById(R.id.btnregister)
        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        val btnSignin: Button = findViewById(R.id.signinbtn)
        val etusrname: EditText = findViewById(R.id.etusrname)
        val etpwd: EditText = findViewById(R.id.etpwd)
        btnSignin.setOnClickListener {

            when {
                TextUtils.isEmpty(etusrname.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
                }

                TextUtils.isEmpty(etpwd.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    val email: String = etusrname.text.toString().trim { it <= ' ' }
                    val pass: String = etpwd.text.toString().trim { it <= ' ' }

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val firebaseUser: FirebaseUser = task.result!!.user!!
                                Toast.makeText(
                                    this,
                                    "You are signed in successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("usr_id", firebaseUser.uid)
                                intent.putExtra("email_id", email)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    this,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }
    }
}