package jx.lessons.firebasesmschatwithmvvm.presentation.chatActivity.global


import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import jx.lessons.firebasesmschatwithmvvm.R
import jx.lessons.firebasesmschatwithmvvm.data.model.Sms
import jx.lessons.firebasesmschatwithmvvm.data.utils.SharedPref
import jx.lessons.firebasesmschatwithmvvm.data.utils.UiState
import jx.lessons.firebasesmschatwithmvvm.data.utils.snackbar
import jx.lessons.firebasesmschatwithmvvm.data.utils.toast
import jx.lessons.firebasesmschatwithmvvm.databinding.FragmentGlobalChatBinding
import jx.lessons.firebasesmschatwithmvvm.presentation.chatActivity.BaseChatFragment
import jx.lessons.firebasesmschatwithmvvm.presentation.mainActivity.home.HomeViewModel
import jx.lessons.firebasesmschatwithmvvm.presentation.mainActivity.home.PostAdapter

@AndroidEntryPoint
class GlobalChatFragment : BaseChatFragment<FragmentGlobalChatBinding>(FragmentGlobalChatBinding::inflate) {
    val viewModel: GlobalChatViewModel by viewModels()
    lateinit var adapter: ChatAdapter
    var sms=""
    private val shared by lazy {
        SharedPref(requireContext())
    }
    var unixTime :Long=0
    override fun onViewCreate() {
        observer()
        viewModel.getAllSms()
        binding.list.setHasFixedSize(true)
        binding.list.layoutManager=LinearLayoutManager(requireContext())
        binding.swipe.setOnRefreshListener {
            viewModel.getAllSms()
        }
        binding.sendSms.setOnClickListener {
            if (shared.getEmail()=="" && shared.getGender()==""){
                requireActivity().finish()
                toast("Oldin ro'yxatdan o'tgan bo'lishiz kerak")
            }else{
                if (binding.getSmsText.text.isNotEmpty() && binding.getSmsText.text.toString()!=""){
                    sms= binding.getSmsText.text.toString()
                    unixTime=System.currentTimeMillis()
                    shared.getGender()?.let { it1 -> viewModel.sendSms(shared.getEmail()!!,sms, it1,unixTime) }
                }
            }
        }
    }

    private fun observer() {
        viewModel.getAllSms.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Loading->{
                    binding.swipe.isRefreshing = true
                }
                is UiState.Failure->{
                    binding.swipe.isRefreshing = false
                    toast(state.message.toString())
                }
                is UiState.Success->{
                    binding.swipe.isRefreshing = false
                    adapter = state.data?.let { ChatAdapter(it,requireContext()) }!!
                    binding.list.adapter=adapter
                    binding.list.scrollToPosition(adapter.list.size-1)
                }
            }
        }
        viewModel.sendSms.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Loading->{

                }
                is UiState.Failure->{
                    toast("Failure send")
                }
                is UiState.Success->{
//                    if (shared.getEmail()==""){
//                        toast("Oldin ro'yxatdan o'tishingiz kerak")
//                    }else{
//                        shared.getGender()
//                            ?.let { Sms(sms, shared.getEmail()!!, it,unixTime) }
//                            ?.let { adapter.addUser(it) }
//                    }
                }
            }
        }
    }
}