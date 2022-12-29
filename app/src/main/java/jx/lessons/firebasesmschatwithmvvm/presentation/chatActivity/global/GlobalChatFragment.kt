package jx.lessons.firebaseSmsChatWithMvvm.presentation.chatActivity.global


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import jx.lessons.firebaseSmsChatWithMvvm.R
import jx.lessons.firebaseSmsChatWithMvvm.data.cloudMessaging.FcmNotificationsSender
import jx.lessons.firebaseSmsChatWithMvvm.data.utils.*
import jx.lessons.firebaseSmsChatWithMvvm.databinding.FragmentGlobalChatBinding
import jx.lessons.firebaseSmsChatWithMvvm.presentation.chatActivity.BaseChatFragment
import jx.lessons.firebaseSmsChatWithMvvm.presentation.chatActivity.ChatAdapter


@AndroidEntryPoint
class GlobalChatFragment : BaseChatFragment<FragmentGlobalChatBinding>(FragmentGlobalChatBinding::inflate) {
    val viewModel: GlobalChatViewModel by viewModels()
    lateinit var adapter: ChatAdapter
    var sms=""
    private val shared by lazy {
        SharedPref(requireContext())
    }
    private var token: String? = null
    var imageUri:Uri?=null
    var unixTime :Long=0
    override fun onViewCreate() {
        observer()
        viewModel.getAllSms()
        binding.list.setHasFixedSize(true)
        binding.list.layoutManager=LinearLayoutManager(requireContext())
        binding.swipe.setOnRefreshListener {
            viewModel.getAllSms()
        }
        binding.sendPhoto.setOnClickListener {
            val dialog = requireContext().createDialog(R.layout.dialog_goto_gallery_or_camera, true)
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
        binding.sendSms.setOnClickListener {
            if (shared.getEmail()=="" && shared.getGender()==""){
                requireActivity().finish()
                toast("Oldin ro'yxatdan o'tgan bo'lishiz kerak")
            }else{
                if (binding.getSmsText.text.isNotEmpty() && binding.getSmsText.text.toString()!=""){
                    sms= binding.getSmsText.text.toString()
                    unixTime=System.currentTimeMillis()
                    shared.getGender()?.let { it1 -> viewModel.sendSms(shared.getEmail()!!,sms, it1,unixTime, "".toUri()) }
                }
            }
        }
//        FirebaseMessaging.getInstance().subscribeToTopic("all")
    }

    private fun observer() {

        viewModel.getAllSms.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Loading->{
                    binding.swipe.isRefreshing = true
                }
                is UiState.Failure->{
                    binding.swipe.isRefreshing = false
                    toast(state.message.toString())
                }
                is UiState.Success->{
                    binding.swipe.isRefreshing = false
                    adapter = state.data?.let { ChatAdapter(it,requireContext()) }!!
                    binding.list.adapter=adapter
                    binding.list.scrollToPosition(adapter.list.size-1)
                }
            }
        }
        viewModel.sendSms.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Loading->{

                }
                is UiState.Failure->{
                    toast("Failure send")
                }
                is UiState.Success->{
                    // 1 user notification

                    val notificationsSender = FcmNotificationsSender("egO9u4zJ4FYUmA4ZUJlOgn2Dkwt2",
                    "Jamshid",
                    sms,
                    requireContext(),
                    requireActivity())
                    notificationsSender.SendNotifications()


                    // all users notification
//                    val notificationSender = FcmNotificationsSender("/topics/all",
//                        shared.getName(),
//                        sms, requireContext(),requireActivity()
//                    )
//                    notificationSender.SendNotifications()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK && requestCode== ImagePicker.REQUEST_CODE) {
            imageUri=data?.data
            val dialog = requireContext().bottomSheetDialog(R.layout.dialog_bottom_send_photo,requireActivity())
            val imageView = dialog.findViewById<ImageView>(R.id.imageBottom)
            val sendPhoto = dialog.findViewById<ImageView>(R.id.sendBottom)
            val smsTxt = dialog.findViewById<EditText>(R.id.smsText)
            imageView?.setImageURI(imageUri)
            sendPhoto?.setOnClickListener {
                imageUri?.let { uri ->
                    if (shared.getEmail() == ""){
                        toast("you must register first")
                        requireActivity().finish()
                    }else{
                        shared.getEmail()?.let { email ->
                            shared.getGender()?.let { gender ->
                                viewModel.sendSms(
                                    email = email,
                                    smsText = smsTxt?.text.toString(),
                                    gender = gender,
                                    imageUri = uri,
                                    unixTime = System.currentTimeMillis()
                                )
                                dialog.dismiss()
                            }
                        }
                    }
                }
            }
        }
    }
}