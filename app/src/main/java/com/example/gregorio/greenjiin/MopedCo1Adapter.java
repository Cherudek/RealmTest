package com.example.gregorio.greenjiin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.gregorio.greenjiin.MopedCo1Adapter.MyViewHolder;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;


public class MopedCo1Adapter extends RealmRecyclerViewAdapter<MopedModel, MyViewHolder> {

  private Context context;

  public MopedCo1Adapter(
      @Nullable OrderedRealmCollection<MopedModel> data,
      boolean autoUpdate, Context context) {
    super(data, autoUpdate);
    this.context = context;
  }

  public MopedCo1Adapter(
      @Nullable OrderedRealmCollection<MopedModel> data,
      boolean autoUpdate, boolean updateOnModification, Context context) {
    super(data, autoUpdate, updateOnModification);
    this.context = context;
  }

  @NonNull
  @Override
  public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.photo_item, viewGroup, false);
    return new MyViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
    final MopedModel mopedModel = getItem(i);
    myViewHolder.setItem(mopedModel);

  }


  class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView imageView;
    TextView mopedName;
    MopedModel mopedModel;


    public MyViewHolder(@NonNull View itemView) {
      super(itemView);
      imageView = itemView.findViewById(R.id.photo_place_id);
      mopedName = itemView.findViewById(R.id.moped_name);

    }

    void setItem(MopedModel mopedModel) {
      String mopedUrl = mopedModel.getMopedUrl();
      this.mopedModel = mopedModel;

      Glide.with(context)
          .load(mopedUrl)
          .into(this.imageView);

      this.imageView.setContentDescription(context.getString(R.string.photo_gallery_detail_view));
      this.mopedName.setText(mopedModel.getMopedName());
    }

    @Override
    public void onClick(View view) {

    }
  }

}
