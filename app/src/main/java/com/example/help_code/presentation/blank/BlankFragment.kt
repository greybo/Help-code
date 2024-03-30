package com.example.help_code.presentation.blank

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentBlankBinding
import com.example.help_code.presentation.blank.coroutine.LatestNewsViewModel
import com.example.help_code.presentation.jackson.JacksonMapperViewModel
import timber.log.Timber


class BlankFragment : BaseBindingFragment<FragmentBlankBinding>(FragmentBlankBinding::inflate) {

    private val viewModel: BlankViewModel by viewModels()
    private val viewModel2: LatestNewsViewModel by viewModels()
    private val collectionViewModel: CollectionViewModel by viewModels()
    private val vaultViewModel: VaultViewModel by viewModels()
    private val firebaseViewModel: FirebaseAuthViewModel by viewModels()
    private val exceptionViewModel: ExceptionCoroutineViewModel by viewModels()

    companion object {
        const val TAG = "BlankFragment"

        private const val REQ_ONE_TAP = 2 // Can be any integer unique to the Activity.
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.blankFragmentButton.setOnClickListener {
            exceptionViewModel.runTask()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.d(TAG, "Got ID token. $requestCode")
//        when (requestCode) {
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
//        }
    }
}
