package jx.lessons.firebasesmschatwithmvvm.presentation.listpostuser

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenCreated
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import jx.lessons.firebasesmschatwithmvvm.data.utils.UiState
import jx.lessons.firebasesmschatwithmvvm.data.utils.getEmail
import jx.lessons.firebasesmschatwithmvvm.data.utils.snackbar
import jx.lessons.firebasesmschatwithmvvm.databinding.FragmentListPostUserBinding
import jx.lessons.firebasesmschatwithmvvm.presentation.BaseFragment
import jx.lessons.firebasesmschatwithmvvm.presentation.person.PersonViewModel
import jx.lessons.firebasesmschatwithmvvm.presentation.person.PersonAdapter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListPostUserFragment : BaseFragment<FragmentListPostUserBinding>(FragmentListPostUserBinding::inflate) {
    val viewModel:PersonViewModel by viewModels()
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
                            state.data?.let { adapter.setData(it) }
                            binding.list.scrollToPosition(requireArguments().getInt("POSITION", 0))
                        }
                    }
                }
            }
        }
    }
}