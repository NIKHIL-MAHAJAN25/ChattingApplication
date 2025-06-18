package com.nikhil.chattingapplication.navigation

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.nikhil.chattingapplication.MainActivity
import com.nikhil.chattingapplication.R
import com.nikhil.chattingapplication.databinding.FragmentProfileBinding
import com.nikhil.chattingapplication.supabase.Myapp
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.UploadStatus
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.uploadAsFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentProfileBinding
    private var auth:FirebaseAuth=FirebaseAuth.getInstance()
    val db= Firebase.firestore
    private val PICK_IMAGE_REQUEST = 1
    private val PERMISSION_REQUEST_CODE = 100
    private val MANAGE_EXTERNAL_STORAGE_REQUEST_CODE = 101
    private lateinit var supabaseClient: SupabaseClient
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentProfileBinding.inflate(layoutInflater)
        supabaseClient=(requireActivity().application as Myapp).supabaseClient
        fetchdata()

        binding.logout.setOnClickListener{
            auth.signOut()
            Toast.makeText(requireContext(), "Successfully logged out", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), MainActivity::class.java)

            startActivity(intent)
        }
        binding.profileImage2.setOnClickListener {
            checkPermission()
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun fetchdata(){
        val current=auth.currentUser
        if(current!=null)
        {
            db.collection("Users").document(current.uid).get().addOnSuccessListener { document->
                if (document != null && document.exists()) {
                    binding.userName.text=document.getString("name")
                    val profile=document.getString("profileimageurl")
                    binding.userEmail.text=current.email

                    Log.d("ProfileFragment", "Fetched profile URL: $profile")

                    // Load image with better error handling
                    if (!profile.isNullOrEmpty()) {
                        Glide.with(requireContext())
                            .load(profile)
                            .placeholder(R.drawable.baseline_home_24)
                            .error(R.drawable.baseline_home_24) // Add error placeholder
                            .into(binding.profileImage)
                    } else {
                        // Set default image if no profile URL exists
                        binding.profileImage.setImageResource(R.drawable.baseline_home_24)
                    }
                }
            }.addOnFailureListener { e ->
                Log.e("ProfileFragment", "Error fetching data: ${e.message}")
                Toast.makeText(requireContext(), "Error fetching data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    PERMISSION_REQUEST_CODE
                )
            } else {
                pickImage()
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            } else {
                pickImage()
            }
        }
    }

    private fun requestManage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // For Android 11 (API level 30) and above
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                startActivityForResult(intent, PERMISSION_REQUEST_CODE)
            } catch (e: ActivityNotFoundException) {
                // Handle the case where the intent is not available (maybe the device is on a lower version)
                Log.e("PermissionRequest", "Activity not found for the permission intent.")
            }
        } else {
            // Handle this case for older Android versions (below Android 11)
            Log.e("PermissionRequest", "The permission is only available on Android 11 (API level 30) and above.")
        }
//        // Request the user to open settings to allow full access
//        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
//        startActivityForResult(intent, MANAGE_EXTERNAL_STORAGE_REQUEST_CODE)
    }
    private fun pickImage(){
        val intent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_REQUEST)
    }

    private fun uploadImageToSupabase(uri: Uri) {
        val byteArray = uriToByteArray(requireContext(), uri)
        val fileName = "uploads/${System.currentTimeMillis()}.jpg"

        val bucket = supabaseClient.storage.from("sample") // Choose your bucket name

        // Use lifecycleScope for safe coroutine usage
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Upload image and handle the response
                bucket.uploadAsFlow(fileName, byteArray).collect { status ->
                    withContext(Dispatchers.Main) {
                        when (status) {
                            is UploadStatus.Progress -> {
//                                val progress = (status.totalBytesSent.toFloat() / status.contentLength * 100)
                                Log.d("Upload", "Progress%")
                            }
                            is UploadStatus.Success -> {
                                Log.d("Upload ", "Upload Success")
                                handleUploadSuccess(bucket, fileName)






                                Toast.makeText(requireContext(), "Upload Successful!", Toast.LENGTH_SHORT).show()
                            }

                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("Upload", "Error uploading image: ${e.message}")
                    Toast.makeText(requireContext(), "Error uploading image: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun uriToByteArray(context: Context, uri: Uri): ByteArray {
        val inputStream = context.contentResolver.openInputStream(uri)
        return inputStream?.readBytes() ?: ByteArray(0)
    }
    @RequiresApi(Build.VERSION_CODES.R)


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with file operations
                    Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT).show()
                    pickImage()
                } else {
                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            MANAGE_EXTERNAL_STORAGE_REQUEST_CODE -> {
                if (Environment.isExternalStorageManager()) {
                    // The user granted permission for full access
                    Toast.makeText(requireContext(), "Full storage access granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Storage permission not granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            data?.data?.let { uri ->
                // Handle the selected image (upload it to Supabase)
                uploadImageToSupabase(uri)
            }
        }
    }
    private fun handleUploadSuccess(bucket: Any, fileName: String) {
        try {
            val imageUrl = supabaseClient.storage.from("sample").publicUrl(fileName)
            Log.d("ProfileFragment", "Generated public URL: $imageUrl")

            val currentUser = auth.currentUser
            if (currentUser != null) {
                Log.d("ProfileFragment", "Updating Firestore with new image URL")
                db.collection("Users")
                    .document(currentUser.uid)
                    .update("profileimageurl", imageUrl)
                    .addOnSuccessListener {
                        Log.d("ProfileFragment", "Firestore update successful")
                        Toast.makeText(requireContext(), "Profile image updated!", Toast.LENGTH_SHORT).show()
                        fetchdata()}
                    .addOnFailureListener { e ->
                        Log.e("FirestoreUpdate", "Failed to update profile image URL: ${e.message}", e)
                        Toast.makeText(requireContext(), "Failed to update profile image: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Log.e("ProfileFragment", "Current user is null")
                Toast.makeText(requireContext(), "User authentication error", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("ProfileFragment", "Error in handleUploadSuccess: ${e.message}", e)
            Toast.makeText(requireContext(), "Error processing upload success: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}