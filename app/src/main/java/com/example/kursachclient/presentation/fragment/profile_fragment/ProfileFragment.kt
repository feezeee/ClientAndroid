package com.example.kursachclient.presentation.fragment.profile_fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.example.kursachclient.R
import com.example.kursachclient.SharedPreference
import com.example.kursachclient.databinding.FragmentProfileBinding
import com.example.kursachclient.presentation.fragment.BaseFragment
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ProfileFragment : BaseFragment() {
    lateinit var binding: FragmentProfileBinding
    lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        pref = SharedPreference(requireContext())
        viewModel = ProfileViewModel()
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBarIsDisplayed(true)

        viewModel.liveData.observe(viewLifecycleOwner) {
            try {
                binding.tvSettingFullUserName.text = "${it.firstName} ${it.lastName}"
                binding.tvSettingPhoneNumber.text = it.phoneNumber.toString()
                var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                binding.tvOrderDescriptionCreatedDate.text =
                    Instant.ofEpochSecond(it.registration_date)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime().format(formatter).toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            progressBarIsDisplayed(false)
        }
        binding.btnProfileLogout.setOnClickListener {
            try {
                pref.clearValue()
                findNavController().navigate(R.id.action_settingFragment_to_loginFragment)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
        viewModel.liveDataShowToast.observe(viewLifecycleOwner) { message ->
            try {
                showToast(message)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        viewModel.liveDataSignOutAndRedirect.observe(viewLifecycleOwner) {
            try {
                signOutAndRedirect()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        viewModel.liveDataNeedToNotifyProgressBarHide.observe(viewLifecycleOwner) {
            try {
                progressBarIsDisplayed(false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        viewModel.getUserProfile(pref.getToken())
    }

    private fun progressBarIsDisplayed(isDisplayed: Boolean) {
        try {
            when (isDisplayed) {
                true -> {
                    binding.btnProfileLogout.isEnabled = false
                    binding.clProfileProgressBar.visibility = View.VISIBLE
                }
                false -> {
                    binding.btnProfileLogout.isEnabled = true
                    binding.clProfileProgressBar.visibility = View.GONE
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}