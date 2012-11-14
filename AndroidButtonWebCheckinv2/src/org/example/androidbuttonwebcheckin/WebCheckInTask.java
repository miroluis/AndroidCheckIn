package org.example.androidbuttonwebcheckin;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;


import org.example.androidbuttonwebcheckinv2.R;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.widget.TextView;

public class WebCheckInTask implements Runnable {
	   private final AndroidButtonWebCheckin AppMae;
	   private final String CardID;
	   WebCheckInTask(AndroidButtonWebCheckin AppMae, String CardID) {
		   this.AppMae = AppMae;   
		   this.CardID = CardID;
		   }
	   
	   public void run() {
		   String retorno = doCheckIn(CardID);
		   AppMae.setResult(retorno);
	   }
	   
	   private String doCheckIn(String CardID) {
		      HttpURLConnection con = null;
		    //  Log.d(TAG, "doTranslate(" + original + ", " + from + ", "
				    //      + to + ")");
		      String contem = CardID;
			  contem = "Enviado para o Servidor\n\rA resposta do Servidor:\n\r";
            // origText.setText(contem, TextView.BufferType.EDITABLE);
			 
           //  HttpURLConnection con = null;
             String UID = CardID;// UIDText.getText().toString() ;                                   
             URL url = null;             
			try {
				url = new URL("http://www.nfc-portugal.com/nfcconnect/HeaderRequest.php"+ "?CardID=" + UID);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             HttpURLConnection urlConnection = null;
			try {
				urlConnection = (HttpURLConnection) url.openConnection();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
			InputStream in = null;
			try {
				in = new BufferedInputStream(urlConnection.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		//	InputStream in = urlConnection.openStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String result, line = null;
			try {
				line = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			result = line;
			try {
				while((line=reader.readLine())!=null){
				    result+=line;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(result);
		
            // contem = origText.getText().toString();
             
             
           int ini =  result.indexOf(">>");
           int fim = result.indexOf("<<");
           if(ini >0 & fim >0)
           {
             contem += result.substring(ini+2, fim);
           }
           else
           {
        	   contem += result;
           }
             contem += "\n\rEnviado às: ";
             
             SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
             Date dt = new Date();
             
             contem += timeFormat.format(dt);
             
             
          //   origText.setText(contem, TextView.BufferType.EDITABLE);
             return(contem);                
           
	   }
}
