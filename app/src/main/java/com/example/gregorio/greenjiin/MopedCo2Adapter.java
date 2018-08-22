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
import com.example.gregorio.greenjiin.MopedCo2Adapter.MyViewHolder;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class MopedCo2Adapter extends RealmRecyclerViewAdapter<MopedModel, MyViewHolder> {

  private Context context;

  public MopedCo2Adapter(
      @Nullable OrderedRealmCollection<MopedModel> data,
      boolean autoUpdate) {
    super(data, autoUpdate);
  }

  public MopedCo2Adapter(
      @Nullable OrderedRealmCollection<MopedModel> data,
      boolean autoUpdate, boolean updateOnModification) {
    super(data, autoUpdate, updateOnModification);
  }

  @NonNull
  @Override
  public MopedCo2Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    context = viewGroup.getContext();

    View view = LayoutInflater.from(context)
        .inflate(R.layout.photo_item2, viewGroup, false);
    return new MopedCo2Adapter.MyViewHolder(view);
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
      imageView = itemView.findViewById(R.id.photo_place_id2);
      mopedName = itemView.findViewById(R.id.moped_name2);

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
