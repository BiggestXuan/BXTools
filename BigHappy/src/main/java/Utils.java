import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *  @Author Biggest_Xuan
 *  2023/05/04
 */

public class Utils {
    public static double getTotalMoney(){
        double m = 0;
        try{
            for(Lottery l : getYourLottery()){
                m += getMoney(getWinLevel(l));
            }
        }catch (Exception e){
            System.out.println("The lottery is not drawn!");
        }
        return m;
    }

    public static Lottery getLottery() throws Exception {
        URL url = new URL("https://www.cjcp.cn/ajax_kj.php?jsoncallback=jsonp1532326738107&dlt_qs="+Main.num);
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String s = bf.readLine();
        s = s.split("\\*\\*\\*")[1];
        String[] o = s.split("\\*");
        HashSet<Integer> red = new HashSet<>();
        HashSet<Integer> blue = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            red.add(Integer.valueOf(o[i]));
        }
        for (int i = 5; i < 7; i++) {
            blue.add(Integer.valueOf(o[i]));
        }
        return new Lottery(red,blue);
    }

    public static List<Lottery> getYourLottery(){
        List<Lottery> list = new ArrayList<>();
        for (int i = 0; i < Main.ticket.length; i++) {
            int[] tickets = Main.ticket[i];
            HashSet<Integer> red = new HashSet<>();
            HashSet<Integer> blue = new HashSet<>();
            for (int j = 0; j < 5; j++) {
                red.add(tickets[j]);
            }
            for (int j = 5; j < 7; j++) {
                blue.add(tickets[j]);
            }
            list.add(new Lottery(red,blue));
        }
        return list;
    }

    public static double getMoney(int level){
        return switch (level) {
            case 1, 2 -> throw new BiggestLotteryException();
            case 3 -> 10000;
            case 4 -> 3000;
            case 5 -> 300;
            case 6 -> 200;
            case 7 -> 100;
            case 8 -> 15;
            case 9 -> 5;
            default -> 0;
        };
    }

    public static int getWinLevel(Lottery yours) throws Exception{
        Lottery official = getLottery();
        int[] result = yours.equals(official);
        int a = result[0];
        int b = result[1];
        if(a == 5 && b == 2){
            return 1;
        }else if(a == 5 && b == 1){
            return 2;
        }else if(a == 5 && b == 0){
            return 3;
        }else if(a == 4 && b == 2){
            return 4;
        }else if(a == 4 && b == 1){
            return 5;
        }else if(a == 3 && b == 2){
            return 6;
        }else if(a == 4 && b == 0){
            return 7;
        }else if((a == 3 && b == 1) || (a == 2 && b == 2)){
            return 8;
        }
        else if((a == 3 && b == 0) || (a <= 1 && b == 2) || (a == 2 && b == 1)){
            return 9;
        }
        return 0;
    }
}
