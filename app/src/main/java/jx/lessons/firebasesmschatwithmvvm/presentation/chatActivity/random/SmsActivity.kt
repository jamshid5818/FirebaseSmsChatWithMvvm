package jx.lessons.firebaseSmsChatWithMvvm.presentation.chatActivity.random

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import jx.lessons.firebaseSmsChatWithMvvm.R
import jx.lessons.firebaseSmsChatWithMvvm.data.model.UserInfo
import jx.lessons.firebaseSmsChatWithMvvm.data.utils.SharedPref
import jx.lessons.firebaseSmsChatWithMvvm.data.utils.UiState
import jx.lessons.firebaseSmsChatWithMvvm.data.utils.toast
import jx.lessons.firebaseSmsChatWithMvvm.databinding.ActivitySmsBinding
import jx.lessons.firebaseSmsChatWithMvvm.presentation.chatActivity.ChatAdapter

@AndroidEntryPoint
class SmsActivity : AppCompatActivity() {
    lateinit var binding:ActivitySmsBinding
    val viewModel:RandomChatViewModel by viewModels()
    val shared by lazy {
        SharedPref(this)
    }
    lateinit var adapter: ChatAdapter
    private var fireKey:String?=null
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