import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

class Main {

    // -------------- code for function_five ----------------------------//

    private static List<List<String>> readCSV(String fileName) {
        String file = fileName;
        String delimiter = ",";
        String line;
        List<List<String>> lines = new ArrayList();
        try (BufferedReader br =
                     new BufferedReader(new FileReader(file))) {
            while((line = br.readLine()) != null){
                List<String> values = Arrays.asList(line.split(delimiter));
                lines.add(values);
            }
        } catch (Exception e){
            System.out.println("File reading error!");
        }
        return lines;
    }

    private static void function_five() {
        try {
            System.out.println("Part 1:");
            List<List<String>> reviewCSV = readCSV("./data/order_reviews.csv");
            List<List<String>> itemCSV = readCSV("./data/order_items.csv");
            HashMap<String, String> orderToSellerMap = new HashMap<>();
            for (int i = 1; i < itemCSV.size(); i ++) {
                String orderID = itemCSV.get(i).get(0);
                String sellerID = itemCSV.get(i).get(3);
                orderToSellerMap.put(orderID, sellerID);
            }
            HashMap<String, Double> reviewToScoreMap = new HashMap<>();
            for (int i = 1; i < reviewCSV.size(); i ++) {
                try {
                    String orderID = reviewCSV.get(i).get(1);
                    String score = reviewCSV.get(i).get(2);
                    if (reviewToScoreMap.get(orderID) == null) {
                        reviewToScoreMap.put(orderID, Double.parseDouble(score));
                    } else {
                        reviewToScoreMap.put(orderID, (reviewToScoreMap.get(orderID) + Double.parseDouble(score)) / 2);
                    }
                } catch (Exception e) {
                    continue;
                }
            }
            ArrayList<Map.Entry<String, Double>> sorted = new ArrayList<>(reviewToScoreMap.entrySet());
            sorted.sort(Comparator.comparingDouble(Map.Entry::getValue));
            for (int i = 0; i < 10; i++) {
                System.out.print((i + 1) + ": ");
                System.out.println(orderToSellerMap.get(sorted.get(i).getKey()));
            }
            System.out.println("--- ---");
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            System.out.println("Part 2:");
            List<List<String>> productCSV = readCSV("./data/products.csv");
            List<List<String>> productTranslateCSV = readCSV("./data/product_category_name_translation.csv");
            List<List<String>> itemCSV = readCSV("./data/order_items.csv");
            HashMap<String, String> productToName = new HashMap<>();
            for (int i = 1; i < productTranslateCSV.size(); i ++) {
                String product = productTranslateCSV.get(i).get(0);
                String translated = productTranslateCSV.get(i).get(1);
                productToName.put(product, translated);
            }
            HashMap<String, Double> productToFrequencyMap = new HashMap<>();
            for (int i = 1; i < itemCSV.size(); i ++) {
                try {
                    String productID = itemCSV.get(i).get(2);
                    if (productToFrequencyMap.get(productID) == null) {
                        productToFrequencyMap.put(productID, 1.0);
                    } else {
                        productToFrequencyMap.put(productID, productToFrequencyMap.get(productID) + 1.0);
                    }
                } catch (Exception e) {
                    continue;
                }
            }

            HashMap<String, String> productIDToProductMap = new HashMap<>();
            for (int i = 1; i < productCSV.size(); i ++) {
                try {
                    String productID = productCSV.get(i).get(0);
                    String productName = productCSV.get(i).get(1);
                    productIDToProductMap.put(productID, productName);
                } catch (Exception e) {
                    continue;
                }
            }

            ArrayList<Map.Entry<String, Double>> sorted = new ArrayList<>(productToFrequencyMap.entrySet());
            sorted.sort(Comparator.comparingDouble(Map.Entry::getValue));
            for (int i = 0; i < 10; i++) {
                System.out.print((i + 1) + ": ");
                String productID = sorted.get(i).getKey();
                System.out.println(productIDToProductMap.get(productID) + ": " + productToName.get(productIDToProductMap.get(productID)));
            }
            System.out.println("--- ---");
        } catch (Exception e) {
            System.out.println(e);
        }

    }
    // -------------- code for function_five ends ----------------------//

