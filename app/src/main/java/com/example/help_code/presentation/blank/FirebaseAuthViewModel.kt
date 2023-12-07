package com.example.help_code.presentation.blank

import com.example.help_code.HelpCodeApplication
import com.example.help_code.R
import com.example.help_code.base.CompositeViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import timber.log.Timber
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FirebaseAuthViewModel : CompositeViewModel() {

    private lateinit var signInRequest: BeginSignInRequest
    private var auth: FirebaseAuth = Firebase.auth
    private val showOneTapUI = true

    private val executor: ExecutorService =
        Executors.newFixedThreadPool(2)

    fun runTask() {
        Timber.d("is login: ${auth.currentUser != null}")
        Timber.d("token: ${auth.currentUser?.getIdToken(false)?.result?.token}")
        authWithPassword("sergeybotl@gmail.com", "@Sergey29")
    }

    private fun authWithPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(executor) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d("createUserWithEmail:success")
                    val user = auth.currentUser
                    Timber.d(user?.tenantId)
                    Timber.d(task.result.credential.toString())
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.w(BlankFragment.TAG, "createUserWithEmail:failure", task.exception)
                }
            }
    }

    //fun authWithFirebase(){
//    val googleCredential = oneTapClient.getSignInCredentialFromIntent(data)
//    val idToken = googleCredential.googleIdToken
//    when {
//        idToken != null -> {
//            // Got an ID token from Google. Use it to authenticate
//            // with Firebase.
//            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
//            auth.signInWithCredential(firebaseCredential)
//                .addOnCompleteListener(this) { task ->
//                    if (task.isSuccessful) {
//                        // Sign in success, update UI with the signed-in user's information
//                        Log.d(TAG, "signInWithCredential:success")
//                        val user = auth.currentUser
//                        updateUI(user)
//                    } else {
//                        // If sign in fails, display a message to the user.
//                        Log.w(TAG, "signInWithCredential:failure", task.exception)
//                        updateUI(null)
//                    }
//                }
//        }
//        else -> {
//            // Shouldn't happen.
//            Log.d(TAG, "No ID token!")
//        }
//    }
//}
    fun singUp() {
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(HelpCodeApplication.instance.getString(R.string.your_web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .build()
    }

    fun loginCustomToken(customToken: String?) {
        customToken?.let {
            auth.signInWithCustomToken(it)
                .addOnCompleteListener(executor) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Timber.d("signInWithCustomToken:success")
                        val user = auth.currentUser
                        Timber.d("$user")
                    } else {
                        // If sign in fails, display a message to the user.
                        Timber.w("signInWithCustomToken:failure", task.exception)
                    }
                }
        }
    }
}