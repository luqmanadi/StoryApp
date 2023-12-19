package com.ndiman.storyapp.ui.addstory

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ndiman.storyapp.R
import com.ndiman.storyapp.data.Result
import com.ndiman.storyapp.databinding.ActivityAddStoryBinding
import com.ndiman.storyapp.ui.ViewModelFactory
import com.ndiman.storyapp.ui.home.MainActivity
import com.ndiman.storyapp.utils.getImageUri
import com.ndiman.storyapp.utils.reduceFileImage
import com.ndiman.storyapp.utils.uriToFile

class AddStoryActivity : AppCompatActivity() {

    private val viewModel by viewModels<AddStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationRequest: LocationRequest

    private var binding: ActivityAddStoryBinding? = null

    private var currentImageUri: Uri? = null

    private var lat: Double? = null
    private var lon: Double? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLocation()
                }
                else -> {
                    // No location access granted.
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setUpAction()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun getMyLocation() {
        if     (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    lat = location.latitude
                    lon = location.longitude
                    Log.d("AddStory", "Cek Lat: $lat, Cek Lon: $lon")

                } else {
                    Toast.makeText(
                        this@AddStoryActivity,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun startGalery(){
        launcherGalery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCamera(){
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }


    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ){isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private val launcherGalery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ){ uri: Uri? ->
        if (uri != null){
            currentImageUri = uri
            showImage()
        } else{
            Log.d("Photo Picker", "No Media Selected")
        }

    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding?.previewImageView?.setImageURI(it)
        }
    }

    private fun uploadImage(){
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "ShowImage: ${imageFile.path}")
            val description = binding?.descriptionEditText?.text.toString()

            var latitude: Double? = null
            var longitude: Double? = null

            if (binding?.checkBoxGps!!.isChecked){
                latitude = lat
                longitude = lon
            }

            if (description.isEmpty()){
                showErrorDataDescriptionIsEmpty()
            } else{
                Log.d("AddStory", "Cek Latitude: $latitude, Cek Longitude: $longitude")
                viewModel.createStory(imageFile, description, latitude, longitude).observe(this){result ->
                    result?.let {
                        when(it){
                            is Result.Loading -> {
                                showLoading(true)
                            }
                            is Result.Success -> {
                                val message = it.data.message.toString()
                                showToast(message)
                                showLoading(false)
                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                            is Result.Error -> {
                                val error = it.error.toString()
                                showToast(error)
                                showLoading(false)
                            }
                        }
                    }
                }
            }
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun createLocationRequest(){
        locationRequest = LocationRequest.create()
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        client.checkLocationSettings(builder.build())
            .addOnSuccessListener {
                getMyLocation()
            }
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    try {
                        resolutionLauncher.launch(
                            IntentSenderRequest.Builder(exception.resolution).build()
                        )
                    } catch (sendEx: IntentSender.SendIntentException) {
                        Toast.makeText(this, sendEx.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private val resolutionLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            when (result.resultCode) {
                RESULT_OK ->
                    Log.i(TAG, "onActivityResult: All location settings are satisfied.")
                RESULT_CANCELED ->
                    Toast.makeText(
                        this,
                        "Anda harus mengaktifkan GPS untuk menggunakan aplikasi ini!",
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }

    private fun showErrorDataDescriptionIsEmpty(){
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.failed_upload))
            .setMessage(resources.getString(R.string.description_empty))
            .setPositiveButton(resources.getString(R.string.ok)){_, _ ->
            }
            .create()
            .show()
    }

    private fun setUpAction(){
        binding?.topAppBar?.setNavigationOnClickListener {
            @Suppress("DEPRECATION")
            onBackPressed()
        }

        binding?.btnGalery?.setOnClickListener {
            startGalery()
        }

        binding?.btnCamera?.setOnClickListener {
            startCamera()
        }

        binding?.checkBoxGps?.setOnCheckedChangeListener { compoundButton, _ ->
            if (compoundButton.isChecked){
                createLocationRequest()
            }
        }

        binding?.btnUpload?.setOnClickListener {
            uploadImage()
        }

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(state: Boolean) { binding?.progressIndicator?.visibility = if (state) View.VISIBLE else View.GONE }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        private const val TAG = "AddStoriesActivity"
    }
}