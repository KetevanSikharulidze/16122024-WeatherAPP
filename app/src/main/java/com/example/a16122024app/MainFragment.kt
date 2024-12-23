package com.example.a16122024app

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.a16122024app.adapters.VPAdapter
import com.example.a16122024app.adapters.WeatherAdapter
import com.example.a16122024app.databinding.FragmentMainBinding
import com.example.a16122024app.fragments.DaysFragment
import com.example.a16122024app.fragments.HoursFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import org.json.JSONObject

const val API_KEY = "62c45b028a7f491d87864226241612"

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val viewModel: MainViewModel by activityViewModels()

    private val fList = listOf(
        HoursFragment.newInstance(),
        DaysFragment.newInstance()
    )

    private val tList = listOf("HOURS", "DAYS")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        updateCurrentCard()
        requestWeatherData("Tbilisi")
    }

    private fun init() = with(binding){
        val adapter = VPAdapter(activity as FragmentActivity, fList)
        vp.adapter = adapter
        TabLayoutMediator(tabLayout, vp){ tab, position ->
            tab.text = tList[position]
        }.attach()
    }

    private fun updateCurrentCard() = with(binding){
        viewModel.liveDataCurrent.observe(viewLifecycleOwner){
            val maxMinTemp = "${it.maxTemp}°C/${it.minTemp}°C"
            tvCity.text = it.city
            tvDate.text = it.date
            tvCondition.text = it.condition
            tvTemp.text = it.temp
            tvMinMaxTemp.text = maxMinTemp
            Picasso.get().load("https:"+it.imgURL).into(ivCondition)
        }
    }

    private fun parseCurrentData(mainObject: JSONObject, weatherItem: WeatherModel){
        val item = WeatherModel(
            mainObject.getJSONObject("location").getString("name"),
            mainObject.getJSONObject("current").getString("temp_c"),
            mainObject.getJSONObject("current").getJSONObject("condition").getString("text"),
            mainObject.getJSONObject("current").getJSONObject("condition").getString("icon"),
            mainObject.getJSONObject("current").getString("last_updated"),
            weatherItem.minTemp,
            weatherItem.maxTemp,
            weatherItem.hours
        )
        Log.d("MyLog","City: ${item.city}")
        Log.d("MyLog","temp: ${item.temp}")
        Log.d("MyLog","condition: ${item.condition}")
        Log.d("MyLog","date: ${item.date}")
        Log.d("MyLog","minTemp: ${item.minTemp}")
        Log.d("MyLog","maxTemp: ${item.maxTemp}")
        Log.d("MyLog","hours: ${item.hours}")

        viewModel.liveDataCurrent.value = item
    }

    private fun parseDays(mainObject: JSONObject): List<WeatherModel>{
        val list = ArrayList<WeatherModel>()
        val daysArray = mainObject.getJSONObject("forecast").getJSONArray("forecastday")
        val name = mainObject.getJSONObject("location").getString("name")
        for (i in 0 until daysArray.length()){
            val day = daysArray[i] as JSONObject
            val item = WeatherModel(
                name,
                "",
                day.getJSONObject("day").getJSONObject("condition").getString("text"),
                day.getJSONObject("day").getJSONObject("condition").getString("icon"),
                day.getString("date"),
                day.getJSONObject("day").getString("mintemp_c"),
                day.getJSONObject("day").getString("maxtemp_c"),
                day.getJSONArray("hour").toString()
            )
            list.add(item)
        }
        viewModel.liveDataList.value = list
        return list
    }

    private fun parseWeatherData(result: String){
        val mainObject = JSONObject(result)
        val list = parseDays(mainObject)
        parseCurrentData(mainObject, list[0])
    }

    private fun requestWeatherData(city: String){
        val url = "https://api.weatherapi.com/v1/forecast.json?key=" +
                API_KEY +
                "&q=" +
                city +
                "&days=" +
                "7" +
                "&aqi=no&alerts=no\n"

        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET, url,
            {
                result -> Log.d("MyLog","Result: ${parseWeatherData(result)}")
            },
            {
                error -> Log.d("MyLog","Error: $error")
            }
        )

        queue.add(request)
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}