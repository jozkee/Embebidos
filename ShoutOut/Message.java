package test.Droidlogin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import test.Droidlogin.library.Httppostaux;
import test.Droidlogin.library.setting;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class Message extends Activity {
	
	private Button btnSend;
	private EditText inputMsg;

	private String threadID = null;
	String values[];
	String user;
	
    Httppostaux post;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msginterface);
		

        post=new Httppostaux();

		btnSend = (Button) findViewById(R.id.btnSend);
		inputMsg = (EditText) findViewById(R.id.inputMsg);

		// Getting the person name from previous screen
		Intent i = getIntent();
		threadID = i.getStringExtra("thread");
		user = i.getStringExtra("user");
		loadList(threadID);

		btnSend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				postComment(inputMsg.getText().toString(), threadID, user);
				loadList(threadID);
				inputMsg.setText("");
			}
		});
		
	
}
	private void loadList(String thread){
    	final ListView listview = (ListView) findViewById(R.id.list_view_messages);

        values = consultarComentarios(thread);
        
        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
          list.add(values[i]);
        }
        
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
            android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

          @Override
          public void onItemClick(AdapterView<?> parent, final View view,
              int position, long id) {
        	  
          }
        });
    }
	
	private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
            List<String> objects) {
          super(context, textViewResourceId, objects);
          for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i), i);
          }
        }

        @Override
        public long getItemId(int position) {
          String item = getItem(position);
          return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
          return true;
        }
    }
	
	public String[] consultarComentarios(String thread){
		int ThreadID;
		String desc;
    	ArrayList<NameValuePair> rango= new ArrayList<NameValuePair>();
		rango.add(new BasicNameValuePair("thread",thread));

  		JSONArray jdata=post.getserverdata(rango, setting.URL_connect+"/getcomments.php");
  		
  		if (jdata!=null && jdata.length() > 0){
    		JSONObject json_data; //creamos un objeto JSON
			try {
	            String[] values = new String[jdata.length()-1];
	            
				for(int j=0;j<jdata.length()-1; j++){
					json_data = jdata.getJSONObject(j); //leemos el primer segmento en nuestro caso el unico
					ThreadID=json_data.getInt("ThreadID");//accedemos al valor
					desc=json_data.getString("Desc");
					values[j]=desc;
	    	 	}  
				return values;
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}		        
  		}else{	//json obtenido invalido verificar parte WEB.
    			Log.e("JSON  ", "ERROR");
    			return null;
  		}
  		
		
	}
	
	private void postComment(String text, String thread, String user){
		int id;
		id=getUserID(user);
		ArrayList<NameValuePair> newThread= new ArrayList<NameValuePair>();
		newThread.add(new BasicNameValuePair("text",text));
		newThread.add(new BasicNameValuePair("thread",thread));
		newThread.add(new BasicNameValuePair("user",String.valueOf(id)));
		
  		JSONArray jdata=post.getserverdata(newThread, setting.URL_connect+"/postcomment.php");
	}
	
	public int getUserID(String username){
    	int id=-1;
    	
    	ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
		postparameters2send.add(new BasicNameValuePair("usuario",username));

  		JSONArray jdata=post.getserverdata(postparameters2send, setting.URL_connect+"/getuserid.php");
  		
  		if (jdata!=null && jdata.length() > 0){
    		JSONObject json_data; //creamos un objeto JSON
			try {
				json_data = jdata.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
				 id=json_data.getInt("userid");//accedemos al valor 						
				 Log.e("userid","Usuario= "+id);//muestro por log que obtuvimos
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		            
    		 return id;
	  }else{	//json obtenido invalido verificar parte WEB.
    			 Log.e("JSON  ", "ERROR");
	    		return 0;
	  }
	}
	
}