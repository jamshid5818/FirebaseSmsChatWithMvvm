package jx.lessons.firebasesmschatwithmvvm.presentation.mainActivity.person

import android.content.Intent
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import jx.lessons.firebasesmschatwithmvvm.R
import jx.lessons.firebasesmschatwithmvvm.data.utils.*
import jx.lessons.firebasesmschatwithmvvm.databinding.FragmentPersonBinding
import jx.lessons.firebasesmschatwithmvvm.presentation.mainActivity.BaseFragment
import jx.lessons.firebasesmschatwithmvvm.presentation.mainActivity.MainActivity

@AndroidEntryPoint
class PersonFragment : BaseFragment<FragmentPersonBinding>(FragmentPersonBinding::inflate) {
    val viewModel: PersonViewModel by viewModels()
    lateinit var adapter: PersonAdapter
    private val shared by lazy {
        SharedPref(requireContext())
    }
    override fun onViewCreate() {
        observer()
        binding.gridView.setOnItemClickListener { adapterView, view, i, l ->
            val bundle = bundleOf("POSITION" to i)
            navController.navigate(R.id.action_personFragment_to_listPostUserFragment,bundle)
        }
        binding.exit.setOnClickListener {
            viewModel.logout()
            shared.setEmail("")
            shared.setGender("")
            startActivity(Intent(requireActivity(),MainActivity::class.java))
            requireActivity().finish()
        }
        shared.getEmail()?.let {
            viewModel.getUserInfo(firebasePathgmail(it))
            viewModel.getUserPosts(it)
        }
        binding.swipe.setOnRefreshListener {
            shared.getEmail()?.let {
                viewModel.getUserInfo(firebasePathgmail(it))
                viewModel.getUserPosts(it)
            }
        }

    }

    private fun observer() {
        viewModel.getUserPosts.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Loading->{
                    binding.swipe.isRefreshing=true

                }
                is UiState.Failure->{
                    snackbar(state.message.toString(), binding.fullLayout)
                    binding.swipe.isRefreshing=false
                }
                is UiState.Success->{
                    adapter =
                        PersonAdapter(
                            state.data,
                            requireContext()
                        )
                    binding.gridView.adapter = adapter
                    binding.swipe.isRefreshing=false
                }
            }
        }
        viewModel.getUserInfo.observe(viewLifecycleOwner) { state ->
            when (state) {
                 is UiState.Loading -> {
                     binding.swipe.isRefreshing = true
                 }
                 is UiState.Failure -> {
                     binding.swipe.isRefreshing = false
                 }
                 is UiState.Success -> {
                     binding.profileAge.text = state.data?.age.toString()
                     if (state.data?.gender == "Male") {
                          binding.profileImage.setImageResource(R.drawable.male_avatar)
                     } else if (state.data?.gender == "Female") {
                          binding.profileImage.setImageResource(R.drawable.female_avatar)
                       }
                     binding.profileName.text = state.data?.name
                     binding.profileAge.text = state.data?.age.toString()
                     binding.profileGender.text = state.data?.gender
                     binding.swipe.isRefreshing = false
                 }
            }
        }
    }

}