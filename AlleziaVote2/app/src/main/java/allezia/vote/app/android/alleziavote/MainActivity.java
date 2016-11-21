package allezia.vote.app.android.alleziavote;

import android.app.Activity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends ActionBarActivity {
    DrawerLayout drawerLayout;
    Database db= new Database(this);
    Button like ,dislike;
    ImageView view;
    TextView owner ,desc,coin;
    ArrayList products = new ArrayList();
    int currentCoin =0;
    int lastCoin = 0;
    int listener = 0;
    HashMap<String,String> datas = new HashMap<>();
    HashMap<Integer,Integer> result = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        setupToolbar();
        controlCoin();
        like = (Button) findViewById(R.id.like);
        dislike = (Button) findViewById(R.id.dislike);
        view = (ImageView) findViewById(R.id.product_image);
        owner = (TextView) findViewById(R.id.owner_name);
        desc = (TextView) findViewById(R.id.description);
        coin = (TextView) findViewById(R.id.coin);
        coin.setText(String.valueOf(currentCoin));
        getData();

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (result.size()==5){
                        System.out.println(result);
                        result.clear();
                    }
                    currentCoin +=lastCoin;
                    coin.setText(String.valueOf(currentCoin));
                    result.put((Integer) products.get(listener),1);

                }catch (Exception e){}

                showProduct();
            }
        });
        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (result.size()==5){
                        System.out.println(result);
                        result.clear();
                    }
                    currentCoin +=lastCoin;
                    coin.setText(String.valueOf(currentCoin));
                    result.put((Integer) products.get(listener),0);
                }catch (Exception e){}
                showProduct();

            }
        });


}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("name :"+item.getItemId());
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //NavigationView ekranda açmak için kullancagimiz iconu ActionBar'da gösterilmesini sagladik
        final ActionBar ab = getSupportActionBar();
        ab.setTitle("ALLEZIA");
        ab.setHomeAsUpIndicator(R.mipmap.ic_launcher);
        ab.setDisplayHomeAsUpEnabled(true);
    }
    private class Listesener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.item_navigation_drawer_starred:
                    Toast.makeText(getApplicationContext(),"butona basıldı",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
    public void getData(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://allezia.com/app/request/getJSON.php?type=products", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                JSONArray response = null;
                try {
                    response = new JSONArray(responseString);
                    System.out.println(response);
                if (statusCode==200){
                    System.out.println("row count  :"+db.getRowCount());
                    if (db.getRowCount()<=0){
                    for(int i=0;i< response.length();i++) {

                        JSONObject singleData = response.getJSONObject(i);
                        System.out.println(singleData);
                        db.productAdd(singleData.getString("id"),
                                singleData.getString("image_url"),
                                singleData.getString("owner"),
                                singleData.getString("description"),
                                singleData.getString("coin"));
                    }
                        products =db.products();
                        showProduct();
                    }else{
                        db.resetTables();
                    }
                }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void showProduct(){
        if(listener!=products.size()){

            int id = (int) products.get(listener);
            datas= db.getProduct(id);
            String link = datas.get(db.IMAGE);
            Picasso.with(this).
                    load(link).
                    fit().
                    centerInside().
                    into(view);
            owner.setText(datas.get(db.OWNER));
            desc.setText(datas.get(db.DESCRIPTION));
            System.out.println(datas);
            lastCoin = Integer.parseInt(datas.get(db.COIN));

            //currentCoin+=datas.get(db.COIN);
            listener++;
        }else{
            products.clear();
            getData();
            System.out.println(result);
            listener = 0;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println(result);
        db.setCurrentCoin(this,currentCoin);
    }
    public void controlCoin(){
        int coin = db.getPrefInt(this,"coin");
        currentCoin =coin;
    }
}

