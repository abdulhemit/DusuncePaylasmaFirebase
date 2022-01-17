package com.okuuyghur.dusuncepaylasmafirebase.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.okuuyghur.dusuncepaylasmafirebase.databinding.ActivityPaylasimBinding

class PaylasimActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaylasimBinding
    val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaylasimBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.btnPaylasim.setOnClickListener {
            Paylasim()
        }
    }
    fun Paylasim(){
        val paylasilanYorumlar = binding.paylasimText.text.toString()
        val kullaniciAdi = auth.currentUser!!.displayName.toString()
        val tarih = Timestamp.now()
        val PaylasimMap = hashMapOf<String,Any>()
        PaylasimMap.put("paylasilanYorumlar",paylasilanYorumlar)
        PaylasimMap.put("kullaniciAdi",kullaniciAdi)
        PaylasimMap.put("tarih",tarih)

        db.collection("Paylasimlar").add(PaylasimMap).addOnCompleteListener { task->
            if(task.isSuccessful){
                finish()
            }
        }.addOnFailureListener { exception->
            Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_LONG).show()
        }

    }
}