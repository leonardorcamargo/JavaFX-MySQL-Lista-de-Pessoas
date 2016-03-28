package com.javafx.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsavel pela conexão com Banco de Dados MySQL
 * 
 * @author Raphael
 *
 */
public class ConnectionFactory {

	public static Connection getConnection() {
		String url = "jdbc:mysql://localhost/ram_alapure";
		try {
			return DriverManager.getConnection(url, "root", "admin");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}