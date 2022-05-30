package com.example.foodies

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View

import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap

import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.foodies.databinding.ActivityAddDishesBinding

import com.example.foodies.databinding.DialogCustomImageSelectionBinding
import com.example.foodies.databinding.DialogCustomListBinding
import com.example.foodies.entity.Foodies
import com.example.foodies.ui.Constants
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.tutorials.eu.favdish.view.adapters.CustomListItemAdapter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class AddDishes : AppCompatActivity(), View.OnClickListener {
    private lateinit var mbind: ActivityAddDishesBinding
    private var imageUri: Uri? = null
    private lateinit var mcustomd: Dialog
    private var mimagepath:String=""
    private var mfooddetails:Foodies?=null
    private val mfoodiesvm:Foodiesviewmodel by viewModels{
        foodiesvmfactory((application as FoodiesAplication).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        mbind = ActivityAddDishesBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(mbind.root)
        setupab()
        if (intent.hasExtra(Constants.EXTRA_DISH_DETAILS)){
            mfooddetails=intent.getParcelableExtra(Constants.EXTRA_DISH_DETAILS)
        }
        mfooddetails?.let {
            if (it.id!=0){
                mimagepath=it.image
                Glide.with(this).load(mimagepath).centerCrop().into(mbind.ivDishImage)
                mbind.etTitle.setText(it.title)
                mbind.etType.setText(it.type)
                mbind.etCategory.setText(it.category)
                mbind.etIngredients.setText(it.ingredients)
                mbind.etCookingTime.setText(it.cooking_time)
                mbind.etDirectionToCook.setText(it.direction_to_cook)

                mbind.btnAddDish.text = resources.getString(R.string.lbl_update_dish)

            }

        }
        mbind.ivAddDishImage.setOnClickListener(this)
        mbind.etType.setOnClickListener(this)
        mbind.etCategory.setOnClickListener(this)
        mbind.etCookingTime.setOnClickListener(this)
mbind.btnAddDish.setOnClickListener(this)
    }

    private fun setupab() {
        setSupportActionBar(mbind.toolbarAddDishActivity)
        if (mfooddetails!=null && mfooddetails!!.id!=0){
            supportActionBar?.let {
                it.title="Edit Dishes"

            }

        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mbind.toolbarAddDishActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }


    override fun onClick(p0: View?) {

        when (p0!!.id) {
            R.id.iv_add_dish_image -> {
                customImageSelectionDialog()
                return
            }
            R.id.et_type -> {
                customdialog("Select Dish Type", Constants.dishTypes(), Constants.DISH_TYPE)

            }
            R.id.et_category -> {
                customdialog(
                    "Select Dish Category ",
                    Constants.dishCategories(),
                    Constants.DISH_CATEGORY
                )

            }
            R.id.et_cooking_time -> {
                customdialog(
                    " Select Cooking Time",
                    Constants.dishCookTime(),
                    Constants.DISH_COOKING_TIME
                )
            }
            R.id.btn_add_dish -> {

                // Define the local variables and get the EditText values.
                // For Dish Image we have the global variable defined already.

                val title = mbind.etTitle.text.toString().trim { it <= ' ' }
                val type = mbind.etType.text.toString().trim { it <= ' ' }
                val category = mbind.etCategory.text.toString().trim { it <= ' ' }
                val ingredients = mbind.etIngredients.text.toString().trim { it <= ' ' }
                val cookingTimeInMinutes = mbind.etCookingTime.text.toString().trim { it <= ' ' }
                val cookingDirection = mbind.etDirectionToCook.text.toString().trim { it <= ' ' }

                when {

                    /* TextUtils.isEmpty(mImagePath) -> {
                            Toast.makeText(
                                this@AddUpdateDishActivity,
                                resources.getString(R.string.err_msg_select_dish_image),
                                Toast.LENGTH_SHORT
                            ).show()
                        }*/

                    TextUtils.isEmpty(title) -> {
                        Toast.makeText(
                            this,
                            resources.getString(R.string.err_msg_enter_dish_title),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    TextUtils.isEmpty(type) -> {
                        Toast.makeText(
                            this,
                            resources.getString(R.string.err_msg_select_dish_type),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    TextUtils.isEmpty(category) -> {
                        Toast.makeText(
                            this,
                            resources.getString(R.string.err_msg_select_dish_category),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    TextUtils.isEmpty(ingredients) -> {
                        Toast.makeText(
                            this,
                            resources.getString(R.string.err_msg_enter_dish_ingredients),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    TextUtils.isEmpty(cookingTimeInMinutes) -> {
                        Toast.makeText(
                            this,
                            resources.getString(R.string.err_msg_select_dish_cooking_time),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    TextUtils.isEmpty(cookingDirection) -> {
                        Toast.makeText(
                            this,
                            resources.getString(R.string.err_msg_enter_dish_cooking_instructions),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {

                        var dishID = 0
                        var imageSource = Constants.DISH_IMAGE_SOURCE_LOCAL
                        var favoriteDish = false

                        mfooddetails?.let {
                            if (it.id != 0) {
                                dishID = it.id
                                imageSource = it.imageSource
                                favoriteDish = it.favorite_dish
                            }
                        }

                        val foodish: Foodies = Foodies(
                            mimagepath,
                           imageSource,
                            title,
                            type,
                            category,
                            ingredients,
                            cookingTimeInMinutes,
                            cookingDirection,
                            favoriteDish,
                            dishID


                        )
                        if (dishID == 0) {
                            mfoodiesvm.insert(foodish)
                            Toast.makeText(
                                this,
                                "You successfully added your favorite dish details.",
                                Toast.LENGTH_SHORT
                            ).show()

                            Log.i("Insertion", "Success")
                            // Finish the Activity
                            finish()

                        }
                        else{
                            mfoodiesvm.updatefoodies(foodish)

                            Toast.makeText(
                                this,
                                "You successfully updated your favorite dish details.",
                                Toast.LENGTH_SHORT
                            ).show()

                            // You even print the log if Toast is not displayed on emulator
                            Log.e("Updating", "Success")
                        }
finish()
                    }
                }
            }

        }

    }


    private fun customImageSelectionDialog() {
        mcustomd = Dialog(this)

        val binding: DialogCustomImageSelectionBinding =
            DialogCustomImageSelectionBinding.inflate(layoutInflater)


        mcustomd.setContentView(binding.root)


        binding.tvCamera.setOnClickListener {


            Dexter.withContext(this)
                .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                        if (report!!.areAllPermissionsGranted()) {


                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(intent, CAMERA)
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                        token: PermissionToken?
                    ) {

                        showRationalDialogForPermissions()
                        // END
                    }
                }).onSameThread()
                .check()


            mcustomd.dismiss()
        }
        binding.tvGallery.setOnClickListener {


            Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {

                        val galleryIntent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.INTERNAL_CONTENT_URI
                        )

                        startActivityForResult(galleryIntent, GALLERY)
                        // END
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        Toast.makeText(
                            this@AddDishes,
                            "You have denied the storage permission to select image.",
                            Toast.LENGTH_SHORT
                        ).show()

                    }


                    override fun onPermissionRationaleShouldBeShown(
                        p0: com.karumi.dexter.listener.PermissionRequest?,
                        token: PermissionToken?
                    ) {
                        showRationalDialogForPermissions()
                    }
                }).onSameThread()
                .check()

            mcustomd.dismiss()


            //Start the dialog and display it on screen.


        }

        mcustomd.show()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA) {


                val thumbnail: Bitmap = data!!.extras!!.get("data") as Bitmap
                Glide.with(this).load(thumbnail).into(mbind.ivDishImage)
                mimagepath = saveImagesTOSTORAGE(thumbnail)
                Log.i("Image Path", mimagepath)
                mbind.ivAddDishImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_baseline_edit_24
                    )
                )

            } else if (requestCode == GALLERY) {

                data?.let {
                    imageUri = data.data

                    Glide.with(this).load(imageUri).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                               Log.e("TAG","Error While Loading",e)
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                resource.let {
                                    val bitmap: Bitmap = resource!!.toBitmap()
                                    mimagepath=saveImagesTOSTORAGE(bitmap)
                                    return false
                                }

                            }
                        }

                        )

                        .into(mbind.ivDishImage)

                    // Replace the add icon with edit icon once the image is selected.
                    mbind.ivAddDishImage.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_baseline_edit_24
                        )
                    )
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Cancelled", "Cancelled")
        }
    }

    fun selectfood(item: String, selection: String) {
        when (selection) {
            Constants.DISH_TYPE -> {
                mcustomd.dismiss()
                mbind.etType.setText(item)
            }
            Constants.DISH_CATEGORY -> {
                mcustomd.dismiss()
                mbind.etCategory.setText(item)
            }
            else -> {
                mcustomd.dismiss()
                mbind.etCookingTime.setText(item)
            }

        }
    }

    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this)
            .setMessage("It Looks like you have turned off permissions required for this feature. It can be enabled under Application Settings")
            .setPositiveButton(
                "GO TO SETTINGS"
            ) { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    fun customdialog(title: String, itemsList: List<String>, selection: String) {
      mcustomd= Dialog(this)
        val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)
        mcustomd.setContentView(binding.root)
        binding.tvTitle.text = title
        binding.rvList.layoutManager = LinearLayoutManager(this)
        val adapter = CustomListItemAdapter(this, itemsList,null, selection)
        binding.rvList.adapter = adapter
        mcustomd.show()
        mcustomd.setCanceledOnTouchOutside(true)

    }

    companion object {
        private const val CAMERA = 1
        private const val GALLERY = 2
private const val IMAGE_DIRECTORY="Foodies"
    }
    private fun saveImagesTOSTORAGE(bitmap: Bitmap):String{
        val wrapper=ContextWrapper(applicationContext)
        var file=wrapper.getDir(IMAGE_DIRECTORY,Context.MODE_PRIVATE)
        file= File(file,"${UUID.randomUUID()}.jpg")
        try {
            val stream:OutputStream=FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
            stream.flush()
            stream.close()

        }catch (e:IOException){
            e.printStackTrace()
        }
return file.absolutePath
    }
}






