package jx.lessons.firebasesmschatwithmvvm.presentation.chatActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import jx.lessons.firebasesmschatwithmvvm.R
import jx.lessons.firebasesmschatwithmvvm.databinding.ActivityChatBinding
import jx.lessons.firebasesmschatwithmvvm.presentation.chatActivity.random.RandomChatFragment
import jx.lessons.firebasesmschatwithmvvm.presentation.chatActivity.global.GlobalChatFragment
import jx.lessons.firebasesmschatwithmvvm.presentation.chatActivity.kontakt.ContactChatFragment

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {
    lateinit var binding:ActivityChatBinding
    private val adapter by lazy{
        ViewPagerAdapter(this.supportFragmentManager, this.lifecycle)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        initViews()
    }
    private fun initViews(){
        setupViewPager(binding.viewPager)
        TabLayoutMediator(
            binding.tabLayout, binding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = adapter.mFragmentTitleList[position]
        }.attach()
        for (i in 0 until binding.tabLayout.tabCount) {
            val tv = LayoutInflater.from(this)
                .inflate(R.layout.custom_tab, null) as TextView
            binding.tabLayout.getTabAt(i)!!.customView = tv
        }
    }
    private fun setupViewPager(viewPager: ViewPager2){
        adapter.addFragment(GlobalChatFragment(), "Global chat")
        adapter.addFragment(ContactChatFragment(), "Shaxsiy chatlar")
        adapter.addFragment(RandomChatFragment(), "Chat Butilka")
        viewPager.adapter=adapter
        viewPager.offscreenPageLimit=1
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_out_right,R.anim.slide_out_left)
    }

}