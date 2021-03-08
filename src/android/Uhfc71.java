package it.dynamicid;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Uhfc71 extends CordovaPlugin {
	
	public InventoryUhfc71 iu;
	
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if ("scan".equals(action)) {
			scan(args.getString(0), args.getLong(1), args.getLong(2), callbackContext);
			return true;
		}
		
		if ("inizializzazione".equals(action)) {
			inizializzazione(args.getString(0), args.getLong(1), args.getLong(2), callbackContext);
			return true;
		}

		return false;
	}

	private void scan(String epc, long waittime, long txpower, CallbackContext callbackContext) {
		try {
			 Context context = this.cordova.getActivity().getApplicationContext();
							
			//this.iu = new InventoryUhfc71(context, txpower);			
			
			this.iu.listaTags = new ArrayList<String>();
			this.iu.tagList = new ArrayList<HashMap<String, String>>();
			
			this.iu.StartInventoryStream();
			try {
				Thread.sleep(waittime);
			} catch (Exception e) {

			}
			this.iu.StopInventoryStream();
			
			
			String result = "NO-TAGS";

			if(epc.isEmpty()) {
				result = this.iu.GetTags();
				
				if(result.isEmpty()){
					result = "NO-TAGS";
				}
				
				
								
			}else {

				String tags[] = result.split(",");
				List<String> lista = new ArrayList<String>();

				for(int i=0; i<tags.length; i++) {
					lista.add(tags[i]);
				}

				if(lista.contains(epc)) {
					result="OK";
				}else {
					result="KO";
				}

			}

			callbackContext.success(result);

		} catch (Exception e) {
			callbackContext.error(e.toString());
		}


	}
	
	
	private void inizializzazione(String epc, long waittime, long txpower, CallbackContext callbackContext) {
		try {
			Context context = this.cordova.getActivity().getApplicationContext();							
			this.iu = new InventoryUhfc71(context, txpower);			
			
		} catch (Exception e) {
			callbackContext.error(e.toString());
		}


	}	
	
}
