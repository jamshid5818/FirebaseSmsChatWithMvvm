package jx.lessons.firebasesmschatwithmvvm.presentation.chatActivity.kontakt

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jx.lessons.firebasesmschatwithmvvm.R

class ContactChatFragment : Fragment() {

    companion object {
        fun newInstance() = ContactChatFragment()
    }

    private lateinit var viewModel: ContactChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact_chat, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ContactChatViewModel::class.java)
        // TODO: Use the ViewModel
    }

}