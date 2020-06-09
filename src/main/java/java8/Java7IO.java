package xmht.java8;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author shengjk1
 * @date 2019/11/21
 */
public class Java7IO {
	public static void main(String[] args) throws IOException {
		Path path = Paths.get("/Users/iss/sourceCode/spark/utilDemo/src/main/resources/rebel.test");
		List<String> strings = Files.readAllLines(path);
		strings.forEach(x-> System.out.println(x));
		
		HashMap<String, String> stringStringHashMap = new HashMap<>();
		stringStringHashMap.put("a","a");
		
		try(Stream<String> stream = Files.lines(path)) {
			Map<String, String> result = stream.filter(str -> (str != null && str.contains("#"))).map(str -> str.split("#", -1))
					.collect(Collectors.toMap(str -> str[0], str -> str[1]));
			stringStringHashMap.putAll(result);
			
			System.out.println("=======");
			stringStringHashMap.forEach((k,v)-> System.out.println(k+v));
		}
		
		path = Paths.get("/Users/iss/sourceCode/spark/utilDemo/src/main/resources/rebel.test");
		Files.write(path,"aaaaa\n".getBytes(), StandardOpenOption.APPEND,StandardOpenOption.CREATE);
		
		
		InputStream inputStream = Files.newInputStream(path);
		path= Paths.get("/Users/iss/sourceCode/spark/utilDemo/src/main/resources/rebel.txt");
		Files.copy(inputStream,path, StandardCopyOption.REPLACE_EXISTING);
		
//		Files.createDirectories()
		
//		Objects.equals()... hash、toString、null
		
//		Integer.compare()
		
		System.out.println(Double.parseDouble("+1.0"));
		System.out.println(Integer.parseInt("+1"));
		
		
		String join = String.join("/", "usr", "local", "bin");
		System.out.println(join);
		String join1 = String.join(",", ZoneId.getAvailableZoneIds());
		System.out.println(join1);
		
		
		//数字类
	}
}
