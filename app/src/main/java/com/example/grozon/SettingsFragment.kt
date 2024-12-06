//package com.example.grozon
//
//import android.content.Context
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.EditText
//import android.widget.ImageView
//import androidx.fragment.app.Fragment
//import com.bumptech.glide.Glide
//
//class SettingsFragment : Fragment() {
//
//    private lateinit var nameEditText: EditText
//    private lateinit var bioEditText: EditText
//    private lateinit var saveButton: Button
//    private lateinit var profileImageView: ImageView
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_settings2, container, false)
//
//        nameEditText = view.findViewById(R.id.edit_name)
//        bioEditText = view.findViewById(R.id.edit_bio)
//        saveButton = view.findViewById(R.id.save_button)
//        profileImageView = view.findViewById(R.id.profile_image)
//
//        // Fill existing data
//        val sharedPref = requireActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE)
//        val email = sharedPref.getString("current_user_email", null) ?: ""
//        nameEditText.setText(sharedPref.getString("$email:name", "User Name"))
//        bioEditText.setText(sharedPref.getString("$email:D.O.B", "User Bio"))
//
//        // Load profile image using Glide
//        val profileImageUrl = sharedPref.getString("$email:profile_image_url", "")
//        if (!profileImageUrl.isNullOrEmpty()) {
//            Glide.with(this).load(profileImageUrl).into(profileImageView)
//        }
//
//        saveButton.setOnClickListener {
//            val newName = nameEditText.text.toString()
//            val newBio = bioEditText.text.toString()
//
//            // Optionally, get a new profile image from ImagePicker or Camera
//            val newImageUrl = "https://example.com/new-image.jpg" // Replace with actual URL
//
//            // Save the new profile data
//            (activity as Action).handleProfileUpdate(newImageUrl, newName, newBio)
//        }
//
//        return view
//    }
//}
