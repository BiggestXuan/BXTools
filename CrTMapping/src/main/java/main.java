import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class main {
    private static final List<File> list = new ArrayList<>();
    private static final List<String> name = new ArrayList<>();
    private static final List<String> mapping = new ArrayList<>();
    private static final List<String> mapping_log = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        getAllFiles("scripts");
        mapping_log.add("------------File Mapping------------");
        for(File f : list){
            String randomName = getRandomName(7,false);
            FileUtils.copyFile(f,new File("sout/"+randomName+".zs"));
            mapping_log.add(f.getPath()+" -> "+randomName+".zs");
        }
        genMapping();
        replace();
        replaceBlank();
        writeMappingLog();
    }

    public static void getAllFiles(String clientBase){
        File file = new File(clientBase);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File value : files) {
                if (value.isDirectory()) {
                    getAllFiles(value.getPath());
                } else {
                    list.add(value);
                }
            }
        } else {
            list.add(file);
        }
    }

    public static String getRandomName(int max,boolean num){
        char[] cha ={'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
        int len = Math.max(1,(int) (Math.random() * max));
        char[] c = new char[len];
        for (int i = 0; i < len; i++) {
            int index = (int) (Math.random() * cha.length);
            if(num && Math.random() >= 0.5){
                int n = (int) Math.round(Math.random()*10);
                String s = String.valueOf(n);
                char[] cs = s.toCharArray();
                c[i] = cs[0];
            }else{
                c[i] = cha[index];
            }
        }
        String n = getCharName(c);
        if(!name.contains(n)){
            name.add(n);
            return n;
        }
        return getRandomName(max,num);
    }

    private static String getCharName(char[] c){
        StringBuilder builder = new StringBuilder();
        for(char cc : c){
            builder.append(cc);
        }
        return builder.toString().toLowerCase(Locale.ROOT);
    }

    private static List<String> getAllFunction() throws IOException {
        List<String> l = new ArrayList<>();
        for(File f : list){
            for(String s : Files.readAllLines(f.toPath())){
                if(s.contains("public function")){
                    l.add(s.split(" ")[2].split("\\(")[0]);
                }
            }
        }
        return l;
    }

    private static String getFunctionMappingName(){
        String s = "func_";
        s += getRandomName(7,true);
        s += "_";
        s += getRandomName(9,true);
        s += "_";
        s += getRandomName(12,true);
        if(!mapping.contains(s)){
            mapping.add(s);
            return s;
        }
        return getFunctionMappingName();
    }

    private static void genMapping() throws IOException {
        File file = new File("mapping.txt");
        if(!file.exists()){
            file.createNewFile();
        }
        FileWriter writer = new FileWriter(file);
        mapping_log.add("------------Function Mapping------------");
        for(String s : getAllFunction()){
            String name = getFunctionMappingName();
            writer.write(s+";"+name+"\n");
            mapping_log.add(s+" -> "+name);
        }
        writer.flush();
        writer.close();
    }

    private static void writeMappingLog() throws IOException{
        File file = new File("mapping_log.txt");
        if(!file.exists()){
            file.createNewFile();
        }
        FileWriter writer = new FileWriter(file);
        for(String s : mapping_log){
            writer.write(s+"\n");
        }
        writer.flush();
        writer.close();
    }

    @Nullable
    public static String getFunctionMapping(String function) throws IOException {
        File file = new File("mapping.txt");
        for(String s : Files.readAllLines(file.toPath())){
            if(s.contains(function)){
                return s.split(";")[1];
            }
        }
        return null;
    }

    private static void replace() throws IOException {
        list.clear();
        getAllFiles("sout");
        for(File f : list){
            for(String s : Files.readAllLines(new File("mapping.txt").toPath())){
                if(fileContainer(f,s.split(";")[0]+"(")){
                    autoReplaceStr(f,s.split(";")[0]+"(",s.split(";")[1]+"(");
                }
            }
        }
    }

    private static void replaceBlank() throws IOException {
        list.clear();
        getAllFiles("sout");
        for(File f : list){
            autoReplaceStr(f,"\r","");
        }
    }

    private static boolean fileContainer(File file,String s) throws IOException {
        for(String ss : Files.readAllLines(file.toPath())){
            if(ss.contains(s)){
                return true;
            }
        }
        return false;
    }

    public static void autoReplaceStr(File file, String oldstr, String newStr) throws IOException {
        Long fileLength = file.length();
        byte[] fileContext = new byte[fileLength.intValue()];
        FileInputStream in = null;
        PrintWriter out = null;
        in = new FileInputStream(file.getPath());
        in.read(fileContext);
        String str = new String(fileContext, "utf-8");//字节转换成字符
        str = str.replace(oldstr, newStr);
        out = new PrintWriter(file.getPath(), "utf-8");//写入文件时的charset
        out.write(str);
        out.flush();
        out.close();
        in.close();
    }
}
