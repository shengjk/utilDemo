package graphx;

import org.neo4j.driver.v1.*;

import static org.neo4j.driver.v1.Values.parameters;

/**
 * Created by shengjk1 on 2017/4/11 0011.
 * Neo4j Cypher Java API
 */
public class Neo4jDemo {
	public static void main(String[] args) {
		
		Driver driver = GraphDatabase.driver("bolt://192.168.1.101:7687", AuthTokens.basic("neo4j", "123456"));
		Session session = driver.session();
		
		session.run("CREATE (a:Person1 {name: {name}, title: {title}})",
				parameters("name", "Arthur", "title", "King"));
		StatementResult result = session.run("MATCH (a:Person1) WHERE a.name = {name} " +
						"RETURN a.name AS name, a.title AS title",
				parameters("name", "Arthur"));
		while (result.hasNext()) {
			Record record = result.next();
			System.out.println(record.get("id").asString()+" "+record.get("title").asString() + " " + record.get("name").asString());
		}
		session.close();
		driver.close();
	}
	
	
	
	
}
