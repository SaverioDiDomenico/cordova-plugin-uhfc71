package it.dynamicid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.rscja.deviceapi.RFIDWithUHF;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

public class InventoryUhf {

	public Context  mContext;
	public Handler handler;	
	public RFIDWithUHF mReader; 
	public boolean loopFlag;

	List<String> listaTags;
	private HashMap<String, String> map;
	private ArrayList<HashMap<String, String>> tagList;


	public InventoryUhf(Context context, long txpower) {
		super();
		mContext = context;
		loopFlag = false;
		listaTags = new ArrayList<String>();

		Toast.makeText(context, "instanzio", Toast.LENGTH_LONG).show();
		try {
			mReader = RFIDWithUHF.getInstance();
		} catch (Exception ex) {
			Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
		}
		Toast.makeText(context, "potenza", Toast.LENGTH_LONG).show();
		try {
			mReader.setPower((int) txpower);
		} catch (Exception ex) {
			Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
		}

		tagList = new ArrayList<HashMap<String, String>>();

		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				String result = msg.obj + "";
				String[] strs = result.split("@");
				addEPCToList(strs[0],strs[1]);
				//mContext.playSound(1);
			}
		};
	}

	public void StartInventoryStream() {

		if (mReader.startInventoryTag((byte)0, (byte)0)) {

			loopFlag = true;

			new TagThread(80).start();
		} else {
			// messaggio di errore
		}
	}


	/**
	 * Stop streaming.
	 */
	public void StopInventoryStream()
	{

		if (loopFlag) {

			loopFlag = false;

			if (mReader.stopInventory()) {
				//Stop OK
			} else {
				//Stop KO

			}

		}

	}

	public String GetTags() {
		String retval = "";    	

		for(int i=0; i<listaTags.size(); i++) {
			String epcString;
			epcString = listaTags.get(i);
			retval = retval + epcString + ",";           
		}   	

		return retval;
	}

	private void addEPCToList(String epc,String rssi) {
		if (!TextUtils.isEmpty(epc)) {
			int index = checkIsExist(epc);

			map = new HashMap<String, String>();

			map.put("tagUii", epc);
			map.put("tagCount", String.valueOf(1));
			map.put("tagRssi", rssi);

			if (index == -1) {
				tagList.add(map);
				if(!listaTags.contains(epc)) {                	
					listaTags.add(epc);                            	
				}
				//LvTags.setAdapter(adapter);
				//tv_count.setText("" + adapter.getCount());
			} else {
				int tagcount = Integer.parseInt(tagList.get(index).get("tagCount"), 10) + 1;

				map.put("tagCount", String.valueOf(tagcount));

				tagList.set(index, map);

			}

			//adapter.notifyDataSetChanged();

		}
	}

	public int checkIsExist(String strEPC) {
		int existFlag = -1;
		if (isEmpty(strEPC)) {
			return existFlag;
		}

		String tempStr = "";
		for (int i = 0; i < tagList.size(); i++) {
			HashMap<String, String> temp = new HashMap<String, String>();
			temp = tagList.get(i);

			tempStr = temp.get("tagUii");

			if (strEPC.equals(tempStr)) {
				existFlag = i;
				break;
			}
		}

		return existFlag;
	}

	public static boolean isEmpty(CharSequence cs) {

		return cs == null || cs.length() == 0;

	}

	public static int toInt(String str, int defValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
		}
		return defValue;
	}


	class TagThread extends Thread {

		private int mBetween = 80;
		HashMap<String, String> map;

		public TagThread(int iBetween) {
			mBetween = iBetween;
		}

		public void run() {
			String strTid;
			String strResult;

			String[] res = null;

			while (loopFlag) {

				res = mReader.readTagFromBuffer();//.readTagFormBuffer();

				if (res != null) {

					strTid = res[0];
					if (!strTid.equals("0000000000000000")&&!strTid.equals("000000000000000000000000")) {
						strResult = "TID:" + strTid + "\n";
					} else {
						strResult = "";
					}
					Message msg = handler.obtainMessage();
					msg.obj = strResult + "EPC:" + mReader.convertUiiToEPC(res[1]) + "@" + res[2];
					//Log.i("msg", strResult + "EPC:" + mReader.convertUiiToEPC(res[1]) + "@" + res[2]);
					handler.sendMessage(msg);
				}
				try {
					sleep(mBetween);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

}
