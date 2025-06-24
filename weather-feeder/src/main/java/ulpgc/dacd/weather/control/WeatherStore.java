package ulpgc.dacd.weather.control;

import ulpgc.dacd.weather.model.Weather.ForecastEntry;

public interface WeatherStore {
    void storeWeather(ForecastEntry weather);
}
