package com.example.kursachclient.presentation.fragment.login_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.kursachclient.R
import com.example.kursachclient.SharedPreference
import com.example.kursachclient.databinding.FragmentLoginBinding
import com.example.kursachclient.presentation.MainActivity

class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding

    val viewModel = LoginViewModel()
    lateinit var pref: SharedPreference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        pref = SharedPreference(requireContext())
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        // Проверка залогинен ли чел
        if (pref.getValue() == null) {
            (activity as? MainActivity)?.hideBottomNav()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tvRegister.setOnClickListener { findNavController().navigate(R.id.action_loginFragment_to_registrationFragment) }
        binding.btLogin.setOnClickListener {
            viewModel.login(binding.etLogin.text.toString(), binding.etPassword.text.toString())
        }
        viewModel.liveData.observe(viewLifecycleOwner){
            if(it != null && !it.token.isNullOrEmpty()){
                pref.saveValue(it.token)
                (activity as? MainActivity)?.displayBottomNav()
                findNavController().navigate(R.id.action_loginFragment_to_bookFragment)
            }
        }
        viewModel.liveDataToast.observe(viewLifecycleOwner){
            showToast(it)
        }
        if(pref.getValue() != null){
            // Проверка действительности токена
            (activity as? MainActivity)?.displayBottomNav()
            findNavController().navigate(R.id.bookFragment)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}