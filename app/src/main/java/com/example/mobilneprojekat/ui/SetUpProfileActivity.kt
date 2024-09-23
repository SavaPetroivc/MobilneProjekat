package com.example.mobilneprojekat.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.mobilneprojekat.R
import com.example.mobilneprojekat.UserViewModel
import com.example.mobilneprojekat.data.Profile
import com.example.mobilneprojekat.data.Score
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class SetUpProfileActivity : AppCompatActivity() {

    private var viewModel: UserViewModel = UserViewModel()
    private lateinit var avatar: ImageView
    private var byteArray: ByteArray? = null
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_up_profile)

        val setUpProfileButton = findViewById<Button>(R.id.setUpProfileButton)
        val avatar = findViewById<ImageView>(R.id.addPhoto)

        uid = intent.extras?.getString("uid")!!
        Log.d("UID_SUP", uid)

        setUpProfileButton.setOnClickListener{
            val usernameInput = findViewById<EditText>(R.id.usernameSUP)
            val username = usernameInput.text.toString()
            val nameInput = findViewById<EditText>(R.id.nameSUP)
            val name = nameInput.text.toString()
            val lastnameInput = findViewById<EditText>(R.id.lastnameSUP)
            val lastname = lastnameInput.text.toString()
            val phoneInput = findViewById<EditText>(R.id.phoneSUP)
            val phone = phoneInput.text.toString()

            if(username.isNotEmpty() && name.isNotEmpty()
                && lastname.isNotEmpty() && phone.isNotEmpty()){

                val profile: Profile = Profile(username, name, lastname, phone)
                val score: Score = Score(username, 0)

                viewModel.addScore(score, uid)
                viewModel.addProfile(profile, uid)
            }
        }

        avatar.setOnClickListener{
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i, 123)
        }

        viewModel.addedProfile.observe(this, Observer { addedProfile ->
            if(addedProfile == true){
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("uid", uid)
                Log.d("Saljem", uid)

                if(byteArray!=null){
                    Log.d("URI", "Odgovara")
                    viewModel.addAvatar(byteArray!!, uid)

                    viewModel.addedAvatar.observe(this, Observer { addedAvatar ->
                        if(addedAvatar == true){
                            startActivity(intent)
                            finish()
                        }
                    })
                }
                else{
                    startActivity(intent)
                    finish()
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 123){
            var bmp = data!!.extras!!.get("data") as Bitmap
            val stream = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            byteArray = stream.toByteArray()

            avatar = findViewById<ImageView>(R.id.addPhoto)
            avatar.setImageBitmap(bmp)
        }
    }
}