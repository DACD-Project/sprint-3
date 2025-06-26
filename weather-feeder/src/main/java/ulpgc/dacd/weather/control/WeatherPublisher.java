package ulpgc.dacd.weather.control;

import ulpgc.dacd.weather.model.WeatherEvent;

public interface WeatherPublisher {
    void publish(WeatherEvent event);
}