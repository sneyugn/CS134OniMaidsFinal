package com.example.cs134onimaidsfinal.model


import com.google.gson.annotations.SerializedName

data class CurrentResponseApi(
    @SerializedName("base")
    val base: String?, // stations
    @SerializedName("clouds")
    val clouds: Clouds?,
    @SerializedName("cod")
    val cod: Int?, // 200
    @SerializedName("coord")
    val coord: Coord?,
    @SerializedName("dt")
    val dt: Int?, // 1726660758
    @SerializedName("id")
    val id: Int?, // 3165523
    @SerializedName("main")
    val main: Main?,
    @SerializedName("name")
    val name: String?, // Province of Turin
    @SerializedName("rain")
    val rain: Rain?,
    @SerializedName("sys")
    val sys: Sys?,
    @SerializedName("timezone")
    val timezone: Int?, // 7200
    @SerializedName("visibility")
    val visibility: Int?, // 10000
    @SerializedName("weather")
    val weather: List<Weather?>?,
    @SerializedName("wind")
    val wind: Wind?
) {
    data class Clouds(
        @SerializedName("all")
        val all: Int? // 83
    )
    data class Coord(
        @SerializedName("lat")
        val lat: Double?, // 45.133
        @SerializedName("lon")
        val lon: Double? // 7.367
    )
    data class Main(
        @SerializedName("feels_like")
        val feelsLike: Double?, // 282.93
        @SerializedName("grnd_level")
        val grndLevel: Int?, // 910
        @SerializedName("humidity")
        val humidity: Int?, // 60
        @SerializedName("pressure")
        val pressure: Int?, // 1021
        @SerializedName("sea_level")
        val seaLevel: Int?, // 1021
        @SerializedName("temp")
        val temp: Double?, // 284.2
        @SerializedName("temp_max")
        val tempMax: Double?, // 286.82
        @SerializedName("temp_min")
        val tempMin: Double? // 283.06
    )
    data class Rain(
        @SerializedName("1h")
        val h: Double? // 2.73
    )
    data class Sys(
        @SerializedName("country")
        val country: String?, // IT
        @SerializedName("id")
        val id: Int?, // 6736
        @SerializedName("sunrise")
        val sunrise: Int?, // 1726636384
        @SerializedName("sunset")
        val sunset: Int?, // 1726680975
        @SerializedName("type")
        val type: Int? // 1
    )
    data class Weather(
        @SerializedName("description")
        val description: String?, // moderate rain
        @SerializedName("icon")
        val icon: String?, // 10d
        @SerializedName("id")
        val id: Int?, // 501
        @SerializedName("main")
        val main: String? // Rain
    )
    data class Wind(
        @SerializedName("deg")
        val deg: Int?, // 121
        @SerializedName("gust")
        val gust: Double?, // 3.47
        @SerializedName("speed")
        val speed: Double? // 4.09
    )
}