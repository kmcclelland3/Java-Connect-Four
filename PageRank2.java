// Name: Kristen McClelland
// GTID: 902944956
// "I worked on the homework assignment alone, using only course materials."
/* Notes after working through issues
Had to make sure that use of \n was correct
When accepting a scanner input that does not include the new line character
    must use another nextLine() call to clear that new line character and
    accept a new input
Used string array but not sure if another method could be used (while loop)
When adding to a double, make sure that any math calculations product doubles
    not int
Use trim() to remove whitespace on peripherals of a String
Use %.2f to round a double inside a printf statement
*/
import java.util.Scanner;

public class PageRank2 {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter a page name:");
        String name = scan.nextLine();

        System.out.println("\nHow many outbound links does " + name + " have?");
        int n = scan.nextInt();

        System.out.println("\nAvailable pages:");
        System.out.println("Wikipedia");
        System.out.println("Facebook");
        System.out.println("TechCrunch");
        System.out.println("Twitter");
        System.out.println("BlueApron");
        System.out.println("Instagram");
        System.out.println("Pinterest");
        System.out.println("Quora");
        System.out.println("GrubHub");
        System.out.println("Airbnb");

        System.out.println("\nWhich of the above pages does " + name
                + " link to?");
        scan.nextLine();
        String pages = scan.nextLine();

        double points = 0;
        if (pages.contains("Wikipedia")) {
            points += .4;
        }
        if (pages.contains("Facebook")) {
            points += .5;
        }
        if (pages.contains("TechCrunch")) {
            points += .5;
        }
        if (pages.contains("Twitter")) {
            points += (double) (4) / 6;
        }
        if (pages.contains("BlueApron")) {
            points += .25;
        }
        if (pages.contains("Instagram")) {
            points += 1;
        }
        if (pages.contains("Pinterest")) {
            points += (double) (4) / 7;
        }
        if (pages.contains("Quora")) {
            points += .6;
        }
        if (pages.contains("GrubHub")) {
            points += 1;
        }
        if (pages.contains("Airbnb")) {
            points += .5;
        }

        System.out.println("\nWhat damping factor would you like"
                + "to use?");
        double d = scan.nextDouble();

        // formula for page rank
        double pageRank = (1 - d) / n + d * points;

        // rounded to 2 decimal points
        System.out.printf("\nThe PageRank of " + name + " is: %.2f!", pageRank);
    }
}