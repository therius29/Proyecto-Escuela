/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package escuela;

/**
 *
 * @author brian
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Conexion {
    
    
    private final String URL="jdbc:sqlite:escuela.db";
    private final String DRIVER="org.sqlite.JDBC";
    
    //para crud
    private Statement consulta;
    private Connection conex=null;
    public Connection conexionDB() throws SQLException{
		Connection c=null;
		
		try {
			Class.forName(DRIVER).newInstance();
			c=DriverManager.getConnection(URL);
		}catch (ClassNotFoundException |IllegalAccessException |InstantiationException |SQLException e) {
			throw new SQLException(e.getMessage());
		}
		return c;
    }
    
    public void cerrar() throws SQLException{
        if(!this.conex.isClosed()){
            this.conex.close();
        }    
    }
    public Connection getConexion(){
        return conex;
    }
    //login 
    public boolean login(String sql){
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean estado;
        try {
            conex=conexionDB();
            pst = conex.prepareStatement(sql);
            rs = pst.executeQuery();
            if(!rs.isBeforeFirst()){
                estado=false;
                //JOptionPane.showMessageDialog(null,"EL usuario o contraseña no existe");
            }else{
                estado=true;
            }
            conex.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,"EL usuario o contraseña no existe");
            estado=false;
        }
        
        return estado;
    }
    
    //insert y para acutualizar
    public void agregar(String sql)throws SQLException{
        conex=conexionDB();
        consulta=conex.createStatement();
        consulta.execute(sql);
        conex.close();
    }
    
     //solo ejecuta la sentencia pero funcion es la misma q de agregregar
    public void eliminar(String sql)throws SQLException{
        conex=conexionDB();
        consulta=conex.createStatement();
        consulta.execute(sql);
        conex.close();
    }
    
    public DefaultTableModel llenarTabla(){
        String []  nombresColumnas = {"codigo","dni","nombre","apellidos","email"};
        String [] registros = new String[5];
        
        DefaultTableModel modelo = new DefaultTableModel(null,nombresColumnas);        
        String sql = "SELECT * FROM estudiante";
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try {
            conex=conexionDB();
            pst = conex.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next())
            {
                
                registros[0] = rs.getString("codigo");
                registros[1] = rs.getString("dni");
                registros[2] = rs.getString("nombre");
                registros[3] = rs.getString("apellidos");
                registros[4] = rs.getString("email");
                modelo.addRow(registros);
                System.out.println("ENTRO WHILE");
            }
            conex.close();
        }  catch(SQLException e)
        {
            
            JOptionPane.showMessageDialog(null,"Error al conectar::"+e);
            
        }
        return modelo;
    }
	
}
