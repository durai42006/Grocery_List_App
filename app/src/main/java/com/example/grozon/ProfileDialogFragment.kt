package com.example.grozon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class ProfileDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profileImage: CircleImageView = view.findViewById(R.id.profile_image)
        val editName: EditText = view.findViewById(R.id.edit_name)
        val editBio: EditText = view.findViewById(R.id.edit_bio)
        val saveButton: Button = view.findViewById(R.id.save_button)

        // Set existing user details
        Glide.with(requireContext())
            .load("R.drawable.green") // Replace with the actual URL or resource
            .into(profileImage)
        editName.setText("User Name")
        editBio.setText("User Bio")

        saveButton.setOnClickListener {
            // Save changes (implement save logic)
            val name = editName.text.toString()
            val bio = editBio.text.toString()
            // Implement saving the updated name and bio
            dismiss()
        }
    }
}
