package practice.kshitij.com.crashylytics_2;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.callback.Callback;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by Test on 3/12/2017.
 */
public class Retrofit_Class extends AppCompatActivity {

    // 1st call==Gist-Gistfile
    private class GistFile{
        public String filename;
        public String type;


    }
    private class Gist{
        public String id;
        public HashMap<String,GistFile> files;

        @Override
        public String toString(){

            String output=id+": ";
            for (Map.Entry<String,GistFile>file:files.entrySet()){
                output +=file.getKey()+ "="+file.getValue().type+", ";
            }
            return output;
        }
    }
//2nd call-UserSearchResults-UserSummary
    public class UserSummary{

        public String login;
        public String id;

        @Override
        public String toString() {
            return "UserSummary{" + "login='" + login + '\'' + ", id='" + id + '\'' + '}';
        }
    }

    public class UserSearchResults{

        public int total_count;
        public boolean incomplete_results;
        public List<UserSummary> items;
    }
//now to dynamically change url with parameters

    private class UserDetails{

        public String id;
        public String location;

        @Override
        public String toString() {
            return "UserDetails{" + "id='" + id + '\'' + ", location='" + location + '\'' + '}';
        }
    }
    private interface GitHubService{
        //1st call
       @GET("/gists/public")
        List<Gist> getPublicGists();

        //2nd call
        @GET("/search/users")
        UserSearchResults searchResults(@Query("q")String query);

        //get url dynamically i.e we have not statically defined username as in 2nd field
        @GET("/users/{username}")
        UserDetails getUser(@Path("username")String username);

        //for asynchronous requests
        @GET("/users/{username}")
        void asynchronouscall(@Path("username") String username, retrofit2.Callback<UserDetails> callback);
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        //strict mode for synchronous calling of apis
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.retrofit_view);

//new way for version 2 in gradle
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .build();


        final  GitHubService service=retrofit.create(GitHubService.class);


        final ArrayAdapter<Object> listadapter=new ArrayAdapter<Object>(this,android.R.layout.simple_list_item_1);
        ListView listView=(ListView)findViewById(R.id.listView);
        listView.setAdapter(listadapter);
        //for gista(1st call)..commenting it for present
        //listadapter.addAll(service.getPublicGists());
        //for user results(2nd call)
        listadapter.addAll(service.searchResults("nelsonlaqu").items);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserSummary summary=(UserSummary)listadapter.getItem(position);
               // for dynammic call..commenting it for present
                //service.getUser(summary.login);
                service.asynchronouscall(summary.login, new retrofit2.Callback<UserDetails>() {
                    @Override
                    public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {

                        Toast.makeText(Retrofit_Class.this,call.toString(),Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<UserDetails> call, Throwable t) {

                    }
                });

            }
        });

    }
}
