package jx.lessons.firebaseSmsChatWithMvvm.presentation.chatActivity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    fragmentManager: FragmentManager?,
    lifecycle: Lifecycle?
) :
    FragmentStateAdapter(fragmentManager!!, lifecycle!!) {
    private val mFragmentList: MutableList<Fragment> = ArrayList()
    val mFragmentTitleList: MutableList<String> = ArrayList()
    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getItemCount(): Int {
        return mFragmentList.size
    }
}