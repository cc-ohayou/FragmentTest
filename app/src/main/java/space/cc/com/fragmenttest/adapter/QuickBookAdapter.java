package space.cc.com.fragmenttest.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.litepals.Book;

public class QuickBookAdapter extends BaseQuickAdapter<Book, BaseViewHolder> {

        public QuickBookAdapter(int layoutResId, List data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Book item) {
            helper.setText(R.id.book_name, item.getName());
            helper.setText(R.id.book_author, item.getAuthor());
            helper.setText(R.id.book_price, String.valueOf( item.getPrice()));
            helper.setImageResource(R.id.book_image, item.getImageId());
//            helper.setImageBitmap()
//            Picasso.with(context).load(imageUrl).
        }
    }