    // -------------- code for function_four ----------------------------//
    private static void function_four() {

        var client = HttpClient.newHttpClient();
        var httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://dev.beepbeep.tech/v1/sample_customer"))
                .GET()
                .build();
        try {
            var response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            Customer obj = new Gson().fromJson(response.body(), Customer.class);
            ArrayList<Promotion> promotions = obj.promotions;
            promotions.sort((p1, p2) -> p2.title.compareTo(p1.title));
            for (Promotion p : promotions) {
                if (p.type.equals("discount")) {
                    System.out.println("Title: " + p.title + "\nQuantity: " + p.quantity + "\nType: discount" + "\nDiscount: " + p.discount);
                } else {
                    System.out.println("Title: " + p.title + "\nQuantity: " + p.quantity + "\nType: redeem");
                }
                System.out.println("---- ----");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    // -------------- code for function_four ends ----------------------//

    // -------------- code for function_three ----------------------------//
    private static void function_three() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Give me a text input:");
        HashMap<String, List<String>> tokenSentenceListMap = new HashMap<>();
        HashMap<String, Integer> tokenFrequencyMap = new HashMap<>();
        String textInput = sc.nextLine();
        String[] sentences = textInput.split("\\.");
        for (String sentence : sentences) {
            String[] tokens = sentence.trim().split(" ");
            for (String token : tokens) {
                if (tokenFrequencyMap.get(token) == null) {
                    tokenFrequencyMap.put(token, 0);
                } else {
                    tokenFrequencyMap.put(token, tokenFrequencyMap.get(token) + 1);
                }
                if (tokenSentenceListMap.get(token) == null) {
                    List<String> sentenceList = new ArrayList<>();
                    sentenceList.add(sentence);
                    tokenSentenceListMap.put(token, sentenceList);
                } else {
                    List<String> sentenceList = tokenSentenceListMap.get(token);
                    sentenceList.add(sentence);
                    tokenSentenceListMap.put(token, sentenceList);
                }
            }
        }

        // data processing
        ArrayList<Map.Entry<String, Integer>> sorted = new ArrayList<>(tokenFrequencyMap.entrySet());
        sorted.sort(Collections.reverseOrder(Comparator.comparingInt(Map.Entry::getValue)));

        int maxTokens = tokenFrequencyMap.keySet().size();
        if (maxTokens > 10) {
            maxTokens = 10;
        }
        for (int i = 0; i < maxTokens; i++) {
            System.out.println("Index " + (i + 1) + ":");
            System.out.println("Keyword: " + sorted.get(i).getKey());
            System.out.println("Frequency: " + sorted.get(i).getValue());
            System.out.println("first_time: " + tokenSentenceListMap.get(sorted.get(i).getKey()).get(0));
            System.out.println("last_time: " + tokenSentenceListMap.get(sorted.get(i).getKey()).get(tokenSentenceListMap.get(sorted.get(i).getKey()).size() - 1));
            System.out.println("--- ---");
        }

    }
    // -------------- code for function_three ends ----------------------//

    // -------------- code for function_two -----------------------------//
    public static boolean isPrime(int num) {
        if (num <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    private static void printPalPrimeforAB(int A, int B) {
        boolean flag = false;
        for (int i = A; i <= B; i++) {
            if (isPrime(i) && isPalin(i)) {
                System.out.print(i + " ");
                flag = true;
            }
            String s = Integer.toString(i);
            for (int j = 0; j < s.length() - 1; j++) {
                StringBuilder sb = new StringBuilder(s);
                sb.deleteCharAt(j);
                String resultString = sb.toString();
                int resultNum = Integer.parseInt(resultString);
                if (isPrime(resultNum) && isPalin(resultNum)) {
                    System.out.print(i + " ");
                    flag = true;
                }
            }
        }
        if (flag) {
            System.out.println();
        }
    }

    private static void function_two() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Second function selected: Finding palindromic primes between two integers A and B.");
        System.out.println("Please key in two positive integer number, separated by a space");
        try {
            String inputLine = sc.nextLine();
            if (inputLine.split(" ").length != 2) {
                System.out.println("Please give me exactly 2 integer number separated by a space, exiting function...");
                return;
            }
            
            String[] inputStrings = inputLine.split(" ");
            int A = Integer.parseInt(inputStrings[0]);
            int B = Integer.parseInt(inputStrings[1]);
            printPalPrimeforAB(A, B);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("The values provided are too small or too big! Exiting function...");
        }  
    }
    // -------------- code for function_two ends -------------------------//

    // -------------- code for function_one -----------------------------//
    private static boolean isPalin(int x) {
        String s = Integer.toString(x);
        for(int i = 0; i < s.length()/2; i++) {
            if(s.charAt(i) != s.charAt(s.length()-i-1)) {
                return false;
            }
        }
        return true;
    }

    private static void printPalPrime(int n) {
        boolean prime[] = new boolean[n + 1];
        Arrays.fill(prime, true);
        for (int p = 2; p * p <= n; p++) {
            if (prime[p]) {
                for (int i = p*2; i <= n; i += p) {
                    prime[i] = false;
                }
            }
        }

        for (int p = 2; p <=n; p++) {
            if (prime[p] && isPalin(p)) {
                System.out.print(p + " ");
            }
        }
        System.out.println();
    }

    private static void function_one() {
        Scanner sc = new Scanner(System.in);
        System.out.println("First function selected: Finding palindromic primes.");
        System.out.println("Please key in a positive integer number");
        try {
            int inputNumber = sc.nextInt();
            if (inputNumber < 1) {
                System.out.println("Non-positive integer found, exiting function...");
                return;
            }
            printPalPrime(inputNumber);
        } catch (Exception e) {
            System.out.println("The value provided is too small or too big! Exiting function...");
        }
    }
    // -------------- code for function_one ends -------------------------//

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please key in a number between 1 to 5 (inclusive):");
        while(sc.hasNext()) {
            String userInput = sc.nextLine();
            if (userInput.split(" ").length > 1) {
                System.out.println("We only accept a single digit number!");
                System.out.println("Please key in a number between 1 to 5 (inclusive):");
                continue;
            }
            
            try {
                int userInputNumber = Integer.parseInt(userInput);
                switch(userInputNumber) {
                    case 1:
                        function_one();
                        break;
                    case 2:
                        function_two();
                        break;
                    case 3:
                        function_three();
                        break;
                    case 4:
                        function_four();
                        break;
                    case 5:
                        function_five();
                        break;
                    default:
                        throw new Exception();
                }
            } catch (Exception e) {
                System.out.println("The number you key in is not 1 to 5 inclusive!");
            }

            System.out.println("Please key in a number between 1 to 5 (inclusive):");
        }
    }
}

// classes for JSON parsing
class Customer {
    String name;
    String email;
    ArrayList<Promotion> promotions;
}

class Promotion {
    String title;
    int quantity;
    String type;
    double discount;
}