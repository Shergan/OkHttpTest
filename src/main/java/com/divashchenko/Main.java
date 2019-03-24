package com.divashchenko;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {
    private static final String URL = "https://api.privatbank.ua/p24api/exchange_rates?json&date=";

    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient();

        String date = getDate();

        Request request = new Request.Builder()
                .url(getUrlInDate(date))
                .build();

        try {
            Response response = client.newCall(request).execute();

            String answer = getUsd(response.body().string());

            if (answer == null) {
                System.out.println("Invalid date!");
            } else {
                System.out.println(answer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getDate() {
        System.out.println("Enter your date (dd.mm.yyyy): ");
        Scanner scanner = new Scanner(System.in);
        String dateFromConsole = scanner.nextLine();

        try {
            Date date = new SimpleDateFormat("dd.mm.yyyy").parse(dateFromConsole);
            return dateFromConsole;
        } catch (Exception e) {
            System.out.println("Invalid date format!");
            return "00.00.0000";
        }
    }

    private static String getUrlInDate(String date) {
        StringBuilder sb = new StringBuilder(URL);
        sb.append(date);
        return sb.toString();
    }

    private static String getUsd(String response) {

        if (response.contains("404 Not Found")) {
            return "Page not found";
        }

        int start = response.indexOf("baseCurrency\":\"UAH\",\"currency\":\"USD\"");
        int finish = response.indexOf("}", start);

        if (start == -1) {
            return null;
        }

        return response.substring(start, finish);
    }
}
