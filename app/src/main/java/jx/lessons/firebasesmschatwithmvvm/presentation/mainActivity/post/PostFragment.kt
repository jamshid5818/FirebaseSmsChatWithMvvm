package jx.lessons.firebaseSmsChatWithMvvm.presentation.mainActivity.post


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.EditText
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import com.github.dhaval2404.imagepicker.ImagePicker
import jx.lessons.firebaseSmsChatWithMvvm.data.utils.*
import jx.lessons.firebaseSmsChatWithMvvm.databinding.FragmentPostBinding
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import jx.lessons.firebaseSmsChatWithMvvm.R
import jx.lessons.firebaseSmsChatWithMvvm.data.model.Comments
import jx.lessons.firebaseSmsChatWithMvvm.data.model.Downloads
import jx.lessons.firebaseSmsChatWithMvvm.data.model.Likes
import jx.lessons.firebaseSmsChatWithMvvm.data.model.Post
import jx.lessons.firebaseSmsChatWithMvvm.presentation.mainActivity.BaseFragment

@AndroidEntryPoint
class PostFragment : BaseFragment<FragmentPostBinding>(FragmentPostBinding::inflate) {
    val viewModel: PostViewModel by viewModels()
    var tagsList = ArrayList<String>()
    private val sharedPref by lazy {
        SharedPref(requireContext())
    }
    private var imageUri: Uri ="".toUri()
    override fun onViewCreate() {
        observer()
        binding.imageView.setOnClickListener {
            val dialog = requireContext().createDialog(R.layout.dialog_goto_gallery_or_camera,true)
            val gallery = dialog.findViewById<LinearLayoutCompat>(R.id.gallery)
            val camera = dialog.findViewById<LinearLayoutCompat>(R.id.camera)
            gallery.setOnClickListener {
                ImagePicker.with(this).galleryOnly().galleryMimeTypes(arrayOf("image/*")).crop()
                    .maxResultSize(400, 400).start()
                dialog.dismiss()
            }
            camera.setOnClickListener {
                ImagePicker.with(this).cameraOnly().crop().maxResultSize(400, 400).start()
                dialog.dismiss()
            }
        }
        binding.addTagImage.setOnClickListener {
            showAddTagDialog()
        }
        val comments = ArrayList<Comments>()
        val likes = ArrayList<Likes>()
        val downloads= ArrayList<Downloads>()
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
                    imageUri
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK && requestCode== ImagePicker.REQUEST_CODE) {
            imageUri= data?.data!!
            binding.imageView.setImageURI(data.data)
        }

    }

    private fun validation(): Boolean {
        var isValid = true
        if (imageUri.toString()==""){
            isValid = false
            snackbar("You did not post a picture", binding.fullLayout)
            return false
        }
        if (binding.description.text.toString().isEmpty()){
            isValid = false
            snackbar("Write a description", binding.fullLayout)
            return false
        }
        if (binding.title.text.toString().isEmpty()){
            isValid = false
            snackbar("Write a Title", binding.fullLayout)
            return false
        }
        if (tagsList.isEmpty()){
            isValid = false
            snackbar("Enter 1 or more tags", binding.fullLayout)
            return false
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