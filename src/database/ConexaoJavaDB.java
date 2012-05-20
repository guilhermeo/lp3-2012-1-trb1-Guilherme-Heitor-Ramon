package database;

import java.sql.Connection;
import java.sql.DriverManager;


public class ConexaoJavaDB {
 
private static Connection conexao = null;    
    
    public static Connection getConnection() throws Exception {
        
        
       Class.forName("org.apache.derby.jdbc.ClientDriver");
       
       if(conexao == null) {
       conexao = DriverManager.getConnection("jdbc:derby://localhost:1527/Trabalho", "LP3", "lp3");
       }
       return conexao;
       
    };
    
    public static void closeConnection() throws Exception{
    
    conexao.close(); 
        
    };
}
