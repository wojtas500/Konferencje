package GUI;

import java.util.regex.Pattern;

class RegexContainer
{
    static final Pattern emailRegEx = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
    static final Pattern loginRegEx = Pattern.compile("[0-9a-zA-Z]{8,25}");
    static final Pattern passwordRegEx = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,25}$");
    static final Pattern nameRegEx = Pattern.compile("[A-ZĄĆĘŁŃÓŚŹŻ][a-ząćęłńóśźż]{2,100}");
    static final Pattern peselRegEx = Pattern.compile("[0-9]{11}");
    static final Pattern postCodeRegEx = Pattern.compile("[0-9]{2}-[0-9]{3}");
    static final Pattern townRegEx = Pattern.compile("[A-ZĄĆĘŁŃÓŚŹŻ][a-ząćęłńóśźż]+(?:[ '-][A-Z][a-z]+)*");
    static final Pattern streetAdressRegEx = Pattern.compile("[A-ZZĄĆĘŁŃÓŚŹŻa-ząćęłńóśźż0-9]+(?:[ '-][A-ZZĄĆĘŁŃÓŚŹŻa-ząćęłńóśźż0-9]+)*(\\s)+[0-9]{1,3}([/][0-9]{1,3})?");
}
