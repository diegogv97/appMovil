package com.apiconnection.diegoarmando.api_connection.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.apiconnection.diegoarmando.api_connection.Business.News;
import com.apiconnection.diegoarmando.api_connection.R;
import com.apiconnection.diegoarmando.api_connection.Services.NewsService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<News> listsNews = new ArrayList<News>();
    ListView lvNews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvNews = findViewById(R.id.lvNews);
        MainActivity.ExecuteGetNews executeGetNews = new MainActivity.ExecuteGetNews();
        executeGetNews.execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void loadNews(JSONObject jsonResult) {
        listsNews.clear();

        try {
            JSONArray jsonNewsList  = jsonResult.getJSONArray("news");       //Importante
            for (int i = 0; i < jsonNewsList.length(); i++) {
                JSONObject jsonNews = (JSONObject) jsonNewsList.get(i);
                String title = jsonNews.getString("title");
                String hola;
                String description = jsonNews.getString("description");
                String date = jsonNews.getString("date");
                boolean approved = jsonNews.getBoolean("approved");
                News n = new News(title, description, date, approved);
                listsNews.add(n);
            }
            News n = new News("","","",false);
            listsNews.add(n);
            NewsAdapter adapter = new NewsAdapter();
            lvNews.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    public class ExecuteGetNews extends AsyncTask<String, Void, String> {
        boolean isOk = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //rlLoaderEmisoras.setVisibility(View.VISIBLE);
            //rlLogin.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            NewsService api = NewsService.getInstance();
            isOk = api.getNews(1);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(isOk){
                loadNews( NewsService.getInstance().getJsonObjectResponse());
            }else{
                String msj = "Error al obtener las difusiones";

                Toast.makeText(MainActivity.this, msj, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    public class ExecuteCreateNews extends AsyncTask<String, Void, String> {
        boolean isOk = false;
        String title;
        String description;
        String date;
        Boolean approved;

        ExecuteCreateNews(){

        }

        public ExecuteCreateNews(String title, String description, String date, Boolean approved) {
            this.title = title;
            this.description = description;
            this.date = date;
            this.approved = approved;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {
            NewsService api = NewsService.getInstance();

            String[] keys = {"idCommunity","title", "description", "date","photo","approved"};
            String[] values = {"1",title, description,date,"",String.valueOf(approved)};
            isOk = api.createNews(keys,values,0);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(isOk){

                String msj = "Difusion creada";
                Toast.makeText(MainActivity.this, msj, Toast.LENGTH_SHORT).show();

                MainActivity.ExecuteGetNews executeGetNews = new MainActivity.ExecuteGetNews();
                executeGetNews.execute();


            }else{
                String msj = "Error al crear la difusiones";
                Toast.makeText(MainActivity.this, msj, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    public class NewsAdapter extends BaseAdapter {

        public NewsAdapter() {
            super();
        }

        @Override
        public int getCount() {
            return listsNews.size();
        }

        @Override
        public Object getItem(int i) {
            return listsNews.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = getLayoutInflater();
            if (view == null) {
                view = inflater.inflate(R.layout.news_item, null);
            }


            final TextView title = (TextView) view.findViewById(R.id.title);
            final TextView description = (TextView) view.findViewById(R.id.description);
            final TextView date = (TextView) view.findViewById(R.id.date);
            final Switch approved = (Switch) view.findViewById(R.id.approved);
            Button delete = (Button) view.findViewById(R.id.delete);
            Button save = (Button) view.findViewById(R.id.save);


            News n = listsNews.get(i);

            String sTitle = n.getTitle();
            String sDescription = n.getDescription();
            String sDate = n.getDate();
            Boolean bApproved = n.getApproved();

            title.setText(sTitle);
            description.setText(sDescription);
            date.setText(sDate);
            approved.setChecked(bApproved);

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.ExecuteCreateNews executeCreateNews = new MainActivity.ExecuteCreateNews(title.getText().toString(), description.getText().toString(), date.getText().toString(),approved.isChecked());
                    executeCreateNews.execute();
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "deleting", Toast.LENGTH_SHORT).show();
                }
            });






            return view;
        }
    }

}
