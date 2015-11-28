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
import android.widget.ListView;

public class Mesh3  extends Activity
{
	String user;
	int index[];
    Httppostaux post;

	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista);

        post=new Httppostaux();
 	    Bundle extras = getIntent().getExtras();
 	    if (extras != null) {
 	    	user  = extras.getString("user");//usuario
        }else{
        	user="error";
     	}
 	    loadList();         
    }
    
	private void loadList(){

        final ListView listview = (ListView) findViewById(R.id.listview);

        String[] values = consultarComentarios(user);
        index=consultarIndices(user);

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
          list.add(values[i]);
        }
		list.add("Actualizar");
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
            android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

          @Override
          public void onItemClick(AdapterView<?> parent, final View view,
              int position, long id) {
				loadList();
            	if(position<index.length){
            		Intent i=new Intent(Mesh3.this, Message.class);
            		i.putExtra("thread",String.valueOf(index[position]));
            		i.putExtra("user",user);
            		startActivity(i);
            	}
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
	
	public String[] consultarComentarios(String user){
		int ThreadID;
		String desc;
		int userID;
		userID=getUserID(user);
    	ArrayList<NameValuePair> usuario= new ArrayList<NameValuePair>();
    	usuario.add(new BasicNameValuePair("user",String.valueOf(userID)));
		
  		JSONArray jdata=post.getserverdata(usuario, setting.URL_connect+"/getcommented.php");
  		
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
	
	public int[] consultarIndices(String user){/**/
		int ThreadID;
		int userID;
		userID=getUserID(user);
    	ArrayList<NameValuePair> usuario= new ArrayList<NameValuePair>();
    	usuario.add(new BasicNameValuePair("user",String.valueOf(userID)));
  		JSONArray jdata=post.getserverdata(usuario, setting.URL_connect+"/getcommented.php");
  		
  		if (jdata!=null && jdata.length() > 0){
    		JSONObject json_data; //creamos un objeto JSON
			try {
	            int[] values = new int[jdata.length()-1];
	            
				for(int j=0;j<jdata.length()-1; j++){
					json_data = jdata.getJSONObject(j); //leemos el primer segmento en nuestro caso el unico
					ThreadID=json_data.getInt("ThreadID");//accedemos al valor
					values[j]=ThreadID;
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
	
}
