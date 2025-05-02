package ucr.lab.domain;



import java.util.Objects;

public class Person {
    private String name;
    private String mood;
    private int attention;
    private int priority;

    public Person(String name, String mood, int time, int priority) {
        this.name = name;
        this.mood = mood;
        this.attention = time;
        this.priority = priority;
    }

    // Getters y Setters



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public int getAttention() {
        return attention;
    }

    public String setAttention(int attention) {
        this.attention = attention;
        return null;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return name.equals(person.name) && mood.equals(person.mood); // Compara nombre y estado de ánimo
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, mood); // un hash basado en nombre y estado de ánimo
    }


    @Override
    public String toString() {
        return "Name: " + name + ", Mood: " + mood + ", Priority: " + priority;
    }
}
