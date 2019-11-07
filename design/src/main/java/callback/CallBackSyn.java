package callback;

public class CallBackSyn {
	public static void main(String[] args) {
		int a = 10452;
		int b = 423;
		//实例化计算器Calculator类
		CalculatorSyn calculator = new CalculatorSyn(a, b);
		//调用计算器calculator的计算函数
		calculator.calculation();
		//控制台输出
		System.out.println("/========================/");
	}
}

//回调接口
interface CallBackInterfaceSyn {
	//计算的结果回调函数
	public void calculationResult(int a, int b, int result);
}

//计算的具体逻辑类
class LogicSyn {
	//计算的具体逻辑(计算方式)
	public void calculationLogic(int a, int b, CallBackInterfaceSyn callBackInterface) {
		int result = a + b;
		//利用传进来的对象,回调计算结果.
		callBackInterface.calculationResult(a, b, result);
	}
}

//计算器类,实现了回调接口,用于本类的实例化对象传值
class CalculatorSyn implements CallBackInterfaceSyn {
	private int a, b;
	
	public CalculatorSyn(int a, int b) {
		this.a = a;
		this.b = b;
	}
	
	public void calculation() {
        /*匿名实例化计算的具体逻辑类Logic,并调用计算函数.
        this是本类对象,因为实现了回调接口CallBackInterface,所以可以传值.*/
		new LogicSyn().calculationLogic(a, b, this);
	}
	
	//因为实现了回调接口CallBackInterface,必须要重写计算的结果回调函数calculationResult
	public void calculationResult(int a, int b, int result) {
		//控制台输出
		System.out.println(a + " + " + b + " = " + result);
	}
}