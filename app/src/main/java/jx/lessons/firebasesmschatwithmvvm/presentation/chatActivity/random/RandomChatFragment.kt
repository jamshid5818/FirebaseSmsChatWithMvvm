package jx.lessons.firebasesmschatwithmvvm.presentation.chatActivity.random

import android.content.Intent
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import jx.lessons.firebasesmschatwithmvvm.R
import jx.lessons.firebasesmschatwithmvvm.data.utils.UiState
import jx.lessons.firebasesmschatwithmvvm.data.utils.createDialog
import jx.lessons.firebasesmschatwithmvvm.data.utils.toast
import jx.lessons.firebasesmschatwithmvvm.databinding.FragmentRandomChatBinding
import jx.lessons.firebasesmschatwithmvvm.presentation.SmsActivity
import jx.lessons.firebasesmschatwithmvvm.presentation.chatActivity.BaseChatFragment
import jx.lessons.firebasesmschatwithmvvm.presentation.chatActivity.ChatActivity

@AndroidEntryPoint
class RandomChatFragment : BaseChatFragment<FragmentRandomChatBinding>(FragmentRandomChatBinding::inflate) {
    val viewModel:RandomChatViewModel by viewModels()
    override fun onViewCreate() {
        oberver()
        binding.addNewRandomChat.setOnClickListener {
            val dialog = requireContext().createDialog(R.layout.dialog_create_new_random,true)
            val findGirls = dialog.findViewById<LinearLayoutCompat>(R.id.findgirls)
            val findBoys = dialog.findViewById<LinearLayoutCompat>(R.id.findboys)
            findGirls.setOnClickListener {
                viewModel.getNewPerson("Female", System.currentTimeMillis())
            }
            findBoys.setOnClickListener {
                viewModel.getNewPerson("Male", System.currentTimeMillis())
            }
        }
    }

    private fun oberver() {
        viewModel.getNewPerson.observe(viewLifecycleOwner){state->
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