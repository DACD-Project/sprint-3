package ulpgc.dacd.businessunit.view;

import ulpgc.dacd.businessunit.model.CityData;
import java.util.Map;

public interface View {
    void display(Map<String, CityData> datamart);
}
