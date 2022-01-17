package com.okuuyghur.dusuncepaylasmafirebase.view

import android.app.DownloadManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.core.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.okuuyghur.dusuncepaylasmafirebase.R
import com.okuuyghur.dusuncepaylasmafirebase.adapter.DusunceAdapter
import com.okuuyghur.dusuncepaylasmafirebase.model.Paylasim


class DusunceActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    val paylasimlistesi = ArrayList<Paylasim>()
    private lateinit var recyclerAdapter : DusunceAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dusunce)
        auth = Firebase.auth

        firebaseVerial()

        val lm = LinearLayoutManager(this)
        val recyclerView : RecyclerView = findViewById(R.id.recyclerveiw)
        recyclerView.layoutManager = lm
        recyclerAdapter = DusunceAdapter(paylasimlistesi)
        recyclerView.adapter = recyclerAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuinflater = menuInflater
        menuinflater.inflate(R.menu.ana_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.cikisYap){
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else if (item.itemId == R.id.paylas){
            val intent = Intent(this, PaylasimActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
    fun firebaseVerial(){
        db.collection("Paylasimlar").orderBy("tarih",Query.).addSnapshotListener { snapshot, error ->
            if (error != null){
                Toast.makeText(this,error.localizedMessage,Toast.LENGTH_LONG).show()
            } else{
                if (snapshot != null){
                    if(!snapshot.isEmpty){
                        val documents = snapshot.documents

                        paylasimlistesi.clear()

                        for ( document in documents){
                            val kullaniciAdi = document.get("kullaniciAdi") as String
                            val paylasilanYorum = document.get("paylasilanYorumlar") as String
                            val gorselurl = document.get("gorselURL") as String?

                            val indirilenPaylasim = Paylasim(kullaniciAdi,paylasilanYorum,gorselurl)
                            paylasimlistesi.add(indirilenPaylasim)

                        }
                        recyclerAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}