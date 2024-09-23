package com.example.mobilneprojekat

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobilneprojekat.data.Profile
import com.example.mobilneprojekat.data.RepositoryFirebase
import com.example.mobilneprojekat.data.Score
import com.example.mobilneprojekat.data.ScoreDB
import com.example.mobilneprojekat.data.PasswordEvent
import com.example.mobilneprojekat.data.PasswordEventDB
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject

class UserViewModel : ViewModel() {

    private val repository = RepositoryFirebase()

//    private val _loggedIn = MutableLiveData<Boolean?>()
//    val loggedIn: LiveData<Boolean?>
//        get() = _loggedIn

    private val _userReg = MutableLiveData<FirebaseUser?>()
    val userReg: LiveData<FirebaseUser?>
        get() = _userReg

    private val _addedProfile = MutableLiveData<Boolean?>()
    val addedProfile: LiveData<Boolean?>
        get() = _addedProfile

    private val _addedScore = MutableLiveData<Boolean?>()
    val addedScore: LiveData<Boolean?>
        get() = _addedScore

    private val _addedAvatar = MutableLiveData<Boolean?>()
    val addedAvatar: LiveData<Boolean?>
        get() = _addedAvatar

    private val _leaderboard = MutableLiveData<ArrayList<ScoreDB>?>()
    val leaderboard: LiveData<ArrayList<ScoreDB>?>
        get() = _leaderboard

    private val _userProfile = MutableLiveData<Profile?>()
    val userProfile: LiveData<Profile?>
        get() = _userProfile

    private val _addedPasswordEvent = MutableLiveData<Boolean?>()
    val addedPasswordEvent: LiveData<Boolean?>
        get() = _addedPasswordEvent

    private val _updatedScore = MutableLiveData<Boolean?>()
    val updatedScore: LiveData<Boolean?>
        get() = _updatedScore

    private val _passwordEvents = MutableLiveData<ArrayList<PasswordEventDB>?>()
    val passwordEvents: LiveData<ArrayList<PasswordEventDB>?>
        get() = _passwordEvents

    private val _passwordEvent = MutableLiveData<PasswordEvent?>()
    val passwordEvent: LiveData<PasswordEvent?>
        get() = _passwordEvent

    private val _changedEvent = MutableLiveData<Boolean?>()
    val changeEvent: LiveData<Boolean?>
        get() = _changedEvent

    private val _score = MutableLiveData<Score?>()
    val score: LiveData<Score?>
        get() = _score

    fun login(email: String, password: String) {
        repository.login(email, password, OnCompleteListener { task ->
            if(task.isSuccessful){
                _userReg.value = task.result.user
            }
            else{
                _userReg.value = null
            }
        })
    }

    fun signup(email: String, password: String) {
        repository.signup(email, password, OnCompleteListener { task ->
            if(task.isSuccessful){
                _userReg.value = task.result.user
            }
            else{
                _userReg.value = null
            }
        })
    }

    fun addProfile(profile: Profile, uid: String){
        repository.addProfile(profile, uid, OnCompleteListener { task ->
            _addedProfile.value = task.isSuccessful
            })
    }

    fun addScore(score: Score, uid: String){
        repository.addScore(score, uid, OnCompleteListener { task ->
            _addedScore.value = task.isSuccessful
        })
    }

    fun updatePassword(password: String, passwordEventId: String){
        repository.changedPassword(password, passwordEventId, OnCompleteListener{task ->
            _changedEvent.value = task.isSuccessful
        })
    }


    fun addAvatar(byteArray: ByteArray, uid: String){
        repository.addAvatar(byteArray, uid, OnCompleteListener { task ->
            _addedAvatar.value = task.isSuccessful
        })
    }

    fun getLeaderboard(){
        repository.getLeaderboard().orderBy("points", Query.Direction.DESCENDING).addSnapshotListener(EventListener<QuerySnapshot>{ value, error ->
            if(error!=null){
                Log.w(TAG, "Listen failed.", error)
                _leaderboard.value = null
                return@EventListener
            }

            var leaderboardList: ArrayList<ScoreDB> = ArrayList<ScoreDB>()
            for(scr in value!!){
                var scoreItem: ScoreDB = scr.toObject(ScoreDB::class.java)
                leaderboardList.add(scoreItem)
            }
            _leaderboard.value = leaderboardList
        })
    }

    fun getProfile(uid: String){
        repository.getProfile(uid, OnCompleteListener { task ->
            if(task.isSuccessful){
                _userProfile.value = task.result!!.toObject<Profile>()
            }
            else{
                _userProfile.value = null
            }
        })
    }

    fun addPasswordEvent(passwordEvent: PasswordEvent){
        repository.addPasswordEvent(passwordEvent, OnCompleteListener{ task ->
            _addedPasswordEvent.value = task.isSuccessful
        })
    }

    fun updateScore(points: Double, uid: String){
        repository.updateScore(points, uid, OnCompleteListener{ task ->
            _updatedScore.value = task.isSuccessful
        })
    }

    fun getPasswordEvents(){
        repository.getPasswordEvents().addSnapshotListener(EventListener<QuerySnapshot>{ value, error ->
            if(error!=null){
                Log.w(TAG, "Listen failed.", error)
                _passwordEvents.value = null
                return@EventListener
            }

            var passwordEventsList: ArrayList<PasswordEventDB> = ArrayList<PasswordEventDB>()
            for(scr in value!!){
                var passwordEventItem: PasswordEventDB = scr.toObject(PasswordEventDB::class.java)
                passwordEventItem.id = scr.id
                passwordEventsList.add(passwordEventItem)
            }
            _passwordEvents.value = passwordEventsList
        })
    }

    fun getPasswordEvent(passwordEventId: String){
        repository.getPasswordEvent(passwordEventId, OnCompleteListener { task ->
            if(task.isSuccessful){
                _passwordEvent.value = task.result!!.toObject<PasswordEvent>()
            }
            else{
                _passwordEvent.value = null
            }
        })
    }

    fun joinEvent(username: String, passwordEventId: String){
        repository.joinEvent(username, passwordEventId, OnCompleteListener{ task ->
            _changedEvent.value = task.isSuccessful
        })
    }

    fun getScore(uid: String){
        repository.getScore(uid, OnCompleteListener { task ->
            if(task.isSuccessful){
                _score.value = task.result!!.toObject<Score>()
            }
            else{
                _score.value = null
            }
        })
    }

//    fun updateProfile(uid: String){
//        repository.updateProfile(uid, OnCompleteListener{ task ->
//            _updatedScore.value = task.isSuccessful
//        })
//    }

}