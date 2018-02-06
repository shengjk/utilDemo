package javasssss;

import scala.math.Ordered;

import java.io.Serializable;

/**
 * Created by 小省 on 2016/6/22.
 */
public class SecondSortKey implements Serializable, Ordered<SecondSortKey> {
	private int first;
	private int second;
	
	@Override
	public String toString() {
		return "SecondSortKey [first=" + first + ", second=" + second + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + first;
		result = prime * result + second;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SecondSortKey other = (SecondSortKey) obj;
		if (first != other.first)
			return false;
		if (second != other.second)
			return false;
		return true;
	}

	public SecondSortKey(int first, int second) {
		super();
		this.first = first;
		this.second = second;
	}

	@Override
	public int compare(SecondSortKey that) {
		if (this.first - that.getFirst() != 0) {
			return this.first - that.getFirst();

		} else {
			return this.second - that.getSecond();
		}
	}
	
	@Override
	public int compareTo(SecondSortKey that) {
		if (this.first - that.getFirst() != 0) {
			return this.first - that.getFirst();
		}else {
			//第一个跟第二个相反的排序规则
			return -(this.second - that.getSecond());
		}
		
	}
	
	
	@Override
	public boolean $greater(SecondSortKey that) {
		if(this.first>that.first){
			return true;
		}else if(this.first==that.getFirst() && this.second>that.getSecond()){
			return true;

		}
		return false;
	}
	
	
	@Override
	public boolean $greater$eq(SecondSortKey that) {
		if(this.$greater(that)){
			return true;
		}else  if(this.first==that.getFirst() && this.second==that.getSecond()){
			return true;
			
		}
		return false;
	}
	
	@Override
	public boolean $less(SecondSortKey that) {
		if(this.first<that.first){
			return true;
		}else if(this.first==that.getFirst() && this.second<that.getSecond()){
			return true;
			
		}
		return false;
	}

	@Override
	public boolean $less$eq(SecondSortKey that) {
		if(this.$less(that)){
			return true;
		}else if(this.first==that.getFirst() && this.second==that.getSecond()){
			return true;

		}
		return false;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}
}
