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
	
	/**�˷�������Ҫ��д�ĺ��Ĵ��룬�۷���۵���Ҫ�����ڴ�������*/
	public void search(){
		
		
		String strVision = BeeFarming.search(id);
		//System.out.println(strVision);
			
		String[] strs = strVision.split("~");
			//�������*Ϊ�׵��ַ��������������˱ߣ����������˳ʱ����ת90�����ڵĽǶ�
		for (int i = 0; i < strs.length; i++) {
			
			if (strs[i].indexOf('*') == 0) {
				String strTmp = strs[0];
				String s = strTmp.substring(1, 2);
				k=1;
		//		System.out.println("��"+c+"��������");
				c++;		
						Random ra = new Random();
							angle += ra.nextInt(90);
							ratoteImage(angle);
				//			System.out.println("��ȥ�ˣ����ת�Ƕ�"+angle);	
						if(see==1)
						{
							c=0;
							break;
							//if(see==1){
							//	System.out.println("�۷�����˱߽磬����");
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
							System.out.println("��ȥ�ˣ����ת�Ƕ�"+angle);
							
						}*/
				
				//System.out.println(id + " ������========"+see);
			}
			if (see==0&&strs[i].indexOf('-') == 0) {
		//		System.out.println("�������˻�����û���۷�");
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
			//			System.out.println("������ʱû���۷�������");
						}
					//else{
					//	System.out.println("������ʱ����׷�۷�ֱ������������");
					//double a = new Double(s).doubleValue();
					//angle = a;
					//ratoteImage(angle);
					//break;
					
				//	}
					//System.out.println("��ת��"+angle);
				//else
					//System.out.println("ON flower");
			}
			else if(strs[i].indexOf('+')==0)
			{
				String strTmp = strs[i];				
				String []s = strTmp.split(",");
	//			System.out.println("�������۷�");
		
				double a = new Double(s[1]).doubleValue();
				angle = a;
				
				
			
				
				
				ratoteImage(angle);
	//			System.out.println("��ת��"+angle+"�����۷�");
				see=1;
				d=0;
	//			System.out.println("�����۷�"+e+"������");
				e++;
				if(e>60){
					
					e=0;
					angle += 90;
					ratoteImage(angle);
	//				System.out.println("���ܱ��Ի��ˣ���Ҫ����");
				}
				
				
				double a1=a;
				int end=s[2].indexOf(')');
				String s2=s[2].substring(0,end);
				double a2=new Double(s2).doubleValue();
		//		System.out.println("����"+a2);
				a1=(a1+360)%360;
				a2=(a2+360)%360;
				if(a1<180)
				{
					if((a2-a1)>135&&(a2-a1)<225)
					{
						if(a2-a1>180)	ratoteImage(2*a1+180-a2);
						else ratoteImage(2*a1+180-a2);
			//			System.out.println("����");
					}
				}
				
				if(a1>180)
				{
					if((a1-a2)>135&&(a1-a2)<225)
					{
						if(a1-a2>180)	ratoteImage(2*a1-180-a2);
						else ratoteImage(2*a1-a2-180);
		//				System.out.println("����");
					}
						
				}
			} 
			else{
				count++;
	//			System.out.println("ʲôҲû������");
				h=0;
				d++;
				see=0;
				//if(k==0)
			//	c=0;
				if(d==1){
		//			System.out.println("�ոձ��۷�˦����");
					e=0;
					Random ra = new Random();
					angle += 180+ra.nextInt(45);
				ratoteImage(angle);
		//		System.out.println("��һ��ת���ܲ���׷����");
				}
				if(count>9*f&&see==0&&d>1&&h==0){
			//		System.out.println("��ʱ��Ȧ");
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
			
		//	System.out.println("��-.////////////--"+count);
		setXYs(0);
		
	}

	/**����Ʒ�ץ�����۷䣬��boolean dead==true���Ʒ���Ը���dead��ֵ�ж��۷�֪��ɱ����
	�����������޸ģ���BeeFarming��killBee�����е��۷䱻�Ʒ�����󽫱�����*/
	public boolean isCatched(){
	    dead = true;
	    return dead;
	}
	  
}