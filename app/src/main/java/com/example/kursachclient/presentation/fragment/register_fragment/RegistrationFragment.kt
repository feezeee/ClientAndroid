package com.example.kursachclient.presentation.fragment.register_fragment

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.kursachclient.databinding.FragmentRegistrationBinding
import com.example.kursachclient.domain.model.registration.PostRegisterModel
import com.example.kursachclient.presentation.fragment.BaseFragment
import com.example.kursachclient.presentation.fragment.profile_fragment.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : BaseFragment() {
    lateinit var binding: FragmentRegistrationBinding

    private val viewModel by viewModels<RegistrationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnRegistrationRegister.setOnClickListener {
            try{
                progressBarIsDisplayed(true)
                val firstName = binding.etRegistrationFirstName.text.toString()
                val lastName = binding.etRegistrationLastName.text.toString()

                var status = true

                if (firstName.isEmpty()) {
                    binding.etRegistrationFirstName.error = "Не может быть пустым"
                    progressBarIsDisplayed(false)
                    status = false
                }
                if (lastName.isEmpty()) {
                    binding.etRegistrationLastName.error = "Не может быть пустым"
                    progressBarIsDisplayed(false)
                    status = false
                }

                val phoneNumberRegex = "^\\+\\d{3}\\s\\(\\d{2}\\)\\s\\d{3}\\s\\d{2}\\s\\d{2}".toRegex()
                val resultNumber =
                    phoneNumberRegex.matchEntire(binding.etRegistrationPhoneNumber.text.toString())
                if (resultNumber == null) {
                    binding.etRegistrationPhoneNumber.error = "Формат +*** (**) *** ** **"
                    progressBarIsDisplayed(false)
                    status = false
                }
                val login = binding.etRegistrationLogin.text.toString()
                if (login.isEmpty()) {
                    binding.etRegistrationLogin.error = "Не может быть пустым"
                    progressBarIsDisplayed(false)
                    status = false
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(login).matches()) {
                    binding.etRegistrationLogin.error = "Некорректный логин"
                    progressBarIsDisplayed(false)
                    status = false
                }
                val password = binding.etRegistrationPassword.text.toString()
                if (password.isEmpty()) {
                    binding.etRegistrationPassword.error = "Не может быть пустым"
                    progressBarIsDisplayed(false)
                    status = false
                }
                if (password.length < 8) {
                    binding.etRegistrationPassword.error = "Минимальная длина пароля 8 символов"
                    progressBarIsDisplayed(false)
                    status = false
                }

                if(status){
                    val registerModel =
                        PostRegisterModel(firstName, lastName, binding.etRegistrationPhoneNumber.text.toString(), login, password)

                    viewModel.register(registerModel)
                }
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

        binding.ivRegistrationBack.setOnClickListener {
            try {
                findNavController().popBackStack()
            }
            catch (e: Exception){
                e.printStackTrace()
            }

        }

        viewModel.liveData.observe(viewLifecycleOwner) {
            try {
                if(it == true){
                    findNavController().popBackStack()
                }
                else{
                    progressBarIsDisplayed(false)
                }
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

        viewModel.liveDataNeedToNotifyLoginIsNotFree.observe(viewLifecycleOwner) {
            try {
                progressBarIsDisplayed(false)
                binding.etRegistrationLogin.error = "Этот логин уже занят"
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

        viewModel.liveDataShowToast.observe(viewLifecycleOwner) {
            try {
                showToast(it)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    private fun progressBarIsDisplayed(isDisplayed: Boolean) {
        try {
            when (isDisplayed) {
                true -> {
                    binding.btnRegistrationRegister.isEnabled = false
                    binding.clRegistrationProgressBar.visibility = View.VISIBLE
                }
                false -> {
                    binding.btnRegistrationRegister.isEnabled = true
                    binding.clRegistrationProgressBar.visibility = View.GONE
                }
            }
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }
}