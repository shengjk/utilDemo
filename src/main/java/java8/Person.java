package xmht.java8;

/**
 * @author shengjk1
 * @date 2019/11/25
 */
public interface Person {
	long getId();
	default String getName(){return "aaaa";}
}
