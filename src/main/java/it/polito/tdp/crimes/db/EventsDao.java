package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Event;

public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Adiacenza> getArchi(String categoria, int mese) {
		String sql = "select e1.offense_type_id as v1, e2.offense_type_id as v2, "
				+ "count(distinct e1.`neighborhood_id`) as peso "
				+ "from events e1, events e2 "
				+ "where e1.offense_type_id > e2.offense_type_id "
				+ "and e1.offense_category_id = ? "
				+ "and e2.offense_category_id = e1.offense_category_id "
				+ "and month(e1.reported_date) = ? "
				+ "and month(e2.reported_date) = month(e1.reported_date) "
				+ "and e1.`neighborhood_id` = e2.neighborhood_id "
				+ "group by e1.offense_type_id, e2.offense_type_id";
		List<Adiacenza> archi = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1, categoria);
			st.setInt(2, mese);			
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				archi.add(new Adiacenza(res.getString("v1"), res.getString("v2"),
						res.getInt("peso")));
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		return archi;
	}
	
	public List<String> getCategorie() {
		String sql = "select distinct offense_category_id "
				+ "from events";
		List<String> categorie = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;		
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				categorie.add(res.getString("offense_category_id"));
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		return categorie;	
	}
}
