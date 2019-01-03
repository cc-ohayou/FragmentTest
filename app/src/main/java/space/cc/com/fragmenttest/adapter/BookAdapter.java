package space.cc.com.fragmenttest.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.litepals.Book;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    private int height;
    private int width;
    private List<Book> books;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        View bookView;
        TextView bookName;
        TextView bookAuthor;
        TextView bookPrice;
        TextView bookPages;
        TextView bookId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookView=itemView;
            bookName= itemView.findViewById(R.id.book_name);
            bookAuthor= itemView.findViewById(R.id.book_author);
            bookPrice= itemView.findViewById(R.id.book_price);
            bookPages= itemView.findViewById(R.id.book_pages);
            bookId= itemView.findViewById(R.id.book_id);
        }

        public View getBookView() {
            return bookView;
        }

        public void setBookView(View bookView) {
            this.bookView = bookView;
        }

        public TextView getBookName() {
            return bookName;
        }

        public void setBookName(TextView bookName) {
            this.bookName = bookName;
        }

        public TextView getBookAuthor() {
            return bookAuthor;
        }

        public void setBookAuthor(TextView bookAuthor) {
            this.bookAuthor = bookAuthor;
        }

        public TextView getBookPrice() {
            return bookPrice;
        }

        public void setBookPrice(TextView bookPrice) {
            this.bookPrice = bookPrice;
        }

        public TextView getBookPages() {
            return bookPages;
        }

        public void setBookPages(TextView bookPages) {
            this.bookPages = bookPages;
        }

        public View getBookId() {
            return bookId;
        }

        public void setBookId(TextView bookId) {
            this.bookId = bookId;
        }
    }


    public BookAdapter(List<Book> bookList,int height) {
        this.books = bookList;
        //此处将自己想要的item的高度传入 在下面的onCreateViewHolder中设置子项view的高度
        this.height=height;
    }

    public BookAdapter(List<Book> bookList,int height,int width) {
        this.books = bookList;
        //此处将自己想要的item的宽高传入 在下面的onCreateViewHolder中设置子项view的高度
        this.height=height;
        this.width=width;
    }
    public BookAdapter(List<Book> bookList) {
        this.books = bookList;
        //此处将自己想要的item的高度传入 在下面的onCreateViewHolder中设置子项view的高度
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //找到子项item的view布局 类似findViewById找到组件
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_item,parent,false) ;
        ViewGroup.LayoutParams linearParams =view.getLayoutParams();
        //设置高度 一般纵向滚动时有此需求
        if(height>0){
            linearParams.height=height;
        }
//        一般最好宽高都自适应屏幕指定
        //设置宽度 一般横向滚动时有此需求
        if(width>0){
            linearParams.width=width;
        }
        view.setLayoutParams(linearParams);
        final ViewHolder holder=new ViewHolder(view);
        holder.bookView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Book book=books.get(position);
                Toast.makeText(v.getContext(),"clicked  view "+book.getId()
                        ,Toast.LENGTH_SHORT).show();
            }
        });
        holder.bookName.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Book book=books.get(position);
                Toast.makeText(v.getContext(),"clicked  bookName "+book.getId()
                        ,Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Book book=books.get(position);
        viewHolder.bookId.setText("编号:"+String.valueOf(book.getId()));
        viewHolder.bookName.setText("名称："+book.getName());
        viewHolder.bookAuthor.setText("作者："+book.getAuthor());
        viewHolder.bookPrice.setText("价格："+String.valueOf(book.getPrice()));
        viewHolder.bookPages.setText("总页数："+String.valueOf(book.getPages()));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}
