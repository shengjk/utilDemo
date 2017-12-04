package util;

import java.lang.reflect.Field;

public class MyToolsReflex {
	public static void setAllComponentsName(Object f) {
        // 获取f对象对应类中的所有属性域
        Field[] fields = f.getClass().getDeclaredFields();
        for(int i = 0 , len = fields.length; i < len; i++) {
            // 对于每个属性，获取属性名
            String varName = fields[i].getName();
            try {
                // 获取原来的访问控制权限
                boolean accessFlag = fields[i].isAccessible();
                // 修改访问控制权限
                fields[i].setAccessible(true);
                // 获取在对象f中属性fields[i]对应的对象中的变量
//                Object o = fields[i].get(f);
                Object o = fields[i].get(f)==null?" ":fields[i].get(f);
                System.out.println("传入的对象中包含一个如下的变量：" + varName +  " = " + o);
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args)
    {
            //  测试代码用来获取一个JLabel中声名的所有的属性名和其属性变量
        
//            setAllComponentsName(new People("abf",12));
//            setAllComponentsName(11);
        boolean b = null instanceof Object;
		System.out.println(b);
		
    }
 }