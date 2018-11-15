package callback;

/*
扩展知识:另一种回调(同步)，主要是为解决当实现的逻辑不明确时的问题
 */
public class CallBack
{
     public static void main(String []args)
     {
        int aa = 10452;
        int bb = 423;
        //实例化计算器Calculator类,并传一个Logic对象
        Calculator calculator = new Calculator(new Logic()
        {
            //重写计算逻辑函数calculationLogic,实现具体计算逻辑
            public void calculationLogic(int a, int b){
                int result = a * b;
                System.out.println(a + " * " + b + " = " + result);
            }
        });
        //调用计算器calculator的计算函数calculation
        calculator.calculation(aa, bb);
        //控制台输出
        System.out.println("/========================/");
     }
}

//计算的逻辑回调接口
interface Logic
{
    //计算的逻辑回调函数
     public void calculationLogic(int a, int b);
}

//计算器类
class Calculator
{
    private Logic logic;

    public Calculator(Logic logic)
    {
        this.logic = logic;
    }

    public void calculation(int aa, int bb)
    {
        //调用logic对象里的计算逻辑函数calculationLogic
        logic.calculationLogic(aa, bb);
    }
}