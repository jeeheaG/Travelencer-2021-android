package com.example.travelencer_android_2021.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travelencer_android_2021.R
import com.example.travelencer_android_2021.model.ModelAddressSearchList


class AddPlaceSearchResultAdapter(private val addressList: ArrayList<ModelAddressSearchList>) : RecyclerView.Adapter<AddPlaceSearchResultAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddPlaceSearchResultAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_address_search_result, parent, false)
        return CustomViewHolder(view).apply {
            //온클릭 리스너 다는 곳
        }
    }

    override fun onBindViewHolder(holder: AddPlaceSearchResultAdapter.CustomViewHolder, position: Int) {
        holder.name.text = addressList[position].name
        //holder.address.text = addressList[position].address
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvAddressName)
        //val address: TextView = itemView.findViewById(R.id.tvDetailAddress)
    }

}