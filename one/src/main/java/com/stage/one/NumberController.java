package com.stage.one;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*") // Enable CORS for all domains
public class NumberController {

    private static final Logger logger = LoggerFactory.getLogger(NumberController.class);

    @GetMapping("/api/classify-number")
    public ResponseEntity<?> classifyNumber(@RequestParam String number) {
        // Validate that the input is a valid number
        int num;
        try {
            num = Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(number, true));
        }

        // Validate number to be non-negative
        if (num < 0) {
            return ResponseEntity.badRequest().body(new ErrorResponse(number, true));
        }

        // Determine if the number is prime
        boolean isPrime = isPrime(num);

        // Check if the number is perfect
        boolean isPerfect = isPerfect(num);

        // Check if the number is Armstrong
        boolean isArmstrong = isArmstrong(num);

        // Get the list of properties
        List<String> properties = new ArrayList<>();
        if (isArmstrong) properties.add("armstrong");
        if (num % 2 != 0) properties.add("odd");
        else properties.add("even");

        // Calculate the sum of digits
        int digitSum = calculateDigitSum(num);

        // Fetch fun fact about the number
        String funFact = fetchFunFact(num);

        // Create and return the response JSON
        NumberInfo numberInfo = new NumberInfo(num, isPrime, isPerfect, properties, digitSum, funFact);

        return ResponseEntity.ok(numberInfo);
    }

    // Helper method to check if a number is prime
    private boolean isPrime(int number) {
        if (number <= 1) return false;
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) return false;
        }
        return true;
    }

    // Helper method to check if a number is perfect
    private boolean isPerfect(int number) {
        int sum = 0;
        for (int i = 1; i <= number / 2; i++) {
            if (number % i == 0) sum += i;
        }
        return sum == number;
    }

    // Helper method to check if a number is an Armstrong number
    private boolean isArmstrong(int number) {
        int sum = 0, temp = number;
        int digits = String.valueOf(number).length();
        while (temp != 0) {
            int digit = temp % 10;
            sum += Math.pow(digit, digits);
            temp /= 10;
        }
        return sum == number;
    }

    // Helper method to calculate the sum of digits
    private int calculateDigitSum(int number) {
        int sum = 0;
        while (number != 0) {
            sum += number % 10;
            number /= 10;
        }
        return sum;
    }

    // Helper method to fetch fun fact from Numbers API
    private String fetchFunFact(int number) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://numbersapi.com/" + number + "?json";
        try {
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            logger.error("Error fetching fun fact for number " + number, e);
            return "Error fetching fun fact: " + e.getMessage();
        }
    }

    // Inner class to represent the response JSON
    public static class NumberInfo {
        private int number;
        private boolean isPrime;
        private boolean isPerfect;
        private List<String> properties;
        private int digitSum;
        private String funFact;

        public NumberInfo(int number, boolean isPrime, boolean isPerfect, List<String> properties, int digitSum, String funFact) {
            this.number = number;
            this.isPrime = isPrime;
            this.isPerfect = isPerfect;
            this.properties = properties;
            this.digitSum = digitSum;
            this.funFact = funFact != null ? funFact : "No fun fact available";
        }

        public int getNumber() {
            return number;
        }

        public boolean isPrime() {
            return isPrime;
        }

        public boolean isPerfect() {
            return isPerfect;
        }

        public List<String> getProperties() {
            return properties;
        }

        public int getDigitSum() {
            return digitSum;
        }

        public String getFunFact() {
            return funFact;
        }
    }

    // Inner class to represent the error response JSON
    public static class ErrorResponse {
        private String number;
        private boolean error;

        public ErrorResponse(String number, boolean error) {
            this.number = number;
            this.error = error;
        }

        public String getNumber() {
            return number;
        }

        public boolean isError() {
            return error;
        }
    }
}
