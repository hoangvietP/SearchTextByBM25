
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(urlPatterns = {"/search"})
public class SearchController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //get term form ajax
        String term = req.getParameter("data");
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(term);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        term= String.valueOf(json.get("term"));
        String ti= String.valueOf(json.get("ti"));
        String dn= String.valueOf(json.get("dn"));
        String pt= String.valueOf(json.get("pt"));
        String td = String.valueOf(json.get("tdn"));
        String bl= String.valueOf(json.get("bl"));
        String nq= String.valueOf(json.get("nq"));
        String tt = String.valueOf(json.get("tt"));
        JSONObject tdnn = new JSONObject();
        try {
            tdnn = (JSONObject) parser. parse(td);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //tdn
        TDN tdn= new TDN();
        String TDN=tdn.tdn(tdnn,term);
        //bm25 class
        BM25method bm25 = new BM25method();
        //convert term , crop stopword
        term=bm25.CropStopW(term);

        //rank score
        ArrayList<JSONObject> data = null;
        try {
            data = bm25.SearchByTerm(term,TDN,ti,dn,pt,bl,nq,tt);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        JSONArray arr = new JSONArray();
        for (int k =0; k<= data.size()-1;k++){
            JSONObject o = data.get(k);
            try{
                if (!o.get("td").equals("")||!o.get("td").equals(null)){
                    arr.add(o);
                }
            }catch (Exception ii){

            }
        }
        //respone
        resp.setContentType("text/json");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter out = resp.getWriter();
            out.println(arr);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("/Aurora/search.html");
    }
}
