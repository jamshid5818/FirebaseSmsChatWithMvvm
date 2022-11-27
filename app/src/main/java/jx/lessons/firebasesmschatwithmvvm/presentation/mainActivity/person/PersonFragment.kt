package jx.lessons.firebasesmschatwithmvvm.presentation.mainActivity.person



import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenCreated
import dagger.hilt.android.AndroidEntryPoint
import jx.lessons.firebasesmschatwithmvvm.R
import jx.lessons.firebasesmschatwithmvvm.data.utils.SharedPref
import jx.lessons.firebasesmschatwithmvvm.data.utils.UiState
import jx.lessons.firebasesmschatwithmvvm.data.utils.snackbar
import jx.lessons.firebasesmschatwithmvvm.databinding.FragmentPersonBinding
import jx.lessons.firebasesmschatwithmvvm.presentation.BaseFragment
import kotlinx.coroutines.launch

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
        shared.getEmail()?.let {
            viewModel.getUserInfo(viewModel.firebasePathgmail(it))
            viewModel.getUserPosts(it)
        }
        binding.swipe.setOnRefreshListener {
            shared.getEmail()?.let {
                viewModel.getUserInfo(viewModel.firebasePathgmail(it))
                viewModel.getUserPosts(it)
            }
        }

    }

    private fun observer() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.whenCreated {
                viewModel.getUserPosts.observe(viewLifecycleOwner){state->
                    when(state){
                        is UiState.Loading->{

                        }
                        is UiState.Failure->{
                            snackbar(state.message.toString(), binding.fullLayout)
                        }
                        is UiState.Success->{
                            adapter =
                                PersonAdapter(
                                    state.data,
                                    requireContext()
                                )
                            binding.gridView.adapter = adapter

                        }
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.whenCreated {
                viewModel.getUserInfo.observe(viewLifecycleOwner){state->
                    when(state){
                        is UiState.Loading->{

                        }
                        is UiState.Failure->{

                        }
                        is UiState.Success->{
                            binding.profileAge.text = state.data?.age.toString()
                            if (state.data?.gender=="male"){
                                binding.profileImage.setImageResource(R.drawable.male_avatar)
                            }else if (state.data?.gender == "female"){
                                binding.profileImage.setImageResource(R.drawable.female_avatar)
                            }
                            binding.profileName.text = state.data?.name
                            binding.profileAge.text = state.data?.age.toString()
                            binding.profileGender.text = state.data?.gender
                        }
                    }
                }
            }
        }

    }

}