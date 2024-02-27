package com.example.admincinebuddy

import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import org.jetbrains.annotations.Nullable
import java.io.IOException

class UploadThumbnailActivity : AppCompatActivity() {
    private var videothumburi: Uri? = null
    private var thumbnail_url: String? = null
    private lateinit var thumnail_image: ImageView
    private lateinit var mStoragerefthumnails: StorageReference
    private lateinit var referenceVideos: DatabaseReference
    private lateinit var textSelected: TextView
    private lateinit var radioButtonlatest: RadioButton
    private lateinit var radioButtonpopular: RadioButton
    private lateinit var radioButtonNotype: RadioButton
    private lateinit var radioButtonSlide: RadioButton
    private var mStorageTask: StorageTask<UploadTask.TaskSnapshot>? = null
    private lateinit var updatedataref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_thumbnail)
        textSelected = findViewById(R.id.textNothumbnailselected)
        thumnail_image = findViewById(R.id.imageview)
        radioButtonlatest = findViewById(R.id.radiolatestMovies)
        radioButtonpopular = findViewById(R.id.radiobestpopularMovies)
        radioButtonNotype = findViewById(R.id.radioNotype)
        radioButtonSlide = findViewById(R.id.radioSlideMovies)
        mStoragerefthumnails = FirebaseStorage.getInstance().getReference().child("VideoThumbnails")
        referenceVideos = FirebaseDatabase.getInstance().getReference().child("videos")
        val currentUid = intent.extras?.getString("currentuid")
        updatedataref = FirebaseDatabase.getInstance().getReference("videos").child(currentUid!!)

        radioButtonNotype.setOnClickListener {
            updatedataref.child("video_type").setValue("")
            updatedataref.child("video_slide").setValue("")
            Toast.makeText(this@UploadThumbnailActivity, "selected: no type", Toast.LENGTH_SHORT).show()
        }

        radioButtonlatest.setOnClickListener {
            val latestMovies = radioButtonlatest.text.toString()
            updatedataref.child("video_type").setValue(latestMovies)
            updatedataref.child("video_slide").setValue("")
            Toast.makeText(this@UploadThumbnailActivity, "selected: $latestMovies", Toast.LENGTH_SHORT).show()
        }

        radioButtonpopular.setOnClickListener {
            val popularMovies = radioButtonpopular.text.toString()
            updatedataref.child("video_type").setValue(popularMovies)
            updatedataref.child("video_slide").setValue("")
            Toast.makeText(this@UploadThumbnailActivity, "selected: $popularMovies", Toast.LENGTH_SHORT).show()
        }

        radioButtonSlide.setOnClickListener {
            val slideMovies = radioButtonSlide.text.toString()
            updatedataref.child("video_slide").setValue(slideMovies)
            Toast.makeText(this@UploadThumbnailActivity, "selected: $slideMovies", Toast.LENGTH_SHORT).show()
        }
    }

    fun showimagechooser(view: View) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 102)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 102 && resultCode == RESULT_OK && data?.data != null) {
            videothumburi = data.data
            try {
                val thumbname = getFileName(videothumburi!!)
                textSelected.text = thumbname
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, videothumburi)
                thumnail_image.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                   // result = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)

                    if (displayNameIndex != -1) {
                        result = it.getString(displayNameIndex)
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1) {
                result = result?.substring(cut!! + 1)
            }
        }
        return result
    }

    private fun uploadFiles() {
        if (videothumburi != null) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("wait uploading thumbnail...")
            progressDialog.show()
            val video_title = intent.extras?.getString("thumbnailsName")
            val sRef = mStoragerefthumnails.child("$video_title.${getfileExtension(videothumburi!!)}")
            sRef.putFile(videothumburi!!)
                .addOnSuccessListener { taskSnapshot ->
                    sRef.downloadUrl.addOnSuccessListener { uri ->
                        thumbnail_url = uri.toString()
                        updatedataref.child("video_thumb").setValue(thumbnail_url)
                        progressDialog.dismiss()
                        Toast.makeText(this@UploadThumbnailActivity, "files uploaded", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this@UploadThumbnailActivity, e.message, Toast.LENGTH_SHORT).show()
                }
                .addOnProgressListener { taskSnapshot ->
                    val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                    progressDialog.setMessage("uploaded ${(progress).toInt()}%...")
                }
        }
    }

    fun uploadfiletofirebase(view: View) {
        if (textSelected.text == "No Thumbnail Selected") {
            Toast.makeText(this@UploadThumbnailActivity, "First select an image ", Toast.LENGTH_SHORT).show()
        } else {
            if (mStorageTask != null && mStorageTask!!.isInProgress) {
                Toast.makeText(this@UploadThumbnailActivity, "upload files allready in progress", Toast.LENGTH_SHORT).show()
            } else {
                uploadFiles()
            }
        }
    }

    private fun getfileExtension(uri: Uri): String? {
        val cr: ContentResolver = contentResolver
        val mimTypeMap: MimeTypeMap = MimeTypeMap.getSingleton()
        return mimTypeMap.getExtensionFromMimeType(cr.getType(uri))
    }
}


