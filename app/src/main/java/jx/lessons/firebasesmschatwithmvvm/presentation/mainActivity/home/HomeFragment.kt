package jx.lessons.firebasesmschatwithmvvm.presentation.mainActivity.home

import android.app.DownloadManager
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import jx.lessons.firebasesmschatwithmvvm.data.model.Downloads
import jx.lessons.firebasesmschatwithmvvm.data.model.Likes
import jx.lessons.firebasesmschatwithmvvm.data.utils.*
import jx.lessons.firebasesmschatwithmvvm.databinding.FragmentHomeBinding
import jx.lessons.firebasesmschatwithmvvm.presentation.mainActivity.BaseFragment

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    ListClickView {
    val viewModel: HomeViewModel by viewModels()
    lateinit var adapter: PostAdapter
    private val getClickedLikeUsersList = ArrayList<Likes>()
    private var isClickedLike = false
    val shared by lazy {
        SharedPref(requireContext())
    }
    override fun onViewCreate() {
        observer()
        viewModel.getAllPost()
        binding.list.setHasFixedSize(true)
        binding.list.layoutManager = LinearLayoutManager(requireContext())
        binding.swipe.setOnRefreshListener {
            viewModel.getAllPost()
        }
    }

    private fun observer() {
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
        viewModel.addLike.observe(viewLifecycleOwner){state->
            when(state){
                is  UiState.Loading->{
                }
                is UiState.Failure->{
                    toast(state.data)
                }
                is UiState.Success->{
                    toast(state.data)
                }
            }
        }
        viewModel.addDownload.observe(viewLifecycleOwner){state->
            when(state){
                is  UiState.Loading->{

                }
                is UiState.Failure->{
                toast(state.message)
                }
                is UiState.Success->{

                }
            }
        }
    }

    override fun clickedLike(key:String) {
        shared.getEmail()?.let { Likes(it,System.currentTimeMillis()) }
            ?.let { viewModel.addLike(key, it) }
    }

    override fun clickedComment(unixTime:Long) {

    }

    override fun doubleClickImageView(key:String) {
        shared.getEmail()?.let { Likes(it,System.currentTimeMillis()) }
            ?.let { viewModel.addLike(key, it) }
    }

    override fun longClickedLike(key:String) {
        toast("fdasf")
    }

    override fun downloadClicked(url:String,key:String) {
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("File")
            .setDescription("Downloading")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
        val dm = requireActivity().getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(request)
        shared.getEmail()?.let { Downloads(it, key.toLong()) }
            ?.let { viewModel.addDownload(key, it) }
    }

}
