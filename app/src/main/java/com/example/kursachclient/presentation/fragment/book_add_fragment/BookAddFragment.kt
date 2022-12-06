package com.example.kursachclient.presentation.fragment.book_add_fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.kursachclient.SharedPreference
import com.example.kursachclient.databinding.FragmentBookAddBinding
import com.example.kursachclient.domain.model.book.AddBookRequest
import com.example.kursachclient.presentation.MainActivity
import com.example.kursachclient.presentation.fragment.BaseFragment
import java.math.RoundingMode
import java.net.URI
import java.util.jar.Manifest

class BookAddFragment : BaseFragment() {
    lateinit var binding: FragmentBookAddBinding
    lateinit var viewModel: BookAddViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = BookAddViewModel()
        pref = SharedPreference(requireContext())
        binding = FragmentBookAddBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.liveDataShowToast.observe(viewLifecycleOwner) { message ->
            try {
                showToast(message)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

        viewModel.liveDataSignOutAndRedirect.observe(viewLifecycleOwner) {
            try {
                signOutAndRedirect()
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
        viewModel.liveData.observe(viewLifecycleOwner) {
            try {
                progressBarIsDisplayed(false)
                findNavController().popBackStack()
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

        binding.ivBookAddBack.setOnClickListener {
            try {
                findNavController().popBackStack()
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
        binding.ivBookAddAdd.setOnClickListener {
            try {
                progressBarIsDisplayed(true)
                if (binding.etBookAddBookName.text.isEmpty()) {
                    binding.etBookAddBookName.error = "Не может быть пустым"
                    progressBarIsDisplayed(false)
                    return@setOnClickListener
                }
                if (binding.etBookAddBookTitle.text.isEmpty()) {
                    binding.etBookAddBookTitle.error = "Не может быть пустым"
                    progressBarIsDisplayed(false)
                    return@setOnClickListener
                }

                val priceRegex = "^\\d+.\\d{1,2}".toRegex()
                val resultPrice = priceRegex.matchEntire(binding.etBookAddBookPrice.text.toString())
                if (resultPrice == null) {
                    binding.etBookAddBookPrice.error = "Формат *.00"
                    progressBarIsDisplayed(false)
                    return@setOnClickListener
                }
                val resultPriceDecimal = resultPrice.value.toBigDecimal().setScale(2, RoundingMode.UP)
                if (resultPriceDecimal.toString() == "0.00") {
                    binding.etBookAddBookPrice.error = "Минимальная цена 0.01"
                    progressBarIsDisplayed(false)
                    return@setOnClickListener
                }

                val book =
                    AddBookRequest(
                        binding.etBookAddBookName.text.toString(),
                        binding.etBookAddBookTitle.text.toString(),
                        resultPriceDecimal.toDouble(),
                        null
                    )
                viewModel.addBook(book, pref.getToken())
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

        binding.ivBookAddMainImage.setOnClickListener {

            val pictureDialog = context?.let { context -> AlertDialog.Builder(context) }
            pictureDialog?.setTitle("Select Action")
            val pictureDialogItem = arrayOf(
                "Select photo from Gallery",
                "Capture photo from Camera"
            )
            pictureDialog?.setItems(pictureDialogItem) { dialog, which ->

                when (which) {
                    0 -> galleryCheckPermission()
                    1 -> cameraCheckPermission()
                }
            }

            pictureDialog?.show()
        }
    }

    private val CAMERA_REQUEST_CODE = 1
    private val GALLERY_REQUEST_CODE = 2

    private fun gallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun camera() {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }
    private fun cameraCheckPermission() {

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            camera()
        } else {
            showRotationalDialogForPermission()
        }

    }

    private fun galleryCheckPermission() {

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            gallery()
        } else {
            showRotationalDialogForPermission()
        }

    }
    private fun showRotationalDialogForPermission() {
        context?.let {
            AlertDialog.Builder(it)
                .setMessage(
                    "It looks like you have turned off permissions"
                            + "required for this feature. It can be enable under App settings!!!"
                )

                .setPositiveButton("Go TO SETTINGS") { _ , _ ->

                    try {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts(
                            "package",
                            (activity as? MainActivity)?.packageName,
                            null
                        )
                        intent.data = uri
                        startActivity(intent)

                    } catch (e: ActivityNotFoundException) {
                        e.printStackTrace()
                    }
                }
                .setNegativeButton("CANCEL") { dialog, _ ->
                    dialog.dismiss()
                }.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

            when (requestCode) {

                CAMERA_REQUEST_CODE -> {
                    val bitmap = data?.extras?.get("data") as Bitmap

                    // using coroutine image loader (coil)
                    binding.ivBookAddMainImage.load(bitmap) {
                        crossfade(true)
                        crossfade(1000)
                    }
                    binding.ivBookAddMainImage.scaleType = ImageView.ScaleType.FIT_XY
                }

                GALLERY_REQUEST_CODE -> {
                    binding.ivBookAddMainImage.load(data?.data) {
                        Log.d("TAG", data?.data.toString())
                        crossfade(true)
                        crossfade(1000)
                    }
                    binding.ivBookAddMainImage.scaleType = ImageView.ScaleType.FIT_XY

                }
            }
        }
    }

    private fun progressBarIsDisplayed(isDisplayed: Boolean) {
        try {
            when (isDisplayed) {
                true -> {
                    binding.ivBookAddBack.isEnabled = false
                    binding.ivBookAddAdd.isEnabled = false
                    binding.ivBookAddMainImage.isEnabled = false
                    binding.clBookAddProgressBar.visibility = View.VISIBLE
                }
                false -> {
                    binding.ivBookAddBack.isEnabled = true
                    binding.ivBookAddAdd.isEnabled = true
                    binding.ivBookAddMainImage.isEnabled = true
                    binding.clBookAddProgressBar.visibility = View.GONE
                }
            }
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }


}