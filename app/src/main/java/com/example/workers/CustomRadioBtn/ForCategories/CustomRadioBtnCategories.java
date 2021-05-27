package com.example.workers.CustomRadioBtn.ForCategories;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.workers.CustomRadioBtn.CustomRadioButton;
import com.example.workers.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomRadioBtnCategories extends CustomRadioButton {

    // Attribute Variables
    private ConstraintLayout Style;
    private CircleImageView circleImageView;
    private TextView txtCategory;

    //================================================================================
    // Constructors
    //================================================================================

    public CustomRadioBtnCategories(Context context) {
        super(context);
    }

    public CustomRadioBtnCategories(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRadioBtnCategories(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomRadioBtnCategories(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    //================================================================================
    // Overriding default behavior
    //================================================================================

    @Override
    protected void parseAttributes(AttributeSet attrs) {

    }

    @Override
    protected void inflateView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.custom_radio_btn_categories, this, true);
        Style=findViewById(R.id.Style);
        circleImageView = findViewById(R.id.circleImageView);
        txtCategory=findViewById(R.id.txtCategory);
    }

    @Override
    protected void bindView() {

    }

    @Override
    public void setCheckedState() {
        Style.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.style_selected_radiobtn_category));
    }

    @Override
    public void setNormalState() {
        Style.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.style_unselected_radiobtn_category));
    }

    //================================================================================
    // Public methods
    //================================================================================


    public void setImage(String Image) {

        Glide.with(getContext())
                .load(Image)
                .apply(new RequestOptions().override(60, 60))
                .placeholder(R.drawable.ic_error_outline)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_error_outline)
                .into(circleImageView);
    }

    public Drawable getImage(){

        return circleImageView.getDrawable();
    }

    public void setImage(Bitmap icon) {
        circleImageView.setImageBitmap(icon);
    }

    public void setCategory(String Category) {
        txtCategory.setText(Category);
    }
    public String getCategory() {
      return   txtCategory.getText().toString(); }
}