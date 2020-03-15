package packDao;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrdenadorFDAOImpl implements IPartidaDAO {

	public OrdenadorFDAOImpl() {}

	/**
	 * Método que devuelve el ranking de las partidas jugadas en modo ordenador dificil
	 * @author Nuria Lebeña
	 * @param
	 * @return Devuelve un JSONArray con el ranking de las partidas realizadas en el modo ordenador dificil
	 */
	public JSONArray cargarRanking() throws SQLException {
		JSONArray ranking= new JSONArray();
		ConnectionManager con= new ConnectionManager();

		ResultSet resultado=con.execSQL("SELECT nombre, tiempo FROM Partida NATURAL JOIN OrdenadorFacil ORDER BY tiempo ASC Limit 10");
		Boolean hayResultado=resultado.next();
		while (hayResultado){
			String nombre=resultado.getString("nombre");
			int puntuacion=resultado.getInt("tiempo");
			JSONObject json=new JSONObject();
			json.put("nombre",nombre);
			json.put("puntuacion",puntuacion);
			ranking.add(json);
			hayResultado=resultado.next();
		}

		return ranking;
	}

	/**
	 * El método añade una partida en modo fácil y el id de esa partida a OrdenadorFacil en la base de datos
	 * @author Naiara Maneiro
	 * @param pNombre
	 * @param pPuntuacion
	 * @return No devuelve nada
	 */

	public void create(String pNombre, int pPuntuacion) throws SQLException {
		ConnectionManager conexion = new ConnectionManager();
		conexion.execSQL("INSERT INTO Partida (nombre, tiempo) VALUES ('"+pNombre+"', "+pPuntuacion+")");
		ResultSet resultado = conexion.execSQL("SELECT id FROM Partida WHERE nombre='"+pNombre+"' AND tiempo="+pPuntuacion);
		boolean hayResultado=resultado.next();
		int valor=resultado.getInt("id");
		while (hayResultado){
			boolean ultimo=resultado.isLast();
			if(ultimo){
				valor=resultado.getInt("id");
			}
			hayResultado=resultado.next();
		}
		conexion.execSQL("INSERT INTO OrdenadorFacil(id) VALUES ("+valor+")");
	}
}