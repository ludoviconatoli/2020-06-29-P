package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	public List<Player> listAllPlayers(){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void listAllMatchesMese(Map<Integer, Match> map, int m){
		String sql = "SELECT m.MatchID, m.TeamHomeID, m.TeamAwayID, m.teamHomeFormation, m.teamAwayFormation, m.resultOfTeamHome, m.date, t1.Name, t2.Name   "
				+ "FROM Matches m, Teams t1, Teams t2 "
				+ "WHERE m.TeamHomeID = t1.TeamID AND m.TeamAwayID = t2.TeamID AND MONTH(m.date) = ?";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, m);
			
			ResultSet res = st.executeQuery();
			while (res.next()) {
			
				if(!map.containsKey(res.getInt("m.MatchID"))){
					Match match = new Match(res.getInt("m.MatchID"), res.getInt("m.TeamHomeID"), res.getInt("m.TeamAwayID"), res.getInt("m.teamHomeFormation"), 
							res.getInt("m.teamAwayFormation"),res.getInt("m.resultOfTeamHome"), res.getTimestamp("m.date").toLocalDateTime(), res.getString("t1.Name"),res.getString("t2.Name"));
					
					map.put(match.getMatchID(), match);
				}

			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Adiacenza> getArchi(Map<Integer, Match>map, int minuti){
		String sql ="SELECT a1.MatchID, a2.MatchID, COUNT(*) AS peso "
				+ "FROM actions a1, actions a2 "
				+ "WHERE a1.MatchID > a2.MatchID AND a1.PlayerID = a2.PlayerID AND a1.TimePlayed >= ?  AND a2.TimePlayed >= ? "
				+ "GROUP BY a1.MatchID, a2.MatchID";
		
		List<Adiacenza> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, minuti);
			st.setInt(2, minuti);
			
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				if(map.containsKey(res.getInt("a1.MatchID")) && map.containsKey(res.getInt("a2.MatchID"))) {
					Match m1 = map.get(res.getInt("a1.MatchID"));
					Match m2 = map.get(res.getInt("a2.MatchID"));
					
					result.add(new Adiacenza(m1, m2, res.getDouble("peso")));
				}
			}
			
			conn.close();
			return result;
		} catch (SQLException e) {
			System.out.println("Errore in getArchi()");
			return null;
		}
	}
	
}
