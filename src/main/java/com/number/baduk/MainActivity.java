package com.number.baduk;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.number.baduk.R;

import model.Baduk;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	

    boolean typeCheck = true;
    int value = 0;
    String tempType = "black";
    String blackType = "black";
    String whiteType = "white";
    int number = 0;

	static int DEFAULT_NUM = 6;
	static Map<String, String> edgeMap = new HashMap<String, String>();
	ArrayList<Baduk> list = new ArrayList<Baduk>();
	
	int[] black = {3,3,3,3,2,2,2};
	int[] white = {3,3,3,3,2,2,2};
	
	int blackScore = 0;
	int whiteScore = 0;

	boolean gameEnd = false;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_play);

		/*
		 * black : BLACK
		 * white : WHITE
		 * N : NONE
		 * BL : BLACK BLOCK
		 * WL : WHITE BLOCK
		 */
		
		Baduk baduk = null;
		
		int width = 0;
		int height = 0;
		
		for (int i = 0; i < DEFAULT_NUM*DEFAULT_NUM; i++) {
			baduk = new Baduk();
			baduk.setType("N");
			
			if ((i+1) % DEFAULT_NUM == 0) {
				baduk.setI(width++);
				baduk.setJ(height);
				height = 0;
			} else {
				baduk.setI(width);
				baduk.setJ(height++);
			}
			
			list.add(baduk);
		}
        
        // 광고 달기 시작
        //AdView ad1 = (AdView) findViewById(R.id.adView1); //위에 xml id를 adView라고 똑같이 했을 경우입니다.
        //AdRequest adRequest1 = new AdRequest.Builder().build(); 
        //ad1.loadAd(adRequest1);
        // 광고 달기 끝
    }
    
    public void mOnClick(View v) {
    	switch (v.getId()) {
		case R.id.black1:
			value = 1;
			toastType(blackType);
			break;
		case R.id.black2:
			value = 2;
			toastType(blackType);
			break;
		case R.id.black3:
			value = 3;
			toastType(blackType);
			break;
		case R.id.black4:
			value = 4;
			toastType(blackType);
			break;
		case R.id.black5:
			value = 5;
			toastType(blackType);
			break;
		case R.id.black6:
			value = 6;
			toastType(blackType);
			break;
		case R.id.black7:
			value = 7;
			toastType(blackType);
			break;
		case R.id.white1:
			value = 1;
			toastType(whiteType);
			break;
		case R.id.white2:
			value = 2;
			toastType(whiteType);
			break;
		case R.id.white3:
			value = 3;
			toastType(whiteType);
			break;
		case R.id.white4:
			value = 4;
			toastType(whiteType);
			break;
		case R.id.white5:
			value = 5;
			toastType(whiteType);
			break;
		case R.id.white6:
			value = 6;
			toastType(whiteType);
			break;
		case R.id.white7:
			value = 7;
			toastType(whiteType);
			break;
		case R.id.g00:
			number = 0;
			toastGamePlay();
			break;
		case R.id.g01:
			number = 1;
			toastGamePlay();
			break;
		case R.id.g02:
			number = 2;
			toastGamePlay();
			break;
		case R.id.g03:
			number = 3;
			toastGamePlay();
			break;
		case R.id.g04:
			number = 4;
			toastGamePlay();
			break;
		case R.id.g05:
			number = 5;
			toastGamePlay();
			break;
		case R.id.g10:
			number = 6;
			toastGamePlay();
			break;
		case R.id.g11:
			number = 7;
			toastGamePlay();
			break;
		case R.id.g12:
			number = 8;
			toastGamePlay();
			break;
		case R.id.g13:
			number = 9;
			toastGamePlay();
			break;
		case R.id.g14:
			number = 10;
			toastGamePlay();
			break;
		case R.id.g15:
			number = 11;
			toastGamePlay();
			break;
		case R.id.g20:
			number = 12;
			toastGamePlay();
			break;
		case R.id.g21:
			number = 13;
			toastGamePlay();
			break;
		case R.id.g22:
			number = 14;
			toastGamePlay();
			break;
		case R.id.g23:
			number = 15;
			toastGamePlay();
			break;
		case R.id.g24:
			number = 16;
			toastGamePlay();
			break;
		case R.id.g25:
			number = 17;
			toastGamePlay();
			break;
		case R.id.g30:
			number = 18;
			toastGamePlay();
			break;
		case R.id.g31:
			number = 19;
			toastGamePlay();
			break;
		case R.id.g32:
			number = 20;
			toastGamePlay();
			break;
		case R.id.g33:
			number = 21;
			toastGamePlay();
			break;
		case R.id.g34:
			number = 22;
			toastGamePlay();
			break;
		case R.id.g35:
			number = 23;
			toastGamePlay();
			break;
		case R.id.g40:
			number = 24;
			toastGamePlay();
			break;
		case R.id.g41:
			number = 25;
			toastGamePlay();
			break;
		case R.id.g42:
			number = 26;
			toastGamePlay();
			break;
		case R.id.g43:
			number = 27;
			toastGamePlay();
			break;
		case R.id.g44:
			number = 28;
			toastGamePlay();
			break;
		case R.id.g45:
			number = 29;
			toastGamePlay();
			break;
		case R.id.g50:
			number = 30;
			toastGamePlay();
			break;
		case R.id.g51:
			number = 31;
			toastGamePlay();
			break;
		case R.id.g52:
			number = 32;
			toastGamePlay();
			break;
		case R.id.g53:
			number = 33;
			toastGamePlay();
			break;
		case R.id.g54:
			number = 34;
			toastGamePlay();
			break;
		case R.id.g55:
			number = 35;
			toastGamePlay();
			break;
		default:
			break;
		}
		
		for (int i = 0; i < list.size(); i++) {
			System.out.print(list.get(i).getType() + "("+list.get(i).getI()+":"+list.get(i).getJ()+")"+ i +" ");
			if ((i+1) % DEFAULT_NUM == 0) {
				System.out.println();
			}
		}
    }

	private void toastGamePlay() {
		//Toast toast = null;
		if (typeCheck && value > 0) {
			gamePlay(list, number, value, tempType);
		} else {
			//toast = Toast.makeText(getApplicationContext(),"아직 차례가 돌아오지 않았습니다.", 100);
			TextView tv = (TextView)findViewById(R.id.text01);
			tv.setText("아직 차례가 돌아오지 않았습니다.");
			typeCheck = false;
			//toast.setGravity(Gravity.CENTER, 0, 0);
			//toast.show();
		}
		
		if(gameEnd){
	        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
	        
	        TextView tv = (TextView)findViewById(R.id.score);
			tv.setText("흑 : " + blackScore + ", 백 : " + whiteScore);
			
	        // Setting Dialog Title
	        alertDialog.setTitle("알파 버전");
	 
	        // Setting Dialog Message
	        alertDialog.setMessage("흑 : " + blackScore + "," + "백 : " + whiteScore + "\n\n"+ (blackScore > whiteScore ? "흑": "백") + "이 승리했습니다. 현재 밸런스 확인 중입니다. 현재 정보를 메일발송해주시겠습니까?");
	 
	        // Setting Icon to Dialog
	        //alertDialog.setIcon(R.drawable.delete);
	 
	        // Setting Positive "Yes" Button
	        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog,int which) {
		           	Intent it = new Intent(Intent.ACTION_SEND);
		           	String[] mailaddr = {"ku9cu@naver.com","ccamnae@naver.com"};
		            it.setType("plaine/text");
		            it.putExtra(Intent.EXTRA_EMAIL, mailaddr); // 받는사람
		            it.putExtra(Intent.EXTRA_SUBJECT, "[테스트 결과 : ]" + (blackScore > whiteScore ? "흑": "백") + "이 승리했습니다."); // 제목
		            it.putExtra(Intent.EXTRA_TEXT, "\n\n" + "흑 : " + blackScore + "," + "백 : " + whiteScore + "\n\n"); // 첨부내용
		
		            startActivity(it);
		            Toast.makeText(getApplicationContext(), "데이터 수집 감사합니다.", 1000).show();
		            dialog.cancel();
	            }
	        });
	 
	        // Setting Negative "NO" Button
	        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	
	            //Toast.makeText(getApplicationContext(), "You clicked on NO", 100).show();
	            dialog.cancel();
	            }
	        });
	 
	        // Showing Alert Message
	        alertDialog.show();
		} else {
			TextView tv = (TextView)findViewById(R.id.score);
			tv.setText("흑 : " + blackScore + ", 백 : " + whiteScore);
		}
	}

	private void toastType(String type) {
		//Toast toast = null;
		
		if (tempType.equals(type)) {
			//toast = Toast.makeText(getApplicationContext(), type + " : " + value + " 번이 선택되었습니다.", 100);
			TextView tv = (TextView)findViewById(R.id.text01);
			tv.setText(type + " : " + value + " 번이 선택되었습니다.");
			typeCheck = true;
		} else {
			//toast = Toast.makeText(getApplicationContext(), "아직 차례가 돌아오지 않았습니다.", 100);
			TextView tv = (TextView)findViewById(R.id.text01);
			tv.setText("아직 차례가 돌아오지 않았습니다.");
			typeCheck = false;
		}
		
		if (typeCheck){
			if ("black".equals(type)) {
				if (black[value-1] <= 0) {
					//toast = Toast.makeText(getApplicationContext(), value + " : 더 이상 사용할 수 없습니다.", 100);
					TextView tv = (TextView)findViewById(R.id.text01);
					tv.setText(value + " : 더 이상 사용할 수 없습니다.");
					typeCheck = false;
				} 
			} else {
				if (white[value-1] <= 0) {
					//toast = Toast.makeText(getApplicationContext(), value + " : 더 이상 사용할 수 없습니다.", 100);
					TextView tv = (TextView)findViewById(R.id.text01);
					tv.setText(value + " : 더 이상 사용할 수 없습니다.");
					typeCheck = false;
				}
			}
			
		}
		
		//if (toast != null ) {
			//toast.setGravity(Gravity.CENTER, 0, 0);
			//toast.show();
		//}
	}
	
	public void init(){
		edgeMap.put("LU","-" + (DEFAULT_NUM+1));
		edgeMap.put("U","-" + (DEFAULT_NUM));
		edgeMap.put("RU","-" + (DEFAULT_NUM-1));
		edgeMap.put("L","-1");
		edgeMap.put("R","1");
		edgeMap.put("LD","" + (DEFAULT_NUM-1));
		edgeMap.put("D","" + (DEFAULT_NUM));
		edgeMap.put("RD","" + (DEFAULT_NUM+1));
	}
	
	public void gamePlay(ArrayList<Baduk> list, int i, int value, String type){
		if ("N".equals(list.get(i).getType())) {
			String packName = this.getPackageName(); // 패키지명
			int getId = getResources().getIdentifier("g"+list.get(i).getI()+list.get(i).getJ(), "id",  packName);
			ImageView img = (ImageView)findViewById(getId);
			
			String resName = "@drawable/"+type+value;
			int resID = getResources().getIdentifier(resName, "drawable", packName);
			img.setImageDrawable(getResources().getDrawable(resID));
			list.get(i).setType(type);
			list.get(i).setValue(value);
			
			playLogic(list, i, value, type);
			replayList(list, tempType);
			
			if ("black".equals(tempType)) {
				tempType = "white";
				black[value-1] = black[value-1] - 1;
				
				this.getPackageName(); // 패키지명
				getId = getResources().getIdentifier("textblack"+value, "id",  packName);
				TextView tv = (TextView)findViewById(getId);
				tv.setText("x"+black[value-1] );
			} else {
				tempType = "black";
				white[value-1] = white[value-1] - 1;
				
				packName = this.getPackageName(); // 패키지명
				getId = getResources().getIdentifier("textwhite"+value, "id",  packName);//
				TextView tv = (TextView)findViewById(getId);
				tv.setText("x"+white[value-1] );
			}
			
			replayList(list, tempType);
		} else {
			//Toast toast = Toast.makeText(getApplicationContext(), "그곳에는 둘 수 없습니다.", 100);
			TextView tv = (TextView)findViewById(R.id.text01);
			tv.setText("그곳에는 둘 수 없습니다.");
			typeCheck = false;
			//toast.setGravity(Gravity.CENTER, 0, 0);
			//toast.show();
		}

		number = 0;
		this.value = 0;
	}

	private void playLogic(ArrayList<Baduk> list, int i, int value, String type) {
		init();	// 항상 초기화를 해야합니다.
		int width = list.get(i).getI();
		int height = list.get(i).getJ();	//Height
		list.get(i).setType(type);
		int cnt = 0;
		
		if (width == 0) {
			edgeMap.put("LU","X");
			edgeMap.put("U","X");
			edgeMap.put("RU","X");
		}
		
		if (height == 0) {
			edgeMap.put("LU","X");
			edgeMap.put("L","X");
			edgeMap.put("LD","X");
		}
		
		if (width == (DEFAULT_NUM-1)) {
			edgeMap.put("LD","X");
			edgeMap.put("D","X");
			edgeMap.put("RD","X");
		}
		
		if (height == (DEFAULT_NUM-1)) {
			edgeMap.put("RU","X");
			edgeMap.put("R","X");
			edgeMap.put("RD","X");
		}
		
		for (Map.Entry<String, String> entry : edgeMap.entrySet() )
		{
			if (!"X".equals(entry.getValue())) {
				cnt += parseList(list.get(i + Integer.parseInt(entry.getValue())));
			}
		}
		
		if (cnt == value) {
			for (Map.Entry<String, String> entry : edgeMap.entrySet() )
			{
				if (!"X".equals(entry.getValue())) {
					chageList(type, list.get(i + Integer.parseInt(entry.getValue())));
				}
			}
		} else {
			list.get(i).setType(type);
			list.get(i).setValue(value);
			

			List<Baduk> checkList = new ArrayList<Baduk>();
			
			for (Map.Entry<String, String> entry : edgeMap.entrySet() ) {
				if (!"X".equals(entry.getValue())) {
					choiceList(list, i + Integer.parseInt(entry.getValue()), checkList);
				}
			}
		}
	}

	public Object deepCopy(Object oldObj) {
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		Object result;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
				
			oos.writeObject(oldObj);
			oos.flush();
				
			ByteArrayInputStream  bin = new ByteArrayInputStream(bos.toByteArray());
			ois = new ObjectInputStream(bin);
				
			result = ois.readObject();
		}catch(Exception e) {
			e.printStackTrace();
			result = null;
		}finally {
			try {
				if(oos != null) oos.close();
				if(ois != null) ois.close();
			}catch(Exception e) {}
		}
		return result;
	}
	
	private void choiceList(List<Baduk> list, int i, List<Baduk> checkList) {
		init();	
		int width = list.get(i).getI();
		int height = list.get(i).getJ();	//Height
		int cnt = 0;
		int value = list.get(i).getValue();
		String type = list.get(i).getType();
		
		Map<String,String> tempEdgeMap = (Map<String,String>) deepCopy(edgeMap);
		if (width == 0) {
			tempEdgeMap.put("LU","X");
			tempEdgeMap.put("U","X");
			tempEdgeMap.put("RU","X");
		}
		
		if (height == 0) {
			tempEdgeMap.put("LU","X");
			tempEdgeMap.put("L","X");
			tempEdgeMap.put("LD","X");
		}
		
		if (width == (DEFAULT_NUM-1)) {
			tempEdgeMap.put("LD","X");
			tempEdgeMap.put("D","X");
			tempEdgeMap.put("RD","X");
		}
		
		if (height == (DEFAULT_NUM-1)) {
			tempEdgeMap.put("RU","X");
			tempEdgeMap.put("R","X");
			tempEdgeMap.put("RD","X");
		}
		
		/*
		 * 1 2 3
		 * 4 5 6
		 * 7 8 9
		 */
		int[] tempCheck = new int[9];
		for (Map.Entry<String, String> entry : tempEdgeMap.entrySet() )
		{
			if (!"X".equals(entry.getValue())) {
				int temp = parseList(list.get(i + Integer.parseInt(entry.getValue())));
				cnt += temp;
				
				switch (entry.getKey()) {
				case "LU":
					tempCheck[0] = (i + Integer.parseInt(entry.getValue()));
					break;
				case "U":
					tempCheck[1] = (i + Integer.parseInt(entry.getValue()));
					break;
				case "RU":
					tempCheck[2] = (i + Integer.parseInt(entry.getValue()));
					break;
				case "L":
					tempCheck[3] = (i + Integer.parseInt(entry.getValue()));
					break;
				case "R":
					tempCheck[4] = (i + Integer.parseInt(entry.getValue()));
					break;
				case "LD":
					tempCheck[6] = (i + Integer.parseInt(entry.getValue()));
					break;
				case "D":
					tempCheck[7] = (i + Integer.parseInt(entry.getValue()));
					break;
				case "RD":
					tempCheck[8] = (i + Integer.parseInt(entry.getValue()));
					break;
				default:
					break;
				}
			}
		}
		tempCheck[5] = value;
		
		if (cnt == value) {			
			Baduk temp = new Baduk();
			temp.setValue(value);
			temp.setI(width);
			temp.setJ(height);
			temp.setType(type);
			temp.setTempIJ(tempCheck);
			checkList.add(temp);
		}
	}
	
	public int replayList(ArrayList<Baduk> list, String type){
		int score = 0;

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getValue() > 0) {
				if(type.equals(list.get(i).getType())){
					playLogic(list, i, list.get(i).getValue(), list.get(i).getType());
				}
			}
		}
		
		int endCnt = 0;
		
		for (int i = 0; i < list.size(); i++) {
			if (type.equals(list.get(i).getType())){
				score = list.get(i).getValue() + score;
			} else if ((type+"L").equals(list.get(i).getType())){
				score++;
			}
			if ("N".equals(list.get(i).getType())){
				endCnt++;
			}
		}
		
		if (type.equals("black")) {
			blackScore = score;
		} else {
			whiteScore = score;
		}
		
		if (endCnt == 0) {
			gameEnd = true;
		}
		
		return score;
	}
	
	public int parseList(Baduk baduk){
		if("black".equals(baduk.getType()) || "white".equals(baduk.getType())){
			return 1;
		} else {
			return 0;
		}
	}
	
	public void chageList(String type, Baduk baduk){
		if("black".equals(baduk.getType()) || "white".equals(baduk.getType())){
			int getId = getResources().getIdentifier("g"+baduk.getI()+baduk.getJ(), "id",  this.getPackageName());
			ImageView img = (ImageView)findViewById(getId);
			
			if ("black".equals(type)){
				baduk.setType("blackL");
				baduk.setValue(-1);
				img.setImageDrawable(getResources().getDrawable(R.drawable.black0));
			} else if ("white".equals(type)){
				baduk.setType("whiteL");
				baduk.setValue(-1);
				img.setImageDrawable(getResources().getDrawable(R.drawable.white0));
			}
		}
	}
}
