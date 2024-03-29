package com.example.kursachclient.presentation.fragment.login_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.kursachclient.R
import com.example.kursachclient.SharedPreference
import com.example.kursachclient.databinding.FragmentLoginBinding
import com.example.kursachclient.MainActivity
import com.example.kursachclient.presentation.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

@AndroidEntryPoint
class LoginFragment : BaseFragment() {
    lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        pref = SharedPreference(requireContext())
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.hideBottomNav()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tvLoginRegistrationRef.setOnClickListener {
            try {
                findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        binding.btnLoginSignIn.setOnClickListener {
            try {
                progressBarIsDisplayed(true)
                val login = binding.etLoginLogin.text.toString()
                val password = binding.etLoginPassword.text.toString()

                var status = true

                if (login.isEmpty()) {
                    binding.etLoginLogin.error = "Не может быть пустым"
                    progressBarIsDisplayed(false)
                    status = false
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(login).matches()) {
                    binding.etLoginLogin.error = "Некорректный логин"
                    progressBarIsDisplayed(false)
                    status = false
                }

                if (password.isEmpty()) {
                    binding.etLoginPassword.error = "Не может быть пустым"
                    progressBarIsDisplayed(false)
                    status = false
                }
                if (password.length < 8) {
                    binding.etLoginPassword.error = "Минимальная длина пароля 8 символов"
                    progressBarIsDisplayed(false)
                    status = false
                }
                if(status){
                    viewModel.login(login, password)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        viewModel.liveData.observe(viewLifecycleOwner) {
            try {
                progressBarIsDisplayed(false)
                pref.saveToken(it.token)
                pref.saveRole(it.role)
                when(it.role.lowercase()){
                    "user" -> {
                        (activity as? MainActivity)?.hideOrderMenu()
                    }
                    "admin" -> {
                        (activity as? MainActivity)?.showOrderMenu()
                    }
                    else -> {
                        (activity as? MainActivity)?.showOrderMenu()
                    }
                }
                (activity as? MainActivity)?.displayBottomNav()
                findNavController().navigate(R.id.action_loginFragment_to_bookFragment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        viewModel.liveDataShowToast.observe(viewLifecycleOwner) {
            try {
                showToast(it)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        viewModel.liveDataNeedToNotifySomeProblemWithServer.observe(viewLifecycleOwner) {
            try {
                progressBarIsDisplayed(false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        try {
            val token = pref.getToken()
            val role = pref.getRole()
            if (token.isNotEmpty() && role.isNotEmpty()) {
                try {
                    when(role.lowercase()){
                        "user" -> {
                            (activity as? MainActivity)?.hideOrderMenu()
                        }
                        "admin" -> {
                            (activity as? MainActivity)?.showOrderMenu()
                        }
                        else -> {
                            (activity as? MainActivity)?.showOrderMenu()
                        }
                    }
                    (activity as? MainActivity)?.displayBottomNav()
                    findNavController().navigate(R.id.bookFragment)
                }
                catch (e: Exception){
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun progressBarIsDisplayed(isDisplayed: Boolean) {
        try {
            when (isDisplayed) {
                true -> {
                    binding.btnLoginSignIn.isEnabled = false
                    binding.tvLoginRegistrationRef.isEnabled = false
                    binding.clLoginProgressBar.visibility = View.VISIBLE
                }
                false -> {
                    binding.btnLoginSignIn.isEnabled = true
                    binding.tvLoginRegistrationRef.isEnabled = true
                    binding.clLoginProgressBar.visibility = View.GONE
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}