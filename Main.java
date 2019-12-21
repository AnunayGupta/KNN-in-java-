import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;


public class Main {
    public static void main(String[] args) {
        HashMap<Integer,String> hm = new HashMap<Integer,String>() ;
        hm.put(1,"apple") ;
        hm.put(2,"mandarin") ;
        hm.put(3,"orange") ;
        hm.put(4,"lemon") ;
        String file = "/Users/anunay/Desktop/Python/couese_3/fruits.csv";
        String delimiter = "\t";
        String line;
        ArrayList<Fruits> fruits = new ArrayList<>();
        ArrayList<Float> m = new ArrayList<>() ;
        ArrayList<Float> w = new ArrayList<>() ;
        ArrayList<Float> h = new ArrayList<>() ;
        ArrayList<Float> c = new ArrayList<>() ;
        int counter = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((line = br.readLine()) != null) {
                List<String> values = Arrays.asList(line.split(delimiter));
                if (counter == 0) {
                    counter++;
                    continue;
                } else {
                    if (values.get(2).equals("unknown")) {
                        values.set(2, "spanish_belsan");
                    }
                    Fruits s = new Fruits();
                    // Can be avoided by using a constructor
                    s.fruit_label = Integer.valueOf(values.get(0));
                    s.fruit_name = values.get(1);
                    s.fruit_subtype = values.get(2);
                    s.mass = Float.valueOf(values.get(3));
                    m.add(s.mass) ;
                    s.width = Float.valueOf(values.get(4));
                    w.add(s.width) ;
                    s.height = Float.valueOf(values.get(5));
                    h.add(s.height);
                    s.color_score = Float.valueOf(values.get(6));
                    c.add(s.color_score) ;
                    switch (s.fruit_subtype) {
                        case "granny_smith":
                            s.granny_smith = 1;
                            break;
                        case "mandarin":
                            s.mandarin = 1;
                            break;
                        case "braeburn":
                            s.braeburn = 1;
                            break;
                        case "golden_delicious":
                            s.golden_delicious = 1;
                            break;
                        case "cripps_pink":
                            s.cripps_pink = 1;
                            break;
                        case "spanish_jumbo":
                            s.spanish_jumbo = 1;
                            break;
                        case "selected_seconds":
                            s.selected_seconds = 1;
                            break;
                        case "turkey_navel":
                            s.turkey_navel = 1;
                            break;
                        case "spanish_belsan":
                            s.spanish_belsan = 1;
                            break;
                    }
                    fruits.add(s);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        Float maxm = Collections.max(m) ;
        Float maxh = Collections.max(h) ;
        Float maxw = Collections.max(w) ;
        Float maxc = Collections.max(c) ;
        Float minm = Collections.min(m) ;
        Float minh = Collections.min(h) ;
        Float minw = Collections.min(w) ;
        Float minc = Collections.min(c) ;
        Collections.shuffle(fruits,new Random(0));
        ArrayList<Fruits> fruits_train = new ArrayList<>();
        ArrayList<Fruits> fruits_test = new ArrayList<>();
        //scaling
        for (int i = 0; i < fruits.size(); i++) {
            Fruits f = fruits.get(i) ;
            f.mass = (f.mass - minm)/(maxm - minm) ;
            f.width = (f.width - minw)/(maxw - minw) ;
            f.height = (f.height - minh)/(maxh - minh) ;
            f.color_score = (f.color_score - minc)/(maxc- minc) ;
            fruits.set(i,f) ;

            if (i < .8 * fruits.size()) fruits_train.add(fruits.get(i));
            else fruits_test.add(fruits.get(i));
        }
        //Accuracy confusion matrix etc ;
        int[][] confusion_matrix = confusion(fruits_test,fruits_train) ;
        for(int i = 0 ; i < 4 ; i++){
            for(int j = 0 ; j < 4 ; j++){
                System.out.print(confusion_matrix[i][j]+" ");
            }
            System.out.print("\n");
        }
        Fruits o  = new Fruits() ;
        o.mass =  (191.0f - minm)/(maxm-minm) ;
        o.width = (8.3f-minw)/(maxw-minw)  ;
        o.height = (7.2f-minh)/(maxh - minh)  ;
        o.color_score = (.54f-minc)/(maxc-minc);
        o.granny_smith = 1 ;
        o.fruit_label = 3;
        o.fruit_name = "orange" ;
        String j = predict(fruits_train,o,4,hm) ;
        System.out.println(j);

    }
    public static int KNN(ArrayList <Fruits> fruits_train ,int k,Fruits f){
        for(int i = 0 ; i < fruits_train.size();i++){
            Fruits a = fruits_train.get(i);
            float dis = distance(a,f) ;
            a.distance = dis ;
            fruits_train.set(i,a) ;
        }
        Collections.sort(fruits_train,Fruits.disscomparator);
        ArrayList<Integer> fruits_final = new ArrayList<>() ;
        for(int i = 0; i < k ; i++)
        {
            fruits_final.add(fruits_train.get(i).fruit_label) ;
        }
        HashMap<Integer,Integer> elementCountMap = new HashMap<Integer, Integer>();
        for (int i : fruits_final)
        {
            if (elementCountMap.containsKey(i))
            {
                //If an element is present, incrementing its count by 1
                elementCountMap.put(i, elementCountMap.get(i)+1);
            }
            else
            {
                //If an element is not present, put that element with 1 as its value
                elementCountMap.put(i, 1);
            }
        }
        int element = 0;
        int frequency = 1;
        Set<Map.Entry<Integer, Integer>> entrySet = elementCountMap.entrySet();

        for (Map.Entry<Integer, Integer> entry : entrySet)
        {
            if(entry.getValue() > frequency)
            {
                element = entry.getKey();

                frequency = entry.getValue();
            }
        }
        return element ;
    }
    public static float distance(Fruits f, Fruits g)
    {
        float sq = (float) Math.sqrt(Math.pow((f.mass - g.mass), 2) + Math.pow((f.height - g.height), 2) + Math.pow((f.width - g.width), 2) + Math.pow((f.color_score - g.color_score),2)
                + Math.pow((f.mandarin-g.mandarin),2) + Math.pow((f.granny_smith - g.granny_smith),2) + Math.pow((f.braeburn-g.braeburn),2) +Math.pow((f.golden_delicious-g.golden_delicious),2)
                +Math.pow((f.cripps_pink-g.cripps_pink),2)+Math.pow((f.spanish_jumbo-g.spanish_jumbo),2)+Math.pow((f.selected_seconds-g.selected_seconds),2)+Math.pow((f.turkey_navel-g.turkey_navel),2)
                +Math.pow((f.spanish_belsan-g.spanish_belsan),2));
        return sq;
    }
    public static int [][] confusion(ArrayList <Fruits> fruits_test,ArrayList <Fruits> fruits_train){
        int[][] confusion_matrix = new int[4][4];
        for(int j = 0 ; j < fruits_test.size() ; j++){
            int expected_res = fruits_test.get(j).fruit_label ;
            int result = KNN(fruits_train,4,fruits_test.get(j));
            confusion_matrix[expected_res-1][result-1]++ ;
        }
        return confusion_matrix ;
    }
    public static String predict(ArrayList <Fruits> fruits_train ,Fruits f , int k,HashMap<Integer,String> hm){
        int res = KNN(fruits_train,k,f) ;
        String val = hm.get(res) ;
        return val ;
    }
}





