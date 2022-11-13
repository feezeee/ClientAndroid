package com.example.kursachclient.presentation.fragment.register_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.kursachclient.databinding.FragmentRegistrationBinding

class RegistrationFragment : Fragment() {
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
        binding.bRegister.setOnClickListener {
            viewModel.register(
                binding.etFirstName.text.toString(),
                binding.etLastName.text.toString(),
                binding.etPhoneNumber.text.toString(),
                binding.etLogin.text.toString(),
                binding.etPassword.text.toString())
        }

        viewModel.registrationLiveDate.observe(viewLifecycleOwner){
            if(it.isSuccess && it.getOrNull()!!){
                findNavController().navigateUp();
//                findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
            }
        }
    }
}