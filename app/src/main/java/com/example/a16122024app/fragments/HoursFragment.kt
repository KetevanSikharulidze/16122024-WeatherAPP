package com.example.a16122024app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a16122024app.MainViewModel
import com.example.a16122024app.R
import com.example.a16122024app.WeatherModel
import com.example.a16122024app.adapters.WeatherAdapter
import com.example.a16122024app.databinding.FragmentHoursBinding
import org.json.JSONArray
import org.json.JSONObject

class HoursFragment : Fragment() {

    private lateinit var binding: FragmentHoursBinding
    private lateinit var adapter: WeatherAdapter

    private val model : MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHoursBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        model.liveDataCurrent.observe(viewLifecycleOwner){
            adapter.submitList(getHoursList(it))
        }
    }

    private fun getHoursList(weatherItem: WeatherModel): List<WeatherModel>{
        val hoursArray = JSONArray(weatherItem.hours)
        val list = ArrayList<WeatherModel>()
        for (i in 0 until hoursArray.length()){
            val item = WeatherModel(
                "",
                (hoursArray[i] as JSONObject).getString("temp_c"),
                (hoursArray[i] as JSONObject).getJSONObject("condition").getString("text"),
                (hoursArray[i] as JSONObject).getJSONObject("condition").getString("icon"),
                (hoursArray[i] as JSONObject).getString("time"),
                "", "", ""
            )
            list.add(item)
        }
        return list
    }


    private fun initRcView() = with(binding){
        RVHours.layoutManager = LinearLayoutManager(activity)
        adapter = WeatherAdapter(null)
        RVHours.adapter = adapter
        val list = listOf(
            WeatherModel(
                "",
                "3C",
                "Cloudy",
                "",
                "11:00","","",""),
            WeatherModel(
                "",
                "3C",
                "Cloudy",
                "",
                "11:00","","",""),
            WeatherModel(
                "",
                "3C",
                "Cloudy",
                "",
                "11:00","","",""),
            WeatherModel(
                "",
                "3C",
                "Cloudy",
                "",
                "11:00","","",""),
            )
        adapter.submitList(list)
    }

    companion object {
        @JvmStatic
        fun newInstance() = HoursFragment()
    }
}