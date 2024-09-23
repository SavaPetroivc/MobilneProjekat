package com.example.mobilneprojekat.data

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage

class RepositoryFirebase {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance();
    private val db = Firebase.firestore
    private val storage = Firebase.storage

    init {
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false)
            .build()
        db.firestoreSettings = settings
    }

    fun login(email: String, password: String, onCompleteListener: OnCompleteListener<AuthResult>){
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(onCompleteListener)
    }

    fun signup(email: String, password: String, onCompleteListener: OnCompleteListener<AuthResult>){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(onCompleteListener)
    }

    fun addProfile(profile: Profile, uid: String, onCompleteListener: OnCompleteListener<Void>){
        val profileHash = hashMapOf(
            "username" to profile.username,
            "name" to profile.name,
            "lastname" to profile.lastname,
            "phone" to profile.phone
        )

        db.collection("users").document(uid).set(profileHash)
            .addOnCompleteListener(onCompleteListener)
    }


    fun addScore(score: Score, uid: String, onCompleteListener: OnCompleteListener<Void>){
        val scoreHash = hashMapOf(
            "username" to score.username,
            "points" to score.points
        )

        db.collection("leader").document(uid).set(scoreHash)
            .addOnCompleteListener(onCompleteListener)
    }

    fun addAvatar(byteArray: ByteArray, uid: String, onCompleteListener: OnCompleteListener<UploadTask.TaskSnapshot>){
        val ref = storage.reference.child("avatars/$uid.jpg")
        val uploadTask = ref.putBytes(byteArray).addOnCompleteListener(onCompleteListener)
    }

    fun getLeaderboard(): CollectionReference {
        return db.collection("leader")
    }

    fun getProfile(uid: String, onCompleteListener: OnCompleteListener<DocumentSnapshot?>){
        db.collection("users").document(uid).get()
            .addOnCompleteListener(onCompleteListener)
    }

    fun addPasswordEvent(passwordEvent: PasswordEvent, onCompleteListener: OnCompleteListener<Void>){
        db.collection("passwordExplorer").document().set(passwordEvent)
            .addOnCompleteListener(onCompleteListener)
    }

    fun updateScore(points: Double, uid: String, onCompleteListener: OnCompleteListener<Void>){
        db.collection("leader").document(uid).update("points", FieldValue.increment(points))
            .addOnCompleteListener(onCompleteListener)
    }

//    fun updateProfile(uid: String, onCompleteListener: OnCompleteListener<Void>){
//        db.collection("users").document(uid).update("name", "Petar")
//    }

    fun getPasswordEvents(): CollectionReference {
        return  db.collection("passwordExplorer")
    }

    fun getPasswordEvent(passwordEventId: String, onCompleteListener: OnCompleteListener<DocumentSnapshot?>){
        db.collection("passwordExlporer").document(passwordEventId).get()
            .addOnCompleteListener(onCompleteListener)
    }

    fun changedPassword(password: String, passwordEventId: String, onCompleteListener: OnCompleteListener<Void>){
        db.collection("passwordExplorer").document(passwordEventId).update("password", password)
            .addOnCompleteListener(onCompleteListener)
    }
    fun joinEvent(username: String, passwordEventId: String, onCompleteListener: OnCompleteListener<Void>){
        db.collection("passwordExlporer").document(passwordEventId).update("used", FieldValue.arrayUnion(username))
            .addOnCompleteListener(onCompleteListener)
    }

    fun getScore(uid: String, onCompleteListener: OnCompleteListener<DocumentSnapshot?>){
        db.collection("leader").document(uid).get()
            .addOnCompleteListener(onCompleteListener)
    }
}