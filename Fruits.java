import java.util.Comparator;

public class Fruits
{
    public int fruit_label;
    public String fruit_name = "Unknown";
    public Float mass ;
    public Float width ;
    public Float height;
    public Float color_score ;
    String sub1 ;

    public Float distance = Float.MAX_VALUE ;

    public void display()
    {
        System.out.println("Fruit name is " + fruit_name);
        System.out.println("Fruit label is " + fruit_label);
        System.out.println("Fruit subtype is " + sub1);
        System.out.println("Fruit mass is " + mass);
        System.out.println("Fruit width is " + width);
        System.out.println("Fruit height is " + height);
        System.out.println("Fruit color_score is " + color_score);
        System.out.println(distance);
    }
    public static Comparator<Fruits> disscomparator = new Comparator<Fruits>() {
        @Override
        public int compare(Fruits f, Fruits g) {
            return f.distance.compareTo(g.distance);
        }
    };
}
