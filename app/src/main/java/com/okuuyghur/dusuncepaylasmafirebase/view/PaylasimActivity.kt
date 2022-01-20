package com.okuuyghur.dusuncepaylasmafirebase.view

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.okuuyghur.dusuncepaylasmafirebase.databinding.ActivityPaylasimBinding
import java.util.*

class PaylasimActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaylasimBinding
    val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    val storage = Firebase.storage
    var secilenGorsel : Uri? = null
    var secilenBitmap : Bitmap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaylasimBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.btnPaylasim.setOnClickListener {
            Paylasim()
        }
        binding.resimPaylas.setOnClickListener {
            resimPaylas()
        }
    }
    fun Paylasim(){

        // universal id olusturmak
        val uuid = UUID.randomUUID()
        val gorselIsmi = "${uuid}.jpg"

        // gorseli storage kayt etmek
        val reference = storage.reference
        val gorselReference = reference.child("images").child(gorselIsmi)
        if (secilenGorsel != null){
            gorselReference.putFile(secilenGorsel!!).addOnSuccessListener {
                //  gorselin url ni almak
                val yuklenengorselreference = reference.child("images").child(gorselIsmi)
                yuklenengorselreference.downloadUrl.addOnSuccessListener{
                    val downloadUrl = it.toString()

                    // firebase.firestory'e ekleme yaptigimiz yer

                    val paylasilanYorumlar = binding.paylasimText.text.toString()
                    val kullaniciAdi = auth.currentUser!!.displayName.toString()
                    val tarih = Timestamp.now()
                    val PaylasimMap = hashMapOf<String,Any>()
                    PaylasimMap.put("paylasilanYorumlar",paylasilanYorumlar)
                    PaylasimMap.put("kullaniciAdi",kullaniciAdi)
                    PaylasimMap.put("tarih",tarih)
                    PaylasimMap.put("gorselUrl",downloadUrl)

                    db.collection("Paylasimlar").add(PaylasimMap).addOnCompleteListener { task->
                        if(task.isSuccessful){
                            finish()
                        }
                    }.addOnFailureListener { exception->
                        Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_LONG).show()
                    }




                }

            }
         } else {
            // firebase.firestory'e ekleme yaptigimiz yer

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

    fun resimPaylas(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)

        }else {
            val GaleriIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(GaleriIntent,2)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode == 1){
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED  ){
                val GaleriIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(GaleriIntent,2)
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){

            secilenGorsel = data.data
            if (secilenGorsel != null){

                if (Build.VERSION.SDK_INT >= 28){
                    val kaynak = ImageDecoder.createSource(this.contentResolver,secilenGorsel!!)
                    secilenBitmap = ImageDecoder.decodeBitmap(kaynak)
                    binding.imageView.setImageBitmap(secilenBitmap)
                }else{
                    secilenBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,secilenGorsel)
                    binding.imageView.setImageBitmap(secilenBitmap)


                }
            }

        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}