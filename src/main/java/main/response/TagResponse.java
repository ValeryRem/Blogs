package main.response;

public class TagResponse {
    private final String name;
    private final double weight;

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public TagResponse(String name, double weight) {
        this.name = name;
        this.weight = weight;
    }
}