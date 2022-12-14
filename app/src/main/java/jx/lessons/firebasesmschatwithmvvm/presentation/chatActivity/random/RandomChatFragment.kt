package jx.lessons.firebaseSmsChatWithMvvm.presentation.chatActivity.random

import android.content.Intent
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import jx.lessons.firebaseSmsChatWithMvvm.R
import jx.lessons.firebaseSmsChatWithMvvm.data.utils.SharedPref
import jx.lessons.firebaseSmsChatWithMvvm.data.utils.UiState
import jx.lessons.firebaseSmsChatWithMvvm.data.utils.createDialog
import jx.lessons.firebaseSmsChatWithMvvm.data.utils.toast
import jx.lessons.firebaseSmsChatWithMvvm.databinding.FragmentRandomChatBinding
import jx.lessons.firebaseSmsChatWithMvvm.presentation.chatActivity.BaseChatFragment

@AndroidEntryPoint
class RandomChatFragment : BaseChatFragment<FragmentRandomChatBinding>(FragmentRandomChatBinding::inflate) {
    val viewModel:RandomChatViewModel by viewModels()
    val shared by lazy {
        SharedPref(requireContext())
    }
    override fun onViewCreate() {
        observer()
        binding.addNewRandomChat.setOnClickListener {
            val dialog = requireContext().createDialog(R.layout.dialog_create_new_random,true)
            val findGirls = dialog.findViewById<LinearLayoutCompat>(R.id.findgirls)
            val findBoys = dialog.findViewById<LinearLayoutCompat>(R.id.findboys)
            findGirls.setOnClickListener {
                shared.getEmail()?.let { it1 -> viewModel.getNewPerson("Female", it1) }
            }
            findBoys.setOnClickListener {
                shared.getEmail()?.let { it1 -> viewModel.getNewPerson("Male", it1) }
            }
        }
    }

    private fun observer() {
        viewModel.findNewPerson.observe(viewLifecycleOwner){ state->
            when(state){
                is UiState.Loading->{

                }
                is UiState.Failure->{
                    toast(state.message.toString())
                }
                is UiState.Success->{
                    val intent = Intent(requireActivity(), SmsActivity::class.java)
                    intent.putExtra("EMAIL",state.data)
                    startActivity(intent)
                }
            }
        }
    }
}