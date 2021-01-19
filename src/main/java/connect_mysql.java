import java.sql.*;

public class connect_mysql {
    private String dtb = "";
    private String table;
    private String DB_URL = "";
    private String USER_NAME = "root";
    private String PASSWORD = "";
    Connection conn = null;

    public connect_mysql(String dtb, String USER_NAME, String PASSWORD) {
        this.dtb = dtb;
        this.USER_NAME = USER_NAME;
        this.PASSWORD = PASSWORD;
        this.DB_URL = "jdbc:mysql://localhost:3306/" + dtb + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    }

    public String[][] getdt(String query, int ccolum, int slcl, int[] type) throws SQLException {

        String[][] data = new String[slcl][ccolum];
        Connection connect = connect();
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        int i = 0;
        while (rs.next()) {
            for (int j = 0; j <= ccolum - 1; j++) {
                if (type[j] == 0) {
                    data[i][j] = "" + rs.getInt(j + 1);
                }
                if (type[j] == 1) {
                    data[i][j] = "" + rs.getString(j + 1);
                }
            }
            i++;
        }
        connect.close();
        return data;
    }
    public boolean insertData(String query){
        boolean status=false;
        try
        {
            Connection connection = connect();
            Statement st = connection.createStatement();
            st.executeUpdate(query);
            connection.close();
            status=true;
        }
        catch (Exception e)
        {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }


        return status;
    }
    public Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL,
                    USER_NAME,
                    PASSWORD);
        } catch (Exception ex) {
            System.out.println("connect failure!");
            ex.printStackTrace();
        }
        return conn;
    }

//    public static void main(String[] args) {
//        connect_mysql cnn= new connect_mysql("ten database","username","password");
//////        int[] ccl={1,1}   gia trị nhập vào mảng là 1 hoặc 0, 1 là kiểu varchar,char, 0 là int,   kiểu dữ liệu các cột cần lấy;
////        String[][] data =null;   mang data chua du lieu can lay;
////        data= cnn.getdt("query","so luong cot can lay int","so du lieu can lay",ccl);
////        boolean status= false;
////        status=cnn.insertData("query"); ínert thành công status=true và ngc lại
//    }
    public void test() throws SQLException {
        String[][] data = new String[106+67][6];
        Connection connect = connect();
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM NQ");
        int i = 0;
        int[] type = {0,1,0,1,0,1};
        while (rs.next()) {
            for (int j = 0; j <6; j++) {
                if (type[j] == 0) {
                    data[i][j] = "" + rs.getInt(j + 1);
                }
                if (type[j] == 1) {
                    data[i][j] = "" + rs.getString(j + 1);
                }
            }
            i++;
        }
        connect.close();
    }
    public static void main(String[] args) throws SQLException {
        connect_mysql cnn= new connect_mysql("DataDOC","root","");
        cnn.test();

    }
}
