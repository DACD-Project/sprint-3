package ulpgc.dacd.businessunit.view;

import ulpgc.dacd.businessunit.model.CityData;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.stream.Collectors;

public class GuiView implements View {
    private JFrame frame;
    private JPanel dataPanel;
    private JLabel recommendationLabel;
    private Map<String, CityData> currentDatamart;

    public GuiView() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
                e.printStackTrace();
            }

            frame = new JFrame("Weather and Destinations");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600);
            frame.setLocationRelativeTo(null);
            frame.getContentPane().setBackground(new Color(135, 206, 235));

            dataPanel = new JPanel(new GridBagLayout());
            dataPanel.setOpaque(false);
            JScrollPane scrollPane = new JScrollPane(dataPanel);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.setBorder(null);

            recommendationLabel = new JLabel();
            recommendationLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            recommendationLabel.setForeground(Color.BLACK);

            frame.setLayout(new BorderLayout(10, 10));
            frame.add(scrollPane, BorderLayout.CENTER);
            frame.add(recommendationLabel, BorderLayout.SOUTH);
            frame.setVisible(true);

            Timer timer = new Timer(30000, e -> updateDisplay());
            timer.start();
        });
    }

    @Override
    public void display(Map<String, CityData> datamart) {
        this.currentDatamart = datamart;
        SwingUtilities.invokeLater(this::updateDisplay);
    }

    private void updateDisplay() {
        dataPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] headers = {"City", "Temp (°C)", "Humidity (%)", "Wind Speed (m/s)", "Precipitation (%)", "Score"};
        for (int i = 0; i < headers.length; i++) {
            gbc.gridx = i;
            gbc.gridy = 0;
            JLabel headerLabel = new JLabel(headers[i]);
            headerLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            headerLabel.setForeground(Color.BLACK);
            dataPanel.add(headerLabel, gbc);
        }

        int row = 1;
        for (CityData data : currentDatamart.values()) {
            gbc.gridy = row;

            gbc.gridx = 0;
            JLabel cityLabel = new JLabel(data.getCity());
            cityLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            dataPanel.add(cityLabel, gbc);

            String weatherInfo = data.getWeather() != null
                    ? String.format("%.1f | %d | %.1f | %.0f",
                    data.getWeather().getTemperature(),
                    data.getWeather().getHumidity(),
                    data.getWeather().getWindSpeed(),
                    data.getWeather().getPop() * 100)
                    : "N/A | N/A | N/A | N/A";
            String[] weatherValues = weatherInfo.split(" \\| ");
            for (int i = 0; i < 4; i++) {
                gbc.gridx = i + 1;
                JLabel weatherLabel = new JLabel(weatherValues[i]);
                weatherLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
                dataPanel.add(weatherLabel, gbc);
            }

            gbc.gridx = 5;
            JLabel scoreLabel = new JLabel(String.format("%.1f", data.getScore()));
            scoreLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            dataPanel.add(scoreLabel, gbc);

            gbc.gridy = row + 1;
            gbc.gridx = 0;
            gbc.gridwidth = 6;
            String destinationInfo = data.getDestinations() != null && !data.getDestinations().isEmpty()
                    ? data.getDestinations().stream()
                    .map(dest -> String.format("%s (%.1f km, %d pop.)", dest.getName(), dest.getDistance(), dest.getPopulation()))
                    .collect(Collectors.joining(" | "))
                    : "N/A";
            JLabel destinationsLabel = new JLabel("Destinations: " + destinationInfo);
            destinationsLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
            destinationsLabel.setForeground(Color.DARK_GRAY);
            dataPanel.add(destinationsLabel, gbc);
            gbc.gridwidth = 1;

            row += 2;
        }

        CityData bestCity = currentDatamart.values().stream()
                .max((a, b) -> Double.compare(a.getScore(), b.getScore()))
                .orElse(null);
        String recommendation = bestCity != null
                ? "Recommendation: " + bestCity.getCity() + " (Score: " + String.format("%.1f", bestCity.getScore()) + ")"
                : "Recommendation: No sufficient data available";
        recommendationLabel.setText(recommendation);

        dataPanel.revalidate();
        dataPanel.repaint();
    }
}