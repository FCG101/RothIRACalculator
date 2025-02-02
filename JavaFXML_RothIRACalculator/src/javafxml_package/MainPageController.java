package javafxml_package;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public class MainPageController implements Initializable {

    @FXML
    private TextField ageInput;
    @FXML
    private TextField investmentInput;
    @FXML
    private Label resultLabel;
    @FXML
    private Button estimateButton;
    @FXML
    private Slider returnRateSlider;
    @FXML
    private TextField withdrawalAgeInput;

     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        
        returnRateSlider.setMajorTickUnit(1);
        returnRateSlider.setMinorTickCount(0);
        returnRateSlider.setSnapToTicks(true);
    }

    @FXML
    private void calculateInvestment() {
        try {
            int age = Integer.parseInt(ageInput.getText().trim());
            double monthlyAmount = Double.parseDouble(investmentInput.getText().trim());
            int withdrawalAge = Integer.parseInt(withdrawalAgeInput.getText().trim());
            double annualReturnRate = returnRateSlider.getValue() / 100.0;

            
            if (age <= 0) {
                resultLabel.setText("Age must be greater than 0.");
                return;
            }
            if (monthlyAmount <= 0 || monthlyAmount > 500) {
                resultLabel.setText("Investment must be between $1 and $500 per month.");
                return;
            }
            if (withdrawalAge <= age) {
                resultLabel.setText("Withdrawal age must be greater than your current age.");
                return;
            }

            // THis calculates the estimated investment value
            double estimatedInvestment = estimateRothIRA(monthlyAmount, age, withdrawalAge, annualReturnRate);
            String resultText = "Your investment could grow to: " + formatCurrency(estimatedInvestment) + " by age " + withdrawalAge + ".";

            // Applies any penalties if necessary
            boolean earlyWithdrawal = withdrawalAge < 59.5;
            boolean lessThanFiveYears = (withdrawalAge - age) < 5;
            double penaltyAmount = 0;

            if (earlyWithdrawal || lessThanFiveYears) {
                penaltyAmount = estimatedInvestment * 0.10;
                estimatedInvestment -= penaltyAmount;
                resultText += "\n\nPenalty Applied: A 10% early withdrawal penalty of " + formatCurrency(penaltyAmount) + " has been\n deducted:";
                
                if (earlyWithdrawal) {
                    resultText += "\n- Withdrawals before age 59.5 incur a 10% penalty.";
                }
                if (lessThanFiveYears) {
                    resultText += "\n- Your account was held for less than 5 years, resulting in a penalty.";
                }

                resultText += "\n\nFinal Amount After Penalty: " + formatCurrency(estimatedInvestment);
            }

            resultLabel.setText(resultText);

        } catch (NumberFormatException e) {
            resultLabel.setText("Please enter valid numbers.");
        }
    }

    // Method to estimate the investment growth
    private double estimateRothIRA(double monthlyAmount, int currentAge, int withdrawalAge, double annualReturnRate) {
        int years = withdrawalAge - currentAge;
        int months = years * 12;
        double total = 0;

        for (int i = 0; i < months; i++) {
            total = (total + monthlyAmount) * (1 + (annualReturnRate / 12)); 
        }

        return total;
    }

    private String formatCurrency(double value) {
        NumberFormat currencyFormat = NumberFormat.getNumberInstance();
        currencyFormat.setGroupingUsed(true);
        currencyFormat.setMaximumFractionDigits(2);
        currencyFormat.setMinimumFractionDigits(2);
        return "$" + currencyFormat.format(value);
    }
}
