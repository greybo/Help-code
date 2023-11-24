package com.example.help_code.presentation.blank

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.example.help_code.R
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentBlankBinding
import com.example.help_code.presentation.blank.coroutine.LatestNewsViewModel
import com.example.help_code.utilty.toast
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import timber.log.Timber


class BlankFragment : BaseBindingFragment<FragmentBlankBinding>(FragmentBlankBinding::inflate) {
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var auth: FirebaseAuth
    private val viewModel: BlankViewModel by viewModels()
    private val viewModel2: LatestNewsViewModel by viewModels()
    private val viewModel3: BlankViewModel3 by viewModels()
    private val vaultViewModel: VaultViewModel by viewModels()

    companion object {
        const val TAG = "BlankFragment"
        const val identificator = "firebase-service-account@firebase-sa-management.iam.gserviceaccount.com"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase Auth
//        auth = Firebase.auth

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        vaultViewModel.runTask()
        Timber.d("is login: ${auth.currentUser != null}")

// Send token back to client
        binding.blankFragmentButton.setOnClickListener {
//            vaultViewModel.runTask()
//            singUp()
//            signInRequest.isAutoSelectEnabled
//            Timber.i("isAutoSelectEnabled: ${signInRequest.isAutoSelectEnabled}")
            authWithPassword("sergeybotl@gmail.com", "@Sergey29")
        }
    }

    fun authWithPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                    Timber.d(user?.tenantId)
                    Timber.d(task.result.credential.toString())
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    toast("Authentication failed.")
                    updateUI(null)
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
                    .setServerClientId(getString(R.string.your_web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .build()
    }

    // ...
    private val REQ_ONE_TAP = 2 // Can be any integer unique to the Activity.

    private val showOneTapUI = true

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "Got ID token. $requestCode")
        when (requestCode) {
//            REQ_ONE_TAP -> try {
//                val credential: SignInCredential = oneTapClient.getSignInCredentialFromIntent(data)
//                val idToken = credential.googleIdToken
//                if (idToken != null) {
//                    // Got an ID token from Google. Use it to authenticate
//                    // with your backend.
//                    Log.d(TAG, "Got ID token.")
//                }
//            } catch (e: ApiException) {
//
//            }
        }
    }

    fun loginCustomToken(customToken: String?) {
        customToken?.let {
            auth.signInWithCustomToken(it)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCustomToken:success")
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCustomToken:failure", task.exception)

                        toast("Authentication failed.")
                        updateUI(null)
                    }
                }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        toast(user?.email ?: "null email")
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        Timber.e("uid: -> " + (currentUser?.uid ?: "null uid value"))
        currentUser?.getIdToken(true)?.addOnCompleteListener(requireActivity()) {
            if (it.isSuccessful) {
                Timber.e("token: -> ${it.result.token}")

            } else {
                Timber.e(it.exception)
            }
        }
    }
}
