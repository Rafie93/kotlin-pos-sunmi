package com.zerone.zerone_pos.ui.produk
/**
name : rafie
website : wwww.zerone-bjm.com
dibuat : april 2020
 */
import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.zerone.zerone_pos.R
import com.zerone.zerone_pos.helpers.TransactionHelper
import com.zerone.zerone_pos.helpers.TransactionHelper.Companion.createPartFromString
import com.zerone.zerone_pos.repository.ProdukRepository
import com.zerone.zerone_pos.ui.main.CaptureActivityPortrait
import kotlinx.android.synthetic.main.activity_new_produk.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.indeterminateProgressDialog
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.HashMap

class NewProdukActivity : AppCompatActivity() {
    var isStock = 0
    private var bitmap: Bitmap? = null
    private val PICK_IMAGE_REQUEST = 1
    private val MY_CAMERA_REQUEST_CODE = 100
    var imgUploaded = false
    private val myRequestCode = 20
    private lateinit var vm: ProdukViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_produk)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        imageProduk.setOnClickListener {
            pilihLoad()
        }

        scanCode.setOnClickListener(View.OnClickListener {
            run {
                IntentIntegrator(this@NewProdukActivity)
                    .setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES)
                    .setOrientationLocked(true)
                    .setBeepEnabled(true)
                    .setBarcodeImageEnabled(true)
                    .setCaptureActivity(CaptureActivityPortrait::class.java)
                    .initiateScan()
            }
        })

        cbStock.setOnClickListener(View.OnClickListener {
            if (cbStock.isChecked){
                isStock = 1
                editTextStock.isEnabled = true
                editTextStock.setText("")
            }else{
                isStock = 0
                editTextStock.setText("∞")
                editTextStock.isEnabled = false
            }
        })

        buttonSave.setOnClickListener(View.OnClickListener { 
            val code = editTextCode.text.toString()
            val name = editTextName.text.toString()
            val basic_price = editTextHargaDasar.text.toString()
            val selling_price = editTextHargaJual.text.toString()
            val category = editTextKategori.text.toString()
            val stock = editTextStock.text.toString()
            val satuan = editTextSatuan.text.toString()
            val description = editTextDescription.text.toString()
            if (code.isEmpty()){
                editTextCode.error = "Code "+getString(R.string.required)
                editTextCode.requestFocus()
                return@OnClickListener
            }
            if (name.isEmpty()){
                editTextName.error =  getString(R.string.product_name)+" "+getString(R.string.required)
                editTextName.requestFocus()
                return@OnClickListener
            }
            if (basic_price.isEmpty()){
                editTextHargaDasar.error =  getString(R.string.product_basic_price)+" "+getString(R.string.required)
                editTextHargaDasar.requestFocus()
                return@OnClickListener
            }
            if (selling_price.isEmpty()){
                editTextHargaJual.error =  getString(R.string.product_selling_price)+" "+getString(R.string.required)
                editTextHargaJual.requestFocus()
                return@OnClickListener
            }
            if (isStock==1){
                if (stock.isEmpty()){
                    editTextStock.error = "Stok "+getString(R.string.required)
                    editTextStock.requestFocus()
                    return@OnClickListener
                }
            }
            val dialog = indeterminateProgressDialog(message = getString(R.string.please_wait), title = "")
            dialog.show()
            if (imgUploaded){
                val map = HashMap<String, RequestBody>()
                map["code"] = createPartFromString(code)
                map["name"] = createPartFromString(name)
                map["selling_price"] = createPartFromString(selling_price)
                map["unit"] = createPartFromString(satuan)
                map["category"] = createPartFromString(category)
                map["basic_price"] = createPartFromString(basic_price)
                map["description"] = createPartFromString(description)
                map["is_stock"] = createPartFromString(isStock.toString())
                map["stock"] = createPartFromString(stock)
                val file = bitmap?.let { it1 -> TransactionHelper.createTempFile(this@NewProdukActivity,it1) }
                val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
                val body = MultipartBody.Part.createFormData("image", file!!.name, reqFile)
                val factory = ProdukViewModelFactory(ProdukRepository.instance)
                vm = ViewModelProvider(this,factory).get(ProdukViewModel::class.java).apply {
                    viewState.observe(
                        this@NewProdukActivity,
                        Observer(this@NewProdukActivity::handleState)
                    )
                    storeProdukWithImage(body,map)
                    dialog.dismiss()
                }

            }else{
                val factory = ProdukViewModelFactory(ProdukRepository.instance)
                vm = ViewModelProvider(this,factory).get(ProdukViewModel::class.java).apply {
                    viewState.observe(
                        this@NewProdukActivity,
                        Observer(this@NewProdukActivity::handleState)
                    )
                    storeProduk(code,name,basic_price,selling_price,isStock,satuan,category,stock,description)
                    dialog.dismiss()
                }
            }

        })
    }

    private fun handleState(viewState: ProdukViewState?) {
        viewState?.let {
            it.error?.let { error -> showError(error) }
            it.postToServer?.let{postToServer -> showHasil(postToServer)}
        }
    }

    fun showHasil(postToServer: Boolean) {
        if (postToServer){
            Toast.makeText(applicationContext,getString(R.string.product_added),Toast.LENGTH_SHORT).show()
            resetInput()
        }
    }

    fun resetInput(){
        editTextCode.setText("")
        editTextName.setText("")
        editTextKategori.setText("")
        editTextDescription.setText("")
        editTextHargaDasar.setText("")
        editTextHargaJual.setText("")
        editTextStock.setText("")
        editTextSatuan.setText("")
    }

    private fun showError(error: Exception) {
        error.printStackTrace()
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

    fun pilihLoad() {
        val pictureDialog = AlertDialog.Builder(this@NewProdukActivity)
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
        var result: IntentResult? =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                val scannedResult = result.contents
                editTextCode.setText(scannedResult)
            } else {
                Toast.makeText(this@NewProdukActivity, "Failed Scan", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

        if (requestCode == PICK_IMAGE_REQUEST) run {
            if (data != null) {
                val contentURI = data.data
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(
                        this@NewProdukActivity.getContentResolver(),
                        contentURI
                    )
                    imageProduk.setImageBitmap(bitmap)
                    imgUploaded = true
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@NewProdukActivity, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }else if (requestCode == myRequestCode){
            if (data != null) {
                try {
                    bitmap = data!!.getExtras()!!.get("data") as Bitmap?
                    imageProduk.setImageBitmap(bitmap)
                    imgUploaded = true
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@NewProdukActivity, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
