package ma.ofppt.tdi201.quizly;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import ma.ofppt.tdi201.quizly.Common.Common;

import ma.ofppt.tdi201.quizly.Interface.ItemClickListener;
import ma.ofppt.tdi201.quizly.Model.Category;
import ma.ofppt.tdi201.quizly.ViewHolder.CategoryViewHolder;

public class CategoryFragment extends Fragment {

    View myFragment;

    RecyclerView listCategories;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapter;
    FirebaseDatabase database;
    DatabaseReference categories;

    public static CategoryFragment newInstance(){
        CategoryFragment categoryFragment = new CategoryFragment();
        return categoryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        categories = database.getReference("Categories");


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_category, container, false);

        listCategories = (RecyclerView) myFragment.findViewById(R.id.listCategories);
        listCategories.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(container.getContext());
        listCategories.setLayoutManager(layoutManager);

        loadCategories();
        return myFragment;
    }

    private void loadCategories() {
        adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(Category.class,
                R.layout.category_layout,
                CategoryViewHolder.class,
                categories) {
            @Override
            protected void populateViewHolder(final CategoryViewHolder categoryViewHolder, final Category category, int i) {
                categoryViewHolder.categoryName.setText(category.getName());
                Picasso.with(getActivity()).load(category.getImage()).into(categoryViewHolder.categoryImage);
                categoryViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(getActivity(), category.getName(), Toast.LENGTH_SHORT).show();

                        Intent startGame = new Intent(getActivity(), Start.class);
                        startGame.putExtra("resId", category.getImage());
                        Common.categoryId = adapter.getRef(position).getKey();
                        Common.categoryName=category.getName();
                        startActivity(startGame);
                    }
                });


            }
        };
        adapter.notifyDataSetChanged();
        listCategories.setAdapter(adapter);
    }


}
