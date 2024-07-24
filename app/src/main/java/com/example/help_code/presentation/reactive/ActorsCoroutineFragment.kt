package com.example.help_code.presentation.reactive

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentReactiveBinding
import com.example.help_code.presentation.reactive.actor.ActorsCoroutineViewModel

class ActorsCoroutineFragment :
    BaseBindingFragment<FragmentReactiveBinding>(FragmentReactiveBinding::inflate) {

    val viewMode by viewModels<ActorsCoroutineViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewMode.actorTest()
    }
}