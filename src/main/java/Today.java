public class Today {
    int stt=0;
    public String getDateFrStr(String str){
        String date="";
        char[] arr = str.toCharArray();
        int[] in = {0,1,2,3,4,5,6,7,8,9};
        for (int i=0;i<=arr.length-1;i++){
            for (int j=0; j<=9; j++){
                String a = String.valueOf(arr[i]);
                if (a.equals(in[j]+"")){
                    stt=0;
                    date= date+a;
                    break;
                }
                if (j==9 && stt==0 && !date.equals("")){
                    stt=1;
                    date=date+"-";
                }
            }
        }
        return date;
    }

    public int ScoreDate(String date){
        String[] splitDate1=date.split("-");
        String year = splitDate1[2].trim();
        String month = splitDate1[1].trim();
        String day = splitDate1[0].trim();
        String d = year+month+day;
        return Integer.parseInt(d);
    }
//test
//    public static void main(String[] args) {
//        Today td = new Today();
//        System.out.println(td.ScoreDate(td.getDateFrStr(" 27 tháng 11 năm 2015 sdhs")));
//    }
}
