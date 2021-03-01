/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package person;

/**
 *
 * @author Eduardo Montoya
 */
public class person {
    private String name;
    private double height;
    private double weight;
    private double bmi;
    private String meaning;

    public person(String name, double height, double weight) {
        this.name = name;
        this.height = height;
        this.weight = weight;
    }

    public person() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    @Override
    public String toString() {
        return "\t<person>\n"
                + "\t\t<name>"    + this.name +   "</name>\n"
                + "\t\t<height>"  + this.height + "</height>\n"
                + "\t\t<weight>"  + this.weight + "</weight>\n"
                + "\t\t<bmi>"     + this.bmi +    "</bmi>\n"
                + "\t\t<state>"   + this.meaning + "</state>\n"
                + "\t</person>";
    }
}
