package pl.shopgen.validator;

public class RegexPattern {
    //    Minimum eight characters, at least one letter and one number
    public static String PASSWORD = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
}
