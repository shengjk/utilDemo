package util.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by shengjk1 on 2017/12/11
 */
public class XMLParser {
	
//
//	/**
//	 *
//	 * @return
//	 * @throws Exception
//	 */
//	public static List<Plugin> getPluginList() throws Exception {
//		Plugins plugins=convertXmlFileToObject(Plugins.class, ConfigManager.build().getProperty("pluginXml"));
//		return plugins.getPlugin();
//	}
//
//
//	/**
//	 *
//	 * @return
//	 * @throws Exception
//	 */
//	public static List<Topic> getTopicList() throws Exception {
//		Topics topics=convertXmlFileToObject(Topics.class,ConfigManager.build().getProperty("topicXml"));
//		return topics.getTopic();
//	}
////
	
	/**
	 *
	 * @param t
	 * @param xmlPath
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	protected static <T> T  convertXmlFileToObject(Class<T> t, String xmlPath) throws Exception {
		T t1=null;
		try {
			JAXBContext context = JAXBContext.newInstance(t);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			FileReader fr = null;
			try {
				fr = new FileReader(xmlPath);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			t1 = (T) unmarshaller.unmarshal(fr);
		} catch (JAXBException e) {
			throw e;
		}
		return t1;
	}
}