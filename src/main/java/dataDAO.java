import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class dataDAO {
    public String[] getStringFile(String PathFile) throws IOException {
        File file = new File(PathFile);
        FileInputStream fis = new FileInputStream(file.getAbsolutePath());
        HWPFDocument doc = new HWPFDocument(fis);
        WordExtractor we = new WordExtractor(doc);
        String[] paragraphs = we.getParagraphText();
        return paragraphs;
    }
    FileWriter file;
    public boolean saveData(JSONObject ob,String urlFile){
        try {
            // Constructs a FileWriter given a file name, using the platform's default charset
            file = new FileWriter(urlFile);
            file.write(ob.toJSONString());
//            CrunchifyLog("Successfully Copied JSON Object to File...");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {

            try {
                file.flush();
                file.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("add data in data_method");
                return false;
            }
        }
        return true;
    }

    public JSONObject getData(String urlFile){
        JSONParser parser = new JSONParser();
        JSONObject data = new JSONObject();
        try {
            Object obj = parser.parse(new FileReader(urlFile));
            data = (JSONObject) obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    public  boolean SetUpFile(String PathFile) throws IOException {
       String[] data= getStringFile(PathFile);
       int avg=0;
        for (String para : data) {
            String[] split = para.split(" ");
            avg+=split.length;
        }

        Today td = new Today();
        int date=td.ScoreDate(td.getDateFrStr(data[4]));
        String loai = data[8];
        connect_mysql cnn= new connect_mysql("DataDOC","root","");
        cnn.insertData("INSERT INTO `ttvb` (`id`, `path`, `date`, `loaivb`, `avg`, `try`) VALUES (NULL, '"+PathFile+"', '"+date+"', '"+loai+"', '"+avg+"', '');");

       return true;
    }

    //get data in data base
    public ArrayList<JSONObject> getAVPF(String bl,String nq,String tt) throws SQLException {
        ArrayList<JSONObject> arr = new ArrayList<>();
        connect_mysql cnn= new connect_mysql("DataDOC","root","");
        int[] ccl={0,1,0,1,0,1};
        String[][] data =null;
        data= cnn.getdt("SELECT * FROM ttvb",6,100,ccl);
        double avg=0;
        double tr=0;
        int ifr =0;
        int iend=60;
        if (bl.equals("true")){
            ifr=0;
            iend=6;
        }else if (tt.equals("true")){
            ifr=7;
            iend=45;
        }
        else if (nq.equals("true")){
            ifr=47;
            iend=60;
        }
        for (int i = ifr;i<=iend;i++){
            JSONObject obj = new JSONObject();
            obj.put("path",data[i][1]);
            obj.put("date",data[i][2]);
            obj.put("loai",data[i][3]);
            int t = Integer.parseInt(data[i][5]);
            int a = Integer.parseInt(data[i][4]);
            avg+=a;
            tr+=t;
            arr.add(obj);
        }
        JSONObject o = new JSONObject();
        o.put("avg",avg/tr);
        arr.add(o);
//        for (int k =0; k<= arr.size()-1;k++){
//            System.out.println(arr.get(k).toString());
//        }
        return  arr;
    }

    public static void main(String[] args) throws IOException, SQLException {
        dataDAO dt = new dataDAO();
        for (int i=1;i<30;i++) {
            String PathFile = "src/main/webapp/DocFile/NQ"+i+".doc";
            String[] data = dt.getStringFile(PathFile);
            int avg = 0;
            for (String para : data) {
                String[] split = para.split(" ");
                avg += split.length;
            }
            String loai = "";
            int date = 0;
            Today td = new Today();
            try {
                loai = data[8];
                date = td.ScoreDate(td.getDateFrStr(data[4]));
            } catch (ArrayIndexOutOfBoundsException e) {
                try {
                    loai = data[10];
                    date = td.ScoreDate(td.getDateFrStr(data[7]));
                }catch (ArrayIndexOutOfBoundsException u){
                    continue;
                }

            }


            connect_mysql cnn = new connect_mysql("DataDOC", "root", "");
            cnn.insertData("INSERT INTO `ttvb` (`id`, `path`, `date`, `loaivb`, `avg`, `try`) VALUES (NULL, '" + PathFile + "', '" + date + "', '" + loai + "', '" + avg + "', '" + data.length + "');");
//        dataDAO dt = new dataDAO();
//        dt.getAVPF();
        }
    }
}
