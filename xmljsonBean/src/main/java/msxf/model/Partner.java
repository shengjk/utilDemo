package msxf.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Created by landscape.
 *
 * @XstreamOmitField 忽略字段
    这相当于设置某些字段为临时属性，在转换中不再起作用。
    @XStreamConverter(XXX.class)  转换器
    XXX.class是一个实现了com.thoughtworks.xstream.converters.Converter接口的转换器，对某些类型的值进行转换，比如布尔值类型的true或false，如果不加转换器，默认生成的值就是true或false。xstream自带了BooleanConverter转换器，可以将默认值转换成需要的文本值，如果xstream没有需要的转换器就得自己实现Converter接口来自定义转换器。
    根据大象的经验，为了少给自己找麻烦，比如避免使用转换器，最好将与XML元素或属性对应的Java对象属性都设置成String类型，当然列表还是要定义成List类型的。只要不是特别奇葩，一般情况下，示例部分就能满足绝大部分的需求。
 */
public class Partner {
// 别名注解，这个别名就是XML文档中的元素名，Java的属性名不一定要与别名一致
    @XStreamAsAttribute()
    @XStreamAlias("Encoding")
    private String Encoding;
    @XStreamAsAttribute()
    private String ip;
    @XStreamAsAttribute()
    private String tableName;
    // 属性注解，此price就是book的属性，在XML中显示为：<book price="108">
    @XStreamAsAttribute()
    @XStreamAlias("workDir")
    private String workDir;

    private Parameters parameters;

    public Partner() {
    }

    public Partner(String encoding, String tableName, String workDir, String ip, Parameters parameters) {
        Encoding = encoding;
        this.tableName = tableName;
        this.workDir = workDir;
        this.ip = ip;
        this.parameters = parameters;
    }

    public void setEncoding(String encoding) {
        Encoding = encoding;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getWorkDir() {
        return workDir;
    }

    public void setWorkDir(String workDir) {
        this.workDir = workDir;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public String getEncoding() {

        return Encoding;
    }

}
