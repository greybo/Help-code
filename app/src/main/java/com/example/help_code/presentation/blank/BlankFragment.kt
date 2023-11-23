package com.example.help_code.presentation.blank

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentBlankBinding
import com.example.help_code.presentation.blank.coroutine.LatestNewsViewModel
import com.example.help_code.utilty.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class BlankFragment : BaseBindingFragment<FragmentBlankBinding>(FragmentBlankBinding::inflate) {
    private lateinit var auth: FirebaseAuth
    private val viewModel: BlankViewModel by viewModels()
    private val viewModel2: LatestNewsViewModel by viewModels()
    private val viewModel3: BlankViewModel3 by viewModels()
    private val vaultViewModel: VaultViewModel by viewModels()

    companion object {
        const val TAG = "BlankFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase Auth
//        auth = Firebase.auth

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vaultViewModel.runTask()

        binding.blankFragmentButton.setOnClickListener { vaultViewModel.runTask() }

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
//        val currentUser = auth.currentUser
//        toast(currentUser?.uid ?: "null uid value")
    }
}
