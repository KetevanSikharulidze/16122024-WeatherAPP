package com.example.a16122024app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.a16122024app.R
import com.example.a16122024app.WeatherModel
import com.example.a16122024app.databinding.ItemBinding
import com.squareup.picasso.Picasso

class WeatherAdapter: ListAdapter<WeatherModel,WeatherAdapter.Holder>(Comparator()) {

    class Holder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemBinding.bind(view)
        fun bind(item: WeatherModel) = with(binding){
            tvDate.text = item.date
            tvCondition.text = item.condition
            tvTemp.text = item.temp.ifEmpty {
                "${item.maxTemp}°C/${item.minTemp}°C"
            } + "°C"
            Picasso.get().load("https:"+item.imgURL).into(im)
        }
    }

    class Comparator: DiffUtil.ItemCallback<WeatherModel>(){
        override fun areItemsTheSame(oldItem: WeatherModel, newItem: WeatherModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: WeatherModel, newItem: WeatherModel): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

}