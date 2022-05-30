package com.example.foodies.fragments

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodies.*
import com.example.foodies.databinding.FragmentNotificationsBinding
import com.example.foodies.entity.Foodies
import com.example.foodies.ui.Constants
import com.example.foodies.ui.notifications.NotificationsViewModel

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private var mbind:FragmentNotificationsBinding?=null
private lateinit var mrandomvm:Randomfoodvm
private var mProgressDialog:Dialog?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       mbind= FragmentNotificationsBinding.inflate(layoutInflater,container,false)
        return mbind!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

mrandomvm=ViewModelProvider(this).get(Randomfoodvm::class.java)
        mrandomvm.getrandomfromapi()
        randomDishViewModelObserver()
        mbind!!.srlRandom.setOnRefreshListener {
            mrandomvm.getrandomfromapi()

        }
    }
    private fun randomDishViewModelObserver() {

        mrandomvm.randomdishresponse.observe(
            viewLifecycleOwner,
            Observer { randomDishResponse ->
                randomDishResponse?.let {
                 if (mbind!!.srlRandom.isRefreshing){
                     mbind!!.srlRandom.isRefreshing=false
                 }
               setrandomdishUi(randomDishResponse.recipes[0])
                }
            })

        mrandomvm.randomdishloadingerror.observe(
            viewLifecycleOwner,
            Observer { dataError ->
                dataError?.let {
                    if (mbind!!.srlRandom.isRefreshing){
                        mbind!!.srlRandom.isRefreshing=false
                    }
                    Log.i("Random Dish API Error", "$dataError")
                }
            })

        mrandomvm.loadfood.observe(viewLifecycleOwner, Observer { loadRandomDish ->
            loadRandomDish?.let {
                if (loadRandomDish && !mbind!!.srlRandom.isRefreshing) {
                    showCustomProgressDialog() // Used to show the progress dialog
                } else {
                    hideProgressDialog()
                }

                Log.i("Random Dish Loading", "$loadRandomDish")
            }
        })
    }
    private fun setrandomdishUi(recipies:RandomDish.Recipe){
        Glide.with(requireActivity()).load(recipies.image).into(mbind!!.ivDishImage)
        mbind!!.tvTitle.text=recipies.title
        var dishType: String = "other"

        if (recipies.dishTypes.isNotEmpty()) {
            dishType = recipies.dishTypes[0]
            mbind!!.tvType.text = dishType
        }

        // There is not category params present in the response so we will define it as Other.
        mbind!!.tvCategory.text = "Other"

        var ingredients = ""
        for (value in recipies.extendedIngredients) {

            if (ingredients.isEmpty()) {
                ingredients = value.original
            } else {
                ingredients = ingredients + ", \n" + value.original
            }
        }

        mbind!!.tvIngredients.text = ingredients

        // The instruction or you can say the Cooking direction text is in the HTML format so we will you the fromHtml to populate it in the TextView.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mbind!!.tvCookingDirection.text = Html.fromHtml(
                recipies.instructions,
                Html.FROM_HTML_MODE_COMPACT
            )
        } else {
            @Suppress("DEPRECATION")
            mbind!!.tvCookingDirection.text = Html.fromHtml(recipies.instructions)
        }
mbind!!.ivFavoriteDish.setImageDrawable(ContextCompat.getDrawable(requireActivity(),R.drawable.ic_favorite_unselected,))
       var addedtofav=false

        mbind!!.tvCookingTime.text =
            resources.getString(
                R.string.lbl_estimate_cooking_time,
                recipies.readyInMinutes.toString()
            )


        mbind!!.ivFavoriteDish.setOnClickListener {

            if (addedtofav) {
                Toast.makeText(requireActivity(), "Already Added to Favorites", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val randomDishDetails = Foodies(
                    recipies.image,
                    Constants.DISH_IMAGE_SOURCE_ONLINE,
                    recipies.title,
                    dishType,
                    "Other",
                    ingredients,
                    recipies.readyInMinutes.toString(),
                    recipies.instructions,
                    true
                )
                // END

                // TODO Step 8: Create an instance of FavDishViewModel class and call insert function and pass the required details.
                // START
                val mFavDishViewModel: Foodiesviewmodel by viewModels {
                    foodiesvmfactory((requireActivity().application as FoodiesAplication).repository)
                }

                mFavDishViewModel.insert(randomDishDetails)
                addedtofav = true
                mbind!!.ivFavoriteDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_selected
                    )
                )

                Toast.makeText(
                    requireActivity(),
                    resources.getString(R.string.msg_added_to_favorites),
                    Toast.LENGTH_SHORT
                ).show()
                // END
            }
        }

    }
    private fun showCustomProgressDialog() {
        mProgressDialog = Dialog(requireActivity())

        mProgressDialog?.let {
            /*Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.*/
            it.setContentView(R.layout.dialog_progress)

            //Start the dialog and display it on screen.
            it.show()
        }
    }

    private fun hideProgressDialog() {
        mProgressDialog?.let {
            it.dismiss()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
mbind=null
    }
}