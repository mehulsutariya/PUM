package pl.polsl.pum2.shoppingapp.helpers;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class DoubleParser {
    public static double parse(String numberString) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        if (numberString.length() > 0) {
            try {
                return numberFormat.parse(numberString).doubleValue();
            } catch (ParseException e) {
                numberFormat = NumberFormat.getNumberInstance(Locale.US);
                try {
                    return numberFormat.parse(numberString).doubleValue();
                } catch (ParseException e2) {
                    return 0.0;
                }
            }
        } else {
            return 0.0;
        }
    }
}
