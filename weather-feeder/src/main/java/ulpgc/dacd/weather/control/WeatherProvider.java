package ulpgc.dacd.weather.control;

import ulpgc.dacd.weather.model.WeatherEvent;

public interface WeatherProvider {
    WeatherEvent getWeather(double latitude, double longitude, String cityName);
}