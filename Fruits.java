import java.util.Comparator;

public class Fruits
{
    public int fruit_label;
    public String fruit_name = "Unknown";
    public String fruit_subtype ;
    public Float mass ;
    public Float width ;
    public Float height;
    public Float color_score ;
    public int granny_smith =  0 ;
    public int mandarin = 0  ;
    public int braeburn = 0 ;
    public int golden_delicious = 0 ;
    public int cripps_pink = 0 ;
    public int spanish_jumbo = 0 ;
    public int selected_seconds = 0 ;
    public int turkey_navel = 0 ;
    public int spanish_belsan = 0 ;
    public Float distance = Float.MAX_VALUE ;

    public void display()
    {
        System.out.println("Fruit name is " + fruit_name);
        System.out.println("Fruit label is " + fruit_label);
        System.out.println("Fruit subtype is " + fruit_subtype);
        System.out.println("Fruit mass is " + mass);
        System.out.println("Fruit width is " + width);
        System.out.println("Fruit height is " + height);
        System.out.println("Fruit color_score is " + color_score);
        System.out.println("granny smith is " + granny_smith);
        System.out.println("mandrin is " + mandarin);
        System.out.println("braeburn is" + braeburn);
        System.out.println("golden_delicious is " + golden_delicious);
        System.out.println("cripps_pink is " + cripps_pink);
        System.out.println("spanish_jumbo is " + spanish_jumbo);
        System.out.println("selected_seconds is " + selected_seconds);
        System.out.println("turkey_navel is "+ turkey_navel);
        System.out.println("spanish_belsan is " + spanish_belsan);
        System.out.println(distance);
    }
    public static Comparator<Fruits> disscomparator = new Comparator<Fruits>() {
        @Override
        public int compare(Fruits f, Fruits g) {
            return f.distance.compareTo(g.distance);
        }
    };
}
