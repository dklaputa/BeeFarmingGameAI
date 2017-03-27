import java.awt.*;
import java.awt.image.*;
import java.util.*;

public class HoneyBee extends Bee{
	
	private int id;
	private boolean isDanger=false;
	private int num=0;
	
	static int numFlower=0;
	private int safeTime=0;
	private static int tellOthersDanger[]={0,0,0};
	private int EscapeOrientation=0;
	private int numEscape=0;
	private int numAlert=0;
	private double foundFlower[]=null;
	private static ArrayList AngleAndPos[]={new ArrayList(),new ArrayList(),new ArrayList(),new ArrayList()};
	private static rangeAssignment ra=new rangeAssignment();
	private int isEscape;
	private int riskDegree;
	static int actionRange[]=new int[3];//Range (0,左边 1,中间 2,右边) (3,左边 4,右边) (5,无限制)
	private static int actionMode[]=new int[3];//Mode (0,安全 1,逃跑)
	private static int Alive[]={0,0,0};
	private static double currentLocation[][]=new double[3][2];
	private static int outTime;
	
	public HoneyBee(int id,int x, int y, double angle,boolean isAlive,Image img){
		super(id,x,y,angle,isAlive,img);
		this.id = id;

		currentLocation[id][0]=x;
		currentLocation[id][1]=y;
	}
	
