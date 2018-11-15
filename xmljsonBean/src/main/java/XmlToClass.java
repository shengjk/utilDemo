import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import xxx.model.Parameters;
import xxx.model.Partner;
import xxx.model.Partners;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by landscape.
 */
public class XmlToClass {
    public static void main(String[] args) {
        List<Partner> partnerList=new ArrayList<Partner>();
//        XStream xStream=new XStream();
        XStream xStream=new XStream(new DomDriver("utf8"));
        Partners partners=new Partners();
        Parameters parameters=new Parameters();
        Partner partner=new Partner();

        parameters.setFrist("aa");
        parameters.setScends("bb");
        partner.setEncoding("123");
        partner.setIp("192.168");
        partner.setTableName("a");
        partner.setWorkDir("F:\1");
        partner.setParameters(parameters);
        partnerList.add(partner);
        partners.setPartnerList(partnerList);

        xStream.alias("partners",Partners.class);
        xStream.alias("partner",Partner.class);
        xStream.alias("parameters",Parameters.class);
//        xStream.alias("address",Address.class);  
        xStream.setMode(XStream.NO_REFERENCES);
        //也可以吧相应的标签给去掉
//        xStream.addImplicitCollection(Person.class, "addresses");  
//        xStream.useAttributeFor(Person.class,"name");  
        //注册使用了注解的VO  
        xStream.processAnnotations(new Class[]{Partners.class,Partner.class,Parameters.class});
        String xml = xStream.toXML(partners);

        System.out.println(xml);


        partners= (Partners) xStream.fromXML(new File("F:\\ideaProgect\\jackson\\src\\main\\resources\\config.xml"));
        List<Partner> partnersList=partners.getPartnerList();
        System.out.println(partnerList.size());
        for (Partner partner1:partnersList){
            System.out.println(partner1.getEncoding());
            System.out.println(partner1.getIp());
            System.out.println(partner1.getTableName());
            System.out.println(partner1.getWorkDir());
        }
    }

}
