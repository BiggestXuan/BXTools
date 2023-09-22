import java.util.HashSet;

/**
 *  @Author Biggest_Xuan
 *  2023/05/04
 */

public record Lottery (HashSet<Integer> red,HashSet<Integer> blue){
    public int[] equals(Lottery lottery){
        int a = 0;
        int b = 0;
        for(Integer i: lottery.red){
            if(this.red.contains(i)){
                a++;
            }
        }
        for(Integer i : lottery.blue){
            if(this.blue.contains(i)){
                b++;
            }
        }
        return new int[]{a,b};
    }
}
