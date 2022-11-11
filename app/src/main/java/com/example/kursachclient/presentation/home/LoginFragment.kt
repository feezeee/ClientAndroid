package com.example.kursachclient.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        if(pref.getValue() == null){
            (activity as? MainActivity)?.hideBottomNav()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tvRegister.setOnClickListener { findNavController().navigate(R.id.action_loginFragment_to_registrationFragment) }
        binding.btLogin.setOnClickListener {
            viewModel.login(binding.etLogin.text.toString(), binding.etPassword.text.toString())

        }
        viewModel.tokenLiveData.observe(viewLifecycleOwner){

            if(it.isSuccess){
                pref.saveValue(it.getOrNull()!!.token)
                findNavController().navigate(R.id.homeFragment)
            }
            else{
//                ............ Делаем что-то
            }

        }
        if(pref.getValue() != null){
            // Проверка действительности токена
            (activity as? MainActivity)?.displayBottomNav()
            findNavController().navigate(R.id.homeFragment)
        }
    }
}