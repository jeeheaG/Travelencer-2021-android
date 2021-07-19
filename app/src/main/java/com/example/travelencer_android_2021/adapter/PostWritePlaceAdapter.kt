package com.example.travelencer_android_2021.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.travelencer_android_2021.R
import com.example.travelencer_android_2021.model.ModelCourseSpot

class PostWritePlaceAdapter(private val placeList: ArrayList<ModelCourseSpot>, private val mContext: Context): RecyclerView.Adapter<PostWritePlaceAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostWritePlaceAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_place_simple, parent, false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {
                val curPosition: Int = adapterPosition
                val place: ModelCourseSpot = placeList[curPosition]
                //Toast.makeText(parent.context, "이름:${place.name}, 위치:${place.location}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: PostWritePlaceAdapter.CustomViewHolder, position: Int) {
        holder.name.text = placeList[position].name
        holder.loc.text = placeList[position].location

        // TODO : 여행지 장소 입력 시에 맛집인지 관광지인지 구분하는 데이터도 받아야 하겠네..임시로 true false넣어둠
        if(true){
            holder.icon.setImageResource(R.drawable.ic_food)
        }
        else if(false){
            holder.icon.setImageResource(R.drawable.ic_location_yellow)
        }

//        holder.itemView.setOnClickListener{
//            val intent = Intent(mContext, PlaceDetailActivity::class.java)
//            mContext.startActivity(intent)
//        }
    }
    override fun getItemCount(): Int {
        return placeList.size
    }

    //뷰홀더
    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.ivPlaceCategory) //아이콘
        val name: TextView = itemView.findViewById(R.id.tvPlaceName) //장소 이름
        val loc: TextView = itemView.findViewById(R.id.tvPlaceLocation) //지역
    }

}