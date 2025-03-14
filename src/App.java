import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Scanner;

public class App {
    static final int PASS_MIN_LENGTH = 8;
    static final int PASS_MAX_LENGTH = 16;

    public static void main(String[] args) throws Exception {
        Scanner s = new Scanner(System.in);
        while (true) {
            System.out.print("Enter a password or 'exit'> ");
            String input = s.nextLine();

            if (input.equals("exit")) {
                s.close();
                break;
            } else {
                System.out.println();
                checkPassword(input);
                System.out.println();
            }
        }
    }

    // easy to translate!!!
    static String[] errors = {
        "no input/other error",
        "password must be at least 8 characters long",
        "password cannot be over 16 characters long",
        "does not contain uppercase",
        "does not contain lowercase",
        "does not contain numbers",
        "does not contain a special symbol",
    };

    // Checks a password. Prints when OK, and prints what's wrong if not.
    static void checkPassword(String pass) {
        int flags = passwordValidator(pass);
        if (flags == 0) {
            System.out.println("Password is OK!");
        } else {
            System.out.println("Please fix the following issues with your password:");

            // Loop though bits until out of bits
            for (int i = 0; flags != 0; i++) {
                // If bit is set, print corresponding error message.
                if ((flags & 1) == 1) {
                    System.out.println(errors[i-1]);
                }
                flags >>= 1;
            }   
        }
    }


    // Returns 0 if password is valid
    // error bitflags:
    // 2: no input/other error
    // 4: too short
    // 8: too long
    //   The following will only be set if >1 would be set
    // 16: does not contain uppercase
    // 32: does not contain lowercase
    // 64: does not contain numbers
    // 128: does not contain a special symbol
    static int passwordValidator(String pass) {
        int errFlags = 0;

        if (pass.equals("") || pass == null)
            errFlags |= 2;
            
        if (pass.length() < PASS_MIN_LENGTH)
            errFlags |= 4;
        else if (pass.length() > PASS_MAX_LENGTH) 
            errFlags |= 8;

        //// 3/4 of these must be true for a valid password ////
        boolean hasUppercase = containsRegex(pass, "\\p{Lu}");
        boolean hasLowercase = containsRegex(pass, "\\p{Ll}");
        boolean hasNumbers = containsRegex(pass, "\\d+");
        boolean hasSpecial = containsRegex(pass, "[~!@#$%^&*()\\-=+_]");

        // bitflags for these semi-optional requirements
        int req = 0;

        if (!hasUppercase) 
            req |= 16;
        if (!hasLowercase)
            req |= 32;
        if (!hasNumbers)
            req |= 64;
        if (!hasSpecial)
            req |= 128;

        // if MORE THAN ONE requirement not met, pass along errors to "main" errors
        if (Integer.bitCount(req) > 1) {
            errFlags |= req;
        }

        return errFlags;
    }

    // Returns true if regex string `reg` is matched in `s`
    static boolean containsRegex(String s, String reg) {
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(s);
        
        return matcher.find();
    }
}