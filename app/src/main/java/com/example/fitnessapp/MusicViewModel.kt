package com.example.fitnessapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessapp.API.MyData
import com.example.fitnessapp.API.NetworkResponse
import com.example.fitnessapp.API.RetrofitInstance
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class MusicViewModel:ViewModel()
{
    private val auth:FirebaseAuth=FirebaseAuth.getInstance()
    private val _authState=MutableLiveData<AuthState>()
    val authState : LiveData<AuthState> = _authState
    private val _userData = MutableLiveData<Map<String, String>>()
    val userData: LiveData<Map<String, String>> = _userData

    private val _userEmail = MutableLiveData<String?>()
    val userEmail: LiveData<String?> = _userEmail


    private val database = FirebaseDatabase.getInstance().reference

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks
    private val firestore = Firebase.firestore

    init {
        checkAuthStatus()
        fetchUserData()
        fetchTasks()
    }

    fun checkAuthStatus(){
        if(auth.currentUser!=null){
            _authState.value=AuthState.Authenticated
            _userEmail.value = auth.currentUser?.email
        }
        else{
            _authState.value=AuthState.Unauthenticated
        }
    }
    fun loginbro(email:String,password:String)
    {

        if(email.isNullOrEmpty()||password.isNullOrEmpty()){
            _authState.value=AuthState.Error("Empty Fields")
            return
        }
        _authState.value=AuthState.Loading
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    _authState.value=AuthState.Authenticated
                }
                else{
                    _authState.value=AuthState.Error(it.exception?.message?:"Something went Wrong")
                }
            }


    }
    fun signupbro(email:String,password:String)
    {

        if(email.isNullOrEmpty()||password.isNullOrEmpty()){
            _authState.value=AuthState.Error("Empty Fields")
            return
        }
        _authState.value=AuthState.Loading
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value =
                        AuthState.Error(it.exception?.message ?: "Something went Wrong")
                }
            }

    }

    fun signupbro(email: String, password: String, username: String) {
        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            _authState.value = AuthState.Error("Empty Fields")
            return
        }
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: ""
                    // Store username in Firebase Realtime Database or Firestore
                    database.child("users").child(userId).setValue(mapOf("username" to username))
                        .addOnSuccessListener {
                            _authState.value = AuthState.Authenticated
                        }
                        .addOnFailureListener { exception ->
                            _authState.value = AuthState.Error(exception.message ?: "Error saving user data")
                        }
                } else {
                    _authState.value = AuthState.Error(it.exception?.message ?: "Signup failed")
                }
            }
    }


    fun fetchUserData() {
        val userId = auth.currentUser?.uid ?: return
        database.child("users").child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.value as? Map<String, String>
                _userData.value = data ?: emptyMap()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors
            }
        })
    }



    fun logout(){
        auth.signOut()
        _authState.value=AuthState.Unauthenticated
        _userData.value = emptyMap() // Clear user data on logout
        _userEmail.value = null
    }

    fun isUserLoggedIn(): Boolean {
        checkAuthStatus()
        return FirebaseAuth.getInstance().currentUser != null
    }

    //fun fetchTasks() {
    // val userId = auth.currentUser?.uid ?: return
    //firestore.collection("tasks")
    // .whereEqualTo("userId", userId)
    // .addSnapshotListener { snapshot, e ->
    //   if (e != null || snapshot == null) return@addSnapshotListener
    // val taskList = snapshot.documents.mapNotNull { doc ->
    //   doc.toObject(Task::class.java)
    //}
    //_tasks.postValue(taskList)
    //}}
    fun fetchTasks() {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("tasks")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) return@addSnapshotListener
                val taskList = snapshot.documents.mapNotNull { doc ->
                    val task = doc.toObject(Task::class.java)
                    task?.let { it.copy(id = doc.id) } // Add the document ID to the task
                }
                _tasks.postValue(taskList)
            }
    }


    fun addTask(name: String, goalDuration: String, actualDuration: String) {
        val userId = auth.currentUser?.uid ?: return
        val task = Task(name, goalDuration, actualDuration, userId)
        firestore.collection("tasks").add(task)
    }


    fun addTask(name: String, goalDuration: String) {
        val userId = auth.currentUser?.uid ?: return
        val task = Task(name, goalDuration, "", userId) // Using an empty string for `actualDuration`
        firestore.collection("tasks").add(task)
    }

    private val MusicApi = RetrofitInstance.MusicAPI
    private val _musicResult =MutableLiveData<NetworkResponse<MyData>>()
    val musicResult : LiveData<NetworkResponse<MyData>> =_musicResult


    fun getMusic() {
        viewModelScope.launch {
            _musicResult.value = NetworkResponse.Loading
            try {
                val response = MusicApi.getMusic("eminem")
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        _musicResult.value = NetworkResponse.Success(body)
                    } else {
                        _musicResult.value = NetworkResponse.Error("Empty Response Body")
                    }
                } else {
                    _musicResult.value = NetworkResponse.Error("Response not successful: ${response.code()}")
                }
            } catch (e: Exception) {
                _musicResult.value = NetworkResponse.Error("Exception: ${e.message}")
            }
        }
    }

    fun updateTask(taskId: String, actualDuration: String) {
        firestore.collection("tasks").document(taskId).update("actualDuration", actualDuration)
    }



}

sealed class AuthState{
    object Authenticated:AuthState()
    object Unauthenticated:AuthState()
    object Loading : AuthState()
    data class Error(val message: String):AuthState()
}