package com.asj.corevault

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


//import kotlinx.android.synthetic.main.add_info.*

class MainActivity : AppCompatActivity()
{
    private lateinit var  dbref: FirebaseFirestore
    private lateinit var itemRecyclerView:RecyclerView
    private lateinit var itemArrayList:ArrayList<ItemDataClass>
    private lateinit var item_adapter:ItemAdapter
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
            fabaction()
        }

        itemRecyclerView=findViewById(R.id.recyclerView)
        itemRecyclerView.layoutManager=LinearLayoutManager(this)
        itemRecyclerView.setHasFixedSize(true)

        itemArrayList= arrayListOf<ItemDataClass>()
        item_adapter= ItemAdapter(itemArrayList)
        itemRecyclerView.adapter=item_adapter
        EventChangeListener()
    }
    fun EventChangeListener()
    {
        val userId=FirebaseAuth.getInstance().currentUser!!.uid
        dbref= FirebaseFirestore.getInstance()

        dbref.collection(userId)
            .addSnapshotListener(object:EventListener<QuerySnapshot>{
                override fun onEvent(
                    value:QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if(error!=null){
                        Log.e("Firestore error",error.message.toString())
                        return
                    }
                    for(dc: DocumentChange in value?.documentChanges!!)
                    {
                        if(dc.type==DocumentChange.Type.ADDED)
                        {
                            itemArrayList.add(dc.document.toObject(ItemDataClass::class.java))
                        }
                    }
                    item_adapter.notifyDataSetChanged()
                }
            })
    }
    fun fabaction()
    {
        /*
            val addItemDialog=Dialog(this,R.style.Theme_Dialog)
            addItemDialog.setCancelable(true)
            addItemDialog.setContentView(R.layout.add_info)
            */
        //Toast.makeText(this,"you pressed fab",Toast.LENGTH_SHORT).show()
        val dialogBinding=layoutInflater.inflate(R.layout.add_info,null)
        val addItemDialog=Dialog(this)
        addItemDialog.setContentView(dialogBinding)
        addItemDialog.setCancelable(true)

        addItemDialog.setContentView(R.layout.add_info)
        addItemDialog.show()
        val tvcancel:TextView=addItemDialog.findViewById(R.id.tvcancel)
        val tvadd:TextView=addItemDialog.findViewById(R.id.tvadd)

        val etiname:EditText=addItemDialog.findViewById(R.id.etiname)
        val etuname:EditText=addItemDialog.findViewById(R.id.etuname)
        val etpass:EditText=addItemDialog.findViewById(R.id.etpass)

        tvcancel.setOnClickListener {
            addItemDialog.dismiss()
        }
        tvadd.setOnClickListener {
            val iname=etiname.text.toString()
            val uname=etuname.text.toString()
            val pass=etpass.text.toString()
            if(iname.isNotEmpty()&&uname.isNotEmpty()&&pass.isNotEmpty())
            {
                val db= Firebase.firestore
                val userMap= hashMapOf(
                    "itemname" to iname,
                    "username" to uname,
                    "password" to pass
                )

                val userId=FirebaseAuth.getInstance().currentUser!!.uid
                //add new items only to that particular user's collection
                db.collection(userId).document(iname).set(userMap)
                    .addOnSuccessListener {
                        Toast.makeText(applicationContext, "Successfully added item",Toast.LENGTH_SHORT).show()
                        addItemDialog.dismiss()
                    }
                    .addOnFailureListener {
                        Toast.makeText(applicationContext, "Failed to add item",Toast.LENGTH_SHORT).show()
                    }
            }
            else
            {
                Toast.makeText(applicationContext, "Fields cannot be blank",Toast.LENGTH_SHORT).show()
            }
        }

        //val window: Window = addItemDialog.getWindow()
        //window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        /*
        val mDisplayMetrics = windowManager.currentWindowMetrics
        val mDisplayWidth = mDisplayMetrics.bounds.width()
        val mDisplayHeight = mDisplayMetrics.bounds.height()

        // Generate custom width and height and
        // add to the dialog attributes
        // we multiplied the width and height by 0.5,
        // meaning reducing the size to 50%
        val mLayoutParams = WindowManager.LayoutParams()
        mLayoutParams.width = (mDisplayWidth * 0.5f).toInt()
        mLayoutParams.height = (mDisplayHeight * 0.5f).toInt()
        addItemDialog.window?.attributes = mLayoutParams
         */
    }

}
