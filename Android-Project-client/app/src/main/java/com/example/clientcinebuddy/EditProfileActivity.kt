package com.example.clientcinebuddy

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class EditProfileActivity : AppCompatActivity() {

    private lateinit var displayNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        displayNameEditText = findViewById(R.id.editTextDisplayName)
        emailEditText = findViewById(R.id.editTextEmail)
        saveButton = findViewById(R.id.buttonSave)
        auth = FirebaseAuth.getInstance()

        // Pre-fill the display name and email fields with the current user's information
        val currentUser = auth.currentUser
        displayNameEditText.setText(currentUser?.displayName)
        emailEditText.setText(currentUser?.email)

        saveButton.setOnClickListener { saveProfile() }
    }

    private fun saveProfile() {
        val user = auth.currentUser
        val newDisplayName = displayNameEditText.text.toString().trim()
        val newEmail = emailEditText.text.toString().trim()

        if (newDisplayName.isEmpty() || newEmail.isEmpty()) {
            Toast.makeText(this, "Please enter display name and email", Toast.LENGTH_SHORT).show()
            return
        }

        // Update display name
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(newDisplayName)
            .build()

        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Update the user's email
                    user.updateEmail(newEmail)
                        .addOnCompleteListener { emailUpdateTask ->
                            if (emailUpdateTask.isSuccessful) {
                                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Failed to update email", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Failed to update display name", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
