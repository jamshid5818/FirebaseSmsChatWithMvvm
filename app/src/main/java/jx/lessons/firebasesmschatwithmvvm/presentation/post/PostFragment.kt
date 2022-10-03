package jx.lessons.firebasesmschatwithmvvm.presentation.post


import android.app.Activity
import android.net.Uri
import android.util.Log
import android.widget.EditText
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import jx.lessons.firebasesmschatwithmvvm.data.utils.*
import jx.lessons.firebasesmschatwithmvvm.databinding.FragmentPostBinding
import jx.lessons.firebasesmschatwithmvvm.presentation.BaseFragment
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import jx.lessons.firebasesmschatwithmvvm.R
import jx.lessons.firebasesmschatwithmvvm.data.model.Comments
import jx.lessons.firebasesmschatwithmvvm.data.model.Likes
import jx.lessons.firebasesmschatwithmvvm.data.model.Post

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
        binding.publishPostBtn.setOnClickListener {
            if (validation()){
                sharedPref.getEmail()?.let { Comments(it, "") }?.let { comments.add(it) }
                sharedPref.getEmail()?.let { Likes(it) }?.let { likes.add(it) }
                viewModel.upLoadPost(
                    Post(
                        comments,
                        likes,
                        binding.description.text.toString(),
                        binding.title.text.toString(),
                        sharedPref.getEmail()!!,
                        tagsList,
                        ""
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
                binding.imageView.setImageURI(imageUri)
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