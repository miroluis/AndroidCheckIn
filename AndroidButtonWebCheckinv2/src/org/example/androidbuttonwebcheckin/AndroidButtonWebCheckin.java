package org.example.androidbuttonwebcheckin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import org.example.androidbuttonwebcheckinv2.R;
//import org.example.androidbuttonwebcheckin.R;


import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

public class AndroidButtonWebCheckin extends Activity {
	private EditText origText;
	private Button button;
	private EditText UIDText;
	
	private Handler guiThread;
	private ExecutorService transThread;
	private Runnable updateTask;
	private Future transPending;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_button_web_checkin);
        
        
        initThreading();
        findViews();
        setListeners();
        
    }
    
    @Override
    protected void onDestroy() {
       // Terminate extra threads here
       transThread.shutdownNow();
       super.onDestroy();
    }
    
    private void findViews() {
    	origText = (EditText) findViewById(R.id.editText1);
    	button = (Button) findViewById(R.id.button1);
    	UIDText = (EditText) findViewById(R.id.editText2);
    }
    	
  //apresenta uma mensagem suspensa que desapareçe com o tempo
	public void toast2(String message) {
		Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();
	}
	
    private void setListeners() {

    // Define event listeners
    	 button.setOnClickListener(new OnClickListener() {
    		  /* (non-Javadoc)
    		 * @see android.view.View.OnClickListener#onClick(android.view.View)
    		 */    		   		
    		 
    		
    		 
    		public void onClick(View view) {
    			
    			//apresenta uma mensagem suspensa que desapareçe com o tempo
    			 // toast2("Olá Mundo"); 
    			
    			  String contem = origText.getText().toString();
    			  contem = "Enviado para o Servidor\n\rA resposta do Servidor:\n\r";
                 origText.setText(contem, TextView.BufferType.EDITABLE);
    			 
                 String UID =  UIDText.getText().toString() ;  
                 
                 guiThread.removeCallbacks(updateTask);
                 // Start an update if nothing happens after a few milliseconds
                 guiThread.postDelayed(updateTask, 1);
                 
    		}
    	 });
    }

    private void initThreading() {
		// TODO Auto-generated method stub
    	guiThread = new Handler();
        transThread = Executors.newSingleThreadExecutor();

        // This task does a translation and updates the screen
        updateTask = new Runnable() { 
           public void run() {
              // Get text to translate
         	  String contem = origText.getText().toString();
 			  contem = "Enviado para o Servidor\n\rA resposta do Servidor:\n\r";
              origText.setText(contem, TextView.BufferType.EDITABLE);
 			 
              String UID =  UIDText.getText().toString() ;   

              // Cancel previous translation if there was one
              if (transPending != null)
                 transPending.cancel(true); 

              try {
             	 WebCheckInTask webCheckInTask = new WebCheckInTask(AndroidButtonWebCheckin.this, UID);
             	 transPending = transThread.submit(webCheckInTask); 
              }           
              catch (RejectedExecutionException e) {
             // Unable to start new task
            // transText.setText(R.string.translation_error); 
            // retransText.setText(R.string.translation_error);
             	 }
           }
              
           };
	}

	public void setResult(String text) {
        guiSetText(origText, text);
     }
    
    private void guiSetText(final EditText view, final String text) {
        guiThread.post(new Runnable() {
           public void run() {
              view.setText(text);
           }
        });
     }  
}