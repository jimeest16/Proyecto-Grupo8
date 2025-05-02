package ucr.lab.domain;

public class Person {
    private String name;
    private String mood;
    private int attention;
    private String priority;



    public Person(String name, String mood, int time, String priority) {
        this.name = name;
        this.mood = mood;
        this.attention = time;
        this.priority = priority;
    }

    public Person(String name, String mood, int time, int priority) {
        this.name = name;
        this.mood = mood;
        this.attention = time;
        this.priority = String.valueOf(priority);
    }

    public Person(String name) {
        this.name = name;
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
        return "Tiempo asignado: " + attention + " minutos";
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Person person = (Person) obj;
        return name.equalsIgnoreCase(person.name) && mood.equalsIgnoreCase(person.mood);
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode() + mood.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Mood: " + mood + ", Priority: " + priority;
    }
}
