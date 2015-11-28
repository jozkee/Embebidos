package test.Droidlogin;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

public class Mesh extends TabActivity {
	String user;
            /** Called when the activity is first created. */
            @Override
            public void onCreate(Bundle savedInstanceState)
            {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.mesh);
                    
                    Bundle extras = getIntent().getExtras();
                    if (extras != null) {
                  	   user  = extras.getString("user");//usuario
                     }else{
                  	   user="error";
                  	   }
                    // create the TabHost that will contain the Tabs
                    
                    TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
                    TabSpec tab1 = tabHost.newTabSpec("First Tab");
                    TabSpec tab2 = tabHost.newTabSpec("Second Tab");
                    TabSpec tab3 = tabHost.newTabSpec("Third tab");

                   // Set the Tab name and Activity
                   // that will be opened when particular Tab will be selected
                    tab1.setIndicator("Around");
                    tab1.setContent(new Intent(this,Mesh1.class).putExtra("user",user));
                    
                    tab2.setIndicator("Mine");
                    tab2.setContent(new Intent(this,Mesh2.class).putExtra("user",user));

                    tab3.setIndicator("On");
                    tab3.setContent(new Intent(this,Mesh3.class).putExtra("user",user));
                    
                    /** Add the tabs  to the TabHost to display. */
                    tabHost.addTab(tab1);
                    tabHost.addTab(tab2);
                    tabHost.addTab(tab3);
                    
            }
            
            @Override
            public boolean onCreateOptionsMenu(Menu menu)
            {
                MenuInflater menuInflater = getMenuInflater();
                menuInflater.inflate(R.layout.menu, menu);
                return true;
            }
            
            @Override
            public boolean onOptionsItemSelected(MenuItem item)
            {
                 
                switch (item.getItemId())
                {
         
                case R.id.menu_create:
                	Intent i=new Intent(Mesh.this, newThread.class);
                	i.putExtra("user",user);
                	startActivity(i); 
                    return true;
         
                case R.id.menu_preferences:
                    Toast.makeText(Mesh.this, "Preferences is still on development", Toast.LENGTH_SHORT).show();
                    return true;
         
                default:
                    return super.onOptionsItemSelected(item);
                }
            }    
            
            
} 