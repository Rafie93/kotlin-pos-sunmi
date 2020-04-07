package com.zerone.zerone_pos.ui.account

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.zerone.zerone_pos.R
import com.zerone.zerone_pos.helpers.Const
import com.zerone.zerone_pos.helpers.SharedPrefControl
import com.zerone.zerone_pos.helpers.TransactionHelper
import kotlinx.android.synthetic.main.activity_edit_profile.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.indeterminateProgressDialog
import java.io.IOException
import java.util.HashMap

class EditProfileActivity : AppCompatActivity() {
    private val viewModel: AccountViewModel by lazy { ViewModelProvider(this).get(AccountViewModel::class.java) }
    private var bitmap: Bitmap? = null
    private val PICK_IMAGE_REQUEST = 1
    private val MY_CAMERA_REQUEST_CODE = 100
    var imgUploaded = false
    private val myRequestCode = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initView()
        imageProfile.setOnClickListener {
            pilihLoad()
        }

        btnUpdate.setOnClickListener(View.OnClickListener {
            val name = editTextFullname.text.toString().trim()
            val phone = editTextPhone.text.toString().trim()
            val email = editTextEmail.text.toString().trim()


            if(name.isEmpty()){
                editTextFullname.error = getString(R.string.name_hint)+" "+getString(R.string.required)
                editTextFullname.requestFocus()
                return@OnClickListener
            }
            if(phone.isEmpty()){
                editTextFullname.error = getString(R.string.phone_hint)+" "+getString(R.string.required)
                editTextFullname.requestFocus()
                return@OnClickListener
            }
            val dialog = indeterminateProgressDialog(message = getString(R.string.please_wait), title = "")
            dialog.show()
            if (imgUploaded){
                val map = HashMap<String, RequestBody>()
                map["phone"] = TransactionHelper.createPartFromString(phone)
                map["name"] = TransactionHelper.createPartFromString(name)

                val file = bitmap?.let { it1 -> TransactionHelper.createTempFile(this@EditProfileActivity,it1) }
                val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
                val body = MultipartBody.Part.createFormData("image", file!!.name, reqFile)
                viewModel.changeProfileWithImage(body, map)
                with(viewModel) {
                    showToast.observe(this@EditProfileActivity, Observer {
                        dialog.dismiss()
                        Toast.makeText(this@EditProfileActivity, "$it", Toast.LENGTH_LONG).show()
                    })

                    error.observe(this@EditProfileActivity, Observer {
                        Toast.makeText(this@EditProfileActivity, "$it", Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    })

                }
            }else {
                viewModel.changeProfile(name, phone)
                with(viewModel) {
                    showToast.observe(this@EditProfileActivity, Observer {
                        dialog.dismiss()
                        Toast.makeText(this@EditProfileActivity, "$it", Toast.LENGTH_LONG).show()
                    })

                    error.observe(this@EditProfileActivity, Observer {
                        Toast.makeText(this@EditProfileActivity, "$it", Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    })

                }
            }

        })
    }

    fun initView(){
        val name = SharedPrefControl.getInstance(applicationContext).getString(Const.USER_NAMA)
        val email = SharedPrefControl.getInstance(applicationContext).getString(Const.USER_EMAIL)
        val phone = SharedPrefControl.getInstance(applicationContext).getString(Const.USER_TELP)
        val image = SharedPrefControl.getInstance(applicationContext).getString(Const.USER_IMAGE)
        editTextFullname.setText(name)
        editTextEmail.setText(email)
        editTextPhone.setText(phone)
        Glide.with(applicationContext).load(image).into(imageProfile)
    }


    fun pilihLoad() {
        val pictureDialog = AlertDialog.Builder(this@EditProfileActivity)
        pictureDialog.setTitle(getString(R.string.source_image))
        val pictureDialogItems =
            arrayOf( getString(R.string.image_gallery), getString(R.string.image_camera))
        pictureDialog.setItems(pictureDialogItems,
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    0 -> choosePhotoFromGallary()
                    1 -> takePhotoFromCamera()
                }
            })
        pictureDialog.show()

    }

    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
    }

    private fun takePhotoFromCamera() {
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            setupPermissions()
        }else{
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, myRequestCode)

        }

    }

    fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage(getString(R.string.permission_access_camera))
                    .setTitle(getString(R.string.permission_required))
                builder.setPositiveButton("OK"
                ) { dialog, id ->
                    Log.i("CLICK", "Clicked")
                    makeRequest()
                }

                val dialog = builder.create()
                dialog.show()
            } else {
                makeRequest()
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == PICK_IMAGE_REQUEST) run {
            if (data != null) {
                val contentURI = data.data
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(
                        this@EditProfileActivity.getContentResolver(),
                        contentURI
                    )
                    imageProfile.setImageBitmap(bitmap)
                    imgUploaded = true
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@EditProfileActivity, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }else if (requestCode == myRequestCode){
            if (data != null) {
                try {
                    bitmap = data!!.getExtras()!!.get("data") as Bitmap?
                    imageProfile.setImageBitmap(bitmap)
                    imgUploaded = true
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@EditProfileActivity, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.CAMERA),
            MY_CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_CAMERA_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(applicationContext,"Izin camera telah ditolak", Toast.LENGTH_SHORT).show()
                } else {
                    takePhotoFromCamera()
                }
            }
        }
    }
}
