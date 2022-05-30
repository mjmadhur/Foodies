package com.example.foodies.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodies.*
import com.example.foodies.databinding.FragmentDashboardBinding
import com.example.foodies.entity.Foodies
import com.example.foodies.ui.dashboard.DashboardViewModel
import com.tutorials.eu.favdish.view.adapters.FavDishAdapter

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var mbind:FragmentDashboardBinding?=null
private  val mfavoritevm:Foodiesviewmodel by viewModels {
    foodiesvmfactory((requireActivity().application as FoodiesAplication).repository)
}
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mbind= FragmentDashboardBinding.inflate(layoutInflater,container,false)


        return mbind!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       super.onViewCreated(view,savedInstanceState)
        mfavoritevm.favfoodlist.observe(viewLifecycleOwner){
            food->
            food.let {
                mbind!!.rvFavDishesList.layoutManager=GridLayoutManager(requireActivity(),2)
                val adapter1=FavDishAdapter(this@DashboardFragment)
                mbind!!.rvFavDishesList.adapter=adapter1
                if (it.isNotEmpty()){
                   mbind!!.rvFavDishesList.visibility=View.VISIBLE
                    mbind!!.textDashboard.visibility=View.GONE
                    adapter1.dishesList(it)
                }else{
                    mbind!!.textDashboard.visibility=View.VISIBLE
                    mbind!!.textDashboard.text="No Favorite Dishes Added Yet"
                mbind!!.rvFavDishesList.visibility=View.GONE
                }
            }
        }

    }
    fun dishdetails(foodies: Foodies){
        if (requireActivity() is MainActivity){
            (activity as MainActivity).hidebnav()
        }
        findNavController().navigate(DashboardFragmentDirections.actionNavigationDashboardToNavigationDishdetails(foodies))
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity){
            (activity as MainActivity).showbnav()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
mbind=null
    }
}