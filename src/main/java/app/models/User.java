package app.models;
import java.sql.*;
import java.util.UUID;
public class User extends Model {
    public String name;
    public String email;
    public String password;
    public String avatar;
    public String status;
    public String bio;
    public String country;
    public String id;
    public User(String name, String email, String password, String avatar, String country){
        this.name = name;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
        this.status = "inactive";
        this.bio = "";
        this.country = country;
        this.id = UUID.randomUUID().toString();
    }
    public User(String name, String email, String password, String avatar, String status, String bio, String country, String id){
        this.name = name;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
        this.status = status;
        this.bio = bio;
        this.country = country;
        this.id = id;
    }
    public User(String id){
        this.id = id;
    }
    public void save(){
        try{
            connect();
            PreparedStatement pst = conn.prepareStatement("INSERT INTO users (name, email, pwd, avatar, uuid, sts, bio, country) values(?, ?, ?, ?, ?, ?, ?, ?)");
            pst.setString(1, this.name);
            pst.setString(2, this.email);
            pst.setString(3, this.password);
            pst.setString(4, this.avatar);
            pst.setString(5, this.id);
            pst.setString(6, this.status);
            pst.setString(7, this.bio);
            pst.setString(8, this.country);
            pst.execute();
            pst.close();   
            conn.close();
        }catch(ClassNotFoundException ce){
            System.out.println("Driver error: " + ce);
            ce.printStackTrace();
       }catch(SQLException se){
            System.out.println("SQL error: " + se);
            se.printStackTrace();
        }
    }
    public void update(){
        try{
            connect();
            PreparedStatement pst = conn.prepareStatement(
                "UPDATE users SET name=COALESCE(?, name),email=COALESCE(?, email),pwd=COALESCE(?, pwd)," + 
                "avatar=COALESCE(?, avatar),sts=COALESCE(?, sts),bio=COALESCE(?, bio),country=COALESCE(?, country) WHERE uuid=?"
            );
            pst.setString(1, this.name);
            pst.setString(2, this.email);
            pst.setString(3, this.password);
            pst.setString(4, this.avatar);
            pst.setString(5, this.status);
            pst.setString(6, this.bio);
            pst.setString(7, this.country);
            pst.setString(8, this.id);
            pst.executeUpdate();
            pst.close();
            conn.close();
        }catch(ClassNotFoundException ce){
            System.out.println("Driver error: " + ce);
            ce.printStackTrace();
       }catch(SQLException se){
            System.out.println("SQL error: " + se);
            se.printStackTrace();
        }
    }
    public static User getByID(String uuid){
        try{
            connect();
            ResultSet rs = executeQuery("SELECT * FROM users WHERE uuid='" + uuid + "'");
            User user = getByResultSet(rs);
            conn.close();
            return user;
        }catch(ClassNotFoundException ce){
            System.out.println("Driver error: " + ce);
            ce.printStackTrace();
            return null;
       }catch(SQLException se){
            System.out.println("SQL error: " + se);
            se.printStackTrace();
            return null;
        }
    }
    public static User login(String email, String password){
        try{
            connect();
            ResultSet rs = executeQuery("SELECT * FROM users WHERE email='" + email + "' AND pwd='" + password + "'");
            User user = getByResultSet(rs);
            conn.close();
            return user;
        }catch(ClassNotFoundException ce){
            System.out.println("Driver error: " + ce);
            ce.printStackTrace();
            return null;
       }catch(SQLException se){
            System.out.println("SQL error: " + se);
            se.printStackTrace();
            return null;
        }
    }
    public static void deleteByID(String id){
        try{
            connect();
            execute("DELETE FROM users WHERE id='" + id + "'");
        }catch(ClassNotFoundException ce){
            System.out.println("Driver error: " + ce);
            ce.printStackTrace();
       }catch(SQLException se){
            System.out.println("SQL error: " + se);
            se.printStackTrace();
        }
    }
    public static void migrate(){
        try{
            connect();
            execute(
                "CREATE TABLE " +
                "users (name varchar(255)," +
                "email varchar(255)," + 
                "pwd varchar(255)," + 
                "avatar text," +
                "bio text," +
                "country varchar(255)," +
                "sts varchar(255)," +
                "uuid varchar(255)," +
                "PRIMARY KEY (uuid)," +
                "UNIQUE(email))"
            );
        }catch(ClassNotFoundException ce){
            System.out.println("Driver error: " + ce);
            ce.printStackTrace();
       }catch(SQLException se){
            System.out.println("SQL error: " + se);
            se.printStackTrace();
        }
    }
    private static User getByResultSet(ResultSet rs) throws SQLException {
        if(rs.next()){
            String sUuid = rs.getString("uuid");
            String sName = rs.getString("name");
            String sEmail = rs.getString("email");
            String sPassword = rs.getString("pwd");
            String sAvatar = rs.getString("avatar");
            String sStatus = rs.getString("sts");
            String sBio = rs.getString("bio");
            String sCountry = rs.getString("country");
            User user = new User(sName, sEmail, sPassword, sAvatar, sStatus, sBio, sCountry, sUuid);
            return user;
        }else{
            return null;
        }   
    }
}