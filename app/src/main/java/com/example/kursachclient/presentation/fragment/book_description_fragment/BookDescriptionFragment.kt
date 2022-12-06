package com.example.kursachclient.presentation.fragment.book_description_fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.method.KeyListener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import coil.load
import com.bumptech.glide.Glide
import com.example.kursachclient.R
import com.example.kursachclient.SharedPreference
import com.example.kursachclient.databinding.FragmentBookDescriptionBinding
import com.example.kursachclient.domain.instance.RetrofitInstance
import com.example.kursachclient.domain.model.basket.AddBookToBasketRequest
import com.example.kursachclient.domain.model.book.GetBookResponse
import com.example.kursachclient.domain.model.book.UpdateBookRequest
import com.example.kursachclient.presentation.MainActivity
import com.example.kursachclient.presentation.fragment.BaseFragment
import java.math.RoundingMode

class BookDescriptionFragment : BaseFragment() {
    lateinit var binding: FragmentBookDescriptionBinding
    lateinit var viewModel: BookDescriptionViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        pref = SharedPreference(requireContext())
        binding = FragmentBookDescriptionBinding.inflate(inflater, container, false)
        viewModel = BookDescriptionViewModel()
        return binding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        progressBarIsDisplayed(true)

        val book = arguments?.getSerializable("book") as GetBookResponse

        viewModel.liveData.observe(viewLifecycleOwner) {
            try {
                progressBarIsDisplayed(false)
                Log.e("TAG", it.toString())
                binding.etBookDescriptionBookName.setText(it.name)
                binding.etBookDescriptionBookTitle.setText(it.title)
                if(it.image == null){
                    binding.imBookDescriptionMainImage.scaleType = ImageView.ScaleType.FIT_CENTER
                    Glide.with(view)
                        .load(R.drawable.no_photos)
                        .error(R.drawable.no_photos)
                        .into(binding.imBookDescriptionMainImage)
                }
                else{
                    binding.imBookDescriptionMainImage.scaleType = ImageView.ScaleType.FIT_XY
                    Glide.with(view)
                        .load(RetrofitInstance.URL + it.image?.url)
                        .error(R.drawable.no_photos)
                        .into(binding.imBookDescriptionMainImage)
                }

                binding.etBookDescriptionBookPrice.setText(it.price.toBigDecimal()
                    .setScale(2, RoundingMode.UP).toString())
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

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
        viewModel.liveDataNeedToNotifyGoneProgressBar.observe(viewLifecycleOwner) {
            try {
                progressBarIsDisplayed(false)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

        binding.btnBookDescriptionAddToBasket.setOnClickListener {
            try {
                progressBarIsDisplayed(true)
                val addBookToBasketRequest = AddBookToBasketRequest(book.id, 1u)
                viewModel.addOrRemoveFromBasket(addBookToBasketRequest, pref.getToken())
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

        binding.ivBookDescriptionUpdate.setOnClickListener {
            try {
                progressBarIsDisplayed(true)
                if (binding.etBookDescriptionBookName.text.isEmpty()) {
                    binding.etBookDescriptionBookName.error = "Не может быть пустым"
                    progressBarIsDisplayed(false)
                    return@setOnClickListener
                }
                if (binding.etBookDescriptionBookTitle.text.isEmpty()) {
                    binding.etBookDescriptionBookTitle.error = "Не может быть пустым"
                    progressBarIsDisplayed(false)
                    return@setOnClickListener
                }

                val priceRegex = "^\\d+.\\d{1,2}".toRegex()
                val resultPrice = priceRegex.matchEntire(binding.etBookDescriptionBookPrice.text.toString())
                if (resultPrice == null) {
                    binding.etBookDescriptionBookPrice.error = "Формат *.00"
                    progressBarIsDisplayed(false)
                    return@setOnClickListener
                }
                val resultPriceDecimal = resultPrice.value.toBigDecimal().setScale(2, RoundingMode.UP)
                if (resultPriceDecimal.toString() == "0.00") {
                    binding.etBookDescriptionBookPrice.error = "Минимальная цена 0.01"
                    progressBarIsDisplayed(false)
                    return@setOnClickListener
                }

                var updatedBook = UpdateBookRequest(
                    book.id,
                    binding.etBookDescriptionBookName.text.toString(),
                    binding.etBookDescriptionBookTitle.text.toString(),
                    resultPriceDecimal.toDouble(),
                    null)

                viewModel.updateBook(updatedBook, pref.getToken())
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }


        binding.ivBookDescriptionBack.setOnClickListener {
            try{
                findNavController().popBackStack()
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
        viewModel.getBookById(book.id, pref.getToken())

    }

    override fun onStart() {
        super.onStart()
        try{
            when(pref.getRole().lowercase()){
                "user" -> {
                    binding.ivBookDescriptionUpdate.visibility = View.GONE
                    binding.etBookDescriptionBookName.isFocusable = false
                    binding.etBookDescriptionBookTitle.isFocusable = false
                    binding.etBookDescriptionBookPrice.isFocusable = false
                }
                "admin" -> {
                    binding.ivBookDescriptionUpdate.visibility = View.VISIBLE
                    binding.etBookDescriptionBookName.isFocusable = true
                    binding.etBookDescriptionBookTitle.isFocusable = true
                    binding.etBookDescriptionBookPrice.isFocusable = true
                }
                else -> {
                    binding.ivBookDescriptionUpdate.visibility = View.VISIBLE
                    binding.etBookDescriptionBookName.isFocusable = true
                    binding.etBookDescriptionBookTitle.isFocusable = true
                    binding.etBookDescriptionBookPrice.isFocusable = true
                }
            }
        }
        catch (e: Exception){
            e.printStackTrace()
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
                    binding.imBookDescriptionMainImage.load(bitmap) {
                        crossfade(true)
                        crossfade(1000)
                    }
                    binding.imBookDescriptionMainImage.scaleType = ImageView.ScaleType.FIT_XY
                }

                GALLERY_REQUEST_CODE -> {
                    binding.imBookDescriptionMainImage.load(data?.data) {
                        Log.d("TAG", data?.data.toString())
                        crossfade(true)
                        crossfade(1000)
                    }
                    binding.imBookDescriptionMainImage.scaleType = ImageView.ScaleType.FIT_XY

                }
            }
        }
    }

    private fun progressBarIsDisplayed(isDisplayed: Boolean) {
        try {
            when (isDisplayed) {
                true -> {
                    binding.imBookDescriptionMainImage.isEnabled = false
                    binding.ivBookDescriptionUpdate.isEnabled = false
                    binding.clBookDescriptionProgressBar.visibility = View.VISIBLE
                }
                false -> {
                    binding.imBookDescriptionMainImage.isEnabled = true
                    binding.ivBookDescriptionUpdate.isEnabled = true
                    binding.clBookDescriptionProgressBar.visibility = View.GONE
                }
            }
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }
}