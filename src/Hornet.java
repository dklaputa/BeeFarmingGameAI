import java.awt.*;
import java.awt.image.*;
import java.util.*;
public class Hornet extends Bee{
	private int id;
	private boolean dead=false;
	private boolean seen=false;
	int count=0,f=1,see=0,c=0,d=0,e=0,h=0,k=0;
	public Hornet(int id,int x, int y, double angle,boolean isAlive,Image img){
		super(id,x,y,angle,isAlive,img);
		this.id = id;
	}
	
	/**此方法是需要重写的核心代码，蜜蜂采蜜的主要个性在此类体现*/
	public void search(){
		
		
		String strVision = BeeFarming.search(id);
		//System.out.println(strVision);
			
		String[] strs = strVision.split("~");
			//如果碰到*为首的字符串，代表遇到了边，这里是随机顺时针旋转90度以内的角度
		for (int i = 0; i < strs.length; i++) {
			
			if (strs[i].indexOf('*') == 0) {
				String strTmp = strs[0];
				String s = strTmp.substring(1, 2);
				k=1;
		//		System.out.println("第"+c+"次遇到边");
				c++;		
						Random ra = new Random();
							angle += ra.nextInt(90);
							ratoteImage(angle);
				//			System.out.println("出去了，随机转角度"+angle);	
						if(see==1)
						{
							c=0;
							break;
							//if(see==1){
							//	System.out.println("蜜蜂带到了边界，返回");
							//	c=0;
							//	angle += 180;
							//	ratoteImage(angle);
							//	break;
							//}
						//}else{
						
							
						}
						/*else if(c>5){
							c=0;
							Random ra = new Random();
							angle += ra.nextInt(90);
							ratoteImage(angle);
							System.out.println("出去了，随机转角度"+angle);
							
						}*/
				
				//System.out.println(id + " 碰到边========"+see);
			}
			if (see==0&&strs[i].indexOf('-') == 0) {
		//		System.out.println("我遇到了花而且没有蜜蜂");
				h=1;
				String strTmp = strs[i];
				int start = strTmp.indexOf('(');
				int end = strTmp.indexOf(',');
				String s = strTmp.substring(start + 1, end);
				strTmp = strTmp.substring(end + 1);
				end = strTmp.indexOf(')');
				s = strTmp.substring(0, end);
				if (!s.equals("ON")) {
						double a = new Double(s).doubleValue();
						angle = a;
						ratoteImage(angle);
						//angle +=0;
					//	ratoteImage(angle);
			//			System.out.println("遇到花时没有蜜蜂往花炮");
						}
					//else{
					//	System.out.println("遇到花时正在追蜜蜂直接往花中心走");
					//double a = new Double(s).doubleValue();
					//angle = a;
					//ratoteImage(angle);
					//break;
					
				//	}
					//System.out.println("旋转："+angle);
				//else
					//System.out.println("ON flower");
			}
			else if(strs[i].indexOf('+')==0)
			{
				String strTmp = strs[i];				
				String []s = strTmp.split(",");
	//			System.out.println("遇到了蜜蜂");
		
				double a = new Double(s[1]).doubleValue();
				angle = a;
				
				
			
				
				
				ratoteImage(angle);
	//			System.out.println("旋转："+angle+"跟着蜜蜂");
				see=1;
				d=0;
	//			System.out.println("跟了蜜蜂"+e+"个周期");
				e++;
				if(e>60){
					
					e=0;
					angle += 90;
					ratoteImage(angle);
	//				System.out.println("可能被迷惑了，不要你了");
				}
				
				
				double a1=a;
				int end=s[2].indexOf(')');
				String s2=s[2].substring(0,end);
				double a2=new Double(s2).doubleValue();
		//		System.out.println("何珂"+a2);
				a1=(a1+360)%360;
				a2=(a2+360)%360;
				if(a1<180)
				{
					if((a2-a1)>135&&(a2-a1)<225)
					{
						if(a2-a1>180)	ratoteImage(2*a1+180-a2);
						else ratoteImage(2*a1+180-a2);
			//			System.out.println("何珂");
					}
				}
				
				if(a1>180)
				{
					if((a1-a2)>135&&(a1-a2)<225)
					{
						if(a1-a2>180)	ratoteImage(2*a1-180-a2);
						else ratoteImage(2*a1-a2-180);
		//				System.out.println("何珂");
					}
						
				}
			} 
			else{
				count++;
	//			System.out.println("什么也没有遇到");
				h=0;
				d++;
				see=0;
				//if(k==0)
			//	c=0;
				if(d==1){
		//			System.out.println("刚刚被蜜蜂甩掉了");
					e=0;
					Random ra = new Random();
					angle += 180+ra.nextInt(45);
				ratoteImage(angle);
		//		System.out.println("试一下转身看能不能追回来");
				}
				if(count>9*f&&see==0&&d>1&&h==0){
			//		System.out.println("定时绕圈");
					count=0;
					
					if(f%4==2){
						angle+=90;
					ratoteImage(angle);
					}
					else if(f%4==3){
						angle+=90;
						ratoteImage(angle);
						f++;
					}
					else if(f%4==0){
						angle+=90;
						ratoteImage(angle);
					}
					else
					f++;
					if(f>8){
						f=1;
					//	angle+=180;
					//	ratoteImage(angle);
					}
						
					
				
				}
			}
		}
			
		//	System.out.println("：-.////////////--"+count);
		setXYs(0);
		
	}

	/**如果黄蜂抓到了蜜蜂，则boolean dead==true，黄蜂可以根据dead的值判断蜜蜂知否被杀死。
	本方法可以修改，在BeeFarming的killBee方法中当蜜蜂被黄蜂消灭后将被调用*/
	public boolean isCatched(){
	    dead = true;
	    return dead;
	}
	  
}