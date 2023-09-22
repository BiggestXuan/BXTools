import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 *  @Author Biggest_Xuan
 *  2023/05/04
 */

public class Main {
    public static String num = "2023077";
    public static int[][] ticket = new int[][]{
            {4,8,15,23,26,3,7},
            {6,11,14,16,18,5,8},
            {14,18,24,27,33,1,4},
            {1,12,20,26,32,3,9},
            {5,7,25,29,34,6,12}
            /*{4,12,21,26,30,5,7},
            {1,5,14,19,22,2,9},
            {6,10,16,27,34,8,11},
            {7,8,11,23,31,3,6},
            {3,9,17,25,33,4,10},
            {3,11,14,22,29,3,8},
            {1,12,20,26,33,7,12},
            {5,7,12,25,30,9,11},
            {9,16,24,27,31,5,9},
            {8,15,19,24,32,1,4}*/
    };

    public static void main(String[] args) throws Exception{
        test();
        //System.out.println("Money: "+Utils.getTotalMoney());
    }

    public static void test() throws Exception{
        URL url = new URL("http://datachart.500.com/pls/history/inc/history.php");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        InputStream is = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        String s = reader.readLine();
        System.out.println(s);
        String line;
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("test.txt"), StandardCharsets.UTF_8));
        while ((line = reader.readLine()) != null){
            if(line.contains("<td class=\"cfont2\">")){
                int index = 0;
                while (true){
                    index = line.indexOf("<td class=\"cfont2\">",index);
                    if(index == -1){
                        break;
                    }
                    index += "<td class=\"cfont2\">".length();
                    String ss = line.substring(index,index+5);
                    bw.write(ss);
                    bw.newLine();
                    bw.flush();
                }
            }
            /*bw.write(line);
            bw.newLine();
            bw.flush();*/
        }
        //关闭资源
        bw.close();
        reader.close();
        is.close();
    }
}
