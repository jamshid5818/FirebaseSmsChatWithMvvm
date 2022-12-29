package jx.lessons.firebaseSmsChatWithMvvm.presentation.mainActivity.listpostuser

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import jx.lessons.firebaseSmsChatWithMvvm.data.utils.UiState
import jx.lessons.firebaseSmsChatWithMvvm.data.utils.getEmail
import jx.lessons.firebaseSmsChatWithMvvm.data.utils.snackbar
import jx.lessons.firebaseSmsChatWithMvvm.databinding.FragmentListPostUserBinding
import jx.lessons.firebaseSmsChatWithMvvm.presentation.mainActivity.BaseFragment
import jx.lessons.firebaseSmsChatWithMvvm.presentation.mainActivity.person.PersonViewModel

@AndroidEntryPoint
class ListPostUserFragment : BaseFragment<FragmentListPostUserBinding>(FragmentListPostUserBinding::inflate) {
    val viewModel: PersonViewModel by viewModels()
    lateinit var adapter: ListPostUserAdapter
    override fun onViewCreate() {
        observer()
        viewModel.getUserPosts(getEmail(requireContext()))
        binding.list.setHasFixedSize(true)
        binding.list.layoutManager = LinearLayoutManager(requireContext())
        adapter = ListPostUserAdapter(requireContext())
        binding.list.adapter = adapter
    }
    private fun observer() {
        viewModel.getUserPosts.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Loading->{

                }
                is UiState.Failure->{
                    snackbar(state.message.toString(), binding.fullLayout)
                }
                is UiState.Success->{
                    state.data?.let { adapter.setData(it) }
                    binding.list.scrollToPosition(requireArguments().getInt("POSITION", 0))
                }
            }
        }
    }
}