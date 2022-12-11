package com.example.kursachclient.presentation.fragment.book_add_fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.kursachclient.R
import com.example.kursachclient.SharedPreference
import com.example.kursachclient.databinding.FragmentBookAddBinding
import com.example.kursachclient.domain.model.book.AddBookRequest
import com.example.kursachclient.presentation.MainActivity
import com.example.kursachclient.presentation.fragment.BaseFragment
import java.io.ByteArrayOutputStream
import java.math.RoundingMode


class BookAddFragment : BaseFragment() {
    lateinit var binding: FragmentBookAddBinding
    lateinit var viewModel: BookAddViewModel

    private var bitmapMainImage: Bitmap? = null

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

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

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
        viewModel.liveData.observe(viewLifecycleOwner) {
            try {
                progressBarIsDisplayed(false)
                findNavController().popBackStack()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        viewModel.liveDataNeedToNotifyGoneProgressBar.observe(viewLifecycleOwner) {
            progressBarIsDisplayed(false)
        }

        binding.ivBookAddBack.setOnClickListener {
            try {
                findNavController().popBackStack()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        binding.ivBookAddAdd.setOnClickListener {
            try {
                var status = true
                progressBarIsDisplayed(true)
                if (binding.etBookAddBookName.text.isEmpty()) {
                    binding.etBookAddBookName.error = "Не может быть пустым"
                    progressBarIsDisplayed(false)
                    status = false
                }
                if (binding.etBookAddBookTitle.text.isEmpty()) {
                    binding.etBookAddBookTitle.error = "Не может быть пустым"
                    progressBarIsDisplayed(false)
                    status = false
                }

                val priceRegex = "^\\d+.\\d{1,2}".toRegex()
                val resultPrice = priceRegex.matchEntire(binding.etBookAddBookPrice.text.toString())
                if (resultPrice == null) {
                    binding.etBookAddBookPrice.error = "Формат *.00"
                    progressBarIsDisplayed(false)
                    status = false
                }

                val resultPriceDecimal =
                    resultPrice?.value?.toBigDecimal()?.setScale(2, RoundingMode.UP)
                if (resultPrice != null && resultPriceDecimal.toString() == "0.00") {
                    binding.etBookAddBookPrice.error = "Минимальная цена 0.01"
                    progressBarIsDisplayed(false)
                    status = false
                }
                if (status && resultPriceDecimal != null) {

                    var resStr: String? = null

                    if (bitmapMainImage != null) {
                        try {
                            val baos = ByteArrayOutputStream()
                            bitmapMainImage!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                            val b = baos.toByteArray()
                            resStr = Base64.encodeToString(b, Base64.DEFAULT)
                        } catch (e: Exception) {
                            showToast("Проблема с изображением")
                            return@setOnClickListener
                        }
                    }
                    val book =
                        AddBookRequest(
                            binding.etBookAddBookName.text.toString(),
                            binding.etBookAddBookTitle.text.toString(),
                            resultPriceDecimal.toDouble(),
                            resStr
                        )
                    viewModel.addBook(book, pref.getToken())
                }


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.ivBookAddMainImage.setOnClickListener {

            val pictureDialog = context?.let { context -> AlertDialog.Builder(context) }
            pictureDialog?.setTitle("Что сделать")
            val pictureDialogItem = arrayOf(
                "Выбрать фото из галереи",
                "Сделать фото",
                "Очистить"
            )
            pictureDialog?.setItems(pictureDialogItem) { dialog, which ->

                when (which) {
                    0 -> galleryCheckPermission()
                    1 -> cameraCheckPermission()
                    2 -> clearMainImage()
                }
            }

            pictureDialog?.show()
        }
    }

    private fun clearMainImage() {
        binding.ivBookAddMainImage.scaleType = ImageView.ScaleType.FIT_CENTER
        Glide.with(this)
            .load(R.drawable.no_photos)
            .error(R.drawable.no_photos)
            .into(binding.ivBookAddMainImage)
        bitmapMainImage = null
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
                    "У вас нет разрешений!!!"
                )

                .setPositiveButton("Настройки") { _, _ ->

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
                .setNegativeButton("Отмена") { dialog, _ ->
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
                    binding.ivBookAddMainImage.scaleType = ImageView.ScaleType.FIT_XY
                    Glide.with(this).load(bitmap)
                        .error(R.drawable.no_photos)
                        .into(binding.ivBookAddMainImage)
                    bitmapMainImage = bitmap
                }

                GALLERY_REQUEST_CODE -> {
                    binding.ivBookAddMainImage.scaleType = ImageView.ScaleType.FIT_XY

                    Glide.with(this).load(data?.data)
                        .error(R.drawable.no_photos)
                        .into(binding.ivBookAddMainImage)
                    bitmapMainImage = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        data?.data
                    )
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}