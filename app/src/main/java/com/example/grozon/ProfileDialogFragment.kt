import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.grozon.R
import de.hdodenhof.circleimageview.CircleImageView

class ProfileDialogFragment : Fragment() {

    private lateinit var profileImage: CircleImageView
    private lateinit var nameEditText: EditText
    private lateinit var bioEditText: EditText
    private lateinit var saveButton: Button

    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_dialog, container, false)

        profileImage = view.findViewById(R.id.profile_image)
        nameEditText = view.findViewById(R.id.edit_name)
        bioEditText = view.findViewById(R.id.edit_bio)
        saveButton = view.findViewById(R.id.save_button)

        // Load the current user details
        loadUserDetails()

        // Set up the profile image click listener to allow image change
        profileImage.setOnClickListener {
            openImagePicker()
        }

        saveButton.setOnClickListener {
            saveUserProfile()
        }

        return view
    }

    private fun loadUserDetails() {
        val sharedPref = requireActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE)
        val email = sharedPref.getString("current_user_email", null)

        email?.let {
            val name = sharedPref.getString("$email:name", "Default Name")
            val bio = sharedPref.getString("$email:bio", "Default Bio")
            val photoUri = sharedPref.getString("$email:profile_photo", null)

            nameEditText.setText(name)
            bioEditText.setText(bio)

            // Load profile photo using Glide or Picasso
            if (!photoUri.isNullOrEmpty()) {
                Glide.with(this)
                    .load(Uri.parse(photoUri))
                    .into(profileImage)
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                selectedImageUri = uri
                Glide.with(this)
                    .load(uri)
                    .into(profileImage)
            }
        }
    }

    private fun saveUserProfile() {
        val name = nameEditText.text.toString()
        val bio = bioEditText.text.toString()

        val sharedPref = requireActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE)
        val email = sharedPref.getString("current_user_email", null)

        email?.let {
            val editor = sharedPref.edit()
            editor.putString("$email:name", name)
            editor.putString("$email:bio", bio)

            // Save profile photo URI if it's updated
            selectedImageUri?.let { uri ->
                editor.putString("$email:profile_photo", uri.toString())
            }

            editor.apply()

            // Notify user that the profile is saved
            Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 1001
    }
}
