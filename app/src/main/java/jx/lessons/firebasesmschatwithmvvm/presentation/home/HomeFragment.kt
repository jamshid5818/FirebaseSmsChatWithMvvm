package jx.lessons.firebasesmschatwithmvvm.presentation.home

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenCreated
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import jx.lessons.firebasesmschatwithmvvm.data.model.Likes
import jx.lessons.firebasesmschatwithmvvm.data.utils.*
import jx.lessons.firebasesmschatwithmvvm.databinding.FragmentHomeBinding
import jx.lessons.firebasesmschatwithmvvm.presentation.BaseFragment
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),ListClickView {
    val viewModel: HomeViewModel by viewModels()
    lateinit var adapter: PostAdapter
    private val getClickedLikeUsersList = ArrayList<Likes>()
    var isClickedLike = false
    override fun onViewCreate() {
        observer()
        viewModel.getAllPost()
        binding.list.setHasFixedSize(true)
        binding.list.layoutManager = LinearLayoutManager(requireContext())
        adapter = PostAdapter(requireContext(), this)
        binding.list.adapter = adapter
        binding.swipe.setOnRefreshListener {
            viewModel.getAllPost()
        }
    }

    private fun observer() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.whenCreated {
                viewModel.getAllPost.observe(viewLifecycleOwner){state->
                    when(state){
                        is UiState.Loading->{
                            binding.swipe.isRefreshing = true
                        }
                        is UiState.Failure->{
                            binding.swipe.isRefreshing = false
                            snackbar(state.message.toString(), binding.swipe)
                        }
                        is UiState.Success->{
                            binding.swipe.isRefreshing = false
                            state.data?.let { adapter.setData(it) }
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.whenCreated {
                viewModel.getClickedLikeUsers.observe(viewLifecycleOwner){state->
                    when(state){
                        is  UiState.Loading->{

                        }
                        is UiState.Failure->{

                        }
                        is UiState.Success->{
                            state.data?.let { getClickedLikeUsersList.addAll(it) }
                            getClickedLikeUsersList.forEach { likes ->
                                if (likes.email==getEmail(requireContext())){
                                    isClickedLike = true
                                }
                            }

                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.whenCreated {
                viewModel.setLikePost.observe(viewLifecycleOwner){state->
                    when(state){
                        is  UiState.Loading->{

                        }
                        is UiState.Failure->{
                            snackbar("Failure like", binding.homeView)
                        }
                        is UiState.Success->{
                            snackbar("Liked Post", binding.homeView)
                        }
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.whenCreated {
                viewModel.setMinusLike.observe(viewLifecycleOwner){state->
                    when(state){
                        is  UiState.Loading->{

                        }
                        is UiState.Failure->{
                            snackbar("Failure dizlike", binding.homeView)
                        }
                        is UiState.Success->{
                            snackbar("Dizliked Post", binding.homeView)
                        }
                    }
                }
            }
        }
    }

    override fun clickedLike(randomKey:String) {
        viewModel.getClickedLikeUsers(randomKey)
        if (isClickedLike){
            viewModel.setMinusLike(getEmail(requireContext()), randomKey)
        }else{
            viewModel.setLikePost(getClickedLikeUsersList,randomKey, getEmail(requireContext()))
        }
    }

    override fun clickedComment(randomKey:String) {

    }

    override fun doubleClickImageView(randomKey:String) {
        viewModel.getClickedLikeUsers(randomKey)
        if (isClickedLike){
            viewModel.setMinusLike(getEmail(requireContext()), randomKey)
        }else{
            viewModel.setLikePost(getClickedLikeUsersList, randomKey,getEmail(requireContext()))
        }
    }
}
