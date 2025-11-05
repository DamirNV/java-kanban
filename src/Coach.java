import java.util.Objects;

public class Coach {

    //фамилия
    private String surname;
    //имя
    private String name;
    //отчество
    private String middleName;

    public Coach(String surname, String name, String middleName) {
        this.surname = surname;
        this.name = name;
        this.middleName = middleName;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getMiddleName() {
        return middleName;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Coach coach)) return false;
        return Objects.equals(getSurname(), coach.getSurname()) && Objects.equals(getName(), coach.getName()) &&
                Objects.equals(getMiddleName(), coach.getMiddleName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSurname(), getName(), getMiddleName());
    }
}
