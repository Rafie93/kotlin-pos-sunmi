package com.zerone.zerone_pos.ui.produk

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.zerone.zerone_pos.R
import com.zerone.zerone_pos.helpers.TransactionHelper
import com.zerone.zerone_pos.repository.ProdukRepository
import kotlinx.android.synthetic.main.activity_edit_produk.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.indeterminateProgressDialog
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.HashMap

class EditProdukActivity : AppCompatActivity() {
    var id=""
    var isStock=0
    private var bitmap: Bitmap? = null
    private val PICK_IMAGE_REQUEST = 1
    var imgUploaded = false
    private val myRequestCode = 20
    private lateinit var vm: ProdukViewModel
    val factory = ProdukViewModelFactory(ProdukRepository.instance)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_produk)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initView()
        imageProduk.setOnClickListener {
            pilihLoad()
        }

        cbStock.setOnClickListener(View.OnClickListener {
            if (cbStock.isChecked){
                isStock = 1
                editTextStock.isEnabled = true
            }else{
                isStock = 0
                editTextStock.isEnabled = false
            }
        })

    }
    fun initView(){
        var bundle :Bundle ?=intent.extras
        id = bundle!!.getString("id")!!
        val name = bundle!!.getString("name")
        val code = bundle!!.getString("code")!!
        val basic_price = bundle!!.getString("basic_price")
        val selling_price = bundle!!.getString("selling_price")
        isStock = bundle!!.getInt("is_stock")!!
        val stock = bundle!!.getString("stock")!!
        val description = bundle!!.getString("description").toString()
        val satuan = bundle!!.getString("satuan").toString()
        val image = bundle!!.getString("image").toString()
        val category = bundle!!.getString("category").toString()

        editTextCode.setText(code)
        editTextName.setText(name)
        editTextKategori.setText(category)
        editTextDescription.setText(description)
        editTextHargaDasar.setText(basic_price)
        editTextHargaJual.setText(selling_price)
        editTextStock.setText(stock)
        editTextSatuan.setText(satuan)

        Glide.with(applicationContext)
            .load(image)
            .into(imageProduk)

        if (isStock==1){
            editTextStock.isEnabled = true
            cbStock.isChecked = true
        }
        //SIMPAN
        buttonUpdate.setOnClickListener(View.OnClickListener {
            val code = editTextCode.text.toString()
            val name = editTextName.text.toString()
            val basic_price = editTextHargaDasar.text.toString()
            val selling_price = editTextHargaJual.text.toString()
            val category = editTextKategori.text.toString()
            val stock = editTextStock.text.toString()
            val satuan = editTextSatuan.text.toString()
            val description = editTextDescription.text.toString()

            if (imgUploaded){
                val map = HashMap<String, RequestBody>()
                map["code"] = TransactionHelper.createPartFromString(code)
                map["name"] = TransactionHelper.createPartFromString(name)
                map["selling_price"] = TransactionHelper.createPartFromString(selling_price)
                map["unit"] = TransactionHelper.createPartFromString(satuan)
                map["category"] = TransactionHelper.createPartFromString(category)
                map["basic_price"] = TransactionHelper.createPartFromString(basic_price)
                map["description"] = TransactionHelper.createPartFromString(description)
                map["is_stock"] = TransactionHelper.createPartFromString(isStock.toString())
                map["stock"] = TransactionHelper.createPartFromString(stock)
                val file = bitmap?.let { it1 -> TransactionHelper.createTempFile(this@EditProdukActivity,it1) }
                val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
                val body = MultipartBody.Part.createFormData("image", file!!.name, reqFile)
                val factory = ProdukViewModelFactory(ProdukRepository.instance)
                vm = ViewModelProvider(this,factory).get(ProdukViewModel::class.java).apply {
                    viewState.observe(
                        this@EditProdukActivity,
                        Observer(this@EditProdukActivity::handleState)
                    )
                    updateProdukWithImage(id,body,map)
                }

            }else {
                vm = ViewModelProvider(this, factory).get(ProdukViewModel::class.java).apply {
                    viewState.observe(
                        this@EditProdukActivity,
                        Observer(this@EditProdukActivity::handleState)
                    )
                    updateProduk(
                        code,
                        name,
                        basic_price,
                        selling_price,
                        isStock,
                        satuan,
                        category,
                        stock,
                        description,
                        id
                    )
                }
            }
        })

        //HAPUS
        buttonDelete.setOnClickListener(View.OnClickListener {
            vm = ViewModelProvider(this,factory).get(ProdukViewModel::class.java).apply {
                viewState.observe(
                    this@EditProdukActivity,
                    Observer(this@EditProdukActivity::handleDelete)
                )
                deleteProduk(id)
            }
        })
    }

    private fun handleState(viewState: ProdukViewState?) {
        viewState?.let {
            it.error?.let { error -> showError(error) }
            it.postToServer?.let{postToServer -> showHasil(postToServer)}
        }
    }

    private fun handleDelete(viewState: ProdukViewState?) {
        viewState?.let {
            it.error?.let { error -> showError(error) }
            it.postToServer?.let{postToServer -> showHasilDelete(postToServer)}
        }
    }

    fun showHasilDelete(postToServer: Boolean) {
        if (postToServer){
            Toast.makeText(applicationContext,getString(R.string.product_deleted),Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, ProdukActivity::class.java)
            startActivity(intent)
        }
    }

    fun showHasil(postToServer: Boolean) {
        if (postToServer){
            Toast.makeText(applicationContext,getString(R.string.product_updated),Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, ProdukActivity::class.java)
            startActivity(intent)
        }
    }
    private fun showError(error: Exception) {
        error.printStackTrace()
    }

    fun pilihLoad() {
        val pictureDialog = AlertDialog.Builder(this@EditProdukActivity)
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
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, myRequestCode)
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE_REQUEST) run {
            if (data != null) {
                val contentURI = data.data
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(
                        this@EditProdukActivity.getContentResolver(),
                        contentURI
                    )
                    imageProduk.setImageBitmap(bitmap)
                    imgUploaded = true
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@EditProdukActivity, "Failed!", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(this@EditProdukActivity, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



}
