package com.okuuyghur.dusuncepaylasmafirebase.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.okuuyghur.dusuncepaylasmafirebase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        binding.kayitOl.setOnClickListener {
            kayitOl()
        }
        binding.girisYap.setOnClickListener {
            girisYap()
        }
        val guncelKullanici = auth.currentUser
        if (guncelKullanici != null){
            val intent = Intent(this, DusunceActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    fun kayitOl(){
        val email = binding.Email.text.toString()
        val parola = binding.Password.text.toString()
        val kullaniciAdi = binding.KullaniciAdi.text.toString()
        if (kullaniciAdi == "") {
            Toast.makeText(this, "kullanıcı adınızı giriniz", Toast.LENGTH_LONG).show()

        }else{
            auth.createUserWithEmailAndPassword(email.trim(),parola.trim())
                .addOnCompleteListener { task->
                    if (task.isSuccessful){
                        val guncelKullanici = auth.currentUser
                        val ProfilGuncellemeIstegi = userProfileChangeRequest {
                            displayName = kullaniciAdi
                        }
                        if (guncelKullanici != null){
                            guncelKullanici.updateProfile(ProfilGuncellemeIstegi).addOnCompleteListener { task->
                                if (task.isSuccessful){
                                    Toast.makeText(this,"Profin kullanıcı adı eklendi", Toast.LENGTH_LONG).show()

                                }
                            }
                        }

                        val intent = Intent(this, DusunceActivity::class.java)
                        startActivity(intent)
                        finish()


                    }

                }
                .addOnFailureListener { exception->
                    Toast.makeText(this,"işlem arızali", Toast.LENGTH_LONG).show()

                    Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
                    Log.i("ixlem","işlem arızali")

                }


    }


    }
    fun girisYap (){
        val email = binding.Email.text.toString()
        val parola = binding.Password.text.toString()
        auth.signInWithEmailAndPassword(email.trim(),parola.trim())
            .addOnCompleteListener { task->
                if (task.isSuccessful){
                    val guncelKullanici = auth.currentUser?.displayName.toString()
                    Toast.makeText(this,"hoş geldin: ${guncelKullanici}", Toast.LENGTH_LONG).show()

                    val intent = Intent(this, DusunceActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }
            .addOnFailureListener { exception->
                Toast.makeText(this,"Giriş arızali", Toast.LENGTH_LONG).show()

                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
                Log.i("girix","Giriş arızali") }



    }
}