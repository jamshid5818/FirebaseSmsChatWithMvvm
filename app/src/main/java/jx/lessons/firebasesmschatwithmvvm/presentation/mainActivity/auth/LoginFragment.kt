package jx.lessons.firebaseSmsChatWithMvvm.presentation.mainActivity.auth

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import jx.lessons.firebaseSmsChatWithMvvm.R
import jx.lessons.firebaseSmsChatWithMvvm.data.utils.*
import jx.lessons.firebaseSmsChatWithMvvm.databinding.FragmentLoginBinding
import jx.lessons.firebaseSmsChatWithMvvm.presentation.mainActivity.BaseFragment

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    val viewModel: AuthViewModel by viewModels()
    private val shared by lazy {
        SharedPref(requireContext())
    }
    override fun onViewCreate() {
        observer()
        binding.loginBtn.setOnClickListener {
            if (validation()) {
                shared.getToken()?.let { it1 ->
                    viewModel.login(
                        email = binding.emailEt.text.toString(),
                        password = binding.passEt.text.toString(),
                        it1
                    )
                }
            }
        }
        binding.forgotPassLabel.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }

        binding.registerLabel.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun validation(): Boolean {
        var isValid = true

        if (binding.emailEt.text.isNullOrEmpty()){
            isValid = false
            toast(getString(R.string.enter_email))
        }else{
            if (!binding.emailEt.text.toString().isValidEmail()){
                isValid = false
                toast(getString(R.string.invalid_email))
            }
        }
        if (binding.passEt.text.isNullOrEmpty()){
            isValid = false
            toast(getString(R.string.enter_password))
        }else{
            if (binding.passEt.text.toString().length < 8){
                isValid = false
                toast(getString(R.string.invalid_password))
            }
        }
        return isValid
    }

    private fun observer() {
        viewModel.login.observe(viewLifecycleOwner) { state ->
            when(state){
                is UiState.Loading -> {
                    binding.loginBtn.text = ""
                    binding.loginProgress.show()
                }
                is UiState.Failure -> {
                    binding.loginBtn.text = "Login"
                    binding.loginProgress.gone()
                    toast(state.message)
                }
                is UiState.Success -> {
                    binding.loginBtn.text = "Login"
                    binding.loginProgress.gone()
                    toast(state.data)
                    shared.setEmail(firebasePathgmail(binding.emailEt.text.toString()))
                    shared.setGender(state.data.toString())
                    toast(state.data.toString())
                    findNavController().navigate(R.id.action_loginFragment_to_personFragment)
                }
            }
        }
    }
}