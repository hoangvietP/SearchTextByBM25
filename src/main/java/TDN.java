import org.json.simple.JSONObject;

public class TDN {
    public String tdn(JSONObject obj,String term){
        String[] count = term.split(" ");
        String tdnn="";
        if (count.length<3){
            JSONObject json = new JSONObject();
            json= (JSONObject) obj.get("query");
            json= (JSONObject) json.get("pages");
            Object[] ob = json.values().toArray();
            for (int i=0;i<=2;i++){
                JSONObject ter = (JSONObject) ob[i];
                String index= String.valueOf(ter.get("index"));
                if (!ter.get("title").equals(term) && index.equals("1")){
                    tdnn=String.valueOf(ter.get("title"));
                    break;
                }
            }
        }
        System.out.println("Từ đồng nghĩa: "+term+" => "+tdnn);
        return tdnn;
    }

}
