import java.util.Comparator;

public class Fruits
{
    public int fruit_label;
    public String fruit_name = "Unknown";
    public Float mass ;
    public Float width ;
    public Float height;
    public Float color_score ;
    subtype sub ;
    //public int encode = sub.getvalue() ;

    enum subtype{
        GRANNY_SMITH(1) , MANDARIN(2) ,BRAEBURN(3) , GOLDEN_DELICIOUS(4) ,CRIPPS_PINK(5),SPANISH_JUMBO(6),SELECTED_SECONDS(7),
        TURKEY_NAVEL(8),SPANISH_BELSON(9) ;
        private int i  ;
        subtype(int i) {
            this.i = i ;
        }
        public int getvalue(){
            return this.i ;
        }
    }
    public Float distance = Float.MAX_VALUE ;

    public void display()
    {
        System.out.println("Fruit name is " + fruit_name);
        System.out.println("Fruit label is " + fruit_label);
        System.out.println("Fruit subtype is " + sub);
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