	/**此方法是需要重写的核心代码，蜜蜂采蜜的主要个性在此类体现*/
	public void search(){
		currentLocation[id][0]=getCurrentPosition().getX();
		currentLocation[id][1]=getCurrentPosition().getY();
//		getSeperate(id);
		isEscape--;
		int foundpartner=-1;
		boolean foundhornet=false;
//		if(isEscape<0) System.out.println(id+"反向逃跑中");
		stillAliveNumber();
		System.out.println(id+" "+actionMode[id]+" "+tellOthersDanger[id]);
		if (actionMode[id] != 1) {
			if (tellOthersDanger[id] == 1) {
				numEscape = 0;
				actionMode[id] = 1;
				tellOthersDanger[id] = 0;
			} else if (tellOthersDanger[id] == 2) {
				safeTime = 0;
				actionMode[id] = 0;
				tellOthersDanger[id] = 0;
			} else if (tellOthersDanger[id] == 3) {
				numAlert = 0;
				actionMode[id] = 2;
				tellOthersDanger[id] = 0;
			}
			if (safeTime > 3 && tellOthersDanger[id] != 2) {
				tellOthersDanger[id] = 3;
			}
		}
		if(numEscape>3){
			actionMode[id]=2;
			numAlert=-1;
			numEscape=0;
		}
		if(numAlert>1){
			actionMode[id]=0;
			numAlert=0;
		}
		

	//	if (actionMode[id]==0&&isEscape<0) {
	//		isEscape=false;
	//	}
		if (actionMode[id]==1) {
			for(int j=0;j<3;j++){
				if(j!=id) tellOthersDanger[j]=2;
			}
			safeTime=0;
			if (isEscape<0) {
		//		System.out.println(id + "反向逃跑中");
		//		System.out.println(id + "执行" + actionMode[id] + " " + isEscape);
				isEscape = 5;
				while (!rotate(EscapeOrientation%4)) {
					EscapeOrientation++;
				}
				numEscape++;
			}
			String strVision = BeeFarming.search(id);
			String[] strs = strVision.split("~");
			for (int i = 0; i < strs.length; i++) {
				//如果碰到-为首的字符串，代表遇到了花，这里是向其中一朵花飞（即使同时看到多个花）
				if (strs[i].indexOf('-') == 0) {
					String strTmp = strs[i];
					int start = strTmp.indexOf('(');
					int end = strTmp.indexOf(',');
					String s = strTmp.substring(start + 1, end);
					int honey = new Integer(s).intValue();
					strTmp = strTmp.substring(end + 1);
					end = strTmp.indexOf(')');
					s = strTmp.substring(0, end);
					if (!s.equals("ON")) {
						double a = new Double(s).doubleValue();
						if(getCurrentPosition().getX()<270){
							AngleAndPos[0].add(new double[]{0,getCurrentPosition().getX(),getCurrentPosition().getY(),a});
						}
						else if(getCurrentPosition().getX()<400&&getCurrentPosition().getX()>=270){
							AngleAndPos[1].add(new double[]{1,getCurrentPosition().getX(),getCurrentPosition().getY(),a});
						}
						else if(getCurrentPosition().getX()<530&&getCurrentPosition().getX()>=400){
							AngleAndPos[2].add(new double[]{2,getCurrentPosition().getX(),getCurrentPosition().getY(),a});
						}
						else {
							AngleAndPos[3].add(new double[]{3,getCurrentPosition().getX(),getCurrentPosition().getY(),a});
						}
						System.out.println("存入");
					//	angle = a+45;
					//	ratoteImage(angle);
					}
				}
			}
		}
		else if (actionMode[id]==2) {
			safeTime=0;
			System.out.println("警惕"+numAlert);
			numAlert++;
			setRange(actionRange[id]);
			String strVision = BeeFarming.search(id);
			String[] strs = strVision.split("~");
			for (int i = 0; i < strs.length; i++) {
			/*	if (strs[i].indexOf('*') == 0) {
					String strTmp = strs[i];
					String s = strTmp.substring(1, 2);
					//	Random ra = new Random();
					//	angle += ra.nextInt(90);
					ratoteImage(angle);
					if (s.equals("W")) {
						rotate(2);
						//	angle = ra.nextInt(180) - 90;
					} else if (s.equals("E")) {
						rotate(0);
						//	angle = ra.nextInt(180) + 90;
					} else if (s.equals("S")) {
						rotate(3);
						//	angle = -ra.nextInt(180);
					} else if (s.equals("N")) {
						rotate(1);
						//	angle = ra.nextInt(180);
					}
					//	System.out.println(id + " 碰到边");
				}*/
				//如果碰到-为首的字符串，代表遇到了花，这里是向其中一朵花飞（即使同时看到多个花）
				if (strs[i].indexOf('-') == 0) {
					String strTmp = strs[i];
					int start = strTmp.indexOf('(');
					int end = strTmp.indexOf(',');
					String s = strTmp.substring(start + 1, end);
					int honey = new Integer(s).intValue();
					strTmp = strTmp.substring(end + 1);
					end = strTmp.indexOf(')');
					s = strTmp.substring(0, end);
					if (!s.equals("ON")) {
						double a = new Double(s).doubleValue();
						if(getCurrentPosition().getX()<270){
							AngleAndPos[0].add(new double[]{0,getCurrentPosition().getX(),getCurrentPosition().getY(),a});
						}
						else if(getCurrentPosition().getX()<400&&getCurrentPosition().getX()>=270){
							AngleAndPos[1].add(new double[]{1,getCurrentPosition().getX(),getCurrentPosition().getY(),a});
						}
						else if(getCurrentPosition().getX()<530&&getCurrentPosition().getX()>=400){
							AngleAndPos[2].add(new double[]{2,getCurrentPosition().getX(),getCurrentPosition().getY(),a});
						}
						else {
							AngleAndPos[3].add(new double[]{3,getCurrentPosition().getX(),getCurrentPosition().getY(),a});
						}
					//	angle = a;
						
					//	angle = -a;
					//	ratoteImage(-angle);
					}
				}
				else if (strs[i].indexOf('+') == 0) {
					String strTmp = strs[i];
					int start = strTmp.indexOf('(');
					int end = strTmp.indexOf(',');
					String s = strTmp.substring(start + 1, end);
					int meetID = new Integer(s).intValue();
					if (meetID == 9) {
				//		foundhornet=true;
						strTmp = strTmp.substring(end + 1);
						end = strTmp.indexOf(',');
						s = strTmp.substring(0, end);
						double angle1= new Double(s).doubleValue();
						strTmp = strTmp.substring(end + 1);
						end = strTmp.indexOf(')');
						s = strTmp.substring(0, end);
						double angle2= new Double(s).doubleValue();
						if (Math.abs((angle1-angle2)%360)<=270&&Math.abs((angle1-angle2)%360)>=90) {
							angle = angle1+180;
							ratoteImage(angle);
							actionMode[id] = 1;
							
						}
					}foundpartner=getSeperate(id);
						if((foundpartner!=-1)){
							tellOthersDanger[foundpartner]=1;
						}
						for(int j=0;j<3;j++){
							if(j!=id&&j!=foundpartner) tellOthersDanger[j]=2;
						}
						System.out.println("看到马蜂");
				}
			}
		}
		else if (actionMode[id]==0) {
			safeTime++;
			isEscape=15;
			//	System.out.println(id+" "+Math.abs(angle%360));
			String strVision = BeeFarming.search(id);
			getSeperate(id);
			if(strVision.equals("")&&setRange(actionRange[id])){
				if((Math.pow(getCurrentPosition().getX()-foundFlower[1],2)+Math.pow(getCurrentPosition().getY()-foundFlower[2],2))<2500){
				//	angle=foundFlower[3];
				//	ratoteImage(angle);
					System.out.println("转向入范围");
					AngleAndPos[(int) foundFlower[0]].remove(0);
				}
				else{
					angle=BeeFarming.getVectorDegree((int)getCurrentPosition().getX(),(int) getCurrentPosition().getY(),(int) foundFlower[1],(int) foundFlower[2]);
					ratoteImage(angle);
					System.out.println((Math.abs(getCurrentPosition().getX()-foundFlower[0])+"转向"));
				}
			}
			else {
				String[] strs = strVision.split("~");
				for (int i = 0; i < strs.length; i++) {
					//如果碰到*为首的字符串，代表遇到了边，这里是随机顺时针旋转90度以内的角度
					if (strs[i].indexOf('*') == 0) {
						outTime++;
						Random rd=new Random();
						if (outTime>=0) {
							String strTmp = strs[i];
							String s = strTmp.substring(1, 2);
							//	Random ra = new Random();
							//	angle += ra.nextInt(90);
							ratoteImage(angle);
							if (s.equals("W")) {
								rotate(2);
								//	angle = ra.nextInt(180) - 90;
							} else if (s.equals("E")) {
								rotate(0);
								//	angle = ra.nextInt(180) + 90;
							} else if (s.equals("S")) {
								rotate(3);
								//	angle = -ra.nextInt(180);
							} else if (s.equals("N")) {
								rotate(1);
								//	angle = ra.nextInt(180);
							}
							//	System.out.println(id + " 碰到边");
							outTime=0;
						}
					}
					//如果碰到-为首的字符串，代表遇到了花，这里是向其中一朵花飞（即使同时看到多个花）
					else if (strs[i].indexOf('-') == 0) {
						String strTmp = strs[i];
						int start = strTmp.indexOf('(');
						int end = strTmp.indexOf(',');
						String s = strTmp.substring(start + 1, end);
						int honey = new Integer(s).intValue();
						strTmp = strTmp.substring(end + 1);
						end = strTmp.indexOf(')');
						s = strTmp.substring(0, end);
						if (!s.equals("ON")) {
							double a = new Double(s).doubleValue();
							angle = a;
							ratoteImage(angle);
							//System.out.println("旋转："+angle);
						}
						else{
							numFlower++;
						}
							//System.out.println("ON flower");
					}
					else if (strs[i].indexOf('+') == 0) {
						String strTmp = strs[i];
						int start = strTmp.indexOf('(');
						int end = strTmp.indexOf(',');
						String s = strTmp.substring(start + 1, end);
						int meetID = new Integer(s).intValue();
						if (meetID == 9) {
					//		foundhornet=true;
							strTmp = strTmp.substring(end + 1);
							end = strTmp.indexOf(',');
							s = strTmp.substring(0, end);
							double angle1= new Double(s).doubleValue();
							strTmp = strTmp.substring(end + 1);
							end = strTmp.indexOf(')');
							s = strTmp.substring(0, end);
							double angle2= new Double(s).doubleValue();
							if (Math.abs((angle1-angle2)%360)<=270&&Math.abs((angle1-angle2)%360)>=90) {
								angle = angle1+180;
								ratoteImage(angle);
								actionMode[id] = 1;
								
							}
							foundpartner=getSeperate(id);
							if((foundpartner!=-1)){
								tellOthersDanger[foundpartner]=1;
							}
							for(int j=0;j<3;j++){
								if(j!=id&&j!=foundpartner) tellOthersDanger[j]=2;
							}
							System.out.println("看到马蜂");
						}
					}
				}
			}
		}
		setXYs(0);
	}
	private int getSeperate(int id){
		for(int i=0;i<3;i++){
			if(i!=id){
				double distance=Math.pow(currentLocation[id][0]-currentLocation[i][0],2)+Math.pow(currentLocation[id][1]-currentLocation[i][1],2);
				if(distance<10000){
					if(actionMode[i]==1) tellOthersDanger[id]=1;
		if (isSetRange(actionRange[id])) {
			//			Random rd=new Random();
			if (currentLocation[id][0] >= currentLocation[i][0]
					&& currentLocation[id][1] >= currentLocation[i][1])
				angle = 45;
			else if (currentLocation[id][0] <= currentLocation[i][0]
					&& currentLocation[id][1] >= currentLocation[i][1])
				angle = 135;
			else if (currentLocation[id][0] >= currentLocation[i][0]
					&& currentLocation[id][1] <= currentLocation[i][1])
				angle = -45;
			else if (currentLocation[id][0] <= currentLocation[i][0]
					&& currentLocation[id][1] <= currentLocation[i][1])
				angle = -135;
			ratoteImage(angle);
		}
					System.out.println("近");
				//	while (!rotate(EscapeOrientation%4)) {
				//		EscapeOrientation++;
				//	}
					return i;
				}
			}
		}
		return -1;
	}
	
