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
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class Mesh1  extends Activity
{
		String user;
		String values[];
		int index[];
		//el range que usariamos es .15 (approximadamente 5 km de diametro), pero para fines de demostracion se usa uno amplio
		double range=237;
		double longitude,latitude;
		ArrayList<String> list;
		
	    Httppostaux post;
      	   
        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.lista);

            post=new Httppostaux();
	 	    Bundle extras = getIntent().getExtras();
	 	    if (extras != null) {
	 	    	user  = extras.getString("user");//usuario
	 	    }else{
	 	    	user="error";
         	}
           
           	
           configGPS();
           getThreads(user);
           loadList();       
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
        
        private void loadList(){
        	final ListView listview = (ListView) findViewById(R.id.listview);
            /*String[] values = new String[] { "Android con texto excesivamente y estupidamente largo, pero no lo suficiente como para ver como funciona el listview, pero aun me falta un poco mas para forzar los limites de tamaño del view de la list view", user, "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WindowsMobile" };*/
            values = consultarCerca(latitude,longitude,range);
            index=consultarIndices(latitude,longitude,range);
            
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
            	if(position<index.length){
            		Intent i=new Intent(Mesh1.this, Message.class);
            		i.putExtra("thread",String.valueOf(index[position]));
            		i.putExtra("user",user);
            		startActivity(i);
            	}
              }

            });
        }
        
        private void configGPS(){
    		
    		LocationManager mLocationManager;
    		LocationListener mLocationListener;
    		
    		mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    		mLocationListener = new MyLocationListener();
    		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, mLocationListener);
    	
    	}
    	
    	private class MyLocationListener implements LocationListener{

    		@Override
    		public void onLocationChanged(Location location) {
    			// TODO Auto-generated method stub
    			Log.d("HelloGPSActivity","Latitude="+ String.valueOf(location.getLatitude()));
    			Log.d("HelloGPSActivity","Longitud="+ String.valueOf(location.getLongitude()));
    			
    			longitude=location.getLongitude();
    			latitude=location.getLatitude();
    			new asynclogin().execute(user,String.valueOf(longitude),String.valueOf(latitude));
    			loadList();   	
    		}

    		@Override
    		public void onProviderDisabled(String provider) {
    			// TODO Auto-generated method stub
    			
    		}

    		@Override
    		public void onProviderEnabled(String provider) {
    			// TODO Auto-generated method stub
    			
    		}

    		@Override
    		public void onStatusChanged(String provider, int status, Bundle extras) {
    			// TODO Auto-generated method stub
    			
    		}
    	}
    	
    	class asynclogin extends AsyncTask< String, String, String > {
       	 
        	String user;
        	int userID;
            protected void onPreExecute() {
            	//para el progress dialog
            }
     
    		protected String doInBackground(String... params) {
    			//obtnemos usr y ubicacion
    			user=params[0];
    			longitude=Double.parseDouble(params[1]);
    			latitude=Double.parseDouble(params[2]);
                
    			userID=getUserID(user);

    			updateLocation(userID,latitude,longitude);    			
    			
    			return String.valueOf(userID);
            	
    		}
           
    		/*Una vez terminado doInBackground segun lo que halla ocurrido 
    		pasamos a la sig. activity
    		o mostramos error*/
            protected void onPostExecute(String result) {

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
    	
    	public void updateLocation(int user, double lat, double lon){
        	ArrayList<NameValuePair> location2send= new ArrayList<NameValuePair>();
        	location2send.add(new BasicNameValuePair("usuario",String.valueOf(user)));
        	location2send.add(new BasicNameValuePair("latitud",String.valueOf(lat)));
        	location2send.add(new BasicNameValuePair("longitud",String.valueOf(lon)));
    		
      		JSONArray jdata=post.getserverdata(location2send, setting.URL_connect+"/updatelocation.php");
    	}
    	
    	public void getThreads(String username){
    		int userID;
    		
    		userID=getUserID(user);
        	
        	ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
    		postparameters2send.add(new BasicNameValuePair("usuario",String.valueOf(userID)));

      		JSONArray jdata=post.getserverdata(postparameters2send, setting.URL_connect+"/getlocation.php");
      		consultarCerca(latitude, longitude, range);
      		
    	}
    	
    	public String[] consultarCerca(double lat, double lon, double r){/**/
    		int ThreadID;
    		String desc;
        	ArrayList<NameValuePair> rango= new ArrayList<NameValuePair>();
    		rango.add(new BasicNameValuePair("latitud",String.valueOf(lat)));
    		rango.add(new BasicNameValuePair("longitud",String.valueOf(lon)));
    		rango.add(new BasicNameValuePair("radio",String.valueOf(r)));
    		

      		JSONArray jdata=post.getserverdata(rango, setting.URL_connect+"/getnear.php");
      		
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
    	public int[] consultarIndices(double lat, double lon, double r){/**/
    		int ThreadID;
        	ArrayList<NameValuePair> rango= new ArrayList<NameValuePair>();
    		rango.add(new BasicNameValuePair("latitud",String.valueOf(lat)));
    		rango.add(new BasicNameValuePair("longitud",String.valueOf(lon)));
    		rango.add(new BasicNameValuePair("radio",String.valueOf(r)));
      		JSONArray jdata=post.getserverdata(rango, setting.URL_connect+"/getnear.php");
      		
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
