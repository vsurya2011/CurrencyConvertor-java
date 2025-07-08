import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;

public class CurrencyConverterUI extends JFrame {

    JComboBox<String> fromCurrency, toCurrency;
    JTextField amountField, resultField;
    JButton convertBtn;

    String[] currencies = {"USD", "INR", "EUR", "GBP", "JPY", "AUD", "CAD"};

    public CurrencyConverterUI() {
        setTitle("Currency Converter");
        setSize(500, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // FROM Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("From Currency:"), gbc);

        // FROM Combo
        gbc.gridx = 1;
        fromCurrency = new JComboBox<>(currencies);
        add(fromCurrency, gbc);

        // TO Label
        gbc.gridx = 2;
        add(new JLabel("To Currency:"), gbc);

        // TO Combo
        gbc.gridx = 3;
        toCurrency = new JComboBox<>(currencies);
        add(toCurrency, gbc);

        // Amount Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Amount:"), gbc);

        // Amount Field
        gbc.gridx = 1;
        amountField = new JTextField();
        add(amountField, gbc);

        // Spacer
        gbc.gridx = 2;
        gbc.gridy = 1;
        add(new JLabel(""), gbc);

        // Convert Button
        gbc.gridx = 3;
        convertBtn = new JButton("Convert");
        add(convertBtn, gbc);

        // Converted Label
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Converted Amount:"), gbc);

        // Converted Result
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        resultField = new JTextField();
        resultField.setEditable(false);
        add(resultField, gbc);

        // Action listener
        convertBtn.addActionListener(e -> convertCurrency());

        setVisible(true);
    }

    public void convertCurrency() {
        try {
            String from = (String) fromCurrency.getSelectedItem();
            String to = (String) toCurrency.getSelectedItem();
            String amountText = amountField.getText();

            double amount = Double.parseDouble(amountText);

            String urlStr = String.format("https://api.frankfurter.app/latest?amount=%s&from=%s&to=%s",
                    amount, from, to);

            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            String search = "\"" + to + "\":";
            int index = response.indexOf(search);
            if (index != -1) {
                int start = index + search.length();
                int end = response.indexOf("}", start);
                String result = response.substring(start, end).trim();
                resultField.setText(result);
            } else {
                resultField.setText("Error parsing");
            }

        } catch (Exception e) {
            resultField.setText("Invalid input / Network error");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new CurrencyConverterUI();
    }
}
