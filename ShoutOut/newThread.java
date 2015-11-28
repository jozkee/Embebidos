package test.Droidlogin;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import test.Droidlogin.library.Httppostaux;
import test.Droidlogin.library.setting;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


public class newThread extends Activity {

	// LogCat tag
	private Button btnSend;
	private EditText inputMsg;

    Httppostaux post;

	//private Utils utils;

	// Client name
	private String user = null;

	// JSON flags to identify the kind of JSON response
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msginterface);
		
        post=new Httppostaux();

		btnSend = (Button) findViewById(R.id.btnSend);
		inputMsg = (EditText) findViewById(R.id.inputMsg);
		//listViewMessages = (ListView) findViewById(R.id.list_view_messages);


		// Getting the person name from previous screen
		Intent i = getIntent();
		user = i.getStringExtra("user");

		btnSend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				postThread(inputMsg.getText().toString(), user);
				inputMsg.setText("");
				finish();
			}
		});


}
	private void postThread(String thread, String user){
		int id;
		id=getUserID(user);
		ArrayList<NameValuePair> newThread= new ArrayList<NameValuePair>();
		newThread.add(new BasicNameValuePair("thread",thread));
		newThread.add(new BasicNameValuePair("user",String.valueOf(id)));
		
  		JSONArray jdata=post.getserverdata(newThread, setting.URL_connect+"/postthread.php");
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