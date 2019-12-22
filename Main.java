import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.sql.SQLOutput;
import java.util.*;


public class Main {
    public static void main(String[] args) {
        HashMap<Integer,String> hm = new HashMap<Integer,String>() ;
        HashMap<String , Integer> Enum = new HashMap<String,Integer>() ;
        String file = "/Users/anunay/Desktop/Python/couese_3/fruits.csv";
        String delimiter = "\t";
        String line;
        ArrayList<Fruits> fruits = new ArrayList<>();
        ArrayList<Float> m = new ArrayList<>() ;
        ArrayList<Float> w = new ArrayList<>() ;
        ArrayList<Float> h = new ArrayList<>() ;
        ArrayList<Float> c = new ArrayList<>() ;
        int countdiff = 1;
        int counter = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((line = br.readLine()) != null)
            {
                List<String> values = Arrays.asList(line.split(delimiter));
                if (counter == 0) {
                    counter++;
                    continue;
                }
                else
                {
                    if (values.get(2).equals("unknown"))
                    {
                        values.set(2,hm.get(values.get(0)));
                    }
                    Fruits s = new Fruits();
                    // Can be avoided by using a constructor
                    s.fruit_label = Integer.valueOf(values.get(0));
                    s.fruit_name = values.get(1);
                    if(!hm.containsKey(values.get(1)))
                    {
                        hm.put(s.fruit_label,values.get(1)) ;
                    }
                    s.mass = Float.valueOf(values.get(3));
                    m.add(s.mass) ;
                    s.width = Float.valueOf(values.get(4));
                    w.add(s.width) ;
                    s.height = Float.valueOf(values.get(5));
                    h.add(s.height);
                    s.color_score = Float.valueOf(values.get(6));
                    c.add(s.color_score) ;
                    s.sub1 = values.get(2) ;
                    if(!Enum.containsKey(s.sub1))
                    {
                        Enum.put(s.sub1,countdiff) ;
                        countdiff++ ;
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
        Collections.shuffle(fruits,new Random(2));
        ArrayList<Fruits> fruits_train = new ArrayList<>();
        ArrayList<Fruits> fruits_test = new ArrayList<>();
        //scaling
        for (int i = 0; i < fruits.size(); i++) {
            Fruits f = fruits.get(i) ;
            //f.labeled = Enum.get(f.sub1) ;
            f.mass = (f.mass - minm)/(maxm - minm) ;
            f.width = (f.width - minw)/(maxw - minw) ;
            f.height = (f.height - minh)/(maxh - minh) ;
            f.color_score = (f.color_score - minc)/(maxc- minc) ;
            fruits.set(i,f) ;

            if (i < .8 * fruits.size()) fruits_train.add(fruits.get(i));
            else fruits_test.add(fruits.get(i));
        }
        //Accuracy confusion matrix etc ;
        int[][] confusion_matrix = confusion(fruits_test,fruits_train,Enum) ;
        for(int i = 0 ; i < hm.size() ; i++){
            for(int j = 0 ; j < hm.size() ; j++){
                System.out.print(confusion_matrix[i][j]+" ");
            }
            System.out.print("\n");
        }
        Fruits o  = new Fruits() ;
        o.mass =  (191.0f - minm)/(maxm-minm) ;
        o.width = (8.3f-minw)/(maxw-minw)  ;
        o.height = (7.2f-minh)/(maxh - minh)  ;
        o.color_score = (.54f-minc)/(maxc-minc);
        o.sub1 = "granny_smith";
        o.fruit_label = 3;
        o.fruit_name = "orange" ;
        String j = predict(fruits_train,o,4,hm,Enum) ;
        System.out.println(j);

    }
    public static int KNN(ArrayList <Fruits> fruits_train ,int k,Fruits f,HashMap<String,Integer> Enum){
        ArrayList<Float> f1 = getfeature(f,Enum) ;
        for(int i = 0 ; i < fruits_train.size() ; i++){
            ArrayList<Float> f2 = getfeature(fruits_train.get(i),Enum);
            float dis = distance(f1,f2) ;
            Fruits x = fruits_train.get(i) ;
            x.distance = dis ;
            fruits_train.set(i,x) ;
            //System.out.println(fruits_train.get(i).distance + "  " +fruits_train.get(i).fruit_name );
        }
        Collections.sort(fruits_train,Fruits.disscomparator);
        ArrayList<Integer> fruits_final = new ArrayList<>() ;
        for(int i = 0; i < k ; i++)
        {
            fruits_final.add(fruits_train.get(i).fruit_label) ;
        }
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
    public static float distance(ArrayList <Float> f1 , ArrayList <Float> f2)
    {
        float sum = 0f ;
        for(int i = 0 ; i < f1.size();i++){
            sum += Math.pow((f1.get(i) - f2.get(i)),2) ;
        }
        return sum ;
    }
    public static int [][] confusion(ArrayList <Fruits> fruits_test,ArrayList <Fruits> fruits_train,HashMap<String,Integer> Enum){
        int[][] confusion_matrix = new int[4][4];
        for(int j = 0 ; j < fruits_test.size() ; j++){
            int expected_res = fruits_test.get(j).fruit_label ;
            int result = KNN(fruits_train,4,fruits_test.get(j),Enum);
            confusion_matrix[expected_res-1][result-1]++ ;
        }
        return confusion_matrix ;
    }
    public static String predict(ArrayList <Fruits> fruits_train ,Fruits f , int k,HashMap<Integer,String> hm,HashMap<String,Integer> Enum){
        int res = KNN(fruits_train,k,f,Enum) ;
        String val = hm.get(res) ;
        return val ;
    }
    public static ArrayList<Float> getfeature(Fruits s,HashMap<String,Integer> Enum){
        ArrayList<Float> q  = new ArrayList<>() ;
        q.add(s.mass) ;
        q.add(s.width) ;
        q.add(s.height) ;
        q.add(s.color_score) ;
        int k  = Enum.get(s.sub1)-1 ;
        for(int i = 0 ; i < Enum.size() ; i++){
            if(i == k){
                q.add(1f) ;
            }
            else{
                q.add(0f) ;
            }

        }
        return q ;
    }
}