	private boolean isSetRange(int range){
		switch (range) {
		case 0:
			if (getCurrentPosition().getX() > 280
					&& (Math.abs(angle % 360) > 270 || Math.abs(angle % 360) < 90)) {
		//		Random ra = new Random();
			//	rotate(0);
				return true;
			//	angle = ra.nextInt(180) + 90;
			}
			break;
		case 1:
			if (getCurrentPosition().getX() < 260 && Math.abs(angle % 360) > 90
					&& Math.abs(angle % 360) < 270) {
		//		Random ra = new Random();
			//	rotate(2);
				return true;
		//		angle = ra.nextInt(180) - 90;
			} else if (getCurrentPosition().getX() > 540
					&& (Math.abs(angle % 360) > 270 || Math.abs(angle % 360) < 90)) {
		//		Random ra = new Random();
			//	rotate(0);
				return true;
		//		angle = ra.nextInt(180) + 90;
			}
			break;
		case 2:
			if (getCurrentPosition().getX() < 520 && Math.abs(angle % 360) > 90
					&& Math.abs(angle % 360) < 270) {
				Random ra = new Random();
			//	rotate(2);
				return true;
		//		angle = ra.nextInt(180) - 90;
			}
			break;
		case 3:
			if (getCurrentPosition().getX() > 410
					&& (Math.abs(angle % 360) > 270 || Math.abs(angle % 360) < 90)) {
		//		Random ra = new Random();
			//	rotate(0);
				return true;
		//		angle = ra.nextInt(180) + 90;
			}
			break;
		case 4:
			if (getCurrentPosition().getX() < 390 && Math.abs(angle % 360) > 90
					&& Math.abs(angle % 360) < 270) {
		//		Random ra = new Random();
			//	rotate(2);
				return true;
		//		angle = ra.nextInt(180) - 90;
			}
			break;
		case 5:
			break;
		}
		return false;
	}
	
