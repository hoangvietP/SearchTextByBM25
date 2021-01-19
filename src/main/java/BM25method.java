import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BM25method {
    String[] PtTerm;
    String[] CvTerm;

    public ArrayList<JSONObject> SearchByTerm(String term,String TDN,String ti,String dn,String pt,String bl,String nq,String tt) throws SQLException, IOException {
        dataDAO dt = new dataDAO();
        ArrayList<JSONObject> kq = new ArrayList<>();
        ArrayList<JSONObject> kq1 = new ArrayList<>();
        ArrayList<JSONObject> kqPT = new ArrayList<>();
        ArrayList<JSONObject> kqTDN = new ArrayList<>();
        ArrayList<Double> max = new ArrayList<>();
        ArrayList<Double> maxPT = new ArrayList<>();
        ArrayList<Double> maxTDN = new ArrayList<>();

        ArrayList<Integer> maxTi = new ArrayList<>();
        ArrayList<Integer> maxPTTi = new ArrayList<>();
        ArrayList<Integer> maxTDNTi = new ArrayList<>();

        ArrayList<JSONObject> data = dt.getAVPF(bl,nq,tt);
        JSONObject a = data.get(data.size() - 1);
        String av = String.valueOf(a.get("avg"));
        double avg = Double.parseDouble(av);
        boolean ktTerm= false;
        try{
            int ter= Integer.parseInt(term);
        }catch (NumberFormatException e){
            ktTerm=true;
        }

        for (int i = 0; i <= data.size() - 2; i++) {
            JSONObject dta = data.get(i);
            String[] parag = dt.getStringFile(String.valueOf(dta.get("path")));
            JSONObject sc = Score(parag, term, avg, TDN);
//            System.out.println("tham so vb---------"+sc);
            double score = (double) sc.get("ScoreTerm");
            double scorePT = (double) sc.get("ScoreTermPT");
            double scoreTDN = (double) sc.get("ScoreTermTDN");
            String dats = String.valueOf(dta.get("date"));
            int dateT = Integer.parseInt(dats);
            //set data to rt
            BM25method bm25 = new BM25method();
            int stt = 0;


            JSONObject ob = new JSONObject();
            int kSc = (int) sc.get("kScore");
            String[] tdnd = bm25.GetTD(parag, kSc);
            if (!tdnd[0].equals("")) {
                String loai= String.valueOf(dta.get("loai"));
                String loaiterm="";
                String tieude ="";
                if (ktTerm==true){
                    loaiterm =loai.substring(0,1).toUpperCase() + loai.substring(1).toLowerCase();
                    tieude=tdnd[0].substring(0,1).toUpperCase() + tdnd[0].substring(1).toLowerCase();
                }else {
                    loaiterm= loai;
                    tieude=tdnd[0];
                }
                ob.put("loai",loaiterm );
                ob.put("td", tieude);
                ob.put("nd", tdnd[1]);
                ob.put("score", score);
                String da = String.valueOf(dta.get("date"));
                char[] coj = da.toCharArray();
                String date ="";
                if (coj.length==8){
                    date = da.substring(0, 4) + "-" + da.substring(4, 6) + "-" + da.substring(6, 8);
                }
                ob.put("date", date);
                ob.put("loaiTerm", "TermBM25");
                ob.put("path", dta.get("path"));
                ob.put("term", sc.get("termBM"));
                ob.put("kScore", sc.get("kScore"));
            }

            JSONObject obPT = new JSONObject();
            int kScPT = (int) sc.get("kScorePT");
            String[] tdndPT = bm25.GetTD(parag, kScPT);
            if (!tdndPT[0].equals("")) {
                String loai= String.valueOf(dta.get("loai"));
                String loaiterm="";
                String tieude ="";
                if (ktTerm==true){
                    loaiterm =loai.substring(0,1).toUpperCase() + loai.substring(1).toLowerCase();
                    tieude=tdndPT[0].substring(0,1).toUpperCase() + tdndPT[0].substring(1).toLowerCase();
                }else {
                    loaiterm= loai;
                    tieude=tdnd[0];
                }
                obPT.put("loai", loaiterm);
                obPT.put("td",tieude);
                obPT.put("nd", tdndPT[1]);
                obPT.put("score", scorePT);
                obPT.put("loaiTerm", "termPT");
                String da = String.valueOf(dta.get("date"));
                char[] coj = da.toCharArray();
                String date ="";
                if (coj.length==8){
                    date = da.substring(0, 4) + "-" + da.substring(4, 6) + "-" + da.substring(6, 8);
                }
                obPT.put("date", date);
                obPT.put("path", dta.get("path"));
                JSONArray termPT= new JSONArray();
                for (int l=0;l<=PtTerm.length-1;l++){
                    termPT.add(PtTerm[l]);
                }
                obPT.put("termPT",termPT);
                obPT.put("kScore", sc.get("kScorePT"));
            }
            JSONObject obTDN = new JSONObject();
            if (!TDN.equals("")) {

                int kScTDN = (int) sc.get("kScoreTDN");
                String[] tdndDN = bm25.GetTD(parag, kScTDN);
                if (!tdndDN[0].equals("")) {
                    String loai = String.valueOf(dta.get("loai"));
                    String loaiterm="";
                    String tieude ="";
                    if (ktTerm==true){
                        loaiterm =loai.substring(0,1).toUpperCase() + loai.substring(1).toLowerCase();
                        tieude=tdndDN[0].substring(0,1).toUpperCase() + tdndDN[0].substring(1).toLowerCase();
                    }else {
                        loaiterm= loai;
                        tieude=tdnd[0];
                    }
                    obTDN.put("loai",loaiterm);
                    obTDN.put("td", tieude);
                    obTDN.put("nd", tdndDN[1]);
                    obTDN.put("loaiTerm", "termTDN");
                    obTDN.put("score", scoreTDN);
                    obTDN.put("term", sc.get("termTDN"));
                    String da = String.valueOf(dta.get("date"));
                    char[] coj = da.toCharArray();
                    String date = "";
                    if (coj.length == 8) {
                        date = da.substring(0, 4) + "-" + da.substring(4, 6) + "-" + da.substring(6, 8);
                    }
                    obTDN.put("date", date);
                    obTDN.put("path", dta.get("path"));
                    obTDN.put("kScore", sc.get("kScoreTDN"));
                }
            }
            //set data to arrlist
            if (ti.equals("true")) {
                maxTi.add(dateT);
                kq.add(ob);
                if (pt.equals("true")){
                    maxPTTi.add(dateT);
                    kqPT.add(obPT);
                }
                if (dn.equals("true")){
                    maxTDNTi.add(dateT);
                    if (!TDN.equals("")) {
                        kqTDN.add(obTDN);
                    }
                }
            }
            if (!ti.equals("true")){
                max.add(score);
                kq.add(ob);
                if (pt.equals("true")){
                    maxPT.add(scorePT);
                    kqPT.add(obPT);
                }
                if (dn.equals("true")){
                    maxTDN.add(scoreTDN);
                    if (!TDN.equals("")) {
                        kqTDN.add(obTDN);
                    }
                }
            }
        }

        //set rank only BM25
        Object[] maxx= new Object[kq.size()];
        if (ti.equals("true")){
            maxx=maxTi.toArray();
        }else {
            maxx=max.toArray();
        }
        Object[] kkq= kq.toArray();
        for (int i = 0 ; i < maxx.length - 1; i++) {
            for (int j = i + 1; j < maxx.length; j++) {
                JSONObject obi = (JSONObject) kkq[i];
                JSONObject obj = (JSONObject) kkq[j];
                if (ti.equals("true")){
                    int bi = (int) maxx[i];
                    int bij = (int) maxx[j];
                    if (bi > bij) {
                        maxx[i] = bij;
                        maxx[j] = bi;
                        kkq[i] = obj;
                        kkq[j] = obi;
                    }
                }else {
                    double bi = (double) maxx[i];
                    double bij = (double) maxx[j];
                    if (bi > bij) {
                        maxx[i] = bij;
                        maxx[j] = bi;
                        kkq[i] = obj;
                        kkq[j] = obi;
                    }
                }
            }
        }
        //set rank PT
        Object[] maxxPT= new Object[kqPT.size()];
        if (ti.equals("true")){
            maxxPT=maxPTTi.toArray();
        }else {
            maxxPT=maxPT.toArray();
        }
        Object[] kkqPT= kqPT.toArray();
        for (int i = 0 ; i < maxxPT.length - 1; i++) {
            for (int j = i + 1; j < maxxPT.length; j++) {
                JSONObject obi = (JSONObject) kkqPT[i];
                JSONObject obj = (JSONObject) kkqPT[j];
                if (ti.equals("true")){
                    int bi = (int) maxxPT[i];
                    int bij = (int) maxxPT[j];
                    if (bi > bij) {
                        maxxPT[i] = bij;
                        maxxPT[j] = bi;
                        kkqPT[i] = obj;
                        kkqPT[j] = obi;
                    }
                }else {
                    double bi = (double) maxxPT[i];
                    double bij = (double) maxxPT[j];
                    if (bi > bij) {
                        maxxPT[i] = bij;
                        maxxPT[j] = bi;
                        kkqPT[i] = obj;
                        kkqPT[j] = obi;
                    }
                }
            }
        }
        //set rank TDN
        Object[] maxxTDN= new Object[kqTDN.size()];
        if (ti.equals("true")){
            maxxTDN=maxTDNTi.toArray();
        }else {
            maxxTDN=maxTDN.toArray();
        }
        Object[] kkqTDN= kqTDN.toArray();
        for (int i = 0 ; i < maxxTDN.length - 1; i++) {
            for (int j = i + 1; j < maxxTDN.length; j++) {
                JSONObject obi = (JSONObject) kkqTDN[i];
                JSONObject obj = (JSONObject) kkqTDN[j];
                if (ti.equals("true")){
                    int bi = (int) maxxTDN[i];
                    int bij = (int) maxxTDN[j];
                    if (bi > bij) {
                        maxxTDN[i] = bij;
                        maxxTDN[j] = bi;
                        kkqTDN[i] = obj;
                        kkqTDN[j] = obi;
                    }
                }else {
                    double bi = (double) maxxTDN[i];
                    double bij = (double) maxxTDN[j];
                    if (bi > bij) {
                        maxxTDN[i] = bij;
                        maxxTDN[j] = bi;
                        kkqTDN[i] = obj;
                        kkqTDN[j] = obi;
                    }
                }
            }
        }

        for (int i=kkq.length-1;i>=0;i--) {
                JSONObject obj = (JSONObject) kkq[i];
                kq1.add(obj);
        }
        if (pt.equals("true")){
            for (int i=kkqPT.length-1;i>=0;i--){
                JSONObject obj = (JSONObject) kkqPT[i];
                kq1.add(obj);
            }
        }
        for(int k=0;k<=kqTDN.size()-1;k++){
            kq1.add(kqTDN.get(k));
        }
        return kq1;

    }
    public String[] GetTD(String[] parag,int kSc){
        String value = parag[kSc];
        int count = checkIn("Điều", value);
        String td = "";
        String nd = "";
        if (count == 0) {
            nd = parag[kSc];
            for (int o = 0; o < 10; o++) {
                int co = 0;
                try {
                    co = checkIn("Điều", parag[kSc - o]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    td = "";
                    break;
                }
                if (co > 0) {
                    td = parag[kSc - o];
                    break;
                }
            }
        }
        try {
            if (count > 0) {
                nd = parag[kSc + 1];
                td = parag[kSc];
            }
        }catch (IndexOutOfBoundsException ex){
            nd=  parag[kSc];
            td= parag[kSc];
        }
        String[] rt= {td,nd};
        return rt;
    }

    public JSONObject Score(String[] paragraphs, String term, double avg,String TDN){
        PtTerm(term);
        cvTerm(term);

        boolean ktTerm= false;
        try{
            int ter= Integer.parseInt(term);
            CvTerm= new String[]{term, term};
        }catch (NumberFormatException e){
            ktTerm=true;
        }
        JSONObject data= new JSONObject();
        int k=0;
        double maxSc =0;
        int kSc=0;
        double maxScTDN =0;
        int kScTDN=0;
        double maxScPT =0;
        int kScPT=0;
        String te ="";
        for (String para : paragraphs) {
            String[] split=para.split(" ");
            int leg= split.length;
            //chuan xac tung tu
            int i= checkIn(CvTerm[0],para);
            int i1= checkIn(CvTerm[1],para);
            te=CvTerm[0];
            //tính score
            double score = (2.2*i/(i + 1.2 * (1 - 0.75 + 0.75 * leg / avg)));

            double score1 = (2.2*i1/(i1 + 1.2 * (1 - 0.75 + 0.75 * leg / avg)));
            if (score1>score){
                score=score1;
                te=CvTerm[1];
            }
            if (score>maxSc){
                maxSc= score;
                kSc= k;
            }
            //tim theo tu sau phan tach
            double scPT=0.0;
            for (int l=0;l<=PtTerm.length-1;l++){
                int iPT= checkIn(PtTerm[l],para);
                double scorePT = (2.2*iPT/(iPT + 1.2 * (1 - 0.75 + 0.75 * leg / avg)));
                scPT+=scorePT;
            }
            if (scPT>maxScPT){
                maxScPT= scPT;
                kScPT=k;
            }
            //tim theo tu dong nghia
            if(!TDN.equals("")){
            String[] rt = {TDN.substring(0,1).toLowerCase() + TDN.substring(1).toLowerCase(),TDN.substring(0,1).toUpperCase() + TDN.substring(1).toLowerCase()};
            int iDN= checkIn(rt[0],para);
            int iDN1=checkIn(rt[1],para);
            double scoreDN = (2.25*iDN/(iDN + 1.2 * (1 - 0.75 + 0.75 * leg / avg)));
            double scoreDN1 = (2.25*iDN1/(iDN1 + 1.2 * (1 - 0.75 + 0.75 * leg / avg)));
            TDN=rt[0];
            if (scoreDN1>scoreDN){
                scoreDN=scoreDN1;
                TDN=rt[1];
            }

            if (scoreDN>maxScTDN){
                maxScTDN=scoreDN;
                kScTDN=k;
            }}
            k++;
        }
        data.put("ScoreTerm",maxSc);
        data.put("kScore",kSc);
        data.put("termBM",te);
        if (kSc==kScPT){
            kScPT=0;
            maxScPT=0;
        }
        data.put("ScoreTermPT",maxScPT);
        data.put("termPT",PtTerm);
        data.put("kScorePT",kScPT);
        if (kSc==kScTDN){
            kScTDN=0;
            maxScTDN=0;
        }
        data.put("ScoreTermTDN",maxScTDN);
        data.put("termTDN",TDN);
        data.put("kScoreTDN",kScTDN);
        return data;
    }
    //loai bo va them tu viet hoa
    public void cvTerm(String term){
        String[] rt = {term.substring(0,1).toLowerCase() + term.substring(1).toLowerCase(),term.substring(0,1).toUpperCase() + term.substring(1).toLowerCase()};
        this.CvTerm=rt;
    }
    //phan tach tu khoa
    public void PtTerm(String term){
        String[] rt = term.split(" ");
        this.PtTerm=rt;
    }

    // kiem tra xem chuoi nay co trong chuoi khac bn lan
    public static int checkIn(String term,String doc){
        int i=0;
        Pattern p = Pattern.compile(term);
        Matcher m = p.matcher( doc );
        while (m.find()) {
            i++;
        }
        return i;
    }

    //tach bo stop word
    public String CropStopW(String term){
        String stopword="bị\n" +
                "bởi\n" +
                "cả\n" +
                "các\n" +
                "cái\n" +
                "cần\n" +
                "càng\n" +
                "chỉ\n" +
                "chiếc\n" +
                "cho\n" +
                "chứ\n" +
                "chưa\n" +
                "chuyện\n" +
                "có\n" +
                "có_thể\n" +
                "cứ\n" +"pháp luật\n"+
                "của\n" +
                "cùng\n" +
                "cũng\n" +
                "đã\n" +
                "đang\n" +
                "đây\n" +
                "để\n" +
                "đến_nỗi\n" +
                "đều\n" +
                "điều\n" +
                "do\n" +
                "đó\n" +
                "được\n" +
                "dưới\n" +
                "gì\n" +
                "khi\n" +
                "không\n" +
                "là\n" +
                "lại\n" +
                "lên\n" +
                "lúc\n" +
                "mà\n" +
                "mỗi\n" +
                "một_cách\n" +
                "này\n" +
                "nên\n" +
                "nếu\n" +
                "ngay\n" +
                "nhiều\n" +
                "như\n" +
                "nhưng\n" +
                "những\n" +
                "nơi\n" +
                "nữa\n" +
                "phải\n" +
                "qua\n" +
                "ra\n" +
                "rằng\n" +
                "rằng\n" +
                "rất\n" +
                "rất\n" +
                "rồi\n" +
                "sau\n" +
                "sẽ\n" +
                "so\n" +
                "sự\n" +
                "tại\n" +
                "theo\n" +
                "thì\n" +
                "trên\n" +
                "trước\n" +
                "từ\n" +
                "từng\n" +
                "và\n" +
                "vẫn\n" +
                "vào\n" +
                "vậy\n" +
                "vì\n" +
                "việc\n" +
                "với\n" +
                "vừa";
        String[] stw= stopword.split("\n");
        String[] ter = term.split(" ");
        String rt ="";
        for (int i=0; i<= ter.length-1;i++){
            for (int j =0; j<= stw.length-1;j++){
               if (ter[i].equals(stw[j])){
                   ter[i]="";
               }
            }
            String t =" ";
            if (i==ter.length-1){
                t="";
            }
            if (ter[i].equals("")){
                t="";
            }
            rt=rt+ter[i]+t;
        }
        return rt;
    }

//    public static void main(String[] args) {
//        BM25method bm25 = new BM25method();
//        System.out.println(bm25.CropStopW("tôi đã ra khỏi nhà lúc 3h"));
//    }

//    public static void main(String[] args) throws IOException, SQLException {
////        dataDAO dt = new dataDAO();
////        String[] paragraphs= {};
////        try {
////            paragraphs= dt.getStringFile("src/main/DocFile/blhs.doc");
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////        BM25method bm25 = new BM25method();
////        JSONObject data= bm25.Score(paragraphs,"lao động",24);
////        System.out.println(data.toString());
//
//        BM25method bm25 = new BM25method();
//        ArrayList<JSONObject> data = bm25.SearchByTerm("lao động");
//        for (int k =0; k<= data.size()-1;k++){
//            System.out.println(data.get(k).toString());
//        }
//    }
}
