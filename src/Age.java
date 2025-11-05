public enum Age {
    CHILD("Детская"),
    ADULT("Взрослая");

    private final String russianName;

    Age(String russianName) {
        this.russianName = russianName;
    }

    public String getRussianName() {
        return russianName;
    }

    @Override
    public String toString() {
        return russianName;
    }
}
