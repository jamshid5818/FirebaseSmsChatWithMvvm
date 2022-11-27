package jx.lessons.firebasesmschatwithmvvm.presentation.mainActivity.home

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenCreated
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import jx.lessons.firebasesmschatwithmvvm.R
import jx.lessons.firebasesmschatwithmvvm.data.model.Likes
import jx.lessons.firebasesmschatwithmvvm.data.utils.*
import jx.lessons.firebasesmschatwithmvvm.databinding.FragmentHomeBinding
import jx.lessons.firebasesmschatwithmvvm.presentation.BaseFragment
import jx.lessons.firebasesmschatwithmvvm.presentation.chatActivity.ChatActivity
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    ListClickView {
    val viewModel: HomeViewModel by viewModels()
    lateinit var adapter: PostAdapter
    private val getClickedLikeUsersList = ArrayList<Likes>()
    var isClickedLike = false
    override fun onViewCreate() {
        observer()
        viewModel.getAllPost()
        binding.list.setHasFixedSize(true)
        binding.list.layoutManager = LinearLayoutManager(requireContext())
        binding.swipe.setOnRefreshListener {
            viewModel.getAllPost()
        }
        binding.homeToChat.setOnClickListener {
            val intent = Intent(requireActivity(),ChatActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
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
                            adapter = state.data?.let { PostAdapter(it,requireContext(), this@HomeFragment) }!!
                            binding.list.adapter = adapter
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

    }

    override fun longClickedLike(randomKey: String) {
        toast("fdasf")
    }
}
