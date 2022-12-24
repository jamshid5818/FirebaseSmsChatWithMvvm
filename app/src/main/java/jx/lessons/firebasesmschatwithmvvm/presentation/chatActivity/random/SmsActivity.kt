package jx.lessons.firebasesmschatwithmvvm.presentation.chatActivity.random

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import jx.lessons.firebasesmschatwithmvvm.R
import jx.lessons.firebasesmschatwithmvvm.data.model.Sms
import jx.lessons.firebasesmschatwithmvvm.data.model.UserInfo
import jx.lessons.firebasesmschatwithmvvm.data.utils.SharedPref
import jx.lessons.firebasesmschatwithmvvm.data.utils.UiState
import jx.lessons.firebasesmschatwithmvvm.data.utils.toast
import jx.lessons.firebasesmschatwithmvvm.databinding.ActivitySmsBinding
import jx.lessons.firebasesmschatwithmvvm.presentation.chatActivity.ChatAdapter

@AndroidEntryPoint
class SmsActivity : AppCompatActivity() {
    lateinit var binding:ActivitySmsBinding
    val viewModel:RandomChatViewModel by viewModels()
    val shared by lazy {
        SharedPref(this)
    }
    lateinit var adapter: ChatAdapter
    var fireKey:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySmsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.list.layoutManager=LinearLayoutManager(this)
        observer()
        val extras = intent.extras
        extras?.getString("EMAIL")?.let {
            viewModel.getFindUserInfo(it)
        }
        fireKey = shared.getEmail()+"___"+extras?.getString("EMAIL")
        viewModel.getAllSms(fireKey!!)
        binding.swipe.setOnRefreshListener {
            viewModel.getAllSms(fireKey!!)
        }

        binding.sendSms.setOnClickListener {
            val sms = binding.getsms.text.toString()
            if (sms.isNotEmpty()){
                shared.getGender()
                    ?.let { it1 -> Sms(sms, shared.getEmail()!!, it1,System.currentTimeMillis()) }
                    ?.let { it2 ->
                        viewModel.sendSms(fireKey!!,
                            it2
                        )
                    }
            }
        }
    }

    private fun observer() {
        viewModel.randomChat.observe(this){ state->
            when(state){
                is UiState.Loading->{

                }
                is UiState.Failure->{
                    toast(state.message.toString())
                }
                is UiState.Success->{

                }
            }
        }
        viewModel.getAllSms.observe(this){ state->
            when(state){
                is UiState.Loading->{
                    binding.swipe.isRefreshing=true
                }
                is UiState.Failure->{
                    toast(state.message.toString())
                }
                is UiState.Success->{
                    binding.swipe.isRefreshing = false
                    adapter = state.data?.let { ChatAdapter(it,this) }!!
                    binding.list.adapter=adapter
                    binding.list.scrollToPosition(adapter.list.size-1)
                }
            }
        }
        viewModel.findPersonInfo.observe(this){ state->
            when(state){
                is UiState.Loading->{
                    binding.name.text = "Loading..."
                }
                is UiState.Failure->{
                    toast(state.message.toString())
                }
                is UiState.Success->{
                    val userInfo:UserInfo = state.data!!
                    when(userInfo.gender){
                        "Male"->{
                            binding.imageView.setImageResource(R.drawable.male_avatar)
                        }
                        "Female"->{
                            binding.imageView.setImageResource(R.drawable.female_avatar)
                        }
                    }
                    binding.name.text = userInfo.name
                }
            }
        }
    }
}