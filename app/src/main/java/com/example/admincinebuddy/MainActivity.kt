package com.example.admincinebuddy

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.admincinebuddy.Model.VideoUploadDetails
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import org.apache.commons.io.FilenameUtils
import org.jetbrains.annotations.Nullable
import android.database.Cursor


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private var videoUri: Uri? = null
    private lateinit var text_video_selected: TextView
    private lateinit var videoCategory: String
    private lateinit var videootitle: String
    private lateinit var currentuid: String
    private lateinit var mstorageRef: StorageReference
    private var mUploadsTask: StorageTask<UploadTask.TaskSnapshot>? = null
    private lateinit var referenceVideos: DatabaseReference
    private lateinit var video_description: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        text_video_selected = findViewById(R.id.textvideoselected)
        video_description = findViewById(R.id.movies_description)
        referenceVideos = FirebaseDatabase.getInstance().getReference().child("videos")
        mstorageRef = FirebaseStorage.getInstance().getReference().child("videos")
        val spinner: Spinner = findViewById(R.id.spinner)
        spinner.onItemSelectedListener = this
        val categories: MutableList<String> = ArrayList()
        categories.add("Action")
        categories.add("Adventure")
        categories.add("Sports")
        categories.add("Romantic")
        categories.add("Comedy")
        val dataAdpter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        dataAdpter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = dataAdpter
    }

    override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
        videoCategory = adapterView.getItemAtPosition(i).toString()
        Toast.makeText(this, "selected: $videoCategory", Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(adapterView: AdapterView<*>) {}

    fun openvideoFiles(view: View) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "video/*"
        startActivityForResult(intent, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_OK && data?.data != null) {
            videoUri = data.data
            var path: String? = null
            val cursor: Cursor?
            val coloum_index_data: Int
            val projection = arrayOf(
                MediaStore.MediaColumns.DATA,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Thumbnails.DATA
            )
            val orderby = MediaStore.Video.Media.DEFAULT_SORT_ORDER
            cursor = contentResolver.query(videoUri!!, projection, null, null, orderby)
            coloum_index_data = cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            while (cursor.moveToNext()) {
                path = cursor.getString(coloum_index_data)
                videootitle = FilenameUtils.getBaseName(path)
            }
            text_video_selected.text = videootitle
        }
    }

    fun uploadFileToFirebase(v: View) {
        if (text_video_selected.text == "no video selected") {
            Toast.makeText(this, "please selected an video!", Toast.LENGTH_SHORT).show()
        } else {
            if (mUploadsTask != null && mUploadsTask!!.isInProgress) {
                Toast.makeText(this, "video uploads is all ready in progress.. ", Toast.LENGTH_SHORT).show()
            } else {
                uploadFiles()
            }
        }
    }

    private fun uploadFiles() {
        if (videoUri != null) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("video uploading...")
            progressDialog.show()
            val storageReference = mstorageRef.child(videootitle)
            mUploadsTask = storageReference.putFile(videoUri!!)
                .addOnSuccessListener { takeSnapshot ->
                    storageReference.downloadUrl.addOnSuccessListener { uri ->
                        val video_url = uri.toString()
                        val videoUploadDetails = VideoUploadDetails(
                            "", "", "",
                            video_url, videootitle, video_description.text.toString(), videoCategory
                        )
                        val uploadsid = referenceVideos.push().key
                        referenceVideos.child(uploadsid!!).setValue(videoUploadDetails)
                        currentuid = uploadsid
                        progressDialog.dismiss()
                        if (currentuid == uploadsid) {
                            startThumbnailsActivity()
                        }
                    }
                }
                .addOnProgressListener { taskSnapshot ->
                    val progress =
                        100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                    progressDialog.setMessage("uploaded ${progress.toInt()}%... ")
                }
        } else {
            Toast.makeText(this, "no video selected to upload", Toast.LENGTH_SHORT).show()
        }
    }

    fun startThumbnailsActivity() {
        val intent = Intent(this, UploadThumbnailActivity::class.java)
        intent.putExtra("currentuid", currentuid)
        intent.putExtra("thumbnailsName", videootitle)
        startActivity(intent)
        Toast.makeText(this, "video uploaded successfully upload video thumbnail!", Toast.LENGTH_LONG).show()
    }
}


