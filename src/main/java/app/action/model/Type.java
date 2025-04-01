package app.action.model;

public enum Type {
    FED("Fed"), VACCINATED("Vaccinated"), PARASITE_PROTECTED("Parasite protected"), NEUTERED("Neutered"), ADOPTED("Adopted"), OTHER("Other");

    private final String name;

    Type(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
