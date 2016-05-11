package testmeteortest.com.meteortest;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private List<UserModel> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, address;
        public LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.textView_name);
            address = (TextView) view.findViewById(R.id.textView_addrss);
            linearLayout = (LinearLayout)view.findViewById(R.id.linearlaayout);
        }
    }


    public MoviesAdapter(List<UserModel> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custome_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        UserModel movie = moviesList.get(position);
        holder.name.setText(movie.getName());
        holder.address.setText(movie.getAddress());

        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                RegisterActivity.mMeteor.remove(RegisterActivity.DATABASE_NAME,moviesList.get(holder.getAdapterPosition()).getId());
                notifyDataSetChanged();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
