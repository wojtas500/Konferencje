package GUI;

import java.util.ArrayList;

public class CurrentUser
{
    private static ArrayList<String> Values;

    // Getters

    public static String getID()
    {
        return Values.get(0);
    }

    public static String getLogin()
    {
        return Values.get(1);
    }

    public static String getPasswordHash()
    {
        return Values.get(2);
    }

    public static String getName()
    {
        return Values.get(3);
    }

    public static String getSurname()
    {
        return Values.get(4);
    }

    public static String getEmail()
    {
        return Values.get(5);
    }

    public static String getPesel()
    {
        return Values.get(6);
    }

    public static String getStreetAdress()
    {
        return Values.get(7);
    }

    public static  String getPostCode()
    {
        return Values.get(8);
    }

    public static String getTown()
    {
        return Values.get(9);
    }

    public static ArrayList<String> getValues()
    {
        return Values;
    }

    // Setters

    public static void setLogin(String Login)
    {
        Values.set(1, Login);
    }

    public static void setPasswordHash(String PasswordHash)
    {
        Values.set(2, PasswordHash);
    }

    public static void setName(String Name)
    {
        Values.set(3, Name);
    }

    public static void setSurname(String Surname)
    {
        Values.set(4, Surname);
    }

    public static void setEmail(String Email)
    {
        Values.set(5, Email);
    }

    public static void setPesel(String Pesel)
    {
        Values.set(6, Pesel);
    }

    public static void setStreetAdress(String StreetAdress)
    {
        Values.set(7, StreetAdress);
    }

    public static void setPostCode(String PostCode)
    {
        Values.set(8, PostCode);
    }

    public static void setTown(String Town)
    {
        Values.set(9, Town);
    }

    public static void setValues(ArrayList<String> values)
    {
        Values = values;
    }
}
