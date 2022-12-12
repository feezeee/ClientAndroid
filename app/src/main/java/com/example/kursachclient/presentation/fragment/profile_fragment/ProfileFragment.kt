package com.example.kursachclient.presentation.fragment.profile_fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kursachclient.R
import com.example.kursachclient.SharedPreference
import com.example.kursachclient.databinding.FragmentProfileBinding
import com.example.kursachclient.domain.model.order.GetOrderResponse
import com.example.kursachclient.presentation.fragment.BaseFragment
import com.example.kursachclient.presentation.fragment.order_fragment.OrderViewModel
import com.example.kursachclient.presentation.sheet_dialog_fragment.payment.PaymentSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {
    lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModels<ProfileViewModel>()
    lateinit var adapter: ProfileAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        pref = SharedPreference(requireContext())
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBarProfileIsDisplayed(true)
        progressBarOrdersIsDisplayed(true)

        viewModel.liveData.observe(viewLifecycleOwner) {
            try {
                binding.tvSettingFullUserName.text = "${it.firstName} ${it.lastName}"
                binding.tvSettingPhoneNumber.text = it.phoneNumber.toString()
                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                binding.tvOrderDescriptionCreatedDate.text =
                    Instant.ofEpochSecond(it.registration_date)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime().format(formatter).toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            progressBarProfileIsDisplayed(false)
        }

        viewModel.liveDataOrderList.observe(viewLifecycleOwner) {
            try {
                adapter = ProfileAdapter(it) { it -> payLongClickListener(it) }
                binding.rvProfileOrderList.layoutManager = GridLayoutManager(context, 1)
                binding.rvProfileOrderList.adapter = adapter
                progressBarOrdersIsDisplayed(false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            progressBarProfileIsDisplayed(false)
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

        binding.srProfileOrderList.setOnRefreshListener {
            refresh()
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
        viewModel.liveDataUserInfNeedToNotifyProgressBarHide.observe(viewLifecycleOwner) {
            try {
                progressBarProfileIsDisplayed(false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        viewModel.liveDataPersonalOrdersNeedToNotifyProgressBarHide.observe(viewLifecycleOwner) {
            try {
                progressBarOrdersIsDisplayed(false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        viewModel.getUserProfile(pref.getToken())
        viewModel.getPersonalOrderList(pref.getToken())
    }

    private fun refresh(){
        binding.srProfileOrderList.isRefreshing = false
        progressBarOrdersIsDisplayed(true)
        progressBarProfileIsDisplayed(true)
        viewModel.getPersonalOrderList(pref.getToken())
        viewModel.getUserProfile(pref.getToken())
    }

    private fun payLongClickListener(item: GetOrderResponse) {
        try{

            val paySheetDialog = PaymentSheetDialogFragment(item) { updateInfo() }
            paySheetDialog.show(childFragmentManager, "FEEZE_PAY")
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun updateInfo(){
        progressBarOrdersIsDisplayed(true)
        viewModel.getPersonalOrderList(pref.getToken())
    }


    private fun progressBarProfileIsDisplayed(isDisplayed: Boolean) {
        try {
            when (isDisplayed) {
                true -> {
                    binding.btnProfileLogout.isEnabled = false
                    binding.clProfileProfileProgressBar.visibility = View.VISIBLE
                }
                false -> {
                    binding.btnProfileLogout.isEnabled = true
                    binding.clProfileProfileProgressBar.visibility = View.GONE
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun progressBarOrdersIsDisplayed(isDisplayed: Boolean) {
        try {
            when (isDisplayed) {
                true -> {
                    binding.btnProfileLogout.isEnabled = false
                    binding.clProfileOrdersProgressBar.visibility = View.VISIBLE
                }
                false -> {
                    binding.btnProfileLogout.isEnabled = true
                    binding.clProfileOrdersProgressBar.visibility = View.GONE
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}