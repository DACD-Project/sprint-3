package ulpgc.dacd.weather.control;

import ulpgc.dacd.weather.model.Weather;

public interface WeatherProvider {
    Weather getWeather(double lat, double lon);
}