package com.example.Varosok;

import android.annotation.SuppressLint;

public class City {
    private int id;
    private String name;
    private String country;
    private int population;

    public City(int id, String name, String country, int population) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.population = population;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }


    public void setAgeText(String ageText) {
        if (ageText.equals("")) {
            this.population = 0;
        } else {
            this.population = Integer.parseInt(ageText);
        }
    }
    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        return String.format("nev: %s \norszag: %s \nlakossag: %d\n\n", this.name, this.country, this.population);
    }
}