	private boolean setRange(int range){
		switch (range) {
		case 0:
			if (getCurrentPosition().getX() > 280
					&& (Math.abs(angle % 360) > 270 || Math.abs(angle % 360) < 90)) {
		//		Random ra = new Random();
				rotate(0);
			//	angle = ra.nextInt(180) + 90;
			}
			if(!AngleAndPos[0].isEmpty()){
				foundFlower=(double[]) AngleAndPos[0].get(0);
				return true;
			}
			break;
		case 1:
			if (getCurrentPosition().getX() < 260 && Math.abs(angle % 360) > 90
					&& Math.abs(angle % 360) < 270) {
		//		Random ra = new Random();
				rotate(2);
		//		angle = ra.nextInt(180) - 90;
			} else if (getCurrentPosition().getX() > 540
					&& (Math.abs(angle % 360) > 270 || Math.abs(angle % 360) < 90)) {
		//		Random ra = new Random();
				rotate(0);
		//		angle = ra.nextInt(180) + 90;
			}
			if(!AngleAndPos[1].isEmpty()){
				foundFlower=(double[]) AngleAndPos[1].get(0);
				return true;
			}
			else if(!AngleAndPos[2].isEmpty()){
				foundFlower=(double[]) AngleAndPos[2].get(0);
				return true;
			}
			break;
		case 2:
			if (getCurrentPosition().getX() < 520 && Math.abs(angle % 360) > 90
					&& Math.abs(angle % 360) < 270) {
				Random ra = new Random();
				rotate(2);
		//		angle = ra.nextInt(180) - 90;
			}
			if(!AngleAndPos[3].isEmpty()){
				foundFlower=(double[]) AngleAndPos[3].get(0);
				return true;
			}
			break;
		case 3:
			if (getCurrentPosition().getX() > 410
					&& (Math.abs(angle % 360) > 270 || Math.abs(angle % 360) < 90)) {
		//		Random ra = new Random();
				rotate(0);
		//		angle = ra.nextInt(180) + 90;
			}
			if(!AngleAndPos[0].isEmpty()){
				foundFlower=(double[]) AngleAndPos[0].get(0);
				return true;
			}
			else if(!AngleAndPos[1].isEmpty()){
				foundFlower=(double[]) AngleAndPos[1].get(0);
				return true;
			}
			break;
		case 4:
			if (getCurrentPosition().getX() < 390 && Math.abs(angle % 360) > 90
					&& Math.abs(angle % 360) < 270) {
		//		Random ra = new Random();
				rotate(2);
		//		angle = ra.nextInt(180) - 90;
			}
			if(!AngleAndPos[2].isEmpty()){
				foundFlower=(double[]) AngleAndPos[2].get(0);
				return true;
			}
			else if(!AngleAndPos[3].isEmpty()){
				foundFlower=(double[]) AngleAndPos[3].get(0);
				return true;
			}
			break;
		case 5:
			if(!AngleAndPos[0].isEmpty()){
				foundFlower=(double[]) AngleAndPos[0].get(0);
				return true;
			}
			else if(!AngleAndPos[1].isEmpty()){
				foundFlower=(double[]) AngleAndPos[1].get(0);
				return true;
			}
			else if(!AngleAndPos[2].isEmpty()){
				foundFlower=(double[]) AngleAndPos[2].get(0);
				return true;
			}
			else if(!AngleAndPos[3].isEmpty()){
				foundFlower=(double[]) AngleAndPos[3].get(0);
				return true;
			}
			break;
		}
		return false;
	}
	
