package com.example.kursachclient.presentation.fragment.register_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.kursachclient.databinding.FragmentRegistrationBinding
import com.example.kursachclient.domain.model.registration.PostRegisterModel
import com.example.kursachclient.presentation.fragment.BaseFragment

class RegistrationFragment : BaseFragment() {
    lateinit var binding: FragmentRegistrationBinding

    val viewModel = RegistrationViewModel()

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

                if (firstName.isEmpty()) {
                    binding.etRegistrationFirstName.error = "Не может быть пустым"
                    progressBarIsDisplayed(false)
                    return@setOnClickListener
                }
                if (lastName.isEmpty()) {
                    binding.etRegistrationLastName.error = "Не может быть пустым"
                    progressBarIsDisplayed(false)
                    return@setOnClickListener
                }

                val phoneNumberRegex = "^\\+\\d{3}\\s\\(\\d{2}\\)\\s\\d{3}\\s\\d{2}\\s\\d{2}".toRegex()
                val resultNumber =
                    phoneNumberRegex.matchEntire(binding.etRegistrationPhoneNumber.text.toString())
                if (resultNumber == null) {
                    binding.etRegistrationPhoneNumber.error = "Формат +*** (**) *** ** **"
                    progressBarIsDisplayed(false)
                    return@setOnClickListener
                }
                val login = binding.etRegistrationLogin.text.toString()
                if (login.isEmpty()) {
                    binding.etRegistrationLogin.error = "Не может быть пустым"
                    progressBarIsDisplayed(false)
                    return@setOnClickListener
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(login).matches()) {
                    binding.etRegistrationLogin.error = "Некорректный логин"
                    progressBarIsDisplayed(false)
                    return@setOnClickListener
                }
                val password = binding.etRegistrationPassword.text.toString()
                if (password.isEmpty()) {
                    binding.etRegistrationPassword.error = "Не может быть пустым"
                    progressBarIsDisplayed(false)
                    return@setOnClickListener
                }
                if (password.length < 8) {
                    binding.etRegistrationPassword.error = "Минимальная длина пароля 8 символов"
                    progressBarIsDisplayed(false)
                    return@setOnClickListener
                }

                val registerModel =
                    PostRegisterModel(firstName, lastName, resultNumber.value, login, password)

                viewModel.register(registerModel)
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