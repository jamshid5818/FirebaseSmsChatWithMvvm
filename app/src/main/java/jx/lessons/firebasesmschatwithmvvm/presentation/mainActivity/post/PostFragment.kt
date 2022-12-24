package jx.lessons.firebasesmschatwithmvvm.presentation.mainActivity.post


import android.app.Activity
import android.net.Uri
import android.util.Log
import android.widget.EditText
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import jx.lessons.firebasesmschatwithmvvm.data.utils.*
import jx.lessons.firebasesmschatwithmvvm.databinding.FragmentPostBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import jx.lessons.firebasesmschatwithmvvm.R
import jx.lessons.firebasesmschatwithmvvm.data.model.Comments
import jx.lessons.firebasesmschatwithmvvm.data.model.Downloads
import jx.lessons.firebasesmschatwithmvvm.data.model.Likes
import jx.lessons.firebasesmschatwithmvvm.data.model.Post
import jx.lessons.firebasesmschatwithmvvm.presentation.mainActivity.BaseFragment

@AndroidEntryPoint
class PostFragment : BaseFragment<FragmentPostBinding>(FragmentPostBinding::inflate) {
    val viewModel: PostViewModel by viewModels()
    var tagsList = ArrayList<String>()
    private val sharedPref by lazy {
        SharedPref(requireContext())
    }
    private var imageUri: Uri? = null
    override fun onViewCreate() {
        observer()
        binding.imageView.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)
                .galleryOnly()
                .createIntent {intent->
                    startForPostPictureResult.launch(intent)
                }
        }
        binding.addTagImage.setOnClickListener {
            showAddTagDialog()
        }
        val comments = ArrayList<Comments>()
        val likes = ArrayList<Likes>()
        val downloads= ArrayList<Downloads>()
        comments.add(Comments(sharedPref.getEmail().toString(),"\uD83D\uDC4D\uD83D\uDC4D\uD83D\uDC4D", System.currentTimeMillis()))
        likes.add(Likes(sharedPref.getEmail().toString(), System.currentTimeMillis()))
        downloads.add(Downloads(sharedPref.getEmail().toString(), System.currentTimeMillis()))
        binding.publishPostBtn.setOnClickListener {
            if (validation()){
                val unixTime = System.currentTimeMillis()
                viewModel.upLoadPost(
                    Post(
                        comments,
                        likes,
                        binding.description.text.toString(),
                        binding.title.text.toString(),
                        sharedPref.getEmail()!!,
                        tagsList,
                        unixTime,
                        downloads
                    ),
                    imageUri!!
                )
            }
        }
    }

    private fun showAddTagDialog() {
        val dialog = requireContext().createDialog(R.layout.add_tag_dialog, true)
        val button = dialog.findViewById<MaterialButton>(R.id.tag_dialog_add)
        val editText = dialog.findViewById<EditText>(R.id.tag_dialog_et)
        button.setOnClickListener {
            if (editText.text.toString().isEmpty()) {
                toast(getString(R.string.error_tag_text))
            } else {
                val text = editText.text.toString()
                tagsList.add(text)
                binding.chipGroup.addChip_createPost(text, tagsList)
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private val startForPostPictureResult =registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result:ActivityResult->
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                imageUri = result.data!!.data
                Glide.with(this).load(imageUri).into(binding.imageView)
            }
            getStorageImage.error -> {
                toast("${result.data}")
            }
            else -> {
                Log.d("PostFragment", "Get Image Cancelled")
            }
        }
    }

    private fun validation(): Boolean {
        var isValid = true
        if (imageUri.toString().isEmpty()){
            isValid = false
            snackbar("You did not post a picture", binding.fullLayout)
        }
        if (binding.description.text.toString().isEmpty()){
            isValid = false
            snackbar("Write a description", binding.fullLayout)
        }
        if (binding.title.text.toString().isEmpty()){
            isValid = false
            snackbar("Write a Title", binding.fullLayout)
        }
        if (tagsList.isEmpty()){
            isValid = false
            snackbar("Enter 1 or more tags", binding.fullLayout)
        }
        return isValid
    }

    private fun observer() {
        viewModel.uploadPost.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Loading->{
                    binding.publishPostBtn.gone()
                    binding.progressBarButton.show()
                }
                is UiState.Failure->{
                    binding.progressBarButton.gone()
                    binding.publishPostBtn.show()
                    snackbar("Upload Failed", binding.fullLayout)
                }
                is UiState.Success->{
                    binding.progressBarButton.gone()
                    binding.publishPostBtn.show()
                    snackbar("SuccesFully", binding.fullLayout)
                    navController.popBackStack()
                }
            }
        }
    }
}