	private Point getCurrentPosition(){
		return this.getLocation();
	}
	
	private void stillAliveNumber(){
		ra.removeAll();
	//	System.out.println(actionMode[id]+" "+id);
		Alive[id]++;
		int count=0;
		if((Alive[id]-Alive[0])<2) count++;
		if((Alive[id]-Alive[1])<2) count++;
		if((Alive[id]-Alive[2])<2) count++;
		if(count==3){
			for(int i=0;i<3;i++){
				if(actionMode[i]!=1) ra.add(i);
			}
		}
		else if(count==2){
			if((Alive[id]-Alive[0])>1){
				actionMode[0]=3;
				if(actionMode[1]!=1) ra.add(1);
				if(actionMode[2]!=1) ra.add(2);
			}
			else if((Alive[id]-Alive[1])>1){
				actionMode[1]=3;
				if(actionMode[0]!=1) ra.add(0);
				if(actionMode[2]!=1) ra.add(2);
			}
			else {
				actionMode[2]=3;
				if(actionMode[0]!=1) ra.add(0);
				if(actionMode[1]!=1) ra.add(1);
			}
		}
		else if(count==1){
			for(int i=0;i<3;i++){
				if(i!=id) actionMode[i]=3;
			}
			if(actionMode[id]!=1) ra.add(id);
		}
		System.out.println("count="+(20-numFlower));
	}
	
	private boolean rotate(int edge){
		Random rd=new Random();
		if(((angle % 360)>=270&&edge==1)||((angle % 360)<=90&&(angle % 360)>=0&&edge==0)||
				((angle % 360)>=90&&(angle % 360)<=180&&edge==3)||((angle % 360)<=270&&(angle % 360)>=180&&edge==2)||
				((angle % 360)>=-90&&(angle % 360)<=0&&edge==1)||((angle % 360)<=-270&&edge==0)||
				((angle % 360)>=-270&&(angle % 360)<=-180&&edge==3)||((angle % 360)<=-90&&(angle % 360)>=-180&&edge==2)){
			angle=angle+(rd.nextInt(30)+90);
			ratoteImage(angle);
			return true;
		}
		else if(((angle % 360)>=270&&edge==0)||((angle % 360)<90&&(angle % 360)>=0&&edge==3)||
				((angle % 360)>=90&&(angle % 360)<180&&edge==2)||((angle % 360)<270&&(angle % 360)>=180&&edge==1)||
				((angle % 360)>=-90&&(angle % 360)<0&&edge==0)||((angle % 360)<-270&&edge==3)||
				((angle % 360)>=-270&&(angle % 360)<-180&&edge==2)||((angle % 360)<-90&&(angle % 360)>=-180&&edge==1)){
			angle=angle-(rd.nextInt(30)+90);
			ratoteImage(angle);
			return true;
		}
		else return false;
	}
}

class rangeAssignment{
	private ArrayList<Integer> ids=new ArrayList<Integer>();
	void add(int id){
		ids.add(id);
		assignment();
	}
	void removeAll(){
		ids.removeAll(ids);
	}
	void assignment(){
		if(20-HoneyBee.numFlower>0){
			if(ids.size()==3) {
				for(int i=0;i<3;i++){
					HoneyBee.actionRange[ids.get(i)]=i;
				}
			}
			else if(ids.size()==2) {
				HoneyBee.actionRange[ids.get(0)]=3;
				HoneyBee.actionRange[ids.get(1)]=4;
			}
			else if(ids.size()==1) {
				HoneyBee.actionRange[ids.get(0)]=5;
			}
		}
		else {
			for(int i=0;i<ids.size();i++){
				HoneyBee.actionRange[ids.get(i)]=5;
			}
		}
	}
}
