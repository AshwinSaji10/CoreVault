package com.asj.corevault

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tvUsername:TextView=findViewById(R.id.tvUsername)
        val btnSignOut:Button=findViewById(R.id.btnSignout)

        val fabAdd:FloatingActionButton=findViewById(R.id.fabAdd)

        tvUsername.setText(intent.extras?.getString("email_id"))
        btnSignOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent= Intent(this,LoginActivity::class.java)
            Toast.makeText(this,"You signed out",Toast.LENGTH_SHORT).show()
            startActivity(intent)
            finish()
        }
        fabAdd.setOnClickListener {

        }
    }

}
