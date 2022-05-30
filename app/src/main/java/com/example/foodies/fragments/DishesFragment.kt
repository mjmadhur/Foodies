package com.example.foodies.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodies.*
import com.example.foodies.databinding.DialogCustomListBinding
import com.example.foodies.databinding.FragmentDishesBinding
import com.example.foodies.entity.Foodies
import com.example.foodies.ui.Constants
import com.tutorials.eu.favdish.view.adapters.CustomListItemAdapter
import com.tutorials.eu.favdish.view.adapters.FavDishAdapter
import java.nio.charset.MalformedInputException


class DishesFragment : Fragment() {




    private lateinit var mbinding:FragmentDishesBinding
private lateinit var mfavfoodadap:FavDishAdapter
private lateinit var mcustdialog:Dialog
    private val mFavDishViewModel: Foodiesviewmodel by viewModels {
        foodiesvmfactory((requireActivity().application as FoodiesAplication).repository)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
mbinding.rvDishesList.layoutManager=GridLayoutManager(requireActivity(),2)
        //val adapter=FavDishAdapter(this)
        mfavfoodadap= FavDishAdapter(this)
        mbinding.rvDishesList.adapter=mfavfoodadap
        mFavDishViewModel.alldisheslist.observe(viewLifecycleOwner) { dishes ->
            dishes.let {

                if (it.isNotEmpty()){
                    mbinding.rvDishesList.visibility=View.VISIBLE
                    mbinding.textDishes.visibility=View.GONE
                    mfavfoodadap.dishesList(it)


                }else{
                    mbinding.rvDishesList.visibility=View.GONE
                    mbinding.textDishes.visibility=View.VISIBLE
                }

            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      mbinding= FragmentDishesBinding.inflate(inflater,container,false)
        return mbinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
inflater.inflate(R.menu.add_dd,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add_dishes->{
                startActivity(Intent(requireActivity(),AddDishes::class.java))
            }
            R.id.filter->{
                filterDishesListDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun dishesdetails(favfood:Foodies){
        findNavController().navigate(DishesFragmentDirections.actionNavigationHomeToNavigationDishdetails(
         favfood
        ))
    if (requireActivity() is MainActivity){
        (activity as MainActivity)!!.hidebnav()
        (activity as MainActivity).window.decorView.systemUiVisibility=View.SYSTEM_UI_FLAG_FULLSCREEN
    }



    }
    fun deleteitem(foodies: Foodies) {
        val builder = AlertDialog.Builder(requireActivity())
        //set title for alert dialog
        builder.setTitle(resources.getString(R.string.title_delete_dish))
        //set message for alert dialog
        builder.setMessage(resources.getString(R.string.msg_delete_dish_dialog, foodies.title))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton(resources.getString(R.string.lbl_yes)) { dialogInterface, _ ->
            mFavDishViewModel.deleteitem(foodies)
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        //performing negative action
        builder.setNegativeButton(resources.getString(R.string.lbl_no)) { dialogInterface, which ->
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()  // show the dialog to UI
    }
    private fun filterDishesListDialog() {
     mcustdialog= Dialog(requireActivity())

        val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)

 mcustdialog.setContentView(binding.root)

        binding.tvTitle.text = resources.getString(R.string.title_select_item_to_filter)

        val dishTypes = Constants.dishTypes()

        dishTypes.add(0, Constants.ALL_ITEMS)

        // Set the LayoutManager that this RecyclerView will use.
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        // Adapter class is initialized and list is passed in the param.
        val adapter = CustomListItemAdapter(
            requireActivity(),
            dishTypes,
            this,
            Constants.FILTER_SELECTION
        )
        // adapter instance is set to the recyclerview to inflate the items.
        binding.rvList.adapter = adapter
        //Start the dialog and display it on screen.
       mcustdialog.show()
    }

fun filterselc(filteritem:String){
    mcustdialog.dismiss()
    Log.i("filter Selection",filteritem)
    if (filteritem==Constants.ALL_ITEMS){
        mFavDishViewModel.alldisheslist.observe(viewLifecycleOwner){
            if (it.isNotEmpty()){
                mbinding.rvDishesList.visibility=View.VISIBLE
                mbinding.textDishes.visibility=View.GONE
                mfavfoodadap.dishesList(it)


            }else{
                mbinding.rvDishesList.visibility=View.GONE
                mbinding.textDishes.visibility=View.VISIBLE
            }
        }
    }else{
mFavDishViewModel.getfilterlist(filteritem).observe(viewLifecycleOwner){
        dishes ->
    dishes.let {
        if (it.isNotEmpty()) {

            mbinding.rvDishesList.visibility = View.VISIBLE
            mbinding.textDishes.visibility = View.GONE

            mfavfoodadap.dishesList(it)
        } else {

            mbinding.rvDishesList.visibility = View.GONE
            mbinding.textDishes.visibility = View.VISIBLE
        }
    }

}

    }
}
    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity){
            (activity as MainActivity).showbnav()
        }
    }

}