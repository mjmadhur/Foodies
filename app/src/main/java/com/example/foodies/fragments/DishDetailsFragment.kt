package com.example.foodies.fragments

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.foodies.FoodiesAplication
import com.example.foodies.Foodiesviewmodel
import com.example.foodies.R
import com.example.foodies.databinding.FragmentDishDetailsBinding
import com.example.foodies.entity.Foodies
import com.example.foodies.foodiesvmfactory
import com.example.foodies.ui.Constants
import java.io.IOException
import java.util.*


class DishDetailsFragment : Fragment() {
    private var mbind: FragmentDishDetailsBinding? = null
    private var mfavdetails:Foodies?=null
    private val mfoodiesvm: Foodiesviewmodel by viewModels {
        foodiesvmfactory(((requireActivity().application) as FoodiesAplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_share,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.share -> {
                val type = "text/plain"
                val subject = "Checkout this dish recipe"
                var extraText = ""
                val shareWith = "Share with"

                mfavdetails?.let {

                    var image = ""

                    if (it.imageSource == Constants.DISH_IMAGE_SOURCE_ONLINE) {
                        image = it.image
                    }

                    var cookingInstructions = ""

                    // The instruction or you can say the Cooking direction text is in the HTML format so we will you the fromHtml to populate it in the TextView.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        cookingInstructions = Html.fromHtml(
                            it.direction_to_cook,
                            Html.FROM_HTML_MODE_COMPACT
                        ).toString()
                    } else {
                        @Suppress("DEPRECATION")
                        cookingInstructions = Html.fromHtml(it.direction_to_cook).toString()
                    }

                    extraText =
                        "$image \n" +
                                "\n Title:  ${it.title} \n\n Type: ${it.type} \n\n Category: ${it.category}" +
                                "\n\n Ingredients: \n ${it.ingredients} \n\n Instructions To Cook: \n $cookingInstructions" +
                                "\n\n Time required to cook the dish approx ${it.cooking_time} minutes."
                }


                val intent = Intent(Intent.ACTION_SEND)
                intent.type = type
                intent.putExtra(Intent.EXTRA_SUBJECT, subject)
                intent.putExtra(Intent.EXTRA_TEXT, extraText)
                startActivity(Intent.createChooser(intent, shareWith))

                return true
            }
            // END


            }
        return super.onOptionsItemSelected(item)
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mbind = FragmentDishDetailsBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        // return inflater.inflate(R.layout.fragment_dish_details, container, false)
        return mbind!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val args: DishDetailsFragmentArgs by navArgs()
        mfavdetails=args.dishdetails
        super.onViewCreated(view, savedInstanceState)

        args.let {
            try {
                Glide.with(requireActivity())
                    .load(it.dishdetails.image)
                    .centerCrop()
                    .listener(
                        object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                Log.e("TAG", "Error Loading Image", e)
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
                                    Palette.from(resource!!.toBitmap()).generate() { palette ->
                                        val intcolor = palette?.vibrantSwatch?.rgb ?: 0
                                        mbind!!.rlDishDetailMain.setBackgroundColor(intcolor)
                                    }
                                }
                                return false
                            }
                        }
                    )
                    .into(mbind!!.ivDishImage)

            } catch (e: IOException) {
                e.printStackTrace()
            }

            mbind!!.tvTitle.text = it.dishdetails.title
            mbind!!.tvType.text = it.dishdetails.type.capitalize(Locale.ENGLISH)
            mbind!!.tvCategory.text = it.dishdetails.category
            mbind!!.tvIngredients.text = it.dishdetails.ingredients
            mbind!!.tvCookingDirection.text = it.dishdetails.direction_to_cook
            mbind!!.tvCookingTime.text =
                resources.getString(R.string.lbl_estimate_cooking_time, it.dishdetails.cooking_time)

            if (args.dishdetails.favorite_dish) {
                mbind!!.ivFavoriteDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(), R.drawable.ic_favorite_selected
                    )
                )


            } else {
                mbind!!.ivFavoriteDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(), R.drawable.ic_favorite_unselected
                    )
                )
            }

        }
            mbind!!.ivFavoriteDish.setOnClickListener {
                args.dishdetails.favorite_dish = !args.dishdetails.favorite_dish
                mfoodiesvm.updatefoodies(args.dishdetails)
                if (args.dishdetails.favorite_dish) {
                    mbind!!.ivFavoriteDish.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireActivity(), R.drawable.ic_favorite_selected
                        )
                    )
                    Toast.makeText(requireActivity(), "Added To Favorites", Toast.LENGTH_LONG)
                        .show()
                } else {
                    mbind!!.ivFavoriteDish.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireActivity(), R.drawable.ic_favorite_unselected
                        )
                    )
                    Toast.makeText(requireActivity(), "Removed From Favorites", Toast.LENGTH_LONG)
                        .show()
                }

        }
    }

        override fun onDestroy() {
            super.onDestroy()
            mbind = null
        }

    }
