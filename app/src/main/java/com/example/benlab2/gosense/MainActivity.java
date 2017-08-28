package com.example.benlab2.gosense;

import android.os.Bundle;

import java.io.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import 	java.net.HttpURLConnection;
import org.json.JSONObject;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;


public class MainActivity extends Activity implements OnClickListener {

    TextView isConnected;
    EditText etName, etDeadline, etTask;
    Button btnPost;

    Task task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get reference to the views
        isConnected = (TextView) findViewById(R.id.IsConnected);
        etName = (EditText) findViewById(R.id.etName);
        etDeadline = (EditText) findViewById(R.id.etDeadline);
        etTask = (EditText) findViewById(R.id.etTask);
        btnPost = (Button) findViewById(R.id.btnPost);

        // check if you are connected or not
        //if(isConnected()){
           // isConnected.setBackgroundColor(0xFF00CC00);
            //isConnected.setText("You are conncted");
      //  }
       // else{
           // isConnected.setText("You are NOT conncted");
        //}

        // add click listener to Button "POST"
        btnPost.setOnClickListener(this);

    }

    public static String POST(String url, Task task){
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
           // HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            //HttpPost httpPost = new HttpPost(url);

            String json = "";
            URL webAddress = new URL(url);
            HttpURLConnection  urlConn = (HttpURLConnection)webAddress.openConnection();
            urlConn.setDoOutput(true);
            urlConn.setChunkedStreamingMode(0);

            urlConn.setRequestProperty("Accept", "application/json");
            urlConn.setRequestProperty("Content-Type", "application/json");

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("name", task.getClient_id());
            jsonObject.accumulate("deadline", task.getExpire_Time());
            jsonObject.accumulate("task", task.getTask());

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            DataOutputStream os = new DataOutputStream(urlConn.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(json);
            writer.close();
            os.close();

            int responseCode=urlConn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    result+=line;
                    Log.d("tag", line);
                }
            }
            else {
               // result="";
                result = "Did not work!";
            }









            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
           // StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            //httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
           // httpPost.setHeader("Accept", "application/json");
           // httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
           // HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
           // inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
//            if(inputStream != null)
//                result = convertInputStreamToString(inputStream);
//            else
//                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.btnPost:
                String ID = etName.getText().toString();
                String deadline = etDeadline.getText().toString();
                String task = etTask.getText().toString();
                if(!validate())
                    Toast.makeText(getBaseContext(), "Enter some data!", Toast.LENGTH_LONG).show();
                // call AsynTask to perform network operation on separate thread
                new HttpAsyncTask().execute("http://sample-env-1.rviepxq7aj.us-west-2.elasticbeanstalk.com/task_request", ID, deadline, task);
                //new HttpAsyncTask().execute("http://hmkcode.appspot.com/jsonservlet", ID, deadline, task);
                break;
        }

    }

    private boolean validate(){
        if(etName.getText().toString().trim().equals(""))
            return false;
        else if(etDeadline.getText().toString().trim().equals(""))
            return false;
        else if(etTask.getText().toString().trim().equals(""))
            return false;
        else
            return true;
    }
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            task = new Task();
            task.setClient_id(params[1]);
            task.setExpire_Time(params[2]);
            task.setTask(params[3]);

            return POST(params[0],task);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Data Sent! " + result, Toast.LENGTH_LONG).show();
        }
    }
}
