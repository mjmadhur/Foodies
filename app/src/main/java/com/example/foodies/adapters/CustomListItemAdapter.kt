package com.tutorials.eu.favdish.view.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.foodies.AddDishes
import com.example.foodies.databinding.ItemCustomListLayoutBinding
import com.example.foodies.fragments.DishesFragment


class CustomListItemAdapter(
    private val activity: Activity,
    private val listItems: List<String>,
    private val fragment:Fragment?,
    private val selection: String
) :
    RecyclerView.Adapter<CustomListItemAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCustomListLayoutBinding =
            ItemCustomListLayoutBinding.inflate(LayoutInflater.from(activity), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = listItems[position]

        holder.tvText.text = item
        holder.itemView.setOnClickListener{
            if (activity is AddDishes){
                activity.selectfood(item,selection)
            }
            if (fragment is DishesFragment){
                fragment.filterselc(item)
            }
        }
    }


    override fun getItemCount(): Int {
        return listItems.size
    }


    class ViewHolder(view: ItemCustomListLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        // Holds the TextView that will add each item to
        val tvText = view.tvText
    }
}
// END