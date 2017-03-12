package practice.kshitij.com.crashylytics_2;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.jar.Attributes;

/**
 * Created by Test on 3/11/2017.
 */
public class Recycler_Activity extends AppCompatActivity{

RecyclerView recyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.recycler_view);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        NameListAdapter adapter=new NameListAdapter();
        recyclerView.setAdapter(adapter);
        for(int i=0;i<200;i++){
            adapter.addName("Name"+Integer.toString(i));
        }
    }

    private class NameListAdapter extends RecyclerView.Adapter<NameViewolder>{

        private final ArrayList<String> names;

        private NameListAdapter() {
            names = new ArrayList<>();
        }
private void addName(String name){
    names.add(name);
    notifyItemInserted(names.size()-1);


}
        private void removeName(String name){
            int position=names.indexOf(name);
            if (position==-1){
                return;
            }
            names.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,names.size());

        }

        @Override
        public NameViewolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view=getLayoutInflater().inflate(R.layout.items,parent,false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name=(String)v.getTag();
                    removeName(name);


                }
            });
            return new NameViewolder(view);
        }

        @Override
        public void onBindViewHolder(NameViewolder holder, int position) {
            String name=names.get(position);
            holder.nametextView.setText(name);
            holder.itemView.setTag(name);
            if (position % 2 == 0) {
                holder.nametextView.setBackgroundColor(Color.parseColor("#ffbbcc"));
            }
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.nametextView.setBackground(null);
            }
        }



        @Override
        public int getItemCount() {

            return names.size();
        }
    }
    private class NameViewolder extends RecyclerView.ViewHolder{

        public TextView nametextView;

        public NameViewolder(View itemView) {
            super(itemView);
            nametextView=(TextView)findViewById(R.id.textView);
        }
    }
}
