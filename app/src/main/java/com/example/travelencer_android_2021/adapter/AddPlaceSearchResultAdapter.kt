package com.example.travelencer_android_2021.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travelencer_android_2021.AddPlaceActivity
import com.example.travelencer_android_2021.AddPlaceSearchAddressActivity
import com.example.travelencer_android_2021.R
import com.example.travelencer_android_2021.model.ModelAddressSearchList


class AddPlaceSearchResultAdapter(private val addressList: ArrayList<ModelAddressSearchList>, private val mContext: Context) : RecyclerView.Adapter<AddPlaceSearchResultAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddPlaceSearchResultAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_address_search_result, parent, false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {
                val curPosition = adapterPosition
                val name = addressList[curPosition].name
                val latitude = addressList[curPosition].latitude
                val longitude = addressList[curPosition].longitude

                val resultIntent = Intent(mContext, AddPlaceActivity::class.java)
                resultIntent.putExtra("name", name)
                resultIntent.putExtra("latitude", latitude)
                resultIntent.putExtra("longitude", longitude)

                //Log.d("adapter로그name", "name= ${name}")
                //Log.d("adapter로그lat long", "lat= ${latitude} // long= ${longitude}")

                val activity: AddPlaceSearchAddressActivity = mContext as AddPlaceSearchAddressActivity
                activity.setResult(Activity.RESULT_OK, resultIntent)
                activity.finish()
            }
        }
    }

    override fun onBindViewHolder(holder: AddPlaceSearchResultAdapter.CustomViewHolder, position: Int) {
        holder.name.text = addressList[position].name
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvAddressName)
    }
}